package nl.easthome.antpluslibary.Objects;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;
public class AntPlusFoundSensor
{
    private DeviceType mSensorType;
    private int mDeviceNumber;
    private String mName;
    private AntAddType mAntAddType;

    public AntPlusFoundSensor(DeviceType mSensorType, int mDeviceNumber, String mName, AntAddType mAntAddType) {
        this.mSensorType = mSensorType;
        this.mDeviceNumber = mDeviceNumber;
        this.mName = mName;
        this.mAntAddType = mAntAddType;
    }

    public AntPlusFoundSensor(MultiDeviceSearch.MultiDeviceSearchResult multiDeviceSearchResult, AntAddType antAddType) {
        mSensorType = multiDeviceSearchResult.getAntDeviceType();
        mDeviceNumber = multiDeviceSearchResult.getAntDeviceNumber();
        mName = multiDeviceSearchResult.getDeviceDisplayName();
        mAntAddType = antAddType;
    }

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

    public enum AntAddType{
        NEW,
        EXISTING_AND_MISSING,
        EXISTING_AND_FOUND
    }

}