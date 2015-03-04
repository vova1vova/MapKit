package com.snaprix.android.gms.maps.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.snaprix.android.gms.maps.map.PlayGoogleMap;
import com.snaprix.android.gms.maps.utils.MapKitLogger;

/**
 * Created by vladimirryabchikov on 7/16/14.
 */
public class SupportPlayMapFragment extends SupportMapFragment implements SnaprixMapFragment {
    private final boolean DEBUG = MapKitLogger.isShowLogs();
    private static final String TAG = "PlayMapFragment";

    /**
     * since JELLY_BEAN map view implemented using TextureView
     * it fixes several bugs with black background behind the map
     * and allows map to be moved, transformed and animated in more smooth way
     */
    private static final int BUILD_MAP_AS_TEXTURE_VIEW = Build.VERSION_CODES.JELLY_BEAN;

    private PlayGoogleMap mMap;
    private View mOriginalView;

    private MapFragmentInteractionListener mListener;

    public SupportPlayMapFragment(){
        mMap = new PlayGoogleMap(){

            @Override
            protected GoogleMap getMap(String method) throws MapException{
                GoogleMap map = SupportPlayMapFragment.this.getMap();
                if (map == null){
                    throw new MapException(method + " map == null");
                }

                return map;
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (DEBUG) Log.v(TAG, "onAttach");

        mListener = (MapFragmentInteractionListener) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG) Log.v(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mOriginalView = super.onCreateView(inflater, parent, savedInstanceState);
        if (DEBUG) Log.v(TAG, "onCreateView");

        if (Build.VERSION.SDK_INT < BUILD_MAP_AS_TEXTURE_VIEW){
            setMapTransparent((ViewGroup) mOriginalView);
        }

        ViewGroup view = new TouchableWrapper(getActivity());
        view.addView(mOriginalView);
        return view;
    }

    /**
     * this hack is used to remove black background
     * when map is sliding,
     * on devices before BUILD_MAP_AS_TEXTURE_VIEW (JellyBean, API 16)
     *
     * @param group
     */
    private void setMapTransparent(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                setMapTransparent((ViewGroup) child);
            } else if (child instanceof SurfaceView) {
                child.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public View getView() {
        return mOriginalView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (DEBUG) Log.v(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) Log.v(TAG, "onActivityCreated");

        mListener.onMapCreate(savedInstanceState != null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) Log.v(TAG, "onStart");
        mListener.onMapStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG) Log.v(TAG, "onResume");
        mListener.onMapResume();
    }

    @Override
    public void onPause() {
        mListener.onMapPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mListener.onMapStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mListener.onMapDestroy();
        super.onDestroy();
    }

    public com.snaprix.android.gms.maps.map.GoogleMap getGoogleMap(){
        return mMap;
    }



    private class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mListener.onTouchActionDown();
                    break;

                case MotionEvent.ACTION_UP:
                    mListener.onTouchActionUp();
                    break;
            }

            return super.dispatchTouchEvent(ev);
        }
    }
}
