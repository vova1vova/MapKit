package com.snaprix.android.gms.maps.map;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.nokia.android.gms.maps.CameraUpdateFactory;
import com.nokia.android.gms.maps.model.BitmapDescriptorFactory;
import com.snaprix.android.gms.maps.model.Circle;
import com.snaprix.android.gms.maps.model.Marker;
import com.snaprix.android.gms.maps.model.NokiaCircle;
import com.snaprix.android.gms.maps.model.NokiaMarker;
import com.snaprix.android.gms.maps.utils.NokiaParser;

/**
 * Created by vladimirryabchikov on 9/4/14.
 */
public abstract class NokiaGoogleMap implements GoogleMap {
    protected abstract com.nokia.android.gms.maps.GoogleMap getMap(String method) throws MapException;

    @Override
    public CameraPosition getCameraPosition() throws MapException {
        com.nokia.android.gms.maps.model.CameraPosition nokiaPosition = getMap("getCameraPosition").getCameraPosition();
        return new CameraPosition(NokiaParser.toGoogleLatLng(nokiaPosition.target),
                nokiaPosition.zoom, nokiaPosition.tilt, nokiaPosition.bearing);
    }

    @Override
    public void moveCamera(CameraPosition position) throws MapException {
        getMap("moveCamera").moveCamera(CameraUpdateFactory.newCameraPosition(NokiaParser.toNokiaCameraPosition(position)));
    }

    @Override
    public void animateCamera(CameraPosition position) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(NokiaParser.toNokiaCameraPosition(position)));
    }

    @Override
    public void animateCamera(CameraPosition position, final com.google.android.gms.maps.GoogleMap.CancelableCallback callback) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(NokiaParser.toNokiaCameraPosition(position)), new com.nokia.android.gms.maps.GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
                callback.onCancel();
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
    }

    @Override
    public void animateCamera(CameraPosition position, int durationMs, final com.google.android.gms.maps.GoogleMap.CancelableCallback callback) throws MapException {
        getMap("animateCamera").animateCamera(CameraUpdateFactory.newCameraPosition(NokiaParser.toNokiaCameraPosition(position)), durationMs, new com.nokia.android.gms.maps.GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
                callback.onCancel();
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });
    }

    @Override
    public Circle addCircle(CircleOptions options) throws MapException {
        // TODO
        return new NokiaCircle();
    }

    @Override
    public Marker addMarker(MarkerOptions options, @Nullable Bitmap bitmap) throws MapException {
        com.nokia.android.gms.maps.model.MarkerOptions nokiaOptions = new com.nokia.android.gms.maps.model.MarkerOptions()
                .position(NokiaParser.toNokiaLatLng(options.getPosition()))
                .anchor(options.getAnchorU(), options.getAnchorV())
                .infoWindowAnchor(options.getInfoWindowAnchorU(), options.getInfoWindowAnchorV())
                .title(options.getTitle())
                .snippet(options.getSnippet())
                .draggable(options.isDraggable())
                .visible(options.isVisible())
                .flat(options.isFlat())
                .rotation(options.getRotation())
                .alpha(options.getAlpha());

        if (bitmap != null) {
            nokiaOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

        return new NokiaMarker(getMap("addMarker").addMarker(nokiaOptions));
    }

    @Override
    public TileOverlay addTileOverlay(TileOverlayOptions options) throws MapException {
        // TODO this operation is not supported
        return null;
    }

    @Override
    public void clear() throws MapException {
        getMap("clear").clear();
    }

    @Override
    public void setMapType(int type) throws MapException {
        // TODO this operation is not supported
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
        com.nokia.android.gms.maps.model.VisibleRegion nokiaVisibleRegion = getMap("getProjection").getProjection().getVisibleRegion();
        if (nokiaVisibleRegion == null) throw new MapException("nokiaVisibleRegion == null");

        LatLng nearLeft = NokiaParser.toGoogleLatLng(nokiaVisibleRegion.nearLeft);
        LatLng nearRight = NokiaParser.toGoogleLatLng(nokiaVisibleRegion.nearRight);
        LatLng farLeft = NokiaParser.toGoogleLatLng(nokiaVisibleRegion.farLeft);
        LatLng farRight = NokiaParser.toGoogleLatLng(nokiaVisibleRegion.farRight);
        LatLngBounds latLngBounds = NokiaParser.toGoogleLatLngBounds(nokiaVisibleRegion.latLngBounds);

        return new VisibleRegion(nearLeft, nearRight, farLeft, farRight, latLngBounds);
    }

    @Override
    public final void setOnCameraChangeListener(final @Nullable OnCameraChangeListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnCameraChangeListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(com.nokia.android.gms.maps.model.CameraPosition cameraPosition) {
                    listener.onCameraChange(NokiaParser.toGoogleCameraPosition(cameraPosition));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnCameraChangeListener").setOnCameraChangeListener(internalListener);
    }

    @Override
    public final void setOnMapClickListener(final @Nullable OnMapClickListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnMapClickListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(com.nokia.android.gms.maps.model.LatLng latLng) {
                    listener.onMapClick(NokiaParser.toGoogleLatLng(latLng));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMapClickListener").setOnMapClickListener(internalListener);
    }

    @Override
    public final void setOnMapLongClickListener(final @Nullable OnMapLongClickListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnMapLongClickListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(com.nokia.android.gms.maps.model.LatLng latLng) {
                    listener.onMapLongClick(NokiaParser.toGoogleLatLng(latLng));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMapLongClickListener").setOnMapLongClickListener(internalListener);
    }

    @Override
    public final void setOnMarkerClickListener(final @Nullable OnMarkerClickListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnMarkerClickListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(com.nokia.android.gms.maps.model.Marker marker) {
                    return listener.onMarkerClick(new NokiaMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMarkerClickListener").setOnMarkerClickListener(internalListener);
    }

    @Override
    public final void setOnMarkerDragListener(final @Nullable OnMarkerDragListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnMarkerDragListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(com.nokia.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDragStart(new NokiaMarker(marker));
                }

                @Override
                public void onMarkerDrag(com.nokia.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDrag(new NokiaMarker(marker));
                }

                @Override
                public void onMarkerDragEnd(com.nokia.android.gms.maps.model.Marker marker) {
                    listener.onMarkerDragEnd(new NokiaMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnMarkerDragListener").setOnMarkerDragListener(internalListener);
    }

    @Override
    public final void setOnInfoWindowClickListener(final @Nullable OnInfoWindowClickListener listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.OnInfoWindowClickListener internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(com.nokia.android.gms.maps.model.Marker marker) {
                    listener.onInfoWindowClick(new NokiaMarker(marker));
                }
            };
        } else {
            internalListener = null;
        }
        getMap("setOnInfoWindowClickListener").setOnInfoWindowClickListener(internalListener);
    }

    @Override
    public final void setInfoWindowAdapter(@Nullable final InfoWindowAdapter listener) throws MapException {
        final com.nokia.android.gms.maps.GoogleMap.InfoWindowAdapter internalListener;
        if (listener != null) {
            internalListener = new com.nokia.android.gms.maps.GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(com.nokia.android.gms.maps.model.Marker marker) {
                    return listener.getInfoWindow(new NokiaMarker(marker));
                }

                @Override
                public View getInfoContents(com.nokia.android.gms.maps.model.Marker marker) {
                    return listener.getInfoContents(new NokiaMarker(marker));
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