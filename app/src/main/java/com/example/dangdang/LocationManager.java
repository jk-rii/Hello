package com.example.dangdang;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;


public class LocationManager implements android.location.LocationListener {
    //위치정보를 획득할수 있는 클래스

    private PTLocationListener mPtLocationListener;
    private Context mContext;
    private android.location.LocationManager mLocationManger;

    public LocationManager(Context context) {
        this.mContext = context;
        mLocationManger = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }


    public void registerPTLocationListener(PTLocationListener listener) {
        this.mPtLocationListener = listener;
    }


    public void startLocationUpdate() {
        //권한체크를 한후 위치정보획득을 시작한다.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationManger != null) {
            //500 mils 단위로 위치정보를 획득한다..
            mLocationManger.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 500, 0.0f, this);
        }
        L.d("Location update started ..............: ");
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }

        if (mPtLocationListener != null) mPtLocationListener.onPTLocationChanged(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void removeLocation() {
        //위치정보 획득을 취소 하도록 한다.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManger.removeUpdates(this);
    }

}
