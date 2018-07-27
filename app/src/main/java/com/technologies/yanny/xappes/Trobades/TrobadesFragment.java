package com.technologies.yanny.xappes.Trobades;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technologies.yanny.xappes.R;


public class TrobadesFragment extends Fragment {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private String title;
    private String description;

    private TextView tv_title_trobada;
    private TextView tv_descripcio_trobada;

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton bt_exit;


    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.tv_title_trobada = (TextView) this.getActivity().findViewById(R.id.tv_title_trobada);
        this.tv_descripcio_trobada = (TextView) this.getActivity().findViewById(R.id.tv_descripcio_trobada);

        this.bt_exit = (FloatingActionButton) this.getActivity().findViewById(R.id.bt_exit);

        this.tv_title_trobada.setText(this.title);
        this.tv_descripcio_trobada.setText(this.description);

        this.bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetach();
            }
        });
    }

    public TrobadesFragment() {
        // Required empty public constructor
    }

    public TrobadesFragment newInstance(String param1, String param2) {
        TrobadesFragment fragment = new TrobadesFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, param1);
        args.putString(DESCRIPTION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString(TITLE);
            this.description = getArguments().getString(DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trobades, container, false);


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
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } catch (Exception e) {

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
