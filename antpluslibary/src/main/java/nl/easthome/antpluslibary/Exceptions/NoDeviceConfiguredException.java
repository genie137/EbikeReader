package nl.easthome.antpluslibary.Exceptions;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
/**
 * Created by jorisoosterhuis on 17/11/2017.
 */

public class NoDeviceConfiguredException extends Exception {

    public NoDeviceConfiguredException(DeviceType deviceType) {
        super("No device of the type " + deviceType.toString() + " was configured.");
    }
}
