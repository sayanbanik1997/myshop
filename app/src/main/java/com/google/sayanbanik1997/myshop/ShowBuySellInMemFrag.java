package com.google.sayanbanik1997.myshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowBuySellInMemFrag extends Fragment {
    RecyclerView showBuySellInMemRecyView;
    ArrayList<ArrayList> dataOfFragment;
    TextView addTxt;

    ShowBuySellInMemFrag(ArrayList dataOfFragment){
        this.dataOfFragment = dataOfFragment;
    }
//    DataToSent dataToSent;
//    ShowBuySellInMemFrag(DataToSent dataToSent){
//        this.dataToSent=dataToSent;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_show_buy_sell_in_mem, container, false);

        showBuySellInMemRecyView = view.findViewById(R.id.showBuySellInMemRecyView);
        addTxt = view.findViewById(R.id.addTxt);

        if(dataOfFragment.size()==0){
            Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
        }else {
            addBuySellInRecyAdapter();
        }

        addTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,
                        new BuySellFrag(dataOfFragment)).commit();
            }
        });
        return view;
    }
    private void addBuySellInRecyAdapter(){
        RecyAdapter recyAdapter = new RecyAdapter(R.layout.each_buysell_img_in_mem_sub_layout, dataOfFragment.size()) {
            @Override
            void bind(Vh holder, int position) {
                ((LinearLayout)holder.arrView.get(0)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getParentFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,
                                (BuySellFrag)dataOfFragment.get(position).get(1)).commit();
                    }
                });
                ((ImageView) holder.arrView.get(1)).setImageBitmap(
                        (Bitmap) dataOfFragment.get(position).get(0)
                );
                ((ImageView) holder.arrView.get(2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setMessage("Do you want to delete?");
                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dataOfFragment.remove(dataOfFragment.get(position));
                                addBuySellInRecyAdapter();
                            }
                        });
                        alertBuilder.create();
                        alertBuilder.show();
                    }
                });
            }

            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        arrView.add(itemView.findViewById(R.id.eachBuySellLl));
                        arrView.add(itemView.findViewById(R.id.imgView));
                        arrView.add(itemView.findViewById(R.id.delImg));
                    }
                };
            }
        };
        showBuySellInMemRecyView.setLayoutManager(new LinearLayoutManager(getContext()));
        showBuySellInMemRecyView.setAdapter(recyAdapter);
    }
}