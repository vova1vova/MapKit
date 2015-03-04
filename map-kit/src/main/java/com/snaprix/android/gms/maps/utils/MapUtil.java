package com.snaprix.android.gms.maps.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by vladimirryabchikov on 7/8/14.
 */
public class MapUtil {

    // +interpolateToBottom(from: LatLng, nearLeft: LatLng, nearRight: LatLng, center: LatLng, fraction: double): LatLng
    /**
     * calc {@code LatLng} corresponding to the given {@code from},
     * located on vertical line down from {@code center} in {@code fraction} of this line,
     * counting from {@code center}
     *
     * @param from
     * @param nearLeft
     * @param nearRight
     * @param center
     * @param fraction
     * @return
     */
    public static LatLng interpolateToBottom(LatLng from,
                                             LatLng nearLeft, LatLng nearRight, LatLng center,
                                             double fraction){
        LatLng bottomLatLng = SphericalUtil.interpolate(nearLeft, nearRight, 0.5);

        double distance = SphericalUtil.computeDistanceBetween(bottomLatLng, center) * fraction;
        double heading = SphericalUtil.computeHeading(bottomLatLng, center);

        return SphericalUtil.computeOffset(from, distance, heading);
    }
}