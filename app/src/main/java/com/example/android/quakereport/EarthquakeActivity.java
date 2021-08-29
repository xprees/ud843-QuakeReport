/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.quakereport.data.Earthquake;
import com.example.android.quakereport.data.EarthquakeRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int QUAKE_LOADER_ID = 1;

    private RecyclerView earthquakeRecView;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private TextView emptyView;
    private boolean isConnected;

    private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        refreshLayout = findViewById(R.id.refresh_layout);
        progressBar = findViewById(R.id.progress_bar_circular);
        earthquakeRecView = (RecyclerView) findViewById(R.id.main_quake_recycle_view);
        emptyView = findViewById(R.id.empty_view);

        int orientation = getResources().getConfiguration().orientation;
        int cols = orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;
        earthquakeRecView.setLayoutManager(new GridLayoutManager(this, cols));
        EarthquakeRecyclerViewAdapter adapter = new EarthquakeRecyclerViewAdapter(EarthquakeActivity.this, Collections.emptyList());
        earthquakeRecView.setAdapter(adapter);

        LoaderManager manager = LoaderManager.getInstance(this);
        refreshLayout.setOnRefreshListener(() -> manager.initLoader(QUAKE_LOADER_ID, null, this));

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            // No Internet
            emptyView.setText(R.string.no_connection);
            emptyView.setVisibility(View.VISIBLE);
            earthquakeRecView.setVisibility(View.GONE);
            isConnected = false;
        } else {
            // Internet available
            isConnected = true;
            manager.initLoader(QUAKE_LOADER_ID, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Collection<String> urls = new ArrayList<>();
        urls.add(URL);
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
        //progressBar.post(() -> progressBar.setVisibility(View.VISIBLE));
        return new EarthquakeLoader(this, urls);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        EarthquakeRecyclerViewAdapter adapter = (EarthquakeRecyclerViewAdapter) earthquakeRecView.getAdapter();
        if (adapter == null) return;
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            // quakes loaded
            adapter.addAll(data);
            if (emptyView.getVisibility() != View.GONE) {
                emptyView.setVisibility(View.GONE);
            }
            if (earthquakeRecView.getVisibility() != View.VISIBLE) {
                earthquakeRecView.setVisibility(View.VISIBLE);
            }
        } else {
            // No quakes
            if (isConnected) {
                emptyView.setText(R.string.no_quakes);
            }
            emptyView.setVisibility(View.VISIBLE);
            earthquakeRecView.setVisibility(View.GONE);
        }
        //progressBar.post(() -> progressBar.setVisibility(View.GONE));
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        EarthquakeRecyclerViewAdapter adapter = (EarthquakeRecyclerViewAdapter) earthquakeRecView.getAdapter();
        if (adapter != null) {
            adapter.clear();
        }
    }
}
