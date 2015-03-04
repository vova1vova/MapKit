package com.snaprix.android.gms.maps.internal;

/**
 * Created by vladimirryabchikov on 9/5/14.
 */
public interface MapFragmentInteractionListener {
    void onMapCreate(boolean hasSavedInstanceState);
    void onMapStart();
    void onMapResume();
    void onMapPause();
    void onMapStop();
    void onMapDestroy();

    void onTouchActionDown();
    void onTouchActionUp();
}
