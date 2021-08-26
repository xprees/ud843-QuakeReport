package com.example.android.quakereport.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Vaclav Bily
 */
public class EarthquakeRecyclerViewAdapter extends RecyclerView.Adapter<EarthquakeRecyclerViewAdapter.ViewHolder> {
    private final List<Earthquake> earthquakes;
    private final Context context;

    public EarthquakeRecyclerViewAdapter(Context context, Collection<Earthquake> earthquakeCollection) {
        this.context = context;
        this.earthquakes = new ArrayList<>(earthquakeCollection);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Earthquake earthquake = earthquakes.get(position);

        float magnitude = earthquake.getMagnitude();
        holder.magnitude.setText(String.format(Locale.getDefault(), "%.1f", magnitude));
        holder.magnitude.getBackground().setColorFilter(getMagnitudeBackgroundColor(magnitude), PorterDuff.Mode.SRC);
        String[] loc = earthquake.getLocation().split("of");
        String distance = "";
        String location = earthquake.getLocation();
        if (loc.length >= 2) {
            distance = loc[0] + "of";
            location = loc[1];
        }
        holder.location.setText(location.trim());
        holder.distance.setText(distance.trim());

        Date date = earthquake.getDate();
        holder.date.setText(new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(date));
        holder.time.setText(new SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(date));

        holder.magnitude.getRootView().setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
            context.startActivity(intent);
        });
    }

    /**
     * Returns proper color regarding value of magnitude
     *
     * @param magnitude of earthquake
     * @return color int
     */
    private int getMagnitudeBackgroundColor(float magnitude) {
        if (magnitude <= 2) return context.getColor(R.color.magnitude1);
        if (magnitude <= 3) return context.getColor(R.color.magnitude2);
        if (magnitude <= 4) return context.getColor(R.color.magnitude3);
        if (magnitude <= 5) return context.getColor(R.color.magnitude4);
        if (magnitude <= 6) return context.getColor(R.color.magnitude5);
        if (magnitude <= 7) return context.getColor(R.color.magnitude6);
        if (magnitude <= 8) return context.getColor(R.color.magnitude7);
        if (magnitude <= 9) return context.getColor(R.color.magnitude8);
        if (magnitude <= 10) return context.getColor(R.color.magnitude9);
        return context.getColor(R.color.magnitude10plus);
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView location;
        private final TextView magnitude;
        private final TextView distance;
        private final TextView date;
        private final TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            magnitude = itemView.findViewById(R.id.earthquake_item_magnitude_text);
            location = itemView.findViewById(R.id.earthquake_item_location_text);
            distance = itemView.findViewById(R.id.earthquake_item_distance_text);
            date = itemView.findViewById(R.id.earthquake_item_date_text);
            time = itemView.findViewById(R.id.earthquake_item_time_text);
        }
    }
}
