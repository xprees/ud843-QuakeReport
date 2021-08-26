package com.example.android.quakereport.data;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * @author Vaclav Bily
 */
public class Earthquake {
    private final float magnitude;
    private final String location;
    private final Date date;
    private final String url;

    public Earthquake(float magnitude, String location, Date date, @Nullable String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.date = date;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
