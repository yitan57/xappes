package com.technologies.yanny.xappes.Xapes;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.models.nosql.CavesDO;
import com.amazonaws.models.nosql.XapesDO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.technologies.yanny.xappes.Caves.CavesListFragment;
import com.technologies.yanny.xappes.Database.DynamoDB;
import com.technologies.yanny.xappes.R;
import com.technologies.yanny.xappes.main.MenuActivity;
import com.technologies.yanny.xappes.utilities.Data.Caves;
import com.technologies.yanny.xappes.utilities.S3Transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CrearXapaFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_CAPTURE = 1;
    static final int CROP_PIC = 2;
    static final int RESULT_LOAD_IMAGE = 1;

    private CavesDO cavaSelected;

    private String user;
    private String codigoFinal;

    private File pictureFile;

    private Uri picUri;

    private TextView et_repetides;
    private TextView et_venda;
    private TextView tv_cava;
    private TextView tv_placa;
    private TextView tv_usuari;
    private TextView tv_codigo;

    private Button bt_buscar_xapa;
    private Button bt_foto;
    private Button bt_save;
    private Button bt_select_cava;

    private boolean increment;

    private ImageView iv_picture;
    private ImageView iv_picture2;

    private String cavaName;
    private String xapaId;
    private String newId;
    private String newCavaId;

    private LinearLayout ll_la_meva_coleccio;
    private FrameLayout fl_main;

    private com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    private OnFragmentInteractionListener mListener;

    public CrearXapaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.cavaName = getArguments().getString("cava");
            this.xapaId = getArguments().getString("xappaId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_xapa, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.dynamoDBMapper = new DynamoDB().myDynamoDB(this.getActivity());

        this.tv_cava = (TextView) this.getActivity().findViewById(R.id.tv_cava);
        this.tv_placa = (TextView) this.getActivity().findViewById(R.id.tv_placa);
        this.tv_usuari = (TextView) this.getActivity().findViewById(R.id.tv_usuari);
        this.tv_codigo = (TextView) this.getActivity().findViewById(R.id.tv_codigo);

        this.bt_buscar_xapa = (Button) this.getActivity().findViewById(R.id.bt_buscar_xapa);
        this.bt_foto = (Button) this.getActivity().findViewById(R.id.bt_foto);
        this.bt_save = (Button) this.getActivity().findViewById(R.id.bt_save);
        this.bt_select_cava = (Button) this.getActivity().findViewById(R.id.bt_select_cava);

        this.ll_la_meva_coleccio = (LinearLayout) this.getActivity().findViewById(R.id.ll_la_meva_coleccio);
        this.fl_main = (FrameLayout) this.getActivity().findViewById(R.id.fl_main);

        this.iv_picture = (ImageView) this.getActivity().findViewById(R.id.iv_picture);
        this.iv_picture2 = (ImageView) this.getActivity().findViewById(R.id.iv_picture2);

        ((MenuActivity)getActivity()).showProgress(false);

        this.bt_buscar_xapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChargeXapasFragment();
            }
        });

        this.bt_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        this.bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewXapa(cavaName, codigoFinal, user, cavaSelected, increment);
            }
        });

        this.bt_select_cava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSelectCavaFragment();
            }
        });

        if (this.cavaName != null && this.xapaId != null) {
            if (this.xapaId.equals("-1")) {
                this.increment = true;
                setCava(this.cavaName);
                toggleVisibleCamera(View.VISIBLE);
            } else {
                this.increment = false;
                chargeSelectedCava();
                toggleVisibleCamera(View.GONE);
            }
        }

        this.iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChargeXapasFragment();
            }
        });
        this.iv_picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSelectCavaFragment();
            }
        });

    }

    private void toggleVisibleCamera(int view) {
        this.bt_foto.setVisibility(view);
    }

    private void chargeSelectedCava() {
        setImage(this.xapaId);
        setCava(this.cavaName);
        this.ll_la_meva_coleccio.setVisibility(View.VISIBLE);
        this.tv_codigo.setText(this.xapaId);
        this.user = ((MenuActivity)getActivity()).getUsuari();
        this.codigoFinal = this.xapaId;
        this.tv_usuari.setText(this.user);
        getCava();
    }

    private void loadSelectCavaFragment() {
        ((MenuActivity)getActivity()).showProgress(true);
        ((MenuActivity)getActivity()).setProgressB(80);
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_main, new CavesListFragment())
                .commit();
    }

    private void setCava(String cavaName) {
        togleCavaImg(false);
        this.tv_cava.setText(cavaName);
    }

    private void togleCavaImg(boolean value) {
        if (value) {
            this.tv_cava.setVisibility(View.GONE);
            this.iv_picture2.setVisibility(View.VISIBLE);
        } else {
            this.tv_cava.setVisibility(View.VISIBLE);
            this.iv_picture2.setVisibility(View.GONE);
        }
    }

    private void saveImage(String id) {
        S3Transfer s3 = new S3Transfer();
        //File newFile = getImageUri(this.picture);
        if (this.pictureFile != null) s3.uploadData(getActivity(), this.pictureFile, getResources().getString(R.string.xappesDirectory)+ id + ".jpg");
        else ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, new CrearXapaFragment()).commit();
    }

    private void setImage(String id) {
        S3Transfer s3 = new S3Transfer();
        s3.dowloadData(getActivity(), id+".jpg", this.iv_picture);
        Log.d("URL",getResources().getString(R.string.bucketURL)+id+".jpg");
    }

    public void saveNewXapa(String nameCava, String xapaId, String user, CavesDO cava, boolean increment) {
        final XapesDO newsItem = new XapesDO();

        newsItem.setXapesId(xapaId);
        newsItem.setCavaName(nameCava.toLowerCase());
        newsItem.setUserId(user);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(newsItem);
            }
        }).start();

        if (increment) {
            final CavesDO newsItemCava = cava;

            int numXappes = Integer.parseInt(cava.getNumXappes());
            numXappes++;

            newsItemCava.setNumXappes(String.valueOf(numXappes));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dynamoDBMapper.save(newsItemCava);
                }
            }).start();
        }

        saveImage(xapaId);

    }

    private void loadChargeXapasFragment() {
        ((MenuActivity)getActivity()).showProgress(true);
        ((MenuActivity)getActivity()).setProgressB(50);
        if (this.cavaName == null) getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_main_fragment, new XapesListFragment()).commit();
        else {
            XapesListFragment newFragment = new XapesListFragment();
            Bundle args = new Bundle();

            args.putString("cava", this.cavaName);
            newFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_fragment, newFragment).commit();
        }
    }

    private void dispatchTakePictureIntent() {
        try{
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                // get the Uri for the captured image
                this.picUri= data.getData();
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {
                try {// get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap thePic = (Bitmap) extras.get("data");
                    this.iv_picture.setImageBitmap(thePic);

                    this.pictureFile = new File(getRealPathFromURI(this.picUri));
                    prepareNewXappa();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error cortando imagen", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Error",Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void prepareNewXappa() {
        this.ll_la_meva_coleccio.setVisibility(View.VISIBLE);

        String user = ((MenuActivity)getActivity()).getUsuari();
        this.tv_usuari.setText(user);

        int codigo = Integer.parseInt(getNumId())+1;

        String codigoFinal = "000";
        if (codigo < 10) codigoFinal = "00" + String.valueOf(codigo);
        if (codigo < 100 && codigo >= 10) codigoFinal = "0" + String.valueOf(codigo);
        if (codigo < 1000 && codigo >= 100) codigoFinal = String.valueOf(codigo);

        this.tv_codigo.setText(this.newCavaId + codigoFinal);

        this.codigoFinal = this.newCavaId+codigoFinal;
        this.user = user;

    }

    private void getCava() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                String value = tv_cava.getText().toString();

                Condition condition;

                condition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue(value));
                scanExpression.addFilterCondition("cavaName", condition);

                List<CavesDO> cavesResult = dynamoDBMapper.scan(CavesDO.class, scanExpression);
                System.out.println(cavesResult.toString());

                if (cavesResult.size() > 0) cavaSelected = cavesResult.get(0);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT);
        }
    }

    private String getNumId() {
        this.newId = "";
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

                String value = tv_cava.getText().toString();

                Condition condition;

                    condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ)
                            .withAttributeValueList(new AttributeValue(value));
                    scanExpression.addFilterCondition("cavaName", condition);

                List<CavesDO> cavesResult = dynamoDBMapper.scan(CavesDO.class, scanExpression);
                System.out.println(cavesResult.toString());

                cavaSelected = cavesResult.get(0);

                if (cavesResult.size() > 0) {
                    newId = cavesResult.get(0).getNumXappes();
                    newCavaId = cavesResult.get(0).getCavaId();
                }
            }
        });
        t.start();
        try {
            t.join();
            return this.newId;
        } catch (InterruptedException e) {
            Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT);
            return null;
        }
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
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
