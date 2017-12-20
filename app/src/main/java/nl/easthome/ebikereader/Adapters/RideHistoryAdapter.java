package nl.easthome.ebikereader.Adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.easthome.ebikereader.Helpers.SystemTime;
import nl.easthome.ebikereader.Objects.RideRecording;
import nl.easthome.ebikereader.R;

public class RideHistoryAdapter extends ArrayAdapter<RideRecording> {


    public RideHistoryAdapter(@NonNull Context context, @NonNull ArrayList<RideRecording> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RideRecording rideRecording = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ride_history, parent, false);
        }

        TextView mRideDate = convertView.findViewById(R.id.ride_date);
        TextView mRideDetails = convertView.findViewById(R.id.ride_details);

        mRideDate.setText(SystemTime.convertTimestampToDateTime(rideRecording.getRideStart()));

        return convertView;
    }
}
