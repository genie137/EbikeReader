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

import nl.easthome.antpluslibary.AntPlusDeviceManager;
import nl.easthome.antpluslibary.Enums.AntAddType;
import nl.easthome.antpluslibary.Enums.AntPlusSensorDeviceSaveResult;
import nl.easthome.antpluslibary.Objects.AntPlusFoundSensor;
import nl.easthome.antpluslibary.R;


/**
 * This class is a ListView adapter for discovered and registered ant+ sensors.
 */
public class AntPlusDeviceListViewAdapter extends ArrayAdapter<AntPlusFoundSensor> {
    private Activity mActivity;

    /**
     * Constructor.
     *
     * @param activity            Activity in which the ListView is located.
     * @param antPlusFoundSensors ArrayList containing the found sensors.
     */
    public AntPlusDeviceListViewAdapter(@NonNull Activity activity, @NonNull ArrayList<AntPlusFoundSensor> antPlusFoundSensors) {
        super(activity, 0, antPlusFoundSensors);
        mActivity = activity;
    }

    /**
     * Returns the inflated list item view of the discovered ant+ device.
     *
     * @param position    Position in the listView.
     * @param convertView Previous view that needs to be recycled.
     * @param parent      The view in which the item views live.
     * @return The (new) item list view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final AntPlusFoundSensor device = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ant_device, parent, false);
        }

        final TextView deviceType = convertView.findViewById(R.id.device_name);
        final TextView deviceID = convertView.findViewById(R.id.device_id);
        final Button deviceConnectionToggle = convertView.findViewById(R.id.device_connection_toggle);
        final ImageView deviceStateImage = convertView.findViewById(R.id.device_image);

        if (device != null) {
            deviceType.setText(device.getSensorType().toString());
            deviceID.setText(device.getName());

            //Display different image and toggle label per device state.
            switch (device.getAntAddType()) {
                case NEW:
                    deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                    deviceConnectionToggle.setText(R.string.add_ant_device_button_pair);
                    break;
                case EXISTING_AND_FOUND:
                    deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                    deviceConnectionToggle.setText(R.string.add_ant_device_button_unpair);
                    break;
                case EXISTING_AND_MISSING:
                    deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_help_outline_blue_600_48dp));
                    deviceConnectionToggle.setText(R.string.add_ant_device_button_unpair);
                    break;
            }

            deviceConnectionToggle.setOnClickListener(new AntDeviceOnPairClickListener(device, deviceStateImage, deviceConnectionToggle));

        }

        return convertView;
    }

    protected class AntDeviceOnPairClickListener implements View.OnClickListener {

        private final ImageView deviceStateImage;
        private final Button deviceConnectionToggle;
        private AntPlusFoundSensor device;

        /**
         * This class handles the onClick action for the device pair/unpair button.
         *
         * @param device                 The ant+ device that the onClick acts on.
         * @param deviceStateImage       The ImageView reference of the ListView item for the ant+ device.
         * @param deviceConnectionToggle The Button reference of the ListView items for the ant+ device.
         */
        AntDeviceOnPairClickListener(AntPlusFoundSensor device, ImageView deviceStateImage, Button deviceConnectionToggle) {

            this.device = device;
            this.deviceStateImage = deviceStateImage;
            this.deviceConnectionToggle = deviceConnectionToggle;
        }

        @Override
        public void onClick(View v) {
            String text = deviceConnectionToggle.getText().toString();

            //If the text on the button equals the R.string.add_ant_device_button_pair value, perform pair action and update view.
            //Show user a Toast based on the performed action.
            if (text.equals(mActivity.getResources().getString(R.string.add_ant_device_button_pair))) {
                AntPlusSensorDeviceSaveResult result = new AntPlusDeviceManager(mActivity).saveSensorForType(device.getSensorType(), device.getDeviceNumber());
                switch (result) {
                    case NEW_SENSOR_SAVED:
                        Toast.makeText(getContext(), device.getSensorType() + mActivity.getString(R.string.add_ant_device_msg_added), Toast.LENGTH_LONG).show();
                        deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                        deviceConnectionToggle.setText(R.string.add_ant_device_button_unpair);
                        break;
                    case REPLACED_OLD_SENSOR:
                        Toast.makeText(getContext(), device.getSensorType() + mActivity.getString(R.string.add_ant_device_msg_replaced), Toast.LENGTH_LONG).show();
                        deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_sensor_connected));
                        deviceConnectionToggle.setText(R.string.add_ant_device_button_unpair);
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), R.string.add_ant_device_error, Toast.LENGTH_LONG).show();
                        break;
                }
            }
            //If the text on the button equals the R.string.add_ant_device_button_unpair value, perform unpair action and update view.
            //Show user a Toast based on the performed action.
            else {
                boolean result = new AntPlusDeviceManager(mActivity).removeSensorForType(device.getSensorType());

                if (result) {
                    if (device.getAntAddType().equals(AntAddType.EXISTING_AND_MISSING)) {
                        remove(device);
                    } else {
                        deviceStateImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_new_sensor));
                        deviceConnectionToggle.setText(R.string.add_ant_device_button_pair);
                    }
                    Toast.makeText(getContext(), device.getSensorType() + mActivity.getString(R.string.add_ant_sensor_remove_success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.add_ant_sensor_replace_error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
