package nl.easthome.antpluslibary.Interfaces;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import java.util.concurrent.ConcurrentLinkedDeque;

import nl.easthome.antpluslibary.Objects.AntPlusSensorData;

public abstract class ISensorHandler<Sensor extends AntPluginPcc, SensorData extends AntPlusSensorData> {
    private ConcurrentLinkedDeque<SensorData> mDataQueue;

    public abstract void subscribeToEvents(Sensor sensorConnection);

    public void setDataDeque(ConcurrentLinkedDeque<SensorData> deque) {
        mDataQueue = deque;
    }

    protected SensorData getLatestNonCompletedDataset(Class<SensorData> returnEmptyTypeIfEmptyClazz) {
        if (mDataQueue.size() >= 1) {
            for (SensorData sensorData : mDataQueue) {
                if (!sensorData.isDatasetComplete()) {
                    return sensorData;
                }
            }
        }
        try {
            SensorData sensorData = returnEmptyTypeIfEmptyClazz.newInstance();
            mDataQueue.addFirst(sensorData);
            return sensorData;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
