package com.snaprix.android.gms.maps.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vladimirryabchikov on 11/16/14.
 */
public class GoogleCircle implements Circle {
    private com.google.android.gms.maps.model.Circle mCircle;

    public GoogleCircle(com.google.android.gms.maps.model.Circle circle) {
        mCircle = circle;
    }

    @Override
    public void remove() {
        mCircle.remove();
    }

    @Override
    public String getId() {
        return mCircle.getId();
    }

    @Override
    public void setCenter(LatLng center) {
        mCircle.setCenter(center);
    }

    @Override
    public LatLng getCenter() {
        return mCircle.getCenter();
    }

    @Override
    public void setRadius(double radius) {
        mCircle.setRadius(radius);
    }

    @Override
    public double getRadius() {
        return mCircle.getRadius();
    }

    @Override
    public void setStrokeWidth(float width) {
        mCircle.setStrokeWidth(width);
    }

    @Override
    public float getStrokeWidth() {
        return mCircle.getStrokeWidth();
    }

    @Override
    public void setStrokeColor(int color) {
        mCircle.setStrokeColor(color);
    }

    @Override
    public int getStrokeColor() {
        return mCircle.getStrokeColor();
    }

    @Override
    public void setFillColor(int color) {
        mCircle.setFillColor(color);
    }

    @Override
    public int getFillColor() {
        return mCircle.getFillColor();
    }

    @Override
    public void setZIndex(float zIndex) {
        mCircle.setZIndex(zIndex);
    }

    @Override
    public float getZIndex() {
        return mCircle.getZIndex();
    }

    @Override
    public void setVisible(boolean visible) {
        mCircle.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return mCircle.isVisible();
    }
}
