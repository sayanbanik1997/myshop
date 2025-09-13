package com.google.sayanbanik1997.myshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;



public class ChooseOrCreateShopAct extends AppCompatActivity {
    TextView userNameTxt,logoutTxt   ;
    Button createNewShopBtn ;
    EditText newShopNameEdt ;
    RecyclerView showShopsReview;
    SharedPreference sharedPreference;
    RecyAdapter shopsRecyAdapter  ;
    JSONArray userPermissionListJsonArr;
    ArrayList<HashMap<String, String>> userPermissionAl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_or_create_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            userPermissionListJsonArr = new JSONArray("[]");
        } catch (JSONException e) {
            Toast.makeText(this, "error 97562", Toast.LENGTH_SHORT).show();
        }


        userNameTxt = findViewById(R.id.userNameTxt);
        createNewShopBtn = findViewById(R.id.createNewShopBtn);
        logoutTxt = findViewById(R.id.logoutTxt);
        newShopNameEdt = findViewById(R.id.newShopNameEdt);
        showShopsReview = findViewById(R.id.showShopsReview);

        sharedPreference = new SharedPreference(getApplicationContext());

        new VolleyTakeData(getApplicationContext(), new String[]{"qry"}, new String[]{
                "select * from user_tbl where id=" + sharedPreference.getData("userId")
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    userNameTxt.setText(Other.decrypt((new JSONArray(response)).getJSONObject(0).getString("name")));
                } catch (JSONException e) {
                    Toast.makeText(ChooseOrCreateShopAct.this, "error while parsing json", Toast.LENGTH_SHORT).show();
                }
            }
        };

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreference.removeData("userId");
                Intent signupIntent = new Intent(getApplicationContext(), LoginAct.class);
                startActivity(signupIntent);
            }
        });

        createNewShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newShopNameEdt.getText().toString().isEmpty()){
                    Toast.makeText(ChooseOrCreateShopAct.this, getApplicationContext().getString(R.string.Shop_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                new VolleyTakeData(getApplicationContext(), new String[]{"qry", "tblName"}, new String[]{
                        "insert into shop_tbl (`name`, `datetime`) values ('" + Other.process(newShopNameEdt.getText().toString()) + "', '"+ Datetime.datetimeNowStr() +"')",
                        "shop_tbl"
                }) {
                    @Override
                    protected void doAfterTakingData(String response) {
                        JSONObject createdNewShopJsonObj=null;
                        try {
                            createdNewShopJsonObj = new JSONObject(response);
                            if(createdNewShopJsonObj.getString("name").equals(Other.process(newShopNameEdt.getText().toString()))){
                                JSONObject finalCreatedNewShopJsonObj = createdNewShopJsonObj;
                                String qry = "insert into user_permission_tbl (`shopId`, `userId`, `attribute`, `dateTime`) values ("+
                                        finalCreatedNewShopJsonObj.getString("id") + "," + sharedPreference.getData("userId") + ",'creater','" + LocalDateTime.now()+"')";
                                new VolleyTakeData(getApplicationContext(), new String[]{"qry", "tblName"}, new String[]{
                                        qry, "user_permission_tbl"
                                }) {
                                    @Override
                                    protected void doAfterTakingData(String response) {
                                        try {
                                            JSONObject userPermissionJsonObj = new JSONObject(response);
                                            if(userPermissionJsonObj.getString("shopId").equals(finalCreatedNewShopJsonObj.getString("id")) &&
                                                    userPermissionJsonObj.getString("userId").equals(sharedPreference.getData("userId") )&&
                                                    userPermissionJsonObj.getString("attribute").equals("creater" )
                                                    ){
                                                Toast.makeText(ChooseOrCreateShopAct.this, getApplicationContext().getString(R.string.Shop_created_successfully), Toast.LENGTH_SHORT).show();

                                                userPermissionAl.add(new HashMap<>());
                                                userPermissionAl.get(userPermissionAl.size()-1).put("id", userPermissionJsonObj.getString("lastId"));
                                                userPermissionAl.get(userPermissionAl.size()-1).put("shopId", userPermissionJsonObj.getString("shopId"));
                                                userPermissionAl.get(userPermissionAl.size()-1).put("shopName", newShopNameEdt.getText().toString());
                                                userPermissionAl.get(userPermissionAl.size()-1).put("attribute", userPermissionJsonObj.getString("attribute"));
                                                userPermissionAl.get(userPermissionAl.size()-1).put("datetime", userPermissionJsonObj.getString("datetime"));

                                                setAdapter(userPermissionAl);
                                                newShopNameEdt.setText("");
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(ChooseOrCreateShopAct.this, "json error 492894", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ChooseOrCreateShopAct.this, "json error 987902", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
            }
        });


        new VolleyTakeData(getApplicationContext(),  new String[]{"qry"}, new String[]{
                "select * from user_permission_tbl where userId=" + sharedPreference.getData("userId") +" and updated is null"
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    JSONArray userPermissionJsonArr = new JSONArray(response);
                    userPermissionAl= new ArrayList<>(4);
                    for (int i=0; i< userPermissionJsonArr.length(); i++) {
                        JSONObject userPermissionJsonObj = userPermissionJsonArr.getJSONObject(i);
                        userPermissionAl.add(new HashMap<>());
                        userPermissionAl.get(i).put("id", userPermissionJsonObj.getString("id"));
                        userPermissionAl.get(i).put("shopId", userPermissionJsonObj.getString("shopId"));
                        userPermissionAl.get(i).put("attribute", userPermissionJsonObj.getString("attribute"));
                        userPermissionAl.get(i).put("datetime", userPermissionJsonObj.getString("datetime"));
                    }
                    setAdapter(userPermissionAl);
                } catch (JSONException e) {
                    Toast.makeText(ChooseOrCreateShopAct.this, "error while json parsing", Toast.LENGTH_SHORT).show();
                }
            }
        };

        showShopsReview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    protected  void setAdapter(ArrayList<HashMap<String, String>> datasetAl){
        shopsRecyAdapter  = new RecyAdapter( R.layout.each_txt_del_btn_layout, datasetAl.size()){
            @Override
            void bind(Vh holder, int position) {
                String shopId = datasetAl.get(position).get("shopId");
                ////  need to take all shop names at a time         multiple invocation from database
                new VolleyTakeData(getApplicationContext(),  new String[]{"qry"}, new String[]{
                        "select * from shop_tbl where id=" + shopId
                }) {
                    @Override
                    protected void doAfterTakingData(String response) {
                        String shopName=null;
                        try {
                            shopName = Other.decrypt((new JSONArray(response)).getJSONObject(0).getString("name"));
                        } catch (JSONException e) {
                            Toast.makeText(ChooseOrCreateShopAct.this, "error 029875", Toast.LENGTH_SHORT).show();
                        }
                        ((TextView) holder.arrView.get(0)).setText(shopName);
                    }
                };
                ((CardView) holder.arrView.get(2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPreference.setData("shopId", datasetAl.get(position).get("shopId"));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.nameTxt));
                        arrView.add(itemView.findViewById(R.id.delBtn));
                        arrView.add(itemView.findViewById(R.id.eachTextDelBtnCardView));
                    }
                };
            }
        };

        showShopsReview.setAdapter(shopsRecyAdapter);
    }
}