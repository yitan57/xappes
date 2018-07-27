package com.technologies.yanny.xappes.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.technologies.yanny.xappes.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private String option;

    private ProgressBar pb_main;

    private String usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        this.usuari = intent.getStringExtra("usuari");

        this.pb_main = (ProgressBar) findViewById(R.id.pb_main);

        showProgress(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return getMenuActivity(item);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, new IniciFragment()).commit();
        setOption("inici");
    }

    private boolean getMenuActivity(MenuItem item) {
        return MenuNavigator.getMenuActivity(item, this, getSupportFragmentManager());
    }

    public void setOption(String value) {
        this.option = value;
    }

    public String getOption() {
        return this.option;
    }

    public void showProgress (boolean value) {
        if (value) this.pb_main.setVisibility(View.VISIBLE);
        else this.pb_main.setVisibility(View.INVISIBLE);
    }

    public String getUsuari() {
        return this.usuari;
    }

    public void setProgressB (int value) {
        this.pb_main.setProgress(value);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("SALIR?")
                    .setMessage("Seguro que quieres salir?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            HomeActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }
}