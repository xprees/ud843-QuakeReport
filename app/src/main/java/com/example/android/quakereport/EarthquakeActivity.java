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

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.data.Earthquake;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        RecyclerView earthquakeRecView = (RecyclerView) findViewById(R.id.main_quake_recycle_view);
        earthquakeRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        EarthquakeRecyclerViewAdapter adapter = new EarthquakeRecyclerViewAdapter(this, createSampleData());
        earthquakeRecView.setAdapter(adapter);
    }

    @NonNull
    private List<Earthquake> createSampleData() {
        List<Earthquake> quakes = new ArrayList<>();
        quakes.add(new Earthquake(4.0F, "New York, US", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(1.0F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(4.550F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(1.0F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(8.40F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(1.0F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(8.40F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(4.550F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(8.40F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(8.40F, "New York", new Date(2021, 8, 25, 8, 20)));
        quakes.add(new Earthquake(4.550F, "New York", new Date(2021, 8, 25, 8, 20)));
        return quakes;
    }
}
