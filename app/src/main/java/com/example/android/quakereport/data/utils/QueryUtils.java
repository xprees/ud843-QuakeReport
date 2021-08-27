package com.example.android.quakereport.data.utils;
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

import android.util.Log;

import com.example.android.quakereport.data.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    private static final String TAG = "QueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String jsonResponse) {
        if (jsonResponse == null) {
            return Collections.emptyList();
        }
        List<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                float magnitude = Float.parseFloat(properties.getString("mag"));
                String location = properties.getString("place");
                long time = Long.parseLong(properties.getString("time"));
                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, new Date(time), url);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    /**
     * Creates URL from a text
     *
     * @param urlText input url text
     * @return URL on success, otherwise null
     */
    private static URL createUrl(String urlText) {
        if (urlText == null || urlText.isEmpty()) {
            return null;
        }
        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "createUrl: Incorrect url " + urlText, e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        String response = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                response = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getJsonResponse: ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder textBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream, String.valueOf(StandardCharsets.UTF_8));
        while (scanner.hasNextLine()) {
            textBuilder.append(scanner.nextLine());
        }
        return textBuilder.toString().trim();
    }

    public static String getJsonResponse(String urlText) {
        URL url = createUrl(urlText);
        if (url == null) {
            return null;
        }
        return makeHttpRequest(url);
    }
}
