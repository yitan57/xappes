package com.technologies.yanny.xappes.Xapes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.XapesDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.MenuActivity;

import java.util.ArrayList;
import java.util.List;

public class XapesListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private String cavaName;

    private GridView gv_xapes;

    private List<RequestCreator> imagesList;
    private List<XapesDO> xapes;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    public XapesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.cavaName = getArguments().getString("cava");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xapes_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.gv_xapes = (GridView) getActivity().findViewById(R.id.gv_xapes);

        ((MenuActivity)getActivity()).showProgress(true);
        ((MenuActivity)getActivity()).setProgressB(35);

        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        this.imagesList = new ArrayList<>();

        searchXappes(this.cavaName);
        ((MenuActivity)getActivity()).setProgressB(60);

        this.gv_xapes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MenuActivity)getActivity()).showProgress(true);
                String option = ((MenuActivity)getActivity()).getOption();
                if (option.equals("crear")) {
                    CrearXapaFragment newFragment = new CrearXapaFragment();
                    Bundle args = new Bundle();
                    ((MenuActivity)getActivity()).setProgressB(25);
                    if (position >= xapes.size()) {
                        args.putString("xappaId", "-1");
                        args.putString("cava", cavaName);

                    } else {

                        XapesDO selectedCava = xapes.get(position);

                        args.putString("cava", selectedCava.getCavaName());
                        args.putString("xappaId", selectedCava.getXappa());

                    }
                    ((MenuActivity)getActivity()).setProgressB(70);
                    newFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, newFragment).commit();

                } else {
                    if (position >= xapes.size()) {
                        getActivity().onBackPressed();
                    } else {
                        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                            if (fragment != null && fragment.getClass() == XappaFragment.class) getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                        XappaFragment newFragment = new XappaFragment();
                        Bundle args = new Bundle();
                        List<XapesDO> xappa = new ArrayList<>();
                        xappa.add(xapes.get(position));
                        args.putParcelableArrayList("xappa", (ArrayList) xappa);
                        newFragment.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, newFragment).commit();
                    }
                }
            }
        });
    }

    private void searchXappes(final String value) {
        this.xapes = new ArrayList<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                Condition condition;

                condition = new Condition().withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue("generic"));
                scanExpression.addFilterCondition("userId", condition);

                if(cavaName != null) {
                    condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ)
                            .withAttributeValueList(new AttributeValue(cavaName.toLowerCase()));
                    scanExpression.addFilterCondition("cavaName", condition);
                }

                List<XapesDO> cavesResult = dynamoDBMapper.scan(XapesDO.class, scanExpression);
                System.out.println(cavesResult.toString());

                for (XapesDO cava : cavesResult) {
                    xapes.add(cava);
                }
            }
        });
        t.start();
        loadXappes(t);
    }

    private void loadXappes(Thread t) {
        try {
            t.join();
            ((MenuActivity)getActivity()).setProgressB(90);
            this.imagesList = new ArrayList<>();
            for (XapesDO cava : this.xapes) {
                System.out.println(getResources().getString(R.string.bucketURL) + cava.getXappa() + ".jpg");
                this.imagesList.add(Picasso.get().load(getResources().getString(R.string.bucketURL) + cava.getXappa() + ".jpg"));
            }
            if (((MenuActivity)getActivity()).getOption().equals("crear") && this.cavaName != null) this.imagesList.add(Picasso.get().load(getResources().getString(R.string.bucketURL) + "add.png"));
            if (((MenuActivity)getActivity()).getOption().equals("buscar") && this.xapes.size() == 0)this.imagesList.add(Picasso.get().load(getResources().getString(R.string.bucketURL) + "noInfo.png"));
            this.gv_xapes.setAdapter(new ImageAdapterGridView(this.getActivity()));
            ((MenuActivity)getActivity()).showProgress(false);
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT);
        }

    }


    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imagesList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                String option = ((MenuActivity)getActivity()).getOption();
                if (option.equals("crear")) {
                    mImageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                } else {
                    mImageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                }
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                mImageView = (ImageView) convertView;
            }
            (imagesList.get(position)).into(mImageView);
            return mImageView;
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
