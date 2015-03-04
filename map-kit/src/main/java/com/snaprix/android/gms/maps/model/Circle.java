package com.snaprix.android.gms.maps.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vladimirryabchikov on 11/16/14.
 */
public interface Circle {
    void remove();
    String getId();
    void setCenter(LatLng center);
    LatLng getCenter();
    void setRadius(double radius);
    double getRadius();
    void setStrokeWidth(float width);
    float getStrokeWidth();
    void setStrokeColor(int color);
    int getStrokeColor();
    void setFillColor(int color);
    int getFillColor();
    void setZIndex(float zIndex);
    float getZIndex();
    void setVisible(boolean visible);
    boolean isVisible();
}