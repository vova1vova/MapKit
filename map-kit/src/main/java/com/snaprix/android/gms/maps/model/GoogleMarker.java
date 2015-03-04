package com.snaprix.android.gms.maps.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vladimirryabchikov on 9/4/14.
 */
public class GoogleMarker implements Marker {
    private com.google.android.gms.maps.model.Marker mMarker;

    public GoogleMarker(com.google.android.gms.maps.model.Marker marker) {
        mMarker = marker;
    }

    @Override
    public void remove() {
        mMarker.remove();
    }

    @Override
    public String getId() {
        return mMarker.getId();
    }

    @Override
    public void setPosition(LatLng latlng) {
        mMarker.setPosition(latlng);
    }

    @Override
    public LatLng getPosition() {
        return mMarker.getPosition();
    }

    @Override
    public void setIcon(Bitmap icon) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    public void setAnchor(float anchorU, float anchorV) {
        mMarker.setAnchor(anchorU, anchorV);
    }

    @Override
    public void setInfoWindowAnchor(float anchorU, float anchorV) {
        mMarker.setInfoWindowAnchor(anchorU, anchorV);
    }

    @Override
    public void setTitle(String title) {
        mMarker.setTitle(title);
    }

    @Override
    public String getTitle() {
        return mMarker.getTitle();
    }

    @Override
    public void setSnippet(String snippet) {
        mMarker.setSnippet(snippet);
    }

    @Override
    public String getSnippet() {
        return mMarker.getSnippet();
    }

    @Override
    public void setDraggable(boolean draggable) {
        mMarker.setDraggable(draggable);
    }

    @Override
    public boolean isDraggable() {
        return mMarker.isDraggable();
    }

    @Override
    public void showInfoWindow() {
        mMarker.showInfoWindow();
    }

    @Override
    public void hideInfoWindow() {
        mMarker.hideInfoWindow();
    }

    @Override
    public boolean isInfoWindowShown() {
        return mMarker.isInfoWindowShown();
    }

    @Override
    public void setVisible(boolean visible) {
        mMarker.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return mMarker.isVisible();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleMarker that = (GoogleMarker) o;

        if (!mMarker.equals(that.mMarker)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }
}
