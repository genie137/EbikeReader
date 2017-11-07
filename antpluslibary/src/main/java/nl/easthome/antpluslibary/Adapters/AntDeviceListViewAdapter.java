package nl.easthome.antpluslibary.Adapters;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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


        TextView device_type = convertView.findViewById(R.id.device_type);
        TextView device_id = convertView.findViewById(R.id.device_id);
        final Switch device_switch = convertView.findViewById(R.id.device_connection_toggle);
        final ImageView device_state_image= convertView.findViewById(R.id.device_image);

        device_type.setText(device.mDevice.getAntDeviceType().toString());
        device_id.setText(device.mDevice.getDeviceDisplayName());
        device_switch.setChecked(device.mDevice.isAlreadyConnected());

        switch (new AntPlusDeviceConnector(getContext()).getDeviceState(device.mDevice)){
            case NEW:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                device_switch.setChecked(false);
                break;
            case CONNECTED:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                device_switch.setChecked(true);
                break;
            case MISSING:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_error));
                device_switch.setEnabled(false);
                break;
        }

        CompoundButton.OnCheckedChangeListener mOnCheckedChangeListner = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AntPlusDeviceConnector.AntPlusSensorDeviceSaveResult result = new AntPlusDeviceConnector(getContext()).saveSensorForType(device.mDevice.getAntDeviceType(), device.mDevice.getAntDeviceNumber());
                    System.out.println(result);
                    switch (result){
                        case NEW_SENSOR_SAVED:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " has been added!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            break;
                        case REPLACED_OLD_SENSOR:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " has been replaced!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            break;
                        case NOT_SUPPORTED_SENSOR:
                            Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " is not supported in this app.", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_error));
                            device_switch.setOnCheckedChangeListener(null);
                            buttonView.setChecked(false);
                            device_switch.setOnCheckedChangeListener(this);

                            break;
                        case ERROR:
                            Toast.makeText(getContext(), "Error?", Toast.LENGTH_LONG).show();
                            device_switch.setOnCheckedChangeListener(null);
                            buttonView.setChecked(false);
                            device_switch.setOnCheckedChangeListener(this);
                            break;
                    }
                }
                else {
                    boolean result = new AntPlusDeviceConnector(getContext()).removeSensorForType(device.mDevice.getAntDeviceType());
                    if (result) {
                        Toast.makeText(getContext(), device.mDevice.getAntDeviceType() + " has been removed!", Toast.LENGTH_LONG).show();
                        device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                    }
                    else {
                        Toast.makeText(getContext(), "No " + device.mDevice.getAntDeviceType() + " to be removed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        device_switch.setOnCheckedChangeListener(mOnCheckedChangeListner);
        return convertView;
    }
}
