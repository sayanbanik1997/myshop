package com.google.sayanbanik1997.myshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class EtFrag extends Fragment {
    EditText nameEdt;
    RecyclerView review;
    Button submitBtn;
    Bundle bundle;
    ArrayList<HashMap<String, String >> totalAl = new ArrayList<>();
    SharedPreference sharedPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_et, container, false);
        
        sharedPreference = new SharedPreference(getContext());
        
        bundle = this.getArguments();

        nameEdt = view.findViewById(R.id.nameEdt);
        review = view.findViewById(R.id.review);
        submitBtn = view.findViewById(R.id.submitBtn);

        submitBtn.setVisibility(View.INVISIBLE);

        review.setLayoutManager(new LinearLayoutManager(getContext()));



        JSONArray originalDataForReview=null;
        if(bundle.getString("fragType").equals("prod")){
            new VolleyTakeData(getContext(), new String[]{"qry"}, new String[]{
                    "select * from prod_tbl where shopId = "+sharedPreference.getData("shopId")+" order by id desc"
            }) {
                @Override
                protected void doAfterTakingData(String response) {
                    try {
                        JSONArray prodJsonArr =new JSONArray(response);
                        for(int i=0; i< prodJsonArr.length(); i++){
                            HashMap<String, String> eachProdHm = new HashMap<>();
                            JSONObject eachProdJsonObj = prodJsonArr.getJSONObject(i);
                            eachProdHm.put("id", eachProdJsonObj.getString("id"));
                            eachProdHm.put("name", Other.decrypt(eachProdJsonObj.getString("name")));
                            eachProdHm.put("datetime", eachProdJsonObj.getString("datetime"));
                            eachProdHm.put("updated", eachProdJsonObj.getString("updated"));
                            eachProdHm.put("updatedFrom", eachProdJsonObj.getString("updatedFrom"));
                            totalAl.add(eachProdHm);
                        }
                        setDataToReview(OnlineDbHandler.omitDeletedOrUpdatedRows(totalAl));
                    } catch (JSONException e) {
                        Log.d("aaa", "EtFrag------------"+response);
                        Toast.makeText(getContext(), "json error 02740-1", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        } else if(bundle.getString("fragType").equals("cusSup")){
            new VolleyTakeData(getContext(), new String[]{"qry"}, new String[]{
                    "select * from cus_sup_tbl order by id desc"
            }) {
                @Override
                protected void doAfterTakingData(String response) {
                    try {
                        JSONArray cusSupJsonArr =new JSONArray(response);
                        for(int i=0; i< cusSupJsonArr.length(); i++){
                            HashMap<String, String> eachCusSupHm = new HashMap<>();
                            JSONObject eachProdJsonObj = cusSupJsonArr.getJSONObject(i);
                            eachCusSupHm.put("id", eachProdJsonObj.getString("id"));
                            eachCusSupHm.put("name", Other.decrypt(eachProdJsonObj.getString("name")));
                            eachCusSupHm.put("datetime", eachProdJsonObj.getString("datetime"));
                            eachCusSupHm.put("updated", eachProdJsonObj.getString("updated"));
                            eachCusSupHm.put("updatedFrom", eachProdJsonObj.getString("updatedFrom"));
                            totalAl.add(eachCusSupHm);
                        }
                        setDataToReview(OnlineDbHandler.omitDeletedOrUpdatedRows(totalAl));
                    } catch (JSONException e) {
                        Log.d("aaa", "EtFrag------------"+response);
                        Toast.makeText(getContext(), "json error 02740-1", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }

        return view;
    }

    protected void setDataToReview(ArrayList<HashMap<String, String>> dataAl){
        review.setAdapter( new RecyAdapter( R.layout.each_edt_del_btn_layout, dataAl.size()){
            @Override
            void bind(Vh holder, int position) {
                EditText nameEdt = ((EditText) holder.arrView.get(0));
                HashMap<String, String> eachItemHm=null;
                eachItemHm = dataAl.get(position);
                nameEdt.setText(eachItemHm.get("name"));
                HashMap<String, String> finalEachItemHm = eachItemHm;
                nameEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b && !finalEachItemHm.get("name").equals(nameEdt.getText().toString())){
                            String qry="", tbl="";
                            if(bundle.getString("fragType").equals("prod")){
                                tbl = "prod_tbl";
                            } else if (bundle.getString("fragType").equals("cusSup")) {
                                tbl = "cus_sup_tbl";
                            }
                            OnlineDbHandler.updateDbData(getContext(),tbl,new String[]{"name","datetime"}, new String[]{"'"+Other.encrypt(nameEdt.getText().toString())+"'", "'"+LocalDate.now()+" "+  LocalTime.now().truncatedTo(ChronoUnit.SECONDS)+"'"},  finalEachItemHm.get("id"));   // new boolean[]{true, true},
                        }
                    }
                });

            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.nameEdt));
                        arrView.add(itemView.findViewById(R.id.delBtn));
                    }
                };
            }
        });
    }
}