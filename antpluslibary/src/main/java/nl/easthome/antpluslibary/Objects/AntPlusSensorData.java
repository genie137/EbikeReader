package nl.easthome.antpluslibary.Objects;

import com.google.firebase.database.Exclude;
public abstract class AntPlusSensorData {

    @Exclude
    protected boolean isDatasetComplete = false;

    protected AntPlusSensorData() {
    }

    protected abstract void verifyDatasetCompleted();

    @Exclude
    public boolean isDatasetComplete() {
        return isDatasetComplete;
    }
}
