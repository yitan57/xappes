package com.technologies.yanny.xappes.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.technologies.yanny.xappes.Caves.CavesListFragment;
import com.technologies.yanny.xappes.Maps.MapsFragment;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.Trobades.TrobadesFragment;
import com.technologies.yanny.xappes.Xapes.CrearXapaFragment;
import com.technologies.yanny.xappes.Xapes.MyXappesFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String option;

    private ProgressBar pb_main;

    private String usuari;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.usuari = intent.getStringExtra("usuari");

        this.pb_main = (ProgressBar) findViewById(R.id.pb_main);

        showProgress(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, new IniciFragment()).commit();
        setOption("inici");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.navigationView.setCheckedItem(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_placa) {
            setOption("crear");
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new CrearXapaFragment()).commit();
            this.navigationView.setCheckedItem(R.id.navigation_NovaPlaca);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, new IniciFragment()).commit();
            setOption("inici");
        } else if (id == R.id.navigation_NovaPlaca) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new CrearXapaFragment()).commit();
            setOption("crear");
        } else if (id == R.id.navigation_xappes) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new MyXappesFragment()).commit();
            setOption("album");
        } else if (id == R.id.navigation_caves) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new CavesListFragment()).commit();
            setOption("buscar");
        } else if (id == R.id.navigation_trobades) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new MapsFragment()).commit();
            setOption("trobades");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setOption(String value) {
        this.option = value;
    }

    public String getOption() {
        return this.option;
    }

    public String getUsuari() {
        return this.usuari;
    }

    public void setProgressB (int value) {
        this.pb_main.setProgress(value);
    }

    public void showProgress (boolean value) {
        if (value) this.pb_main.setVisibility(View.VISIBLE);
        else this.pb_main.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
                int lastFragment = getSupportFragmentManager().getFragments().size();
                Fragment fragment = getSupportFragmentManager().getFragments().get(lastFragment-1);

                if (fragment != null && fragment.getClass() != IniciFragment.class) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    if (getSupportFragmentManager().getFragments().size() > 1) setCurrentMenuOption(lastFragment);
                    if (getSupportFragmentManager().getFragments().get(0).getClass() != IniciFragment.class) {
                        startIniciFragment();
                    }
                } else {
                    closeDialog();
                }

            } else {
                closeDialog();
            }
        }
    }

    private void setCurrentMenuOption(int lastFragment) {
        if (getSupportFragmentManager().getFragments().get(lastFragment-1).getClass() == CrearXapaFragment.class) {
            this.navigationView.setCheckedItem(R.id.navigation_NovaPlaca);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.getClass() != CrearXapaFragment.class && fragment.getClass() != IniciFragment.class)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if (getSupportFragmentManager().getFragments().get(lastFragment-1).getClass() == MyXappesFragment.class) {
            this.navigationView.setCheckedItem(R.id.navigation_xappes);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.getClass() != MyXappesFragment.class && fragment.getClass() != IniciFragment.class)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if (getSupportFragmentManager().getFragments().get(lastFragment-1).getClass() == CavesListFragment.class) {
            this.navigationView.setCheckedItem(R.id.navigation_caves);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.getClass() != CavesListFragment.class && fragment.getClass() != IniciFragment.class)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        if (getSupportFragmentManager().getFragments().get(lastFragment-1).getClass() == TrobadesFragment.class) {
            this.navigationView.setCheckedItem(R.id.navigation_caves);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.getClass() != TrobadesFragment.class && fragment.getClass() != IniciFragment.class)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }

    private void startIniciFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, new IniciFragment()).commit();
        this.navigationView.setCheckedItem(R.id.navigation_home);
        setOption("inici");
    }

    private void closeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("SALIR?")
                .setMessage("Seguro que quieres salir?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MenuActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
