package com.snaprix.android.gms.maps.map;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.snaprix.android.gms.maps.model.Circle;
import com.snaprix.android.gms.maps.model.GoogleCircle;
import com.snaprix.android.gms.maps.model.GoogleMarker;
import com.snaprix.android.gms.maps.model.Marker;

/**
 * Created by vladimirryabchikov on 9/4/14.
 */
public abstract class PlayGoogleMap implements GoogleMap {
    protected abstract com.google.android.gms.maps.GoogleMap getMap(String method) throws MapException;

    @Override
    public CameraPosition getCameraPosition() throws MapException {
        return getMap("getCameraPosition").getCameraPosition();
    }

    @Override
    public void moveCamera(CameraPosition position) throws MapException {
        getMap("moveCamera").moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    public void animateCamera(CameraPosition position) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    public void animateCamera(CameraPosition position, com.google.android.gms.maps.GoogleMap.CancelableCallback callback) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(position), callback);
    }

    @Override
    public void animateCamera(CameraPosition position, int durationMs, com.google.android.gms.maps.GoogleMap.CancelableCallback callback) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(position), durationMs, callback);
    }

    @Override
    public Circle addCircle(CircleOptions options) throws MapException {
        return new GoogleCircle(getMap("addCircle").addCircle(options));
    }

    @Override
    public Marker addMarker(MarkerOptions options, @Nullable Bitmap bitmap) throws MapException {
        if (bitmap != null){
            try {
                options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            } catch (NullPointerException e) {
                // IBitmapDescriptorFactory is not initialized
                // https://www.crashlytics.com/electrounion/android/apps/ru.multigo.multitoplivo/issues/5439adb5e3de5099ba07cf7d
                throw new MapException(e);
            }
        }

        return new GoogleMarker(getMap("addMarker").addMarker(options));
    }

    @Override
    public TileOverlay addTileOverlay(TileOverlayOptions options) throws MapException {
        return getMap("addTileOverlay").addTileOverlay(options);
    }

    @Override
    public void clear() throws MapException {
        getMap("clear").clear();
    }

    @Override
    public void setMapType(int type) throws MapException {
        getMap("setMapType").setMapType(type);
    }

    @Override
    public void setMyLocationEnabled(boolean enabled) throws MapException {
        getMap("setMyLocationEnabled").setMyLocationEnabled(enabled);
    }

    @Override
    public void setMyLocationButtonEnabled(boolean enabled) throws MapException {
        getMap("setMyLocationButtonEnabled").getUiSettings().setMyLocationButtonEnabled(enabled);
    }

    @Override
    public void setZoomControlsEnabled(boolean enabled) throws MapException {
        getMap("setZoomControlsEnabled").getUiSettings().setZoomControlsEnabled(enabled);
    }

    @Override
    public VisibleRegion getVisibleRegion() throws MapException {
        try {
            return getMap("getProjection").getProjection().getVisibleRegion();
        } catch (IllegalArgumentException e){
            // Fatal Exception: java.lang.IllegalArgumentException left == right
            // com.google.android.gms.maps.Projection.getVisibleRegion
            //
            // https://www.crashlytics.com/electrounion/android/apps/ru.multigo.multitoplivo/issues/549fe72465f8dfea1574f579
            throw new MapException(e);
        }
    }

    @Override
    public final void setOnCameraChangeListener(@Nullable OnCameraChangeListener listener) throws MapException {
        getMap("setOnCameraChangeListener").setOnCameraChangeListener(listener);
    }

    @Override
    public final void setOnMapClickListener(@Nullable OnMapClickListener listener) throws MapException {
        getMap("setOnMapClickListener").setOnMapClickListener(listener);
    }

    @Override
    public final void setOnMapLongClickListener(@Nullable OnMapLongClickListener listener) throws MapException {
        getMap("setOnMapLongClickListener").setOnMapLongClickListener(listener);
    }

    @Override
    public final void setOnMarkerClickListener(final @Nullable OnMarkerClickListener listener) throws MapException {
        final com.google.android.gms.maps.GoogleMap.OnMarkerClickListener internalListener;
        if (listener != null) {
            internalListener = new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                    return listener.onMarkerClick(new GoogleMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMarkerClickListener").setOnMarkerClickListener(internalListener);
    }

    @Override
    public final void setOnMarkerDragListener(final @Nullable OnMarkerDragListener listener) throws MapException {
        final com.google.android.gms.maps.GoogleMap.OnMarkerDragListener internalListener;
        if (listener != null) {
            internalListener = new com.google.android.gms.maps.GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(com.google.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDragStart(new GoogleMarker(marker));
                }

                @Override
                public void onMarkerDrag(com.google.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDrag(new GoogleMarker(marker));
                }

                @Override
                public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDragEnd(new GoogleMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMarkerDragListener").setOnMarkerDragListener(internalListener);
    }

    @Override
    public final void setOnInfoWindowClickListener(final @Nullable OnInfoWindowClickListener listener) throws MapException {
        final com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener internalListener;
        if (listener != null) {
            internalListener = new com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
                    listener.onInfoWindowClick(new GoogleMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnInfoWindowClickListener").setOnInfoWindowClickListener(internalListener);
    }

    @Override
    public final void setInfoWindowAdapter(@Nullable final InfoWindowAdapter listener) throws MapException {
        final com.google.android.gms.maps.GoogleMap.InfoWindowAdapter internalListener;
        if (listener != null) {
            internalListener = new com.google.android.gms.maps.GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
                    return listener.getInfoWindow(new GoogleMarker(marker));
                }

                @Override
                public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                    return listener.getInfoContents(new GoogleMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setInfoWindowAdapter").setInfoWindowAdapter(internalListener);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) throws MapException {
        getMap("setPadding").setPadding(left, top, right, bottom);
    }
}