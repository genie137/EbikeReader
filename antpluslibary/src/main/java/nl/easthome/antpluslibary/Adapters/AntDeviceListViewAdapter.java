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
import nl.easthome.antpluslibary.Objects.AntPlusSensor;
import nl.easthome.antpluslibary.R;


public class AntDeviceListViewAdapter extends ArrayAdapter<AntPlusSensor>{
    private Activity mActivity;
    final AntDeviceListViewAdapter antDeviceListViewAdapter = this;

    public AntDeviceListViewAdapter(@NonNull Activity context, @NonNull ArrayList<AntPlusSensor> objects) {
        super(context, 0, objects);
        mActivity = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final AntPlusSensor device = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ant_device, parent, false);
        }


        final TextView device_type = convertView.findViewById(R.id.device_type);
        TextView device_id = convertView.findViewById(R.id.device_id);
        final Button device_button = convertView.findViewById(R.id.device_connection_toggle);
        final ImageView device_state_image= convertView.findViewById(R.id.device_image);

        device_type.setText(device.getSensorType().toString());
        device_id.setText(device.getName());

        switch (device.getAntAddType()){
            case NEW:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                device_button.setText("Pair");
                break;
            case EXISTING_AND_FOUND:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                device_button.setText("Unpair");
                break;
            case EXISTING_AND_MISSING:
                device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_help_outline_blue_600_48dp));
                device_button.setText("Unpair");
                break;
        }

        device_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = device_button.getText().toString();

                if (text.equals("Pair")){
                    AntPlusDeviceConnector.AntPlusSensorDeviceSaveResult result = new AntPlusDeviceConnector(mActivity).saveSensorForType(device.getSensorType(), device.getDeviceNumber());
                    switch (result){
                        case NEW_SENSOR_SAVED:
                            Toast.makeText(getContext(), device.getSensorType() + " has been added!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            device_button.setText("Unpair");
                            break;
                        case REPLACED_OLD_SENSOR:
                            Toast.makeText(getContext(), device.getSensorType() + " has been replaced!", Toast.LENGTH_LONG).show();
                            device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                            device_button.setText("Unpair");
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), "Error?", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                else {
                    if (device.getAntAddType().equals(AntPlusSensor.AntAddType.EXISTING_AND_MISSING)) {
                        antDeviceListViewAdapter.remove(device);
                    }

                    boolean result = new AntPlusDeviceConnector(mActivity).removeSensorForType(device.getSensorType());
                    if (result) {
                        device_state_image.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                        device_button.setText("Pair");
                    }
                    else {
                        Toast.makeText(getContext(), "There was no sensor there?", Toast.LENGTH_LONG).show();
                    }




                }
            }
        });
        return convertView;
    }

}
