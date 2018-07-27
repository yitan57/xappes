package com.technologies.yanny.xappes.Xapes;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.models.nosql.XapesDO;
import com.squareup.picasso.Picasso;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class XappaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private XapesDO xappa;

    private ImageView iv_placa;

    private TextView tv_cava_Place;
    private TextView tv_cavaName;
    private TextView tv_placaId;

    private Button bt_exit;
    private Button bt_add;

    public XappaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<XapesDO> xappes = (ArrayList) getArguments().getParcelableArrayList("xappa");
            this.xappa = xappes.get(0);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {

        this.iv_placa = (ImageView) getActivity().findViewById(R.id.iv_placa);

        this.tv_cava_Place = (TextView) getActivity().findViewById(R.id.tv_cava_Place);
        this.tv_cavaName = (TextView) getActivity().findViewById(R.id.tv_cavaName);
        this.tv_placaId = (TextView) getActivity().findViewById(R.id.tv_placaId);

        this.bt_exit = (Button) getActivity().findViewById(R.id.bt_exit);
        this.bt_add = (Button) getActivity().findViewById(R.id.bt_add);

        ((HomeActivity) getActivity()).showProgress(false);

        chargeXappa(this.xappa);
    }

    private void chargeXappa(XapesDO xappa) {
        this.tv_cava_Place.setText(xappa.getCavaPlace());
        this.tv_cavaName.setText(xappa.getCavaName());
        this.tv_placaId.setText(xappa.getXapesId());
        Picasso.get().load(getActivity().getResources().getString(R.string.bucketURL)+xappa.getXapesId()+".jpg").into(this.iv_placa);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xappa, container, false);
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
