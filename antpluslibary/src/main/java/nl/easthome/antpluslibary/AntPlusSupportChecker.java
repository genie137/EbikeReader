package nl.easthome.antpluslibary;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nl.easthome.antpluslibary.Enums.AntSupportState;

/**
 * Based upon android_antlib_4-12-0.jar AntPlusSupportChecker class.
 * Antlib is the old ant library, The support checking was the only thing i needed from the library so i remade it.
 * https://www.thisisant.com/developer/ant/ant-in-android
 */

public class AntPlusSupportChecker {

    private static final String ANT_LIBRARY_NAME = "com.dsi.ant.antradio_library";
    private static final String ANT_RADIO_PACKAGE_NAME = "com.dsi.ant.service.socket";
    private static final String ANT_PLUGIN_PACKAGE_NAME = "com.dsi.ant.plugins.antplus";
    private static final String INTENT_ACTION_QUERY_SERVICE_INFO = "com.dsi.ant.intent.request.SERVICE_INFO";
    private static final String META_DATA_ANT_CHIP_SERVICE_INTERFACE_TYPE_TAG = "ANT_AdapterType";
    private static final String META_DATA_ANT_HARDWARE_TYPES_TAG = "ANT_HardwareType";
    private static final String META_DATA_ANT_CHIP_SERVICE_INTERFACE_TYPE_REMOTE = "remote";
    private static final String META_DATA_ANT_HARDWARE_TYPE_BUILTIN = "built-in";

    /**
     * Checks if the device has ant capabilities
     *
     * @param context The acitivity the method was called from.
     * @return Enum that reports the state of the ant support.
     */
    public static AntSupportState getAntSupportedState(Context context){
        if (hasAntChip(context)){
            return hasAntPackages(context);
        } else {
            return AntSupportState.ANT_NO_CHIP_OR_USB;
        }
    }

    /**
     * Checks if the ant packages are installed on the device.
     *
     * @param context The activity the method was called from.
     * @return Enum that reports the state of the ant support.
     */
    @SuppressWarnings("ConstantConditions")
    private static AntSupportState hasAntPackages(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean hasRadio = false;
        boolean hasPlugin = false;


        try {
            packageManager.getPackageInfo(ANT_RADIO_PACKAGE_NAME, 0);
            hasRadio = true;
        } catch (PackageManager.NameNotFoundException ignored){}

        try {
            packageManager.getPackageInfo(ANT_PLUGIN_PACKAGE_NAME, 0);
            hasPlugin = true;
        } catch (PackageManager.NameNotFoundException ignored){}

        if (hasRadio && hasPlugin){
            return AntSupportState.ANT_SUPPORTED;
        }
        else if (hasPlugin && !hasRadio){
            return AntSupportState.ANT_SUPPORTED_BUT_NO_RADIO;
        }
        else if (!hasPlugin && hasRadio){
            return AntSupportState.ANT_SUPPORTED_BUT_NO_PLUGIN;
        }
        else {
            return AntSupportState.ANT_SUPPORTED_BUT_NO_RADIO_AND_PLUGIN;
        }

    }

    /**
     * Checks if the device has an Ant+ chip or ANT+ USB device attached.
     * @param context The Activity the method was called from.
     * @return True if there is an ant chip in the device.
     */
    @SuppressWarnings("WhileLoopReplaceableByForEach")
    private static boolean hasAntChip(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean antSupported = Arrays.asList(packageManager.getSystemSharedLibraryNames()).contains(ANT_LIBRARY_NAME);
        if(!antSupported) {
            List<ResolveInfo> resolveInfoList = queryForAntServices(context);
            Iterator i$ = resolveInfoList.iterator();

            while(i$.hasNext()) {
                ResolveInfo resolveInfo = (ResolveInfo)i$.next();

                try {
                    if(isBuiltIn(resolveInfo.serviceInfo)) {
                        return true;
                    }
                } catch (IllegalArgumentException ignored) {}
            }
        }

        return antSupported;
    }

    /**
     * Checks if the device is an emulator.
     * Can result into false-positives with other devices!
     * @return True if the device is an emulator.
     */
    public static boolean isDeviceEmulator(){
        return Build.FINGERPRINT.contains("generic_x86");
    }

    /**
     * Checks if the serviceInterface is built into the device.
     * @param serviceInterfaceInfo Object that contains information about the ServiceInterface.
     * @return True if the service was deteceted.
     * @throws IllegalArgumentException If no metadata was found.
     */
    private static boolean isBuiltIn(ServiceInfo serviceInterfaceInfo) throws IllegalArgumentException {
        boolean builtInDetected = false;
        if(null != serviceInterfaceInfo && null != serviceInterfaceInfo.metaData) {
            String serviceInterfaceType = serviceInterfaceInfo.metaData.getString(META_DATA_ANT_CHIP_SERVICE_INTERFACE_TYPE_TAG);
            if(null != serviceInterfaceType) {
                if(META_DATA_ANT_CHIP_SERVICE_INTERFACE_TYPE_REMOTE.equals(serviceInterfaceType)) {
                    String hardwareTypes = serviceInterfaceInfo.metaData.getString(META_DATA_ANT_HARDWARE_TYPES_TAG);
                    if(null != hardwareTypes && hardwareTypes.contains(META_DATA_ANT_HARDWARE_TYPE_BUILTIN)) {
                        builtInDetected = true;
                    }
                } else {
                    builtInDetected = true;
                }
            }

            return builtInDetected;
        } else {
            throw new IllegalArgumentException("No meta data");
        }
    }

    /**
     * Check the system if ant services are installed.
     * @param context The Activity the method was called from.
     * @return The Resolve Info of the services.
     */
    @SuppressLint("WrongConstant")
    private static List<ResolveInfo> queryForAntServices(Context context) {
        return context.getPackageManager().queryIntentServices(new Intent(INTENT_ACTION_QUERY_SERVICE_INFO), 128);
    }


}
