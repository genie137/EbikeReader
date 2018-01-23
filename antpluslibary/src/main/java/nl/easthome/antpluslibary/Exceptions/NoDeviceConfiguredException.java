package nl.easthome.antpluslibary.Exceptions;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;

/**
 * Exception that indicates that from a specific device type no sensor was configured.
 */
public class NoDeviceConfiguredException extends Exception {

    /**
     * Constructor
     *
     * @param deviceType The device type that is not configured.
     */
    public NoDeviceConfiguredException(DeviceType deviceType) {
        super("No device of the type " + deviceType.toString() + " was configured.");
    }
}
