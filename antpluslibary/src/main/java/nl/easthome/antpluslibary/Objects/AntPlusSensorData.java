package nl.easthome.antpluslibary.Objects;

import com.google.firebase.database.Exclude;


/**
 * Abstract Class that contains the received data from the sensor.
 * The @Exclude annotation is from Firebase, and excludes the field from being saved online.
 */
public abstract class AntPlusSensorData {

    @Exclude
    protected boolean isDatasetComplete = false;

    /**
     * Constructor.
     */
    protected AntPlusSensorData() {
    }

    /**
     * Abstract method that needs to check if all fields are filled, and set the super.isDatasetComplete boolean to the result/
     */
    protected abstract void verifyDatasetCompleted();

    /**
     * Getter.
     *
     * @return True if all the fields are filled.
     */
    @Exclude
    public boolean isDatasetComplete() {
        return isDatasetComplete;
    }
}
