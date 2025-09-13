package com.google.sayanbanik1997.myshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Other {

    static final String url = "http://10.188.152.212/me/myShop/";

    static char[] problematicCharArr = new char[]{
            '"', '\'','\\'  // atmost 10 elements are possible otherwise encryption code need to be changed
    };

    protected  static String process(String input){
        return encrypt(input.trim().toLowerCase());
    }
    protected static String encrypt(String input){
        String output="";
        input= input.trim();
        for (int i=0; i<input.length(); i++){
            int probCharInd = -1;
            for(int j=0; j<problematicCharArr.length; j++){
                if(input.charAt(i)== problematicCharArr[j]){
                    probCharInd=j;
                    break;
                }
            }
            if(probCharInd!= -1){
                output+= "1"+ probCharInd;
            }else {
                output+= "0" + input.charAt(i);
            }
        }
        return output;
    }

    protected static String decrypt(String input){
        String output="";
        for (int i=0; i<input.length()/2; i++) {
            if(input.charAt(i*2)=='0'){
                output+= input.charAt(i*2+1);
            }else {
                output+= problematicCharArr[Integer.parseInt(Character.toString(input.charAt(i*2+1)))];
            }
        }
        return output;
    }
}
//  readme
/*

when i insert data to database api gives me last inserted data
 but sometimes it may not be the same that i inserted last
 */

abstract class BackGroundThreadForDoSomeWorkAfterSomeTime{
    BackGroundThreadForDoSomeWorkAfterSomeTime(Context context, int timeAfterWhichWorkWillBeDone){
        HandlerThread backgroundThread = new HandlerThread("MyBackgroundThread");
        backgroundThread.start();

        // 2. Create a Handler for that thread
        Handler backgroundHandler = new Handler(backgroundThread.getLooper());

        // 3. Post work to background thread
        backgroundHandler.post(new Runnable() {
            ProgressDialog progressDialog;
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Wait ... ");
                        progressDialog.setMessage("Gettitng data");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }
                });

                try {
                    Thread.sleep(timeAfterWhichWorkWillBeDone);
                } catch (InterruptedException e) {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        workToDo();
                        backgroundThread.quitSafely();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
    abstract void workToDo();
}