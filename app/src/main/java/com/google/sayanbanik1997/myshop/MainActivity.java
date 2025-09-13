package com.google.sayanbanik1997.myshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN        // hide status bar
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferenceFile", MODE_PRIVATE);
        //SharedPreferences.Editor spEditor = sharedPreferences.edit();

        if (!sharedPreferences.contains("userId")) {
            Intent logInIntent = new Intent(getApplicationContext(), LoginAct.class);
            startActivity(logInIntent);
        } else {
            if (!sharedPreferences.contains("shopId")) {
                Intent logInIntent = new Intent(getApplicationContext(), ChooseOrCreateShopAct.class);
                startActivity(logInIntent);
            }

            ArrayList<ArrayList> dataOfFragment = new ArrayList(4);


            toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);

            drawerLayout = (DrawerLayout) findViewById(R.id.main);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navDrClose, R.string.navDrOpen);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            navView = (com.google.android.material.navigation.NavigationView) findViewById(R.id.navView);
            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                Fragment fragment;
                Bundle bundle;

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.productMenu) {
                        fragment = new EtFrag();
                        bundle = new Bundle();
                        bundle.putString("fragType", "prod");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
                    }
                    else if (item.getItemId() == R.id.cusAndSupMenu) {
                        fragment = new EtFrag();
                        bundle = new Bundle();
                        bundle.putString("fragType", "cusSup");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
                    }
                    else if(item.getItemId()==R.id.buySellMenu){
                        if(dataOfFragment.size()>0){
                            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, (BuySellFrag)dataOfFragment.get(0).get(1)).commit();
                        }else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new BuySellFrag(dataOfFragment)).commit();
                        }
                    }
                    else if(item.getItemId()==R.id.billAndPaymentMenu){
//                        Fragment fragment = new EtFrag();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("fragType", "billOrPayment");
//                        fragment.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new BillAndPaymentFrag()).commit();

                    }
//                if(item.getItemId()==R.id.groupMenu) getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new ShowDbDataFrag()).commit();
                    else if (item.getItemId() == R.id.profileMenu)
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new ProfileFrag()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
            if(dataOfFragment.size()>0){
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout, (BuySellFrag)dataOfFragment.get(0).get(1)).commit();
            }else {
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout, new BuySellFrag(dataOfFragment)).commit();
            }


            //getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout, new BillAndPaymentFrag()).commit();

        }
    }
}