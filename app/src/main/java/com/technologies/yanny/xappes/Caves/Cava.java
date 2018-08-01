package com.technologies.yanny.xappes.Caves;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.Xapes.CrearXapaFragment;
import com.technologies.yanny.xappes.Xapes.MyXappesFragment;
import com.technologies.yanny.xappes.Xapes.XapesListFragment;
import com.technologies.yanny.xappes.main.MenuActivity;

public class Cava {
    private String name;
    private TableRow newRow;
    private android.support.v4.app.FragmentManager fragment;
    private Context context;
    private boolean create;

    public Cava (final String name, final String num, final Context context, final android.support.v4.app.FragmentManager fragment) {
        this.name = name;
        this.fragment = fragment;
        this.context = context;
        TextView nameCava = new TextView(context);
        TextView numberCava = new TextView(context);
        nameCava.setText(name);
        numberCava.setText("     ("+num+")");
        numberCava.setTextColor(Color.BLUE);
        this.newRow = new TableRow(context);
        this.newRow.addView(nameCava);
        this.newRow.addView(numberCava);
        this.newRow.setPadding(5,10,5,10);
        this.newRow.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v){
                        ((MenuActivity)context).showProgress(true);
                        ((MenuActivity)context).setProgressB(50);
                        XapesListFragment newFragment = new XapesListFragment();
                        Bundle args = new Bundle();
                        args.putString("cava", name);
                        newFragment.setArguments(args);
                        ((MenuActivity)context).setProgressB(90);
                        fragment.beginTransaction().add(R.id.fl_main_fragment, newFragment).commit();
                    }
                }
        );
    }

    public TableRow getNewRow() {
        return newRow;
    }

}
