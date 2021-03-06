package com.technologies.yanny.xappes.Xapes;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.CavesDO;
import com.amazonaws.models.nosql.XapesDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.technologies.yanny.xappes.Caves.FilteredCavaFragment;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.MenuActivity;

import java.util.ArrayList;
import java.util.List;

public class MyXappesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private List<RequestCreator> imagesList;
    private List<XapesDO> xapes;

    private String valueCache;

    private GridView gv_les_meves_xapes;

    private TextView et_buscar_album;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    private FloatingActionButton bt_nova_xapa;

    public MyXappesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.imagesList = new ArrayList<>();
        this.gv_les_meves_xapes = (GridView) this.getActivity().findViewById(R.id.gv_les_meves_xapes);

        this.et_buscar_album = (TextView) this.getActivity().findViewById(R.id.et_buscar_album);

        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        ((MenuActivity)getActivity()).showProgress(false);

        this.et_buscar_album.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) || id == 6 || id == 5) {
                    searchXappes();
                }
                return true;
            }
        });

        this.gv_les_meves_xapes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                    if (fragment != null && fragment.getClass() == XappaFragment.class) getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                XappaFragment newFragment = new XappaFragment();
                Bundle args = new Bundle();
                List<XapesDO> xappa = new ArrayList<>();
                xappa.add(xapes.get(position));
                args.putParcelableArrayList("xappa", (ArrayList) xappa);
                args.putString("from", "myXappes");
                newFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, newFragment).commit();
            }
        });

        searchXappes();
    }

    private void searchXappes() {
        this.xapes = new ArrayList<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                String value = et_buscar_album.getText().toString().trim().toLowerCase();

                //String user = ((TextView) getActivity().findViewById(R.id.email)).getText().toString().trim().toLowerCase();
                String user = ((MenuActivity)getActivity()).getUsuari();

                Condition condition;

                if (!value.equals("")) {
                    condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.CONTAINS)
                            .withAttributeValueList(new AttributeValue(value));
                    scanExpression.addFilterCondition("cavaName", condition);
                }

                condition = new Condition().withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue(user));
                scanExpression.addFilterCondition("userId", condition);

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

            this.imagesList = new ArrayList<>();
            for (XapesDO cava : this.xapes) {
                this.imagesList.add(Picasso.get().load(getResources().getString(R.string.bucketURL) + cava.getXappa() + ".jpg"));
                System.out.println(getResources().getString(R.string.bucketURL) + cava.getXappa() + ".jpg");
            }

            this.gv_les_meves_xapes.setAdapter(new ImageAdapterGridView(this.getActivity()));
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
                mImageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                mImageView = (ImageView) convertView;
            }
            (imagesList.get(position)).into(mImageView);
            return mImageView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_xappes, container, false);
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
