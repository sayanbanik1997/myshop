package com.google.sayanbanik1997.myshop;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Bluetooth SPP

    private BluetoothAdapter btAdapter;
    private BluetoothSocket socket;
    private OutputStream output;

    private EditText macInput;
    private Button btnConnect, btnPrint;

    private final ActivityResultLauncher<String[]> permLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                // No-op: we proceed when user grants
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        macInput = findViewById(R.id.macInput);
        btnConnect = findViewById(R.id.btnConnect);
        btnPrint = findViewById(R.id.btnPrint);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            toast("Bluetooth not supported");
            finish();
            return;
        }

        requestBtPermsIfNeeded();

        btnConnect.setOnClickListener(v -> connectToPrinter());
        btnPrint.setOnClickListener(v -> printTest());
    }

    private void requestBtPermsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permLauncher.launch(new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                });
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permLauncher.launch(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION });
            }
        }
    }

    private void connectToPrinter() {
        String mac = "D6:CC:41:05:2A:01";//macInput.getText().toString().trim();
        if (mac.isEmpty()) { toast("Enter printer MAC address"); return; }

        new Thread(() -> {
            try {
                if (!btAdapter.isEnabled()) {
                    runOnUiThread(() -> toast("Enable Bluetooth and try again"));
                    return;
                }
                BluetoothDevice device = btAdapter.getRemoteDevice(mac);
                // Close previous
                closeSocketQuietly();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(() -> toast("Grant BLUETOOTH_CONNECT permission"));
                    return;
                }

                socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
                btAdapter.cancelDiscovery();
                socket.connect();

                output = socket.getOutputStream();

                runOnUiThread(() -> {
                    toast("Connected");
                    btnPrint.setEnabled(true);
                });
            } catch (IOException e) {
                closeSocketQuietly();
                runOnUiThread(() -> toast("Connection failed: " + e.getMessage()));
            }
        }).start();
    }

    private void printTest() {
        if (output == null) { toast("Not connected"); return; }
        new Thread(() -> {
            try {
                EscPos p = new EscPos(output, "GBK");

                p.write(EscPos.INIT)
                        .write(EscPos.ALIGN_CENTER)
                        .write(EscPos.BOLD_ON)
                        .text("Mini BT Printer Demo").nl()
                        .write(EscPos.BOLD_OFF)
                        .nl()
                        .write(EscPos.ALIGN_LEFT)
                        .text("Date: ").text(String.valueOf(System.currentTimeMillis())).nl()
                        .text("Order: #12345").nl()
                        .text("------------------------------").nl()
                        .text("Item A           x2   120.00").nl()
                        .text("Item B           x1    60.00").nl()
                        .text("------------------------------").nl()
                        .write(EscPos.ALIGN_RIGHT)
                        .write(EscPos.BOLD_ON)
                        .text("TOTAL    180.00").nl()
                        .write(EscPos.BOLD_OFF)
                        .write(EscPos.ALIGN_CENTER)
                        .nl().text("Thank you!").nl().nl().nl();

                // Cut (only if your printer has a cutter)
                p.write(EscPos.CUT_FULL);

//                String charset = "UTF-8";
//                output.write(new byte[]{0x1B, 0x40}); // Initialize
//                output.write(new byte[]{0x1B, 0x61, 0x01}); // Center align
//                output.write("PRINT TEST\n".getBytes(charset));
//                output.write(new byte[]{0x1B, 0x61, 0x00}); // Left align
//                output.write("Line 1\n".getBytes(charset));
//                output.write("Line 2\n".getBytes(charset));
//                output.write("\n\n\n".getBytes(charset)); // Feed
//
//                output.write(new byte[]{0x1D, 0x56, 0x00}); // Full cut, ESC/POS
//                output.write("\n".getBytes(charset));


                output.flush();


                runOnUiThread(() -> toast("Printed"));
                output.close();
            } catch (IOException e) {
                runOnUiThread(() -> toast("Print failed: " + e.getMessage()));
            }
        }).start();
    }

    private void toast(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    private void closeSocketQuietly() {
        try { if (output != null) output.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        output = null; socket = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeSocketQuietly();
    }
}