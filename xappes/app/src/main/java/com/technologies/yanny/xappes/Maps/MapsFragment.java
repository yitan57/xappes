package com.technologies.yanny.xappes.Maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.TrobadesDO;
import com.amazonaws.models.nosql.XapesDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.Trobades.TrobadesFragment;
import com.technologies.yanny.xappes.Xapes.CrearXapaFragment;
import com.technologies.yanny.xappes.Xapes.XapesListFragment;
import com.technologies.yanny.xappes.Xapes.XappaFragment;
import com.technologies.yanny.xappes.main.HomeActivity;
import com.technologies.yanny.xappes.utilities.Marca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment{

    private OnFragmentInteractionListener mListener;

    private GridView gv_trobades;

    private List<TrobadesDO> trobades;
    private String[] trobadesList;
    private boolean isOk = false;

    private Button bt_map;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    public MapsFragment() {
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
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {

        ((HomeActivity) getActivity()).showProgress(true);
        ((HomeActivity) getActivity()).setProgressB(35);

        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        this.bt_map = (Button) getActivity().findViewById(R.id.bt_map);

        this.gv_trobades = (GridView) getActivity().findViewById(R.id.gv_trobades);

        searchTrobades();

        this.bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).showProgress(true);

                ((HomeActivity) getActivity()).setProgressB(70);
                Intent newActivity = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                newActivity.putParcelableArrayListExtra("selectedTrobada",(ArrayList) trobades);
                startActivity(newActivity);

                ((HomeActivity) getActivity()).showProgress(false);
            }
        });

        this.gv_trobades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).showProgress(true);

                List<TrobadesDO> selectedTrobada = new ArrayList<>();
                selectedTrobada.add(trobades.get(position/2));

                ((HomeActivity) getActivity()).setProgressB(70);
                Intent newActivity = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                newActivity.putParcelableArrayListExtra("selectedTrobada",(ArrayList) selectedTrobada);
                startActivity(newActivity);

                ((HomeActivity) getActivity()).showProgress(false);
            }
        });
    }

    private void searchTrobades() {
        this.trobades = new ArrayList<>();
        isOk = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                List<TrobadesDO> trobadesResult = dynamoDBMapper.scan(TrobadesDO.class, scanExpression);
                System.out.println(trobadesResult.toString());

                ((HomeActivity) getActivity()).setProgressB(70);

                for (TrobadesDO trobada : trobadesResult) {
                    trobades.add(trobada);
                }

                isOk = true;
            }
        }).start();
        loadTrobades();
    }

    private void loadTrobades() {
        while (!this.isOk);
        ((HomeActivity) getActivity()).setProgressB(90);

        this.trobadesList = new String[this.trobades.size()*2];
        int j = 0;
        for (int i = 0; i < this.trobades.size(); i++) {
            this.trobadesList[j] = this.trobades.get(i).getTrobadaTitle() + " (" + this.trobades.get(i).getTrobadaData() + ")";
            this.trobadesList[j+1] = this.trobades.get(i).getTrobadaText();
            j+=2;
        }

        this.gv_trobades.setAdapter(new TrobadesAdapter(this.getActivity()));
        ((HomeActivity) getActivity()).showProgress(false);
    }

    public class TrobadesAdapter extends BaseAdapter {
        private Context mContext;

        public TrobadesAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return trobadesList.length;
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
                if (position % 2 == 0) {
                    tv.setTextSize(20);
                    tv.setTextColor(Color.RED);
                    tv.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
                } else {
                    tv.setTextSize(14);
                    tv.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
                }
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(trobadesList[position]);
            return tv;
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
