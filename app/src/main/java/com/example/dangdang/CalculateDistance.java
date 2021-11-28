package com.example.dangdang;

import android.location.Location;

public class CalculateDistance {
    public float totalDistance = 0.0f;
    public float preTotalDistance = 0.0f;
    public Location preLocation;
    public Location location;
    public float distance;


    public CalculateDistance() {
        this.preLocation = new Location("Pre Location");
        this.location = new Location("Current Location");
    }

    @Override
    public String toString() {
        return "CalculateDistance{" +
                "totalDistance=" + totalDistance +
                ", preTotalDistance=" + preTotalDistance +
                ", preLocation=" + preLocation +
                ", location=" + location +
                '}';
    }
}
