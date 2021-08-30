package com.example.android.quakereport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.android.quakereport.data.Earthquake;
import com.example.android.quakereport.data.utils.QueryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Vaclav Bily
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String TAG = "QuakeLoader";
    private List<String> urls;

    public EarthquakeLoader(@NonNull Context context, Collection<String> urls) {
        super(context);
        this.urls = new ArrayList<>(urls);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (urls == null || urls.size() <= 0) return null;
        final List<Earthquake> quakes = new ArrayList<>();
        for (String urlText : urls) {
            String response = QueryUtils.getJsonResponse(urlText);
            quakes.addAll(QueryUtils.extractEarthquakes(response));
        }
        return Collections.unmodifiableList(quakes);
    }
    public void updateUrls(Collection<String> urls){
        this.urls = new ArrayList<>(urls);
        forceLoad();
    }
}
