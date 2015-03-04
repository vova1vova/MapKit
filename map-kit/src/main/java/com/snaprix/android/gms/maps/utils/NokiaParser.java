package com.snaprix.android.gms.maps.utils;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by vladimirryabchikov on 9/15/14.
 */
public class NokiaParser {
    public static LatLng toGoogleLatLng(com.nokia.android.gms.maps.model.LatLng nokiaLatLng){
        return new LatLng(nokiaLatLng.latitude, nokiaLatLng.longitude);
    }

    public static com.nokia.android.gms.maps.model.LatLng toNokiaLatLng(LatLng latLng) {
        return new com.nokia.android.gms.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }

    public static LatLngBounds toGoogleLatLngBounds(com.nokia.android.gms.maps.model.LatLngBounds nokiaLatLngBounds){
        return new LatLngBounds(toGoogleLatLng(nokiaLatLngBounds.southwest), toGoogleLatLng(nokiaLatLngBounds.northeast));
    }

    public static CameraPosition toGoogleCameraPosition(com.nokia.android.gms.maps.model.CameraPosition nokiaCameraPosition){
        return new CameraPosition(toGoogleLatLng(nokiaCameraPosition.target),
                nokiaCameraPosition.zoom, nokiaCameraPosition.tilt, nokiaCameraPosition.bearing);
    }

    public static com.nokia.android.gms.maps.model.CameraPosition toNokiaCameraPosition(CameraPosition position){
        return com.nokia.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                new com.nokia.android.gms.maps.model.LatLng(position.target.latitude, position.target.longitude), position.zoom);
    }
}
