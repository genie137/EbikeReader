package nl.easthome.antpluslibary.Adapters;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.easthome.antpluslibary.AntPlusDeviceConnector;
import nl.easthome.antpluslibary.Objects.MultiDeviceSearchResultWithRSSI;
import nl.easthome.antpluslibary.R;


public class AntDeviceListViewAdapter extends ArrayAdapter<MultiDeviceSearchResultWithRSSI>{

    public AntDeviceListViewAdapter(@NonNull Activity context, @NonNull ArrayList<MultiDeviceSearchResultWithRSSI> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MultiDeviceSearchResultWithRSSI device = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ant_device, parent, false);
        }


        final TextView device_type = convertView.findViewById(R.id.device_type);
        TextView device_id = convertView.findViewById(R.id.device_id);
        final Button device_button = convertView.findViewById(R.id.device_connection_toggle);
        final ImageView device_state_image= convertView.findViewById(R.id.device_image);

        device_type.setText(device.mDevice.getAntDeviceType().toString());
        device_id.setText(device.mDevice.getDeviceDisplayName());

        switch (new AntPlusDeviceConnector(getContext()).getDeviceState(device.mDevice)){
            case NEW:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                device_button.setText("Pair");
                break;
            case CONNECTED:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                device_button.setText("Unpair");
                break;
            case MISSING:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_help_outline_blue_600_48dp));
                device_button.setEnabled(false);
                break;
        }

        device_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String text = button.getText().toString();

                if (text.equals("Pair")){
                    AntPlusDeviceConnector.AntPlusSensorDeviceSaveResult result = new AntPlusDeviceConnector(getContext()).saveSensorForType(device.mDevice.getAntDeviceType(), device.mDevice.getAntDeviceNumber());
                    switch (result){
                        case NEW_SENSOR_SAVED:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " has been added!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            device_button.setText("Unpair");
                            break;
                        case REPLACED_OLD_SENSOR:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " has been replaced!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            device_button.setText("Unpair");
                            break;
                        case NOT_SUPPORTED_SENSOR:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " is not supported in this app.", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_error));
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), "Error?", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                else {
                    boolean result = new AntPlusDeviceConnector(getContext()).removeSensorForType(device.mDevice.getAntDeviceType());
                    if (result) {
                        device_button.setText("Pair");
                    }
                    else {

                    }

                }


            }
        });
        return convertView;
    }

}
