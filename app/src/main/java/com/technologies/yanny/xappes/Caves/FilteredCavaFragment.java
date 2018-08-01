package com.technologies.yanny.xappes.Caves;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.amazonaws.models.nosql.CavesDO;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.MenuActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FilteredCavaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TableLayout tl_caves_list_name;

    private List<CavesDO> caves;

    public FilteredCavaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.caves = (ArrayList) getArguments().getParcelableArrayList("caves");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtered_cava, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.tl_caves_list_name = (TableLayout) getActivity().findViewById(R.id.tl_caves_list_name);

        ((MenuActivity)getActivity()).showProgress(true);
        ((MenuActivity)getActivity()).setProgressB(50);

        generateList();
        ((MenuActivity)getActivity()).showProgress(false);
    }

    private void generateList() {
        for (CavesDO cava : this.caves) {
            this.tl_caves_list_name.addView(new Cava(cava.getCavaName(), cava.getNumXappes(), getActivity(), getActivity().getSupportFragmentManager()).getNewRow());
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
