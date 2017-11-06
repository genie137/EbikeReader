package nl.easthome.antpluslibary.Adapters;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.easthome.antpluslibary.Objects.MultiDeviceSearchResultWithRSSI;
import nl.easthome.antpluslibary.R;


public class AntDeviceListViewAdapter extends ArrayAdapter<MultiDeviceSearchResultWithRSSI>{


    public AntDeviceListViewAdapter(@NonNull Activity context, @NonNull ArrayList<MultiDeviceSearchResultWithRSSI> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MultiDeviceSearchResultWithRSSI device = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ant_device, parent, false);
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(device.mDevice.getAntDeviceType().toString());


        return convertView;
    }
}
