package nl.easthome.antpluslibary.Objects;

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;
public class AntPlusSensor
{
    private DeviceType mSensorType;
    private int mDeviceNumber;
    private String mName;
    private AntAddType mAntAddType;

    public AntPlusSensor(DeviceType mSensorType, int mDeviceNumber, String mName, AntAddType mAntAddType) {
        this.mSensorType = mSensorType;
        this.mDeviceNumber = mDeviceNumber;
        this.mName = mName;
        this.mAntAddType = mAntAddType;
    }

    public AntPlusSensor(MultiDeviceSearch.MultiDeviceSearchResult multiDeviceSearchResult, AntAddType antAddType) {
        mSensorType = multiDeviceSearchResult.getAntDeviceType();
        mDeviceNumber = multiDeviceSearchResult.getAntDeviceNumber();
        mName = multiDeviceSearchResult.getDeviceDisplayName();
        mAntAddType = antAddType;
    }

    public DeviceType getmSensorType() {
        return mSensorType;
    }

    public int getmDeviceNumber() {
        return mDeviceNumber;
    }

    public String getmName() {
        return mName;
    }

    public AntAddType getmAntAddType() {
        return mAntAddType;
    }

    public void setmAntAddType(AntAddType mAntAddType) {
        this.mAntAddType = mAntAddType;
    }

    public enum AntAddType{
        NEW,
        EXISTING_AND_MISSING,
        EXISTING_AND_FOUND
    }

}