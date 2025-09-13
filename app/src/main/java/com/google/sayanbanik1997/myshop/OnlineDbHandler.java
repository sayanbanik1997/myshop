package com.google.sayanbanik1997.myshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OnlineDbHandler {
    static  boolean rsltBool;
    static SharedPreference sharedPreference;
    protected static void updateDbData(Context context, String tbl, String[] rowsNeedToBeChanged, String[] data, String id){    //  boolean[] quetation,

        sharedPreference = new SharedPreference(context);

        String qry = "insert into "+ tbl +" (`shopId`, `userId`, " ;
        for(int i=0; i<rowsNeedToBeChanged.length; i++) {
            qry+= "`"+ rowsNeedToBeChanged[i] +"`,";
        }
        qry += "`updatedFrom`) values ("+ sharedPreference.getData("shopId")+", "+sharedPreference.getData("userId")+", " ;
        for(int i=0; i<rowsNeedToBeChanged.length; i++) {
//            if(quetation[i]){
//                qry+="'";
//            }
            qry+=  data[i] ;
//            if(quetation[i]){
//                qry+="'";
//            }
            qry+=",";
        }
        qry+=id+ ")";
        String finalQry = qry;
        new VolleyTakeData(context, new String[]{"qry", "tblName"}, new String[]{
                finalQry, tbl
        }) {
            @Override
            protected void doAfterTakingData(String response) {
                try {
                    JSONObject responseFromServerAfterInsertJsonObj = new JSONObject(response);
                    if (Integer.parseInt(responseFromServerAfterInsertJsonObj.getString("lastId"))>0){
                        String qry = "update "+tbl+" set `updated` = "+ responseFromServerAfterInsertJsonObj.getString("lastId") +" where `id`="+ id;
                        //Toast.makeText(context, qry, Toast.LENGTH_SHORT).show();
                        //Log.d("aaa", "OnlineDbHandler "+ qry);
                        new VolleyTakeData(context, new String[]{"qry", "tblName"}, new String[]{
                                qry, tbl
                        }) {
                            @Override
                            protected void doAfterTakingData(String response) {
                                try {
                                    JSONObject responseFromServerAfterUpdatingJsonObj = new JSONObject(response);
                                    if (Boolean.parseBoolean(responseFromServerAfterUpdatingJsonObj.getString("updOrDelBool"))){
                                        Toast.makeText(context, context.getString(R.string.Successfully_updated), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "error 02872-86424", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("aaa","OnlineDbHandler "+ response);
                                    Log.d("aaa","OnlineDbHandler "+ e);
                                    Toast.makeText(context, "Json error 972420", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                    }else {
                        Toast.makeText(context, "error 0270028-1", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("aaa","OnlineDbHandler "+ response);
                    Log.d("aaa","OnlineDbHandler "+ e);
                    Log.d("aaa","OnlineDbHandler "+ finalQry);
                    Toast.makeText(context, "Json error 0749827", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    static String getOriginalId(ArrayList<HashMap<String, String>> al, HashMap<String ,HashMap<String, String>> hm, String id){
        if(hm == null) {
            hm  = new HashMap<>();
            for (int i = 0; i < al.size(); i++) {
                hm.put(al.get(i).get("id"), al.get(i));
            }
        }
        if(hm.get(id).get("updatedFrom").equals("null")){
            return id;
        }else{
            return getOriginalId(al, hm, hm.get(id).get("updatedFrom"));
        }
    }
    static HashMap<String, String> getUpdatedRowInfo(ArrayList<HashMap<String, String>> al, HashMap<String ,HashMap<String, String>> hm, String id){
        if(hm == null) {
            hm  = new HashMap<>();
            for (int i = 0; i < al.size(); i++) {
                hm.put(al.get(i).get("id"), al.get(i));
            }
        }
        if(hm.get(id).get("updated").equals("null")){
            return hm.get(id);
        }else{
            return getUpdatedRowInfo(al, hm, hm.get(id).get("updated"));
        }
    }
    static ArrayList<HashMap<String, String>> omitDeletedOrUpdatedRows(ArrayList<HashMap<String, String>> totalAl){
        ArrayList<HashMap<String, String >> prodAl = new ArrayList<>();
        for(int i=0; i<totalAl.size();i++){
            if(totalAl.get(i).get("updated").equals("null")){
                prodAl.add(totalAl.get(i));
            }
        }
        return prodAl;
    }
}


abstract class VolleyTakeData {

    protected abstract void doAfterTakingData(String response);
    protected ArrayList<String> rslt;
    VolleyTakeData(Context c, String[] tag, String[] data){
        ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Wait ... ");
        progressDialog.setMessage("Gettitng data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(c);
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                Other.url+ "dbOperation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        doAfterTakingData(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(c, data[0], Toast.LENGTH_LONG).show();
                        Log.d("aaa", "Volley sent data :   "+data[0]);
                    }
                }
        ){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                for(int i=0;i<tag.length; i++) {
                    hashMap.put(tag[i], data[i]);
                }
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

}