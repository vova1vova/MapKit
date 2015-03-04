package com.snaprix.android.gms.maps.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vladimirryabchikov on 9/4/14.
 */
public interface Marker {
    public void remove();

    public String getId();

    public void setPosition(LatLng latlng);

    public LatLng getPosition();

    public void setIcon(Bitmap icon);

    public void setAnchor(float anchorU, float anchorV);

    public void setInfoWindowAnchor(float anchorU, float anchorV);

    public void setTitle(String title);

    public String getTitle();

    public void setSnippet(String snippet);

    public String getSnippet();

    public void setDraggable(boolean draggable);

    public boolean isDraggable();

    public void showInfoWindow();

    public void hideInfoWindow();

    public boolean isInfoWindowShown();

    public void setVisible(boolean visible);

    public boolean isVisible();

//    public void setFlat(boolean flat);
//
//    public boolean isFlat();
//
//    public void setRotation(float rotation);
//
//    public float getRotation();
//
//    public void setAlpha(float alpha);
//
//    public float getAlpha();
}
