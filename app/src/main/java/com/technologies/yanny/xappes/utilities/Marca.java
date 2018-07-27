package com.technologies.yanny.xappes.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technologies.yanny.xappes.R;

public class Marca {
    private String id;
    private String title;
    private String contenido;
    private LatLng position;
    private BitmapDescriptor bitmapDescriptor;

    private Context context;

    public String getId() {
        return id;
    }

    public Marca(String title, String contenido, LatLng position, int resource, Context context) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.contenido = contenido;
        this.position = position;
        setBitmapDescriptor(resource);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public BitmapDescriptor getBitmapDescriptor() {
        return bitmapDescriptor;
    }

    public void setBitmapDescriptor(int resource) {
        if (resource != -1) this.bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(createImageMarker(resource));
        else this.bitmapDescriptor = null;
    }

    public String generateMarker(GoogleMap map) {
        MarkerOptions markerOptions = new MarkerOptions();
        if (this.bitmapDescriptor != null) markerOptions.icon(this.bitmapDescriptor);
        this.id = map.addMarker(markerOptions.position(getPosition())).getId();
        markerOptions.anchor(0.5f, 1);
        return this.id;
    }

    public Bitmap createImageMarker(int resource) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(100, 150, conf);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(20);
        color.setColor(Color.BLUE);
        Bitmap bt = BitmapFactory.decodeResource(this.context.getResources(), resource);
        Bitmap btResized = Bitmap.createScaledBitmap(bt, 100, 150, false);

        // modify canvas
        canvas1.drawBitmap(btResized, 0,0, color);

        return bmp;
    }
}
