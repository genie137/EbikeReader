package nl.easthome.antpluslibary.Interfaces;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import java.util.concurrent.ConcurrentLinkedDeque;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

/**
 * Abstracts class that handles the data gathering from the Ant+ device/sensor.
 *
 * @param <SensorPcc>  Defines the sensor/device type.
 * @param <SensorData> Defines the data type.
 */
public abstract class ISensorHandler<SensorPcc extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private ConcurrentLinkedDeque<SensorData> mDataDeque;

    /**
     * Defines which data needs to be saved.
     * The different readings that can be gathered depends on the deviceType (and its SensorPcc)
     *
     * @param sensorConnection The connection to the sensor, which provides readings based on its methods.
     */
    public abstract void subscribeToEvents(SensorPcc sensorConnection);

    /**
     * Sets the Deque of the sensor.
     * The use of a deque makes sure the newest reading is always the first one to get.
     * @param deque The deque.
     */
    public void setDataDeque(ConcurrentLinkedDeque<SensorData> deque) {
        mDataDeque = deque;
    }

    /**
     * Returns the dataset which has not been completely filled.
     * @param returnEmptyTypeIfEmptyClazz The type of SensorData.
     * @return The latest dataset which has not been filled completely OR a new instance of an empty dataset.
     */
    protected SensorData getLatestNonCompletedDataset(Class<SensorData> returnEmptyTypeIfEmptyClazz) {
        if (mDataDeque.size() >= 1) {
            for (SensorData sensorData : mDataDeque) {
                if (!sensorData.isDatasetComplete()) {
                    return sensorData;
                }
            }
        }
        try {
            SensorData sensorData = returnEmptyTypeIfEmptyClazz.newInstance();
            mDataDeque.addFirst(sensorData);
            return sensorData;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
