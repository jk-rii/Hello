package com.example.dangdang;

import android.content.Context;
import android.location.Location;

public class DistanceCalManager {
    private Context mContext;
    private LocationManager mLocationManager;
    private CalculateDistance mCalculateDistance;

    private PTCalCulateListener mPTCalCulateListener;

    private PTLocationListener mPTLocationListener = new PTLocationListener() {
        @Override
        public void onPTLocationChanged(Location location) {
            if (location == null || location.getAccuracy() > 25 || mCalculateDistance == null) {
                L.e("location return");
                return;
            }
            mCalculateDistance.location = location;

            //해당 함수에서 거리 누적을 계산한다.
            if (!distanceTo()) {
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(mPTCalCulateListener != null){
                        mPTCalCulateListener.onCalculate(mCalculateDistance.totalDistance);
                    }
                }
            }).start();
        }
    };

    public DistanceCalManager(Context mContext) {
        this.mContext = mContext;
        this.mCalculateDistance = new CalculateDistance();
    }

    public void setPTCalCulateListener(PTCalCulateListener listener) {
        this.mPTCalCulateListener = listener;
    }

    public void startDistanceCal(){
        //Gps 센서를 활영하여 위치정보를 받도록한다.
        startLocation();
    }

    public void stopDistanceCal(){
        //위치정보를 받지않으며, 객체를 초기화 시킨다.
        stopLocation();
    }


    public void stopLocation() {
        L.d("stopLocation");
        if (mLocationManager != null) {
            mLocationManager.registerPTLocationListener(null);
            mLocationManager.removeLocation();
        }
        mLocationManager = null;
        mCalculateDistance = null;
    }


    public void startLocation() {
        //위치정보를 받을수있는 LocationManager 활용한다. LocationManager 객체를 생성후 Gps센서를 활성화시킨다.
        L.d("startLocation " + mLocationManager);
        if (mLocationManager == null) {
            mLocationManager = new LocationManager(mContext);
            mLocationManager.registerPTLocationListener(mPTLocationListener);
        }
        mLocationManager.startLocationUpdate();
    }

    public synchronized boolean distanceTo() {
        float distance = mCalculateDistance.preLocation.distanceTo(mCalculateDistance.location);//거리계산

        L.e("[current time : " + mCalculateDistance.location.getTime() + " / " + "pre time : " + mCalculateDistance.preLocation.getTime() + "]");
        long second = 0;

        if (mCalculateDistance.preLocation.getTime() != 0) {
            second = (long) ((mCalculateDistance.location.getTime() - mCalculateDistance.preLocation.getTime()) / 1000.0f);
        }
        mCalculateDistance.preLocation = mCalculateDistance.location;

        L.e("second : " + second + ", distance : " + distance);
        if (second > 5 || (second <= 5 && (distance >= 50.0f || distance <= 1.0f))) {
            return false;
        }
        mCalculateDistance.totalDistance += distance;

        //누적 이동거리를 구해준다.
        mCalculateDistance.distance += distance;
        return true;
    }
}
