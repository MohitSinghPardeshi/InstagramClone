package com.mohitsprojects.instaclone.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mohitsprojects.instaclone.AndroidConstants.MyDB;
import com.mohitsprojects.instaclone.AndroidConstants.MyProperty;
import com.mohitsprojects.instaclone.AndroidConstants.SaveSharedPreference;
import com.mohitsprojects.instaclone.AndroidConstants.noInternetDialog;
import com.mohitsprojects.instaclone.Fragmets.FragmentFavorite;
import com.mohitsprojects.instaclone.Fragmets.FragmentHome;
import com.mohitsprojects.instaclone.Fragmets.FragmentProfile;
import com.mohitsprojects.instaclone.Fragmets.FragmentReel;
import com.mohitsprojects.instaclone.Fragmets.FragmentSearch;
import com.mohitsprojects.instaclone.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String ROOT_FRAGMENT_TAG = "1234";

    private final Fragment fragmentHome = new FragmentHome();
    private final Fragment fragmentSearch = new FragmentSearch();
    private final Fragment fragmentReel = new FragmentReel();
    private final Fragment fragmentFavorite = new FragmentFavorite();
    private final Fragment fragmentProfile =  new FragmentProfile();

   private LinkedList<Fragment> fragmentArrayList = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            startActivity(new Intent(MainActivity.this, AskLoginCreate.class));
            finish();
            // call Login Activity
        }
        //initializing databse in singleton class...
        MyProperty.getInstance().dataBase = new MyDB(this.getApplicationContext());

        try {
            initFun();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initFun() throws IOException, InterruptedException {
        //internet popup
        noInternetDialog noInternet = new noInternetDialog(this);
        if (!noInternet.isConnected()) {
            noInternet.alertDailog();
        }

//        MyPermission.checkAndRequestPermissions(this);
        BottomNavigationView bottomNav = findViewById(R.id.idBotNavi);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.idFragContainer,
                fragmentHome
        ).addToBackStack(null).commit();
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.idBtmNaviHome:
                        selectedFragment = fragmentHome;
                        getSupportFragmentManager().clearBackStack(null);
                        break;
                    case R.id.idBtmNaviSearch:
                        selectedFragment = fragmentSearch;
                        break;
                    case R.id.idBtmNaviReel:
                        selectedFragment = fragmentReel;
                        break;
                    case R.id.idBtmNaviFav:
                        selectedFragment = fragmentFavorite;
                        break;
                    case R.id.idBtmNaviProfile:
                        selectedFragment = fragmentProfile;
                        break;
                    default:
                        return super.onOptionsItemSelected(item);

                }

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.idFragContainer, selectedFragment)
                        .addToBackStack(null).commit();
                                                                                                                                        //               2rayList.add(selectedFragment);

                return true;
            };



    @Override
    public void onBackPressed() {
        List<Fragment> fragments =  getSupportFragmentManager().getFragments();
        Fragment current = fragments.get(fragments.size()-1);
        if(current == fragmentHome){
            finish();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
           finish();
        }else{
            super.onBackPressed();
        }

    }

}