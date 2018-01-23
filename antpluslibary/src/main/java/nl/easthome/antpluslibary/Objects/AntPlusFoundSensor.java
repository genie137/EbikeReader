package nl.easthome.antpluslibary.Objects;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;

import nl.easthome.antpluslibary.Enums.AntAddType;

/**
 * DataClass that contains information about a found sensor.
 */

public class AntPlusFoundSensor
{
    private DeviceType mSensorType;
    private int mDeviceNumber;
    private String mName;
    private AntAddType mAntAddType;

    /**
     * Constructor.
     *
     * @param mSensorType   The type of device.
     * @param mDeviceNumber The ID of the device.
     * @param mName         The Name of the device.
     * @param mAntAddType   Enum that defines how the sensor was added.
     */
    public AntPlusFoundSensor(DeviceType mSensorType, int mDeviceNumber, String mName, AntAddType mAntAddType) {
        this.mSensorType = mSensorType;
        this.mDeviceNumber = mDeviceNumber;
        this.mName = mName;
        this.mAntAddType = mAntAddType;
    }

    /**
     * Constructor.
     * @param multiDeviceSearchResult The result of a devicesearch.
     * @param antAddType Enum that defines how the sensor was added.
     */
    public AntPlusFoundSensor(MultiDeviceSearch.MultiDeviceSearchResult multiDeviceSearchResult, AntAddType antAddType) {
        mSensorType = multiDeviceSearchResult.getAntDeviceType();
        mDeviceNumber = multiDeviceSearchResult.getAntDeviceNumber();
        mName = multiDeviceSearchResult.getDeviceDisplayName();
        mAntAddType = antAddType;
    }

    //Getters and Setters

    public DeviceType getSensorType() {
        return mSensorType;
    }

    public int getDeviceNumber() {
        return mDeviceNumber;
    }

    public String getName() {
        return mName;
    }

    public AntAddType getAntAddType() {
        return mAntAddType;
    }

    public void setAntAddType(AntAddType mAntAddType) {
        this.mAntAddType = mAntAddType;
    }

}