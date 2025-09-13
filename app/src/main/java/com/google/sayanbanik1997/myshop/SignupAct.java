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

import org.json.JSONException;
import org.json.JSONObject;

public class SignupAct extends AppCompatActivity {

    EditText nameEdt, phEdt,emailEdt,passEdt;
    Button signupBtn;
    TextView loginInsteadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEdt= findViewById(R.id.nameEdt);
        phEdt= findViewById(R.id.phEdt);
        emailEdt= findViewById(R.id.emailEdt);
        signupBtn = findViewById(R.id.signupBtn);
        loginInsteadTxt= findViewById(R.id.loginInsteadTxt);
        passEdt = findViewById(R.id.passEdt);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEdt.getText().toString().isEmpty()){
                    Toast.makeText(SignupAct.this, getApplicationContext().getString(R.string.Name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nameEdt.getText().toString().length()>30){
                    Toast.makeText(SignupAct.this, getApplicationContext().getString(R.string.Name_cannot_be_so_big), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phEdt.getText().toString().length()!=10){
                    Toast.makeText(SignupAct.this, getApplicationContext().getString(R.string.Enter_a_valid_ph_no), Toast.LENGTH_SHORT).show();
                    //return;
                }
                if(emailEdt.getText().toString().length()>30){
                    Toast.makeText(SignupAct.this, getApplicationContext().getString(R.string.Email_cannot_be_so_big), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passEdt.getText().toString().length()>30){
                    Toast.makeText(SignupAct.this, getApplicationContext().getString(R.string.Password_cannot_be_so_big), Toast.LENGTH_SHORT).show();
                    return;
                }

                new VolleyTakeData(getApplicationContext(), new String[]{"qry", "tblName"},
                        new String[]{"insert into user_tbl (`name`, `phno`, `email`, `pass`) values ('"+ Other.process(nameEdt.getText().toString())+"'," +
                                "'"+ Other.process(phEdt.getText().toString())+"','" + Other.process(emailEdt.getText().toString())+"', '"+ Other.process(passEdt.getText().toString()) +"')",
                        "user_tbl"}){
                    @Override
                    protected void doAfterTakingData(String response) {
                        try {
                            //Toast.makeText(SignupAct.this, "enter", Toast.LENGTH_SHORT).show();
                            JSONObject responseJsonObj = new JSONObject(response);
                            if(responseJsonObj.getString("name").equals(Other.process(nameEdt.getText().toString())) &&
                                    responseJsonObj.getString("phno").equals(Other.process(phEdt.getText().toString())) &&
                                    responseJsonObj.getString("email").equals(Other.process(emailEdt.getText().toString())) &&
                                    responseJsonObj.getString("pass").equals(Other.process(passEdt.getText().toString()))){
                                SharedPreference sharedPreference = new SharedPreference(getApplicationContext());
                                sharedPreference.setData("userId", responseJsonObj.getString("id"));

                                Toast.makeText(SignupAct.this,getApplicationContext().getString(R.string.User_successfully_added), Toast.LENGTH_SHORT).show();
                                Intent logInIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(logInIntent);
                            }
                        } catch (JSONException e) {
                            Log.d("aaa", "SignupAct "+ response);
                            Toast.makeText(SignupAct.this, "Error while parsing json", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
            }
        });

        loginInsteadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInIntent = new Intent(getApplicationContext(), LoginAct.class);
                startActivity(logInIntent);
            }
        });
    }
}