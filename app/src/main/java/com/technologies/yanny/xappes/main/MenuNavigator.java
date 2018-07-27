package com.technologies.yanny.xappes.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.MenuItem;

import com.technologies.yanny.xappes.Caves.CavesListFragment;
import com.technologies.yanny.xappes.Maps.MapsActivity;
import com.technologies.yanny.xappes.Maps.MapsFragment;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.Xapes.CrearXapaFragment;
import com.technologies.yanny.xappes.Xapes.MyXappesFragment;
import com.technologies.yanny.xappes.main.HomeActivity;

public class MenuNavigator {

    public static boolean getMenuActivity(MenuItem item, Activity activity, android.support.v4.app.FragmentManager fm) {

        try {

            String name = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0).name.toString();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().replace(R.id.fl_main_fragment, new IniciFragment()).commit();
                    ((HomeActivity) activity).setOption("inici");
                    return true;
                case R.id.navigation_NovaPlaca:
                    fm.beginTransaction().replace(R.id.fl_main_fragment, new CrearXapaFragment()).commit();
                    ((HomeActivity) activity).setOption("crear");
                    return true;
                case R.id.navigation_xappes:
                    fm.beginTransaction().replace(R.id.fl_main_fragment, new MyXappesFragment()).commit();
                    ((HomeActivity) activity).setOption("album");
                    return true;
                case R.id.navigation_caves:
                    fm.beginTransaction().replace(R.id.fl_main_fragment, new CavesListFragment()).commit();
                    ((HomeActivity) activity).setOption("buscar");
                    return true;
                case R.id.navigation_trobades:
                    fm.beginTransaction().replace(R.id.fl_main_fragment, new MapsFragment()).commit();
                    ((HomeActivity) activity).setOption("trobades");
                    return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            activity.startActivity(new Intent(activity.getApplicationContext(), HomeActivity.class));
            return false;
        }
        return false;
    }
}
