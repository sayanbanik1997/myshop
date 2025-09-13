package com.google.sayanbanik1997.myshop;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EdttxtReviewSubbtnDialog {
    Context context;
    //TextView idTxt;
    EditText nameEdt;
    RecyclerView review;
    Button submitBtn;
    SharedPreference sharedPreference;
    String originalId;
    ArrayList<HashMap<String, String>> originalProdCusSupAl = new ArrayList<>();
    EdttxtReviewSubbtnDialog(Context context, String qry, String tblName){
        this.context=context;
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edt_review_submitbtn_dialogbox_layout);
        dialog.show();

        sharedPreference =new SharedPreference(context);

        nameEdt = dialog.findViewById(R.id.nameEdt);
        review = dialog.findViewById(R.id.review);
        review.setLayoutManager(new LinearLayoutManager(context));

        submitBtn =dialog.findViewById(R.id.submitBtn);

        ArrayList<HashMap<String, String>> prodCusWithoutUpdatedOrDeletedAl = new ArrayList<>();

        final JSONArray[] originalProdCusSupJsonArr = {null};
        new VolleyTakeData(context, new String[]{"qry"}, new String[]{qry }) {
            @Override
            protected void doAfterTakingData(String response) {
//                    ArrayList<JSONObject> prodCusSupAl= new ArrayList<>();
                try {
                    originalProdCusSupJsonArr[0] = new JSONArray(response);
                } catch (JSONException e) {
                    Toast.makeText(context, "Error EdttxtReviewSubbtnDialog 92550", Toast.LENGTH_SHORT).show();
                }
//                    for(int i=0; i< originalProdCusSupJsonArr[0].length(); i++){
//                        try {
//                            prodCusSupAl.add(originalProdCusSupJsonArr[0].getJSONObject(i));
//                        } catch (JSONException e) {
//                            Toast.makeText(context, "billfrag json error 0284092417", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                //Toast.makeText(context, ""+originalProdCusSupJsonArr[0].length(), Toast.LENGTH_SHORT).show();
                for(int i=0; i<originalProdCusSupJsonArr[0].length(); i++){
                    originalProdCusSupAl.add(new HashMap<>());
                    try {
                        originalProdCusSupAl.get(i).put("id", originalProdCusSupJsonArr[0].getJSONObject(i).get("id").toString());
                        originalProdCusSupAl.get(i).put("name", originalProdCusSupJsonArr[0].getJSONObject(i).get("name").toString());
                        originalProdCusSupAl.get(i).put("updated", originalProdCusSupJsonArr[0].getJSONObject(i).get("updated").toString());
                        originalProdCusSupAl.get(i).put("updatedFrom", originalProdCusSupJsonArr[0].getJSONObject(i).get("updatedFrom").toString());
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error EdttxtReviewSubbtnDialog 950252850", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(context, ""+originalProdCusSupAl.size(), Toast.LENGTH_SHORT).show();
                ArrayList<HashMap<String, String>> prodCusWithoutUpdatedOrDeletedAl =  OnlineDbHandler.omitDeletedOrUpdatedRows(originalProdCusSupAl);

                setProdOrCusSupDataIntoReview( prodCusWithoutUpdatedOrDeletedAl);

            }
        };

        ArrayList<HashMap<String, String>> prodCusLikeEdtAl = new ArrayList<>();

        nameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {                    }@Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {                    }

            @Override
            public void afterTextChanged(Editable editable) {
                originalId="";
                ArrayList<JSONObject> prodCusSupAl= new ArrayList<>();//new JSONArray(originalProdCusSupJsonArr[0].toString());
                for(int i=0; i<  prodCusWithoutUpdatedOrDeletedAl.size(); i++){
                    if(Other.decrypt(prodCusWithoutUpdatedOrDeletedAl.get(i).get("name")).contains(editable.toString())) {
                        prodCusLikeEdtAl.add(prodCusLikeEdtAl.get(i));
                    }
                }
                setProdOrCusSupDataIntoReview(prodCusLikeEdtAl);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEdt.getText().toString().isEmpty()){
                    Toast.makeText(context, context.getString(R.string.Name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                //if(idTxt.getText().toString().equals("id")){
                if(originalId.isEmpty()){
                    String qry = "insert into " + tblName + " (`shopId`, `userId`, `name`, `datetime`) values (" + sharedPreference.getData("shopId") +
                            ", " + sharedPreference.getData("userId") + ", '" + Other.encrypt(nameEdt.getText().toString()) + "', '" + Datetime.datetimeNowStr() + "')";
                    new VolleyTakeData(context, new String[]{"qry", "tblName"}, new String[]{
                            qry, tblName
                    }) {
                        @Override
                        protected void doAfterTakingData(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(Integer.parseInt(jsonObject.getString("lastId"))>0){
                                    originalId = jsonObject.getString("lastId");
                                    doAfterSubbtnClicked();
                                }
                            } catch (JSONException e) {
                                Log.d("aaa", "EdttxtReviewSubbtnDialog "+ response);
                                Toast.makeText(context, "edtTxtReviewSubbtndialog json error 96544529584", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                }else{
                    doAfterSubbtnClicked();
                }
                dialog.dismiss();
            }
        });
    }

    protected void setProdOrCusSupDataIntoReview(ArrayList<HashMap<String, String>> datasetAl){
        RecyAdapter shopsRecyAdapter  = new RecyAdapter( R.layout.each_txt_del_btn_layout, datasetAl.size()){
            @Override
            void bind(Vh holder, int position) {
                ((TextView) holder.arrView.get(0)).setText(Other.decrypt(datasetAl.get(position).get("name")));
                ((CardView)holder.arrView.get(1)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new BackGroundThreadForDoSomeWorkAfterSomeTime(context, 50) {
                            @Override
                            void workToDo()  {
                                originalId=OnlineDbHandler.getOriginalId(originalProdCusSupAl, null, datasetAl.get(position).get("id"));
                            }
                        };
                        //idTxt.setText(datasetAl.get(position).getString("id"));
                        nameEdt.setText(Other.decrypt(datasetAl.get(position).get("name")));
                    }
                });
            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.nameTxt));
                        arrView.add(itemView.findViewById(R.id.eachTextDelBtnCardView));
                    }
                };
            }
        };

        review.setAdapter(shopsRecyAdapter);
    }

    protected abstract void doAfterSubbtnClicked();
    
}
