package com.google.sayanbanik1997.myshop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BillAndPaymentFrag extends Fragment {

    EditText searchBillAndPaymentEdt;
    RecyclerView billAndPaymentReview;
    ArrayList<HashMap<String, ?>> billAndPaymentAl=new ArrayList<>();
    SharedPreference sharedPreference;
    ArrayList<HashMap<String, String>> cusSupAl= new ArrayList<>();
    int[] cusSupArr, prodArr;
    ArrayList<HashMap<String, String>> prodAl= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_bill_and_payment, container, false);

        searchBillAndPaymentEdt = view.findViewById(R.id.searchBillAndPaymentEdt);
        billAndPaymentReview = view.findViewById(R.id.billAndPaymentReview); billAndPaymentReview.setLayoutManager(new LinearLayoutManager(getContext()));


        sharedPreference= new SharedPreference(getContext());

        new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                "select * from prod_tbl where shopId="+ sharedPreference.getData("shopId"), "prod_tbl"
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    JSONArray prodRespJsonArr = new JSONArray(response);
                    prodArr= new int[prodRespJsonArr.length()];
                    for(int i=0; i<prodRespJsonArr.length();i++){
                        JSONObject prodRespJsonObj = prodRespJsonArr.getJSONObject(i);
                        prodAl.add(new HashMap<>());
                        prodAl.get(i).put("id", prodRespJsonObj.getString("id"));
                        prodAl.get(i).put("name", Other.decrypt(prodRespJsonObj.getString("name")));
                        prodAl.get(i).put("updated", prodRespJsonObj.getString("updated"));
                        prodAl.get(i).put("updatedFrom", prodRespJsonObj.getString("updatedFrom"));
                        prodArr[i] = Integer.parseInt(prodAl.get(i).get("id"));
                    }
                } catch (JSONException e) {
                    Log.d("aaa", "--------------------------BillAndPaymentFrag "+e);
                    Toast.makeText(getContext(), "BillAndPaymentFrag json arr error 25205286", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                "select * from cus_sup_tbl where shopId="+ sharedPreference.getData("shopId"), "cus_sup_tbl"
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    JSONArray cusSupRespJsonArr = new JSONArray(response);
                    cusSupArr= new int[cusSupRespJsonArr.length()];
                    for(int i=0; i<cusSupRespJsonArr.length();i++){
                        JSONObject cusSupRespJsonObj = cusSupRespJsonArr.getJSONObject(i);
                        cusSupAl.add(new HashMap<>());
                        cusSupAl.get(i).put("id", cusSupRespJsonObj.getString("id"));
                        cusSupAl.get(i).put("name", Other.decrypt(cusSupRespJsonObj.getString("name")));
                        cusSupAl.get(i).put("updated", cusSupRespJsonObj.getString("updated"));
                        cusSupAl.get(i).put("updatedFrom", cusSupRespJsonObj.getString("updatedFrom"));
                        cusSupArr[i] = Integer.parseInt(cusSupAl.get(i).get("id"));
                    }
                } catch (JSONException e) {
                    Log.d("aaa BillAndPaymentFrag" , e.toString());
                    Toast.makeText(getContext(), "BillAndPaymentFrag json arr error 758636", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                "select * from bill_tbl where updated is null order by datetimeOfEntry", "bill_tbl"
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    JSONArray billResponseJsonArr = new JSONArray(response);
                    new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                            "select * from payment_tbl where updated is null order by datetimeOfEntry", "payment_tbl"
                    }) {
                        @Override
                        protected void doAfterTakingData(String response) {
                            Log.d("aaa", response);
                            try {
                                JSONArray paymentResponseJsonArr = new JSONArray(response);
                                int billCount =0, paymentCount = 0;
                                for (; billCount<billResponseJsonArr.length() && paymentCount<paymentResponseJsonArr.length(); ){
                                    try {
                                        JSONObject billResponseJsonObj = billResponseJsonArr.getJSONObject(billCount);
                                        JSONObject paymentResponseJsonObj = paymentResponseJsonArr.getJSONObject(paymentCount);

                                        HashMap eachBillOrPaymentInfoHm = new HashMap<>();

                                        //Toast.makeText(getContext(), Datetime.parseDateTime( billResponseJsonObj.getString("datetimeOfEntry"))+" "+(Datetime.parseDateTime(paymentResponseJsonObj.getString("datetimeOfEntry"))), Toast.LENGTH_SHORT).show();

                                        if(Datetime.parseDateTime( billResponseJsonObj.getString("datetimeOfEntry")).isAfter(Datetime.parseDateTime(paymentResponseJsonObj.getString("datetimeOfEntry")))){
                                            //Toast.makeText(getContext(), billCount+ " " + paymentCount+ " enter "+paymentResponseJsonObj.getString("id"), Toast.LENGTH_SHORT).show();
                                            eachBillOrPaymentInfoHm.put("paymentId", paymentResponseJsonObj.getString("id"));
                                            eachBillOrPaymentInfoHm.put("cusSupId", paymentResponseJsonObj.getString("cusSupId"));
                                            eachBillOrPaymentInfoHm.put("datetimeOfEntry", paymentResponseJsonObj.getString("datetimeOfEntry"));
                                            eachBillOrPaymentInfoHm.put("amount", paymentResponseJsonObj.getString("amount"));
                                            billAndPaymentAl.add(eachBillOrPaymentInfoHm);
                                            paymentCount++;
                                        }else{
                                            billAndPaymentAl.add(insertBillDataIntoHm(billResponseJsonObj));
                                            billCount++;
                                        }
                                    } catch (JSONException e) {
                                        Log.d("aaa" , e.toString());
                                        Toast.makeText(getContext(), "BillAndPaymentFrag Json Obj error 142525 ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                for (; billCount<billResponseJsonArr.length(); billCount++){
                                   JSONObject billResponseJsonObj = billResponseJsonArr.getJSONObject(billCount);
                                   billAndPaymentAl.add(insertBillDataIntoHm(billResponseJsonObj));
                                }

                                for (; paymentCount<paymentResponseJsonArr.length(); paymentCount++){
                                    JSONObject paymentResponseJsonObj = paymentResponseJsonArr.getJSONObject(paymentCount);

                                    HashMap eachBillOrPaymentInfoHm = new HashMap<>();
                                    eachBillOrPaymentInfoHm.put("paymentId", paymentResponseJsonObj.getString("id"));
                                    eachBillOrPaymentInfoHm.put("cusSupId", paymentResponseJsonObj.getString("cusSupId"));
                                    eachBillOrPaymentInfoHm.put("datetimeOfEntry", paymentResponseJsonObj.getString("datetimeOfEntry"));
                                    eachBillOrPaymentInfoHm.put("amount", paymentResponseJsonObj.getString("amount"));

                                    billAndPaymentAl.add(eachBillOrPaymentInfoHm);
                                }
                                //Toast.makeText(getContext(), "enter", Toast.LENGTH_SHORT).show();

                                new BackGroundThreadForDoSomeWorkAfterSomeTime(getContext(), 2000){
                                    @Override
                                    void workToDo() {
                                        setDataToReview("");
                                        billAndPaymentReview.scrollToPosition(billAndPaymentAl.size() - 1);
                                    }
                                };

                            } catch (JSONException e) {
                                Log.d("aaa " , "billAndPaymentFrag"+e.getMessage());
                                Toast.makeText(getContext(), "billAndPaymentFrag jsonarr error 637373 "+e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "billAndPaymentFrag jsonarr error 9619579250", Toast.LENGTH_SHORT).show();
                }
            }
        };

        searchBillAndPaymentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void setDataToReview(String likeStr){
        billAndPaymentReview.setAdapter(new RecyAdapter( R.layout.each_bill_and_payment_in_review, billAndPaymentAl.size()){
            @Override
            void bind(Vh holder, int position) {
                if(billAndPaymentAl.get(position).containsKey("billId")) {
                    ((TextView) holder.arrView.get(0)).setText(billAndPaymentAl.get(position).get("billId").toString());
                    ArrayList eachItemOfBillTblAl = (ArrayList) billAndPaymentAl.get(position).get("eachItemOfBillTblAl");
                    String prodListStr = "";
                    for (int i=0; i<eachItemOfBillTblAl.size(); i++){
                        prodListStr+=OnlineDbHandler.getUpdatedRowInfo(
                                prodAl, null, ((HashMap)eachItemOfBillTblAl.get(i)).get("prodId").toString()
                        ).get("name")+" ,  ";

//                                prodAl.get(
//                                Arrays.binarySearch(
//                                        ).get("name")+" ,  ";
                    }
                    ((TextView) holder.arrView.get(3)).setText(prodListStr);
                    double total =0d;
                    for(int i=0; i<((ArrayList)billAndPaymentAl.get(position).get("eachItemOfBillTblAl")).size(); i++){
                        double quan=Double.parseDouble(((HashMap)((ArrayList)billAndPaymentAl.get(position).get("eachItemOfBillTblAl")).get(i)).get("quan").toString());
                        double price=Double.parseDouble(((HashMap)((ArrayList)billAndPaymentAl.get(position).get("eachItemOfBillTblAl")).get(i)).get("price").toString());
                        total+= quan* price;
                    }
                    ((TextView) holder.arrView.get(4)).setText(total+"");
                    ((TextView) holder.arrView.get(5)).setText(billAndPaymentAl.get(position).get("haveToPay").toString());
                }else{
                    ((TextView) holder.arrView.get(0)).setText(billAndPaymentAl.get(position).get("paymentId").toString());
                    ((TextView) holder.arrView.get(5)).setText(billAndPaymentAl.get(position).get("amount").toString());
                    ((TextView) holder.arrView.get(3)).setVisibility(View.INVISIBLE);
                }
                if(billAndPaymentAl.get(position).get("cusSupId").equals("0")){
                    ((TextView) holder.arrView.get(1)).setText(getContext().getString(R.string.Unknown));
                }else {
                    ((TextView) holder.arrView.get(1)).setText(
//                            cusSupAl.get(
//                                    Arrays.binarySearch(
//                                            cusSupArr, Integer.parseInt(   ))
//                            ).get("name");
                            OnlineDbHandler.getUpdatedRowInfo(cusSupAl, null, billAndPaymentAl.get(position).get("cusSupId").toString()).get("name"));
                }
                ((TextView) holder.arrView.get(2)).setText(billAndPaymentAl.get(position).get("datetimeOfEntry").toString());
//                if(position==billAndPaymentAl.size()-1){
//                    ((TextView) holder.arrView.get(2)).requestFocus();
//                }
            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.billOrPaymentIdTxt));
                        arrView.add(itemView.findViewById(R.id.cusSupNameTxt));
                        arrView.add(itemView.findViewById(R.id.datetimeTxt));
                        arrView.add(itemView.findViewById(R.id.prodListTxt));
                        arrView.add(itemView.findViewById(R.id.totalTxt));
                        arrView.add(itemView.findViewById(R.id.haveToPayOrPaymentAmountTxt));
                    }
                };
            }
        });
    }
    private HashMap insertBillDataIntoHm(JSONObject billResponseJsonObj) {
        HashMap eachBillOrPaymentInfoHm = new HashMap();
        try {
            eachBillOrPaymentInfoHm.put("billId", billResponseJsonObj.getString("id"));
            eachBillOrPaymentInfoHm.put("cusSupId", billResponseJsonObj.getString("cusSupId"));
            eachBillOrPaymentInfoHm.put("datetimeOfEntry", billResponseJsonObj.getString("datetimeOfEntry"));
            eachBillOrPaymentInfoHm.put("haveToPay", billResponseJsonObj.getString("haveToPay"));
            new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                    "select * from each_item_of_bill_tbl where updated is null and billId = " + billResponseJsonObj.getString("id"), "each_item_of_bill_tbl"
            }) {
                @Override
                protected void doAfterTakingData(String response) {
                    ArrayList<HashMap<String, String>> eachItemOfBillTblAl = new ArrayList<>();
                    try {
                        JSONArray eachItemOfBillTblJsonArr = new JSONArray(response);
                        for (int i = 0; i < eachItemOfBillTblJsonArr.length(); i++) {
                            JSONObject eachItemOfBillTblJsonObj =eachItemOfBillTblJsonArr.getJSONObject(i);
                            eachItemOfBillTblAl.add(new HashMap<>());
                            eachItemOfBillTblAl.get(i).put("id", eachItemOfBillTblJsonObj.getString("id"));
                            eachItemOfBillTblAl.get(i).put("prodId", eachItemOfBillTblJsonObj.getString("prodId"));
                            eachItemOfBillTblAl.get(i).put("quan", eachItemOfBillTblJsonObj.getString("quan"));
                            eachItemOfBillTblAl.get(i).put("price", eachItemOfBillTblJsonObj.getString("price"));
                        }
                        eachBillOrPaymentInfoHm.put("eachItemOfBillTblAl", eachItemOfBillTblAl);
//                        Toast.makeText(getContext(),
//                                prodAl.get(Arrays.binarySearch(prodArr, Integer.parseInt(((HashMap)eachItemOfBillTblAl.get(0)).get("prodId").toString()))).get("name")+""
//                                , Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.d("aaa", "BillAndPaymentFrag"+ e );
                        Log.d("aaa", "BillAndPaymentFrag"+ response);
                        Toast.makeText(getContext(), "billAndPaymentFrag jsonarr error 72052521", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }catch (Exception e){
            Log.d("aaa", "billAndPaymentFrag "+ e.toString());
            Toast.makeText(getContext(), "BillAndPaymentFrag   json Obj error 214101", Toast.LENGTH_SHORT).show();
        }
        return eachBillOrPaymentInfoHm;
    }
}