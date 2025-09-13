package com.google.sayanbanik1997.myshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class ProfileFrag extends Fragment {
    TextView nameTxt, changeShopTxt, logoutTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreference sharedPreference = new SharedPreference(getContext());

        nameTxt = view.findViewById(R.id.nameTxt);
        changeShopTxt = view.findViewById(R.id.changeShopTxt);
        logoutTxt = view.findViewById(R.id.logoutTxt);

        new VolleyTakeData(getContext(), new String[]{"qry"}, new String[]{
                "select * from user_tbl where id="+ sharedPreference.getData("userId")
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    nameTxt.setText(Other.decrypt((new JSONArray(response)).getJSONObject(0).getString("name")));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "json error 982467", Toast.LENGTH_SHORT).show();
                }
            }
        };

        changeShopTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreference.removeData("shopId");
                Intent intent = new Intent(getContext(), ChooseOrCreateShopAct.class);
                startActivity(intent);
            }
        });

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreference.removeData("userId");
                sharedPreference.removeData("shopId");
                Intent intent = new Intent(getContext(), LoginAct.class);
                startActivity(intent);
            }
        });


        return view;
    }
}