package com.snaprix.android.gms.maps;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.snaprix.android.gms.maps.map.GoogleMap;
import com.snaprix.android.gms.maps.model.Circle;
import com.snaprix.android.gms.maps.model.Marker;
import com.snaprix.android.gms.maps.model.MarkerData;
import com.snaprix.android.gms.maps.utils.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladimirryabchikov on 7/3/14.
 */
public class WithMarkersMapPackFragment extends MapPackFragment {
    private static final String EXTRA_HAS_SAVED_POSITION = "extra_has_saved_position";
    private static final String EXTRA_TARGET_LAT = "extra_target_lat";
    private static final String EXTRA_TARGET_LNG = "extra_target_lng";
    private static final String EXTRA_ZOOM = "extra_zoom";
    private static final String EXTRA_BEARING = "extra_bearing";
    private static final String EXTRA_TILT = "extra_tilt";

    private boolean mHasPosition;
    private HashMap<MarkerData, Marker> mDataToMarker = new HashMap<>();

    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;

    private ArrayList<PendingCameraPosition> mPendingPositions = new ArrayList<>();
    private boolean mIsProcessingPendingPosition = false;

    public static WithMarkersMapPackFragment newInstance(){
        return new WithMarkersMapPackFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            boolean hasSavedPosition = savedInstanceState.getBoolean(EXTRA_HAS_SAVED_POSITION, false);
            if (hasSavedPosition){
                double lat = savedInstanceState.getDouble(EXTRA_TARGET_LAT);
                double lng = savedInstanceState.getDouble(EXTRA_TARGET_LNG);
                float zoom = savedInstanceState.getFloat(EXTRA_ZOOM);
                float bearing = savedInstanceState.getFloat(EXTRA_BEARING);
                float tilt = savedInstanceState.getFloat(EXTRA_TILT);

                changeCamera(lat, lng, zoom, bearing, tilt, false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            CameraPosition pos = getGoogleMap().getCameraPosition();
            outState.putBoolean(EXTRA_HAS_SAVED_POSITION, true);
            outState.putDouble(EXTRA_TARGET_LAT, pos.target.latitude);
            outState.putDouble(EXTRA_TARGET_LNG, pos.target.longitude);
            outState.putFloat(EXTRA_ZOOM, pos.zoom);
            outState.putFloat(EXTRA_BEARING, pos.bearing);
            outState.putFloat(EXTRA_TILT, pos.tilt);
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapCreate(boolean hasSavedInstanceState) {
        super.onMapCreate(hasSavedInstanceState);

        // setup map before notifying listeners
        GoogleMap map = getGoogleMap();

        try {
            map.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);

            changeCameraWhenAvailable();
        } catch (GoogleMap.MapException e) {
            // it still could happen on some devices,
            // may be due to Google Play Services unavailable
        }
    }

    @Override
    public void onMapResume() {
        super.onMapResume();

        try {
            getGoogleMap().setMyLocationEnabled(true);
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
        }
    }

    @Override
    public void onMapPause() {
        try {
            getGoogleMap().setMyLocationEnabled(false);
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
        }

        super.onMapPause();
    }

    @Override
    public void onTouchActionUp(){
        super.onTouchActionUp();
        // map was touched by user, consider it as position was set
        mHasPosition = true;
    }

    public void setOnMarkerClickListener(final OnMarkerClickListener listener) throws GoogleMap.MapException {
        GoogleMap.OnMarkerClickListener internalListener = listener != null ? new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                return listener.onMarkerClick(data, marker);
            }
        } : null;

        getGoogleMap().setOnMarkerClickListener(internalListener);
    }

    public void setOnMarkerDragListener(final OnMarkerDragListener listener) throws GoogleMap.MapException {
        GoogleMap.OnMarkerDragListener internalListener = listener != null ? new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                listener.onMarkerDragStart(data, marker);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                listener.onMarkerDrag(data, marker);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                listener.onMarkerDragEnd(data, marker);
            }
        } : null;

        getGoogleMap().setOnMarkerDragListener(internalListener);
    }

    public void setInfoWindowAdapter(final InfoWindowAdapter adapter) throws GoogleMap.MapException {
        GoogleMap.InfoWindowAdapter internalAdapter = adapter != null ? new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                return adapter.getInfoWindow(data, marker);
            }

            @Override
            public View getInfoContents(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                return adapter.getInfoContents(data, marker);
            }
        }: null;

        getGoogleMap().setInfoWindowAdapter(internalAdapter);
    }

    public void setOnInfoWindowClickListener(final OnInfoWindowClickListener listener) throws GoogleMap.MapException {
        GoogleMap.OnInfoWindowClickListener internalListener = listener != null ? new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MarkerData data = getDataForMarker(marker);
                listener.onInfoWindowClick(data, marker);
            }
        } : null;

        getGoogleMap().setOnInfoWindowClickListener(internalListener);
    }

    public void setOnCameraChangeListener(final OnCameraChangeListener listener) throws GoogleMap.MapException {
        OnCameraChangeListener internalListener = listener != null ? new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (SHOW_LOGS) Log.v(TAG, String.format("onCameraChange position=%s target=%s mHasPosition=%b",
                        position, position.target, mHasPosition));

                // filter out positions near (0,0), which could be received here
                if (!mHasPosition) {
                    return;
                }
                listener.onCameraChange(position);
            }
        } : null;

        getGoogleMap().setOnCameraChangeListener(internalListener);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (SHOW_LOGS) Log.v(TAG, String.format("setPadding left=%d top=%d right=%d bottom=%d",
                left, top, right, bottom));

        try {
            getGoogleMap().setPadding(left, top, right, bottom);
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();

            mPaddingLeft = left;
            mPaddingTop = top;
            mPaddingRight = right;
            mPaddingBottom = bottom;
        }
    }

    /**
     * change camera position
     * method calls are serialized and last one would be executed
     * after previous call is finished,
     * intermediate request will be omitted
     *
     * method should be called from UI thread
     *
     * @param lat
     * @param lng
     * @param zoom
     * @param bearing
     * @param tilt
     * @param animated
     * @return
     */
    public boolean changeCamera(double lat, double lng, float zoom, float bearing, float tilt, boolean animated){
        if (SHOW_LOGS) Log.v(TAG, String.format("changeCamera lat=%f lng=%f zoom=%f bearing=%f tilt=%f animated=%b",
                lat, lng, zoom, bearing, tilt, animated));

        final PendingCameraPosition position = new PendingCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(zoom)
                .tilt(tilt)
                .bearing(bearing).build(), animated);

        mPendingPositions.add(position);

        try {
            checkMapCreated();
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
            return false;
        }

        changeCameraWhenAvailable();
        return true;
    }

    public boolean hasPosition() {
        return mHasPosition;
    }

    // getMarker(data: MarkerData): Marker
    @Nullable
    public Marker getMarker(MarkerData data){
        return mDataToMarker.get(data);
    }

    // addMarker(data: MarkerData, options: MarkerOptions, icon: Bitmap)
    public void addMarker(MarkerData data, MarkerOptions options, Bitmap icon){
        Marker marker = getMarker(data);
        boolean hasMarker = (marker != null);
        if (hasMarker){
            if (icon != null){
                boolean hasInfoWindow = marker.isInfoWindowShown();
                try {
                    marker.setIcon(icon);

                    if (hasInfoWindow) {
                        marker.showInfoWindow();
                    }
                } catch (IllegalArgumentException e){
                    // strange "released unknown bitmap reference" happens here
                    if (SHOW_LOGS) Log.w(TAG, String.format("bitmap %s", e.toString()));
                }
            }
        } else {
            try {
                marker = getGoogleMap().addMarker(options, icon);
            } catch (GoogleMap.MapException e) {
                if (SHOW_LOGS) Log.w(TAG, String.format("addMarker data=%s not added!!!", data), e);
                return;
            }
        }
        setData(data, marker);
    }

    // removeMarker(data: MarkerData)
    public void removeMarker(MarkerData data) {
        Marker marker = getMarker(data);
        if (marker != null){
            marker.remove();
            removeData(data, marker);
        }
    }

    // removeMarkers(bounds: LatLngBounds)
    /**
     * remove markers outside of given bounds
     * @param bounds
     */
    public void removeMarkers(LatLngBounds bounds) {
        if (SHOW_LOGS) Log.w(TAG, "removeMarkers " + mDataToMarker.size());

        ArrayList<MarkerData> delete = new ArrayList<>();

        for (Map.Entry<MarkerData, Marker> entry : mDataToMarker.entrySet()) {
            LatLng pos = entry.getValue().getPosition();
            if (bounds == null || !bounds.contains(new LatLng(pos.latitude, pos.longitude))){
                delete.add(entry.getKey());
            }
        }

        for (int i = delete.size() - 1; i >= 0; i--){
            MarkerData data = delete.get(i);
            removeMarker(data);
        }
    }

    // getMarkersCount(): int
    public int getMarkersCount(){
        return mDataToMarker.size();
    }

    public LatLng interpolateToBottom(LatLng latLng, double fraction) throws GoogleMap.MapException {
        if (SHOW_LOGS) Log.d(TAG, String.format("interpolateToBottom from=%s fraction=%f",
                latLng, fraction));

        VisibleRegion vr = getGoogleMap().getVisibleRegion();
        LatLng center = getGoogleMap().getCameraPosition().target;

        return MapUtil.interpolateToBottom(latLng,
                vr.nearLeft, vr.nearRight, center,
                fraction);
    }

    public Circle addCircle(CircleOptions options) throws GoogleMap.MapException {
        return getGoogleMap().addCircle(options);
    }

    private MarkerData getDataForMarker(Marker marker){
        MarkerData data = null;
        for (Map.Entry<MarkerData, Marker> entry : mDataToMarker.entrySet()) {
            if (marker.equals(entry.getValue())) {
                data = entry.getKey();
                break;
            }
        }
        return data;
    }

    private void checkMapCreated() throws GoogleMap.MapException {
        if (getGoogleMap() == null) {
            throw new GoogleMap.MapException("map is not ready");
        }
    }

    private void changeCameraWhenAvailable(){
        if (SHOW_LOGS) Log.v(TAG, String.format("changeCameraWhenAvailable mIsProcessingPendingPosition=%b queue=%d",
                mIsProcessingPendingPosition, mPendingPositions.size()));

        if (!mIsProcessingPendingPosition && mPendingPositions.size() > 0) {
            onChangeCameraAvailable();
        }
    }

    private void onChangeCameraAvailable(){
        if (SHOW_LOGS) Log.v(TAG, String.format("onChangeCameraAvailable queue=%d", mPendingPositions.size()));

        final PendingCameraPosition nextPosition = mPendingPositions.get(mPendingPositions.size() - 1);
        mPendingPositions.clear();

        changeCamera(nextPosition);
    }

    private void changeCamera(PendingCameraPosition position){
        if (SHOW_LOGS) Log.v(TAG, String.format("changeCamera position=%s", position));

        if (position.animated) {
            animateCamera(getGoogleMap(), position.position);
        } else {
            moveCamera(getGoogleMap(), position.position);
        }
    }

    private void moveCamera(GoogleMap map, CameraPosition pos){
        if (SHOW_LOGS) Log.v(TAG, String.format("moveCamera started pos=%s", pos));
        mIsProcessingPendingPosition = true;
        try {
            map.moveCamera(pos);

            if (SHOW_LOGS) Log.v(TAG, String.format("moveCamera finished pos=%s", pos));

            mHasPosition = true;
            mIsProcessingPendingPosition = false;

            changeCameraWhenAvailable();
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
            mIsProcessingPendingPosition = false;
        }
    }

    private void animateCamera(GoogleMap map, final CameraPosition pos){
        if (SHOW_LOGS) Log.v(TAG, String.format("animateCamera started pos=%s", pos));
        mIsProcessingPendingPosition = true;
        try {
            map.animateCamera(pos, new com.google.android.gms.maps.GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    if (SHOW_LOGS) Log.v(TAG, String.format("animateCamera onFinish pos=%s", pos));

                    mHasPosition = true;
                    mIsProcessingPendingPosition = false;

                    changeCameraWhenAvailable();
                }

                @Override
                public void onCancel() {
                    if (SHOW_LOGS) Log.v(TAG, String.format("animateCamera onCancel pos=%s", pos));

                    mHasPosition = true;
                    mIsProcessingPendingPosition = false;

                    /**
                     *  If the animation stops due to interruption by a later camera movement or a user gesture, onCancel() will be called.
                     *  The callback should not attempt to move or animate the camera in its cancellation method
                     */
                }
            });
        } catch (GoogleMap.MapException e) {
            if (SHOW_LOGS) e.printStackTrace();
            mIsProcessingPendingPosition = false;
        }
    }

    private void setData(MarkerData data, Marker marker){
        mDataToMarker.put(data, marker);
    }

    private void removeData(MarkerData data, Marker marker){
        mDataToMarker.remove(data);
    }





    public interface OnMarkerClickListener{
        boolean onMarkerClick(MarkerData data, Marker marker);
    }

    public interface OnMarkerDragListener{
        void onMarkerDragStart(MarkerData data, Marker marker);
        void onMarkerDrag(MarkerData data, Marker marker);
        void onMarkerDragEnd(MarkerData data, Marker marker);
    }

    public interface InfoWindowAdapter{
        View getInfoWindow(MarkerData data, Marker marker);
        View getInfoContents(MarkerData data, Marker marker);
    }

    public interface OnInfoWindowClickListener{
        void onInfoWindowClick(MarkerData data, Marker marker);
    }

    private static class PendingCameraPosition{
        private final CameraPosition position;
        private final boolean animated;

        private PendingCameraPosition(CameraPosition position, boolean animated) {
            this.position = position;
            this.animated = animated;
        }

        @Override
        public String toString() {
            return "PendingCameraPosition{" +
                    "position=" + position +
                    ", animated=" + animated +
                    '}';
        }
    }
}