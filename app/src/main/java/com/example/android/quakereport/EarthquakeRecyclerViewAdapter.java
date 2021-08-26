package com.example.android.quakereport;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quakereport.data.Earthquake;

import java.util.ArrayList;
import java.util.Calendar;
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
        holder.location.setText(earthquake.getLocation());
        holder.distance.setText("10km NW of");

        Date date = earthquake.getDate();
        holder.date.setText(Calendar.getInstance().getTime().toString());
        holder.time.setText(Calendar.getInstance().getTime().toString());
    }

    private int getMagnitudeBackgroundColor(float magnitude) {
        if (magnitude < 2) return context.getColor(R.color.low_danger);
        if (magnitude < 5) return context.getColor(R.color.mid_danger);
        return context.getColor(R.color.high_danger);
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
