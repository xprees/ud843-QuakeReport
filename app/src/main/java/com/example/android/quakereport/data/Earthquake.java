package com.example.android.quakereport.data;

import java.util.Date;

/**
 * @author Vaclav Bily
 */
public class Earthquake {
    private final float magnitude;
    private final String location;
    private final Date date;

    public Earthquake(float magnitude, String location, Date date) {
        this.magnitude = magnitude;
        this.location = location;
        this.date = date;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

}
