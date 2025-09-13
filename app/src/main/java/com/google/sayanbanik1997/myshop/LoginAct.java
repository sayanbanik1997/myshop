package com.google.sayanbanik1997.myshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginAct extends AppCompatActivity {
    EditText idEdt,passEdt;
    Button loginBtn;
    TextView forgotIdTxt,forgotPassTxt, signupInsteadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        forgotIdTxt = findViewById(R.id.forgotIdTxt);
        idEdt = findViewById(R.id.idEdt);
        passEdt = findViewById(R.id.passEdt);
        forgotPassTxt = findViewById(R.id.forgotPassTxt);
        loginBtn = findViewById(R.id.loginBtn);
        signupInsteadTxt = findViewById(R.id.signupInsteadTxt);

        SharedPreference sharedPreference = new SharedPreference(getApplicationContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idEdt.getText().toString().isEmpty()){
                    Toast.makeText(LoginAct.this, getApplicationContext().getString(R.string.Id_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                new VolleyTakeData(getApplicationContext(), new String[]{"qry"}, new String[]{
                        "select * from user_tbl where id=" + idEdt.getText().toString() + " and pass='" + Other.encrypt(passEdt.getText().toString())+"'"
                }) {
                    @Override
                    protected void doAfterTakingData(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            if(jsonArray.length()>0){
                                sharedPreference.setData("userId", jsonArray.getJSONObject(0).getString("id"));
                                Intent intent = new Intent(getApplicationContext(), ChooseOrCreateShopAct.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginAct.this, getApplicationContext().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginAct.this, "json error 0287428", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });

        signupInsteadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInIntent = new Intent(getApplicationContext(), SignupAct.class);
                startActivity(logInIntent);
            }
        });
    }
}