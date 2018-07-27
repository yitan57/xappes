package com.technologies.yanny.xappes.Caves;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.CavesDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class CavesListFragment extends Fragment {

    private LinearLayout ll_caves_list;

    private GridView gv_caves_filter;

    private boolean selected = false;

    private TextView tv_nova_cava_nom;

    private OnFragmentInteractionListener mListener;

    private List<CavesDO> caves;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    private String[] index = {"A","B","C","D","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","V","W","X","Y","Z"};

    public CavesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            List<CavesDO> caves = ((ArrayList) args.getParcelableArrayList("actualProduct"));
            this.caves = caves;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.ll_caves_list = (LinearLayout) getActivity().findViewById(R.id.ll_caves_list);

        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        this.gv_caves_filter = (GridView) this.getActivity().findViewById(R.id.gv_caves_filter);

        ((HomeActivity) getActivity()).showProgress(false);
        chargeIndex();

    }

    private void chargeCaves(final String selected) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                caves = new ArrayList<>();
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();


                ((HomeActivity) getActivity()).setProgressB(25);

                Condition condition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue(selected));
                scanExpression.addFilterCondition("cavaName", condition);

                List<CavesDO> cavesResult = dynamoDBMapper.scan(CavesDO.class, scanExpression);
                System.out.println(cavesResult.toString());

                ((HomeActivity) getActivity()).setProgressB(55);

                for (CavesDO cava : cavesResult) {
                    caves.add(cava);
                }

                ((HomeActivity) getActivity()).setProgressB(80);

                FilteredCavaFragment fragment = new FilteredCavaFragment();

                Bundle args = new Bundle();
                args.putParcelableArrayList("caves", (ArrayList) caves);
                fragment.setArguments(args);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_main_fragment, fragment)
                        .commit();
            }
        }).start();
    }

    private void chargeIndex() {
        this.gv_caves_filter.setAdapter(new IndexAdapter(this.getActivity()));
        this.gv_caves_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).showProgress(true);
                if (!selected) {
                    selected = true;
                    String selectedItem = index[position];
                    chargeCaves(selectedItem);
                }
            }
        });
    }

    public class IndexAdapter extends BaseAdapter {
        private Context mContext;

        public IndexAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return index.length;
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
                tv.setLayoutParams(new GridView.LayoutParams(300, 300));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(50);
                tv.setTextColor(Color.RED);
                tv.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(index[position]);
            return tv;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_caves_list, container, false);
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
