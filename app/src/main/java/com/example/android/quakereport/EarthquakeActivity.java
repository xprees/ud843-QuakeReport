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

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.data.Earthquake;
import com.example.android.quakereport.data.EarthquakeRecyclerViewAdapter;
import com.example.android.quakereport.data.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    private RecyclerView earthquakeRecView;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakeRecView = (RecyclerView) findViewById(R.id.main_quake_recycle_view);
        earthquakeRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        new QuakeAsyncTask().execute(url);

    }

    private class QuakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... strings) {
            if (strings == null || strings.length <= 0) return null;
            List<Earthquake> quakes = new ArrayList<>();
            for (String urlText : strings) {
                String response = QueryUtils.getJsonResponse(urlText);
                quakes.addAll(QueryUtils.extractEarthquakes(response));
            }
            return quakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            if (earthquakes == null || earthquakes.isEmpty()) {
                return;
            }
            EarthquakeRecyclerViewAdapter adapter = new EarthquakeRecyclerViewAdapter(EarthquakeActivity.this, earthquakes);
            earthquakeRecView.setAdapter(adapter);
            super.onPostExecute(earthquakes);
        }
    }
}
