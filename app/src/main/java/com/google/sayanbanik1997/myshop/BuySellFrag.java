package com.google.sayanbanik1997.myshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BuySellFrag extends Fragment {
    SharedPreference sharedPreference;
    TextView billIdTxt, cusSupNameTxt, buyOrSellTxt, datetimeTxt, totalTxt, paid;
    EditText haveToPayEdt;
    RecyclerView eachBillEntryreview;
    Button saveBtn, qrBtn, printBtn, memBtn;
    ArrayList<HashMap<String, String>> billEntriesAl;
    ArrayList<ArrayList> dataOfFragment;
    View view;
    String cusSupId="0";

    BuySellFrag(ArrayList dataOfFragment){
        this.dataOfFragment = dataOfFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.view!=null) {
           return view;
        }
        view = inflater.inflate(R.layout.fragment_buy_sell, container, false);
        this.view=view;

        billIdTxt = view.findViewById(R.id.billIdTxt);
        cusSupNameTxt = view.findViewById(R.id.cusSupNameTxt);
        buyOrSellTxt = view.findViewById(R.id.buyOrSellTxt);
        datetimeTxt = view.findViewById(R.id.datetimeTxt);
        totalTxt = view.findViewById(R.id.totalTxt);
        eachBillEntryreview = view.findViewById(R.id.eachBillEntryreview);
        eachBillEntryreview.setLayoutManager(new LinearLayoutManager(getContext()));

        haveToPayEdt=view.findViewById(R.id.haveToPayEdt);
        saveBtn=view.findViewById(R.id.saveBtn);
        memBtn=view.findViewById(R.id.memBtn);
        printBtn=view.findViewById(R.id.printBtn);

        sharedPreference= new SharedPreference(getContext());

        cusSupNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EdttxtReviewSubbtnDialog edttxtReviewSubbtnDialog =new EdttxtReviewSubbtnDialog(getContext(), "select * from cus_sup_tbl where shopId="+ sharedPreference.getData("shopId") +" ", "cus_sup_tbl") {
                    @Override
                    protected void doAfterSubbtnClicked() {
                        cusSupId = originalId;//idTxt.getText().toString();
                        //Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                        cusSupNameTxt.setText(nameEdt.getText().toString());
                    }
                };
            }
        });
        buyOrSellTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyOrSellTxt.getText().toString().equals("buy")){
                    buyOrSellTxt.setText("sell");
                }else{
                    buyOrSellTxt.setText("buy");
                }
            }
        });

        datetimeTxt.setText(Datetime.datetimeNowStr());
        datetimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthInd, int day) {
                        final Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        new TimePickerDialog(getContext(),
                                (TimePicker view, int selectedHour, int selectedMinute) -> {
                            String time = String.format("%02d-%02d", selectedHour, selectedMinute);
                                datetimeTxt.setText(year+"-"+(monthInd+1)+"-"+day+" "+time+"-"+LocalTime.now().getSecond());
                        },
                                hour,
                                minute,
                                false // 24-hour format
                                 ).show();

                    }
                }, LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth()).show();
            }
        });

        billEntriesAl=new ArrayList<>();
        HashMap<String , String> firstInputIntoBillEntryReviewHm = new HashMap<>();
        billEntriesAl.add(firstInputIntoBillEntryReviewHm);

        setProdOrCusSupDataIntoReview();

        totalTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                haveToPayEdt.setText(totalTxt.getText().toString());
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qry="insert into bill_tbl (`shopId`, `userId`, `cusSupId`, `haveToPay`, `sellBuy`, `datetimeOfEntry`,`datetime`) values(" +
                        sharedPreference.getData("shopId") + ", " + sharedPreference.getData("userId") + ", "+
                        cusSupId +", "+ haveToPayEdt.getText().toString() +", '"+ Other.encrypt(buyOrSellTxt.getText().toString()) +"', '"+datetimeTxt.getText().toString()+"', '"+Datetime.datetimeNowStr()+"')";
                new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{
                        qry, "bill_tbl"
                }) {
                    @Override
                    protected void doAfterTakingData(String response) {
                        try {
                            JSONObject afterInsertOrUpdateJsonObj = new JSONObject(response);
                            int billId =Integer.parseInt(afterInsertOrUpdateJsonObj.getString("lastId"));
                            billIdTxt.setText(billId+"");
                            if(billId> 1){
                                String qry = "insert into each_item_of_bill_tbl (`userId`, `billId`, `prodId`, `quan`, `price`,`datetime`) values ";
                                for (int i=0; i<billEntriesAl.size(); i++){
                                    if(billEntriesAl.get(i).containsKey("prodId")){
                                        if(i>0){
                                            qry+= ",";
                                        }
                                        qry+= "("+
                                                sharedPreference.getData("userId") + ", " +
                                                +billId+ ", " + billEntriesAl.get(i).get("prodId") + ", " + billEntriesAl.get(i).get("quan") + "," +
                                                billEntriesAl.get(i).get("price") + ",'" + datetimeTxt.getText().toString() + "'"
                                                +")";
                                    }
                                }
                                String finalQry = qry;
                                new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{finalQry, "each_item_of_bill_tbl" }) {
                                    @Override
                                    protected void doAfterTakingData(String response) {
                                        JSONObject afterInsertOrUpdateJsonObj = null;
                                        try {
                                            afterInsertOrUpdateJsonObj = new JSONObject(response);

                                            //Log.d("aaa", response);
                                            int billId =Integer.parseInt(afterInsertOrUpdateJsonObj.getString("lastId"));
                                            if(billId> 0){
                                                String qry = "insert into payment_tbl (`shopId`, `userId`,`cusSupId`, `amount`, `datetime`, `datetimeOfEntry`) values(" +
                                                        sharedPreference.getData("shopId") + ", " + sharedPreference.getData("userId") + ", "+
                                                        cusSupId +","+ haveToPayEdt.getText().toString() +", '"+ Datetime.datetimeNowStr() +"', '"+datetimeTxt.getText().toString()+"')";
                                                new VolleyTakeData(getContext(), new String[]{"qry", "tblName"}, new String[]{ qry, "payment_tbl"
                                                }) {
                                                    @Override
                                                    protected void doAfterTakingData(String response) {
                                                        try {
                                                            JSONObject afterInsertingOrUpdatingPaymentJsonObj = new JSONObject(response);
                                                            if(Integer.parseInt(afterInsertingOrUpdatingPaymentJsonObj.get("lastId").toString())>1){
                                                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            Log.d("aaa", e+"");
                                                            Log.d("aaa", qry);
                                                            Toast.makeText(getContext(), "BuysellFrag json obj error 926559", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                };

                                            }
                                        } catch (JSONException e) {
                                            Log.d("aaa", "billFrag    "+ e.toString());
                                            Log.d("aaa", "billFrag    "+ response +" \n "+ finalQry);
                                            Toast.makeText(getContext(), "billFrag error 07252526", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                };
                            }else{
                                Log.d("aaa", "buySellFrag "+ response);
                                Toast.makeText(getContext(), "Errror occurred 075025826", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("aaa", "buySellFrag "+ qry);
                            Log.d("aaa","billFrag   "+response);
                            Toast.makeText(getContext(), "billFrag JsonObj error 02702582", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });
        memBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,
                        new ShowBuySellInMemFrag(dataOfFragment)).commit();
            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createAndSavePdf(BuySellFrag.this.view);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new PdfFrag(BuySellFrag.this)).commit();
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getThisFragAtTopInMem();
    }


int[] lastFocusEdtPosiInd={-1,-1};
    protected void setProdOrCusSupDataIntoReview() {

        RecyAdapter shopsRecyAdapter = new RecyAdapter(R.layout.each_item_in_bill_layout, billEntriesAl.size()) {
            @Override
            void bind(Vh holder, int position) {
                ((TextView)holder.arrView.get(0)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new EdttxtReviewSubbtnDialog(getContext(), "select * from prod_tbl where shopId=" + sharedPreference.getData("shopId"), "prod_tbl") {
                            @Override
                            protected void doAfterSubbtnClicked() {
                                billEntriesAl.get(position).put("prodId", originalId);//idTxt.getText().toString());
                                billEntriesAl.get(position).put("prodName", nameEdt.getText().toString());
                                if(!billEntriesAl.get(position).containsKey("quan")) {
                                    billEntriesAl.get(position).put("quan", "0");
                                    billEntriesAl.get(position).put("price", "0");
                                }
                                //Toast.makeText(getContext(), billEntriesAl.get(position).get("quan"), Toast.LENGTH_SHORT).show();
                                saveBtn.setEnabled(true);
                                ((TextView)holder.arrView.get(0)).setText(nameEdt.getText().toString());
                                ((EditText) holder.arrView.get(1)).setEnabled(true);
                                ((EditText) holder.arrView.get(2)).setEnabled(true);
                                ((EditText) holder.arrView.get(3)).setEnabled(true);
                            }
                        };
                    }
                });
                if(billEntriesAl.get(position).containsKey("prodName")) {
                    ((TextView) holder.arrView.get(0)).setText(billEntriesAl.get(position).get("prodName"));
                    String quan =billEntriesAl.get(position).get("quan");
                    if(Double.parseDouble(quan)> (int)Double.parseDouble(quan)) {
                        ((EditText) holder.arrView.get(1)).setText(quan);
                    }else{
                        ((EditText) holder.arrView.get(1)).setText((int)Double.parseDouble(quan)+"");
                    }
                    String price =billEntriesAl.get(position).get("price");
                    if(Double.parseDouble(price)> (int)Double.parseDouble(price)) {
                        ((EditText) holder.arrView.get(2)).setText(price);
                    }else{
                        ((EditText) holder.arrView.get(2)).setText((int)Double.parseDouble(price)+"");
                    }
                    Double amount = Double.parseDouble(billEntriesAl.get(position).get("quan")) *
                            Double.parseDouble(billEntriesAl.get(position).get("price"));

                    if(amount > ((int) Double.parseDouble(amount+""))) {
                        ((EditText) holder.arrView.get(3)).setText(amount+"");
                    }else{
                        ((EditText) holder.arrView.get(3)).setText((int)  Double.parseDouble(amount+"")+"");
                    }

                }

                if(position!= -1 && position==lastFocusEdtPosiInd[0]) {
                    ((EditText) holder.arrView.get(lastFocusEdtPosiInd[1])).requestFocus();
                    ((EditText) holder.arrView.get(lastFocusEdtPosiInd[1])).setSelection(((EditText) holder.arrView.get(lastFocusEdtPosiInd[1])).getText().toString().length());
                }else {
                    //Toast.makeText(getContext(), "posi -1", Toast.LENGTH_SHORT).show();
                }


                if(!billEntriesAl.get(position).containsKey("prodId")) {
                    ((EditText) holder.arrView.get(1)).setEnabled(false);
                    ((EditText) holder.arrView.get(2)).setEnabled(false);
                    ((EditText) holder.arrView.get(3)).setEnabled(false);
                }
                for (int i=1; i<=3; i++) {
                    int finalI = i;
                    ((EditText) holder.arrView.get(i)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            String keyOfHm = "";
                            if(finalI ==1){
                                keyOfHm="quan";
                            } else if (finalI==2) {
                                keyOfHm="price";
                            }

                            if (!keyOfHm.isEmpty()) {
                                if (editable.toString().isEmpty() || editable.toString().equals("-") || editable.toString().equals(".")) {
                                    if (!((EditText) holder.arrView.get(3)).getText().toString().isEmpty()) {
                                        ((EditText) holder.arrView.get(3)).setText("0");
                                    }
                                    billEntriesAl.get(position).put(keyOfHm, "0");
                                    return;
                                }else{
                                    billEntriesAl.get(position).put(keyOfHm, editable.toString());
                                }
                            } else{
                                //if (editable.toString().isEmpty() || editable.toString().equals("-") || editable.toString().equals(".")) {
                                new BackGroundThreadForDoSomeWorkAfterSomeTime(getContext(), 100) {
                                    @Override
                                    void workToDo() {
                                        calculateTotal();
                                    }
                                };
                                //}
                            }

                            Boolean changeDoneAmongQuanPriceAmount=false;
                            for (int i=3; i>0; i--){
                                if(i!=finalI){
                                    for (int j=1; j<4; j++){
                                        if(j!=i && j!=finalI && !(((EditText) holder.arrView.get(j)).getText().toString().isEmpty() || ((EditText) holder.arrView.get(j)).getText().toString().equals("-")|| ((EditText) holder.arrView.get(j)).getText().toString().equals(".")) &&
                                                !(((EditText) holder.arrView.get(finalI)).getText().toString().isEmpty() || ((EditText) holder.arrView.get(finalI)).getText().toString().equals("-")|| ((EditText) holder.arrView.get(finalI)).getText().toString().equals("."))
                                        ){
                                            Double amount =0d;

                                            if(i== 3){
                                                amount = Double.parseDouble(((EditText) holder.arrView.get(1)).getText().toString())*Double.parseDouble(((EditText) holder.arrView.get(2)).getText().toString());
                                            }else {
                                                if(i==1)
                                                    amount = Double.parseDouble(((EditText) holder.arrView.get(3)).getText().toString())/Double.parseDouble(((EditText) holder.arrView.get(2)).getText().toString());
                                                else {
                                                    amount = Double.parseDouble(((EditText) holder.arrView.get(3)).getText().toString())/Double.parseDouble(((EditText) holder.arrView.get(1)).getText().toString());
                                                }

                                            }

                                            if(!(((EditText) holder.arrView.get(i)).getText().toString().equals(amount+"") ||
                                                    ((EditText) holder.arrView.get(i)).getText().toString().equals((int)  Double.parseDouble(amount+"")+"")
                                                    )){
                                                if(amount > ((int) Double.parseDouble(amount+""))) {
                                                    ((EditText) holder.arrView.get(i)).setText(amount+"");
                                                }else{
                                                    ((EditText) holder.arrView.get(i)).setText((int)  Double.parseDouble(amount+"")+"");
                                                }
                                                lastFocusEdtPosiInd[0] = position;
                                                lastFocusEdtPosiInd[1] = finalI;
                                            }
                                            changeDoneAmongQuanPriceAmount = true;
                                            break;
                                        }
                                    }
                                }
                                if(changeDoneAmongQuanPriceAmount) break;
                            }

                        }
                    });
                }

                ((EditText) holder.arrView.get(3)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {                    }                    @Override                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(position == billEntriesAl.size()-1) {
                            HashMap<String, String> firstInputIntoBillEntryReviewHm = new HashMap<>();
                            billEntriesAl.add(firstInputIntoBillEntryReviewHm);

                            setProdOrCusSupDataIntoReview();
                        }
                    }
                });
                if(position == billEntriesAl.size()-1){
                    ((ImageView) holder.arrView.get(4)).setVisibility(View.INVISIBLE);
                }
                ((ImageView) holder.arrView.get(4)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getContext()).setMessage(R.string.Do_you_want_to_delete)
                                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        billEntriesAl.remove(position);
                                        calculateTotal();
                                        setProdOrCusSupDataIntoReview();
                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            Vh onCreate (View view){
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.prodNameTxt));
                        arrView.add(itemView.findViewById(R.id.quanEdt));
                        arrView.add(itemView.findViewById(R.id.priceEdt));
                        arrView.add(itemView.findViewById(R.id.amountEt));
                        arrView.add(itemView.findViewById(R.id.delBtn));
                    }
                };
            }
        };

        eachBillEntryreview.setAdapter(shopsRecyAdapter);
    }
    protected void calculateTotal(){
        Double total=0.0;
        for(int i= 0; i< billEntriesAl.size(); i++) {
            if(billEntriesAl.get(i).containsKey("prodId"))
            {
                total += Double.parseDouble(billEntriesAl.get(i).get("quan")) * Double.parseDouble(billEntriesAl.get(i).get("price"));
            }
        }
        totalTxt.setText(total+"");
    }
    protected void getThisFragAtTopInMem(){
        boolean found=false;
        for(int i=0; i<dataOfFragment.size(); i++){
            if((BuySellFrag) dataOfFragment.get(i).get(1)==BuySellFrag.this){
                //found=true;
                if(i!=0){
                    dataOfFragment.remove(i);
                }
                break;
            }
        }
        if(dataOfFragment.size()==0 || dataOfFragment.get(0).get(1)!=BuySellFrag.this) {
            ArrayList al = new ArrayList(2);
            Bitmap bitmap = Bitmap.createBitmap(BuySellFrag.this.view.getWidth(), BuySellFrag.this.view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            BuySellFrag.this.view.draw(canvas);
            al.add(bitmap);
            al.add(BuySellFrag.this);
            dataOfFragment.addFirst(al);
        }
    }






    private void createAndSavePdf(View view) {
        int pageWidth =0
                , pageHeight =0;
        Canvas canvas=null;
        PdfDocument pdfDocument=null;
        PdfDocument.Page page=null;
        try {
            // Create a PdfDocument
            pdfDocument = new PdfDocument();
            pageWidth = view.getWidth(); //1500
            pageHeight = view.getHeight(); // 2750;
            // Define the page size
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

            // Start a page
            page = pdfDocument.startPage(pageInfo);
            canvas = page.getCanvas();

            // Draw something on the page
//        Paint paint = new Paint();
//        paint.setTextSize(16);
        } catch (Exception e) {
            Toast.makeText(getContext(), Integer.toString(pageHeight) + Integer.toString(pageWidth), Toast.LENGTH_SHORT).show();
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvass = new Canvas(bitmap);
            view.draw(canvass);
            Bitmap scaledBitmap = scaleBitmapToFit(bitmap, pageWidth, pageHeight);
            canvas.drawBitmap(bitmap, 0, 0, null);
            //canvas.drawText("Hello, this is a sample PDF!", 10, 25, paint);

            // Finish the page
            pdfDocument.finishPage(page);
        }catch (Exception e){
            Toast.makeText(getContext() , "error 593278", Toast.LENGTH_SHORT).show();
        }

        // Save the PDF
        try {
            File pdfFile;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped Storage for Android 10+
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "a.pdf");
                values.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                if (uri != null) {
                    try (OutputStream outputStream = getContext().getContentResolver().openOutputStream(uri)) {
                        pdfDocument.writeTo(outputStream);
                    }
                }
                pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "a.pdf");
            } else {
                // Legacy storage
                pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "a.pdf");
                pdfDocument.writeTo(new FileOutputStream(pdfFile));
            }

            Toast.makeText(getContext(), "PDF saved to Downloads", Toast.LENGTH_SHORT).show();

            // Open the PDF
            //    openPdf(this, pdfFile);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(), "error205250", Toast.LENGTH_SHORT).show();
        } finally{
            pdfDocument.close();
        }
    }
    private Bitmap scaleBitmapToFit(Bitmap bitmap, int width, int height) {
        // Calculate the scaling factor
        float aspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();
        int scaledWidth = width;
        int scaledHeight = (int) (width / aspectRatio);

        if (scaledHeight > height) {
            // Adjust width and height to fit within the page
            scaledHeight = height;
            scaledWidth = (int) (height * aspectRatio);
        }

        // Scale the bitmap
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
    }


}