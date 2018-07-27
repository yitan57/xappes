package com.technologies.yanny.xappes.utilities;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.squareup.picasso.Picasso;
import com.technologies.yanny.xappes.R;

import java.io.File;

public class S3Transfer {

    private Context context;
    private File file;
    private String key;

    private AWSStartupResult awsStartupResult;

    public void uploadData(Context context, File file, String key) {
        this.key = key;
        this.context = context;
        this.file = file;
        AWSMobileClient.getInstance().initialize(context, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                uploadWithTransferUtility();
            }
        }).execute();
    }

    private void uploadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(this.context.getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        this.key, this.file);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }

    public void dowloadData(final Context context, String key, ImageView im) {
        this.context = context;
        this.key = key;

        Picasso.get().load(context.getResources().getString(R.string.bucketURL) + key).into(im);
    }

}
