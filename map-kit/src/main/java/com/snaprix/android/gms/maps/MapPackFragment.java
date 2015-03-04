package com.snaprix.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.here.Util;
import com.snaprix.android.gms.maps.internal.MapFragmentInteractionListener;
import com.snaprix.android.gms.maps.internal.SnaprixMapFragment;
import com.snaprix.android.gms.maps.internal.SupportNokiaMapFragment;
import com.snaprix.android.gms.maps.internal.SupportPlayMapFragment;
import com.snaprix.android.gms.maps.map.GoogleMap;
import com.snaprix.android.gms.maps.utils.MapKitLogger;
import com.snaprix.map.R;

/**
 * Created by vladimirryabchikov on 9/5/14.
 */
public class MapPackFragment extends Fragment
        implements MapFragmentInteractionListener {

    protected boolean SHOW_LOGS = MapKitLogger.isShowLogs();
    protected String TAG;

    private SnaprixMapFragment mMapFragment;
    private MapFragmentInteractionListener mCallback;

    public static MapPackFragment newInstance(){
        return new MapPackFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        TAG = String.format("%s (%s)", getClass().getSimpleName(), activity.getClass().getSimpleName());
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_pack, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fragment fragment = getChildFragmentManager()
                .findFragmentById(R.id.map_pack_layout);
        if (fragment == null) {
            if (Util.isHereMapsAvailable()){
                fragment = new SupportNokiaMapFragment();
            } else {
                fragment = new SupportPlayMapFragment();
            }

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.map_pack_layout, fragment)
                    .commit();

            // fragment should be added synchronously to prevent follow up NPEs
            boolean hasTransactions = getChildFragmentManager().executePendingTransactions();
            if (SHOW_LOGS) Log.v(TAG, String.format("hasTransactions=%b", hasTransactions));
        }
        mMapFragment = (SnaprixMapFragment) fragment;
    }

    @Override
    public final void onStart() {
        super.onStart();
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onStop() {
        super.onStop();
    }

    @Override
    public void onMapCreate(boolean hasSavedInstanceState) {
        if (SHOW_LOGS) Log.v(TAG, "onMapCreate");
        if (mCallback != null) mCallback.onMapCreate(hasSavedInstanceState);
    }

    @Override
    public void onMapStart() {
        if (SHOW_LOGS) Log.v(TAG, "onMapStart");
        if (mCallback != null) mCallback.onMapStart();
    }

    @Override
    public void onMapResume() {
        if (SHOW_LOGS) Log.v(TAG, "onMapResume");
        if (mCallback != null) mCallback.onMapResume();
    }

    @Override
    public void onMapPause() {
        if (SHOW_LOGS) Log.v(TAG, "onMapPause");
        if (mCallback != null) mCallback.onMapPause();
    }

    @Override
    public void onMapStop() {
        if (SHOW_LOGS) Log.v(TAG, "onMapStop");
        if (mCallback != null) mCallback.onMapStop();
    }

    @Override
    public void onMapDestroy() {
        if (SHOW_LOGS) Log.v(TAG, "onMapDestroy");
        if (mCallback != null) mCallback.onMapDestroy();
    }

    @Override
    public void onTouchActionDown() {
        if (SHOW_LOGS) Log.v(TAG, "onTouchActionDown");
        if (mCallback != null) mCallback.onTouchActionDown();
    }

    @Override
    public void onTouchActionUp() {
        if (SHOW_LOGS) Log.v(TAG, "onTouchActionUp");
        if (mCallback != null) mCallback.onTouchActionUp();
    }

    public void setCallback(MapFragmentInteractionListener callback) {
        mCallback = callback;
    }

    public GoogleMap getGoogleMap() {
        return mMapFragment.getGoogleMap();
    }
}