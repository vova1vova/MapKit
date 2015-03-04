package com.snaprix.android.gms.maps.map;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.snaprix.android.gms.maps.model.Circle;
import com.snaprix.android.gms.maps.model.Marker;

/**
 * Created by vladimirryabchikov on 9/4/14.
 */
public interface GoogleMap {
    public CameraPosition getCameraPosition() throws MapException;

//    public float getMaxZoomLevel() throws MapException;
//
//    public float getMinZoomLevel() throws MapException;

    public void moveCamera(CameraPosition position) throws MapException;

    public void animateCamera(CameraPosition position) throws MapException;

    public void animateCamera(CameraPosition position, CancelableCallback callback) throws MapException;

    public void animateCamera(CameraPosition position, int durationMs, CancelableCallback callback) throws MapException;

//    public void stopAnimation() throws MapException;
//
//    public Polyline addPolyline(PolylineOptions options) throws MapException;
//
//    public Polygon addPolygon(PolygonOptions options) throws MapException;
//
    public Circle addCircle(CircleOptions options) throws MapException;

    /**
     *
     * @param options
     * @param bitmap when null - default marker will be used
     * @return added marker
     * @throws MapException
     */
    public Marker addMarker(MarkerOptions options, @Nullable Bitmap bitmap) throws MapException;

//    public GroundOverlay addGroundOverlay(GroundOverlayOptions options) throws MapException;

    public TileOverlay addTileOverlay(TileOverlayOptions options) throws MapException;

    public void clear() throws MapException;

//    public IndoorBuilding getFocusedBuilding() throws MapException;
//
//    public void setOnIndoorStateChangeListener(GoogleMap.OnIndoorStateChangeListener listener) throws MapException;
//
//    public int getMapType() throws MapException;

    public void setMapType(int type) throws MapException;

//    public boolean isTrafficEnabled() throws MapException;
//
//    public void setTrafficEnabled(boolean enabled) throws MapException;
//
//    public boolean isIndoorEnabled() throws MapException;
//
//    public boolean setIndoorEnabled(boolean enabled) throws MapException;
//
//    public boolean isBuildingsEnabled() throws MapException;
//
//    public void setBuildingsEnabled(boolean enabled) throws MapException;
//
//    public boolean isMyLocationEnabled() throws MapException;

    public void setMyLocationEnabled(boolean enabled) throws MapException;

//    @Deprecated
//    public Location getMyLocation() throws MapException;
//
//    public void setLocationSource(LocationSource source) throws MapException;

//    public void setAllGesturesEnabled(boolean enabled) throws MapException;
//
//    public void setCompassEnabled(boolean enabled) throws MapException;

    public void setMyLocationButtonEnabled(boolean enabled) throws MapException;

//    public void setRotateGesturesEnabled(boolean enabled) throws MapException;
//
//    public void setScrollGesturesEnabled(boolean enabled) throws MapException;
//
//    public void setTiltGesturesEnabled(boolean enabled) throws MapException;

    /**
     * change zoom controls visibility, it's hidden by default
     *
     * @param enabled
     * @throws MapException
     */
    public void setZoomControlsEnabled(boolean enabled) throws MapException;

//    public void setZoomGesturesEnabled(boolean enabled) throws MapException;
    

    public VisibleRegion getVisibleRegion() throws MapException;

    public void setOnCameraChangeListener(@Nullable OnCameraChangeListener listener) throws MapException;

    public void setOnMapClickListener(@Nullable OnMapClickListener listener) throws MapException;

    public void setOnMapLongClickListener(@Nullable OnMapLongClickListener listener) throws MapException;

    public void setOnMarkerClickListener(@Nullable OnMarkerClickListener listener) throws MapException;

    public void setOnMarkerDragListener(@Nullable OnMarkerDragListener listener) throws MapException;

    public void setOnInfoWindowClickListener(@Nullable OnInfoWindowClickListener listener) throws MapException;

    public void setInfoWindowAdapter(@Nullable InfoWindowAdapter adapter) throws MapException;

//    @Deprecated
//    public void setOnMyLocationChangeListener(GoogleMap.OnMyLocationChangeListener listener) throws MapException;
//
//    public void setOnMyLocationButtonClickListener(GoogleMap.OnMyLocationButtonClickListener listener) throws MapException;
//
//    public void setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback callback) throws MapException;
//
//    public void snapshot(GoogleMap.SnapshotReadyCallback callback) throws MapException;
//
//    public void snapshot(GoogleMap.SnapshotReadyCallback callback, Bitmap bitmap) throws MapException;

    public void setPadding(int left, int top, int right, int bottom) throws MapException;



    public static class MapException extends Exception {
        public MapException(String detailedMessage) {
            super(detailedMessage);
        }

        public MapException(String detailedMessage, Throwable throwable) {
            super(detailedMessage, throwable);
        }

        public MapException(Throwable throwable) {
            super(throwable);
        }
    }

    public static abstract interface OnMarkerClickListener
    {
        public abstract boolean onMarkerClick(Marker marker);
    }

    public static abstract interface OnMarkerDragListener
    {
        public abstract void onMarkerDragStart(Marker marker);

        public abstract void onMarkerDrag(Marker marker);

        public abstract void onMarkerDragEnd(Marker marker);
    }

    public static abstract interface OnInfoWindowClickListener
    {
        public abstract void onInfoWindowClick(Marker marker);
    }

    public static abstract interface InfoWindowAdapter
    {
        public abstract View getInfoWindow(Marker marker);

        public abstract View getInfoContents(Marker marker);
    }
}