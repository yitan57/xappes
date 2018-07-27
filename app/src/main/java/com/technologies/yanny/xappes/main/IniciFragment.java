package com.technologies.yanny.xappes.main;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.EntradasDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class IniciFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FrameLayout fl_content_tabs;

    private TabLayout tabs;

    private String type;

    private boolean isOk = false;

    private String[] textosEntradas;
    private List<EntradasDO> entradas;

    private GridView gv_content;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    public IniciFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inici, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.fl_content_tabs = (FrameLayout) getActivity().findViewById(R.id.fl_content_tabs);

        this.tabs = (TabLayout) getActivity().findViewById(R.id.tabs);

        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        this.gv_content = (GridView) getActivity().findViewById(R.id.gv_content);

        this.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((HomeActivity) getActivity()).showProgress(true);
                ((HomeActivity) getActivity()).setProgressB(50);
                getEntradas(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getEntradas("NOVETATS");
    }

    private void chargeEntradas() {
        while (!this.isOk);
        this.textosEntradas = new String[this.entradas.size()];

        for (int i = 0; i < this.entradas.size(); i++) {
            this.textosEntradas[i] = this.entradas.get(i).getEntrada();
        }
        this.gv_content.setAdapter(new HomeAdapter(this.getActivity()));

        ((HomeActivity) getActivity()).showProgress(false);
    }

    private void getEntradas(String valueType) {
        this.type = valueType;
        this.entradas = new ArrayList<>();
        this.isOk = false;

        ((HomeActivity) getActivity()).setProgressB(75);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                String user = "hflorido";


                Condition condition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue(type));
                scanExpression.addFilterCondition("entradaType", condition);

                List<EntradasDO> cavesResult = dynamoDBMapper.scan(EntradasDO.class, scanExpression);
                System.out.println(cavesResult.toString());

                ((HomeActivity) getActivity()).setProgressB(85);

                for (EntradasDO aEntrada : cavesResult) {
                    entradas.add(aEntrada);
                }

                isOk = true;
            }
        }).start();
        chargeEntradas();
    }

    public class HomeAdapter extends BaseAdapter {
        private Context mContext;

        public HomeAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return textosEntradas.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;

            if (convertView == null) {
                tv = new TextView(mContext);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(textosEntradas[position]);
            return tv;
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
