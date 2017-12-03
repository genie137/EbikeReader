package nl.easthome.ebikereader.Interfaces;
import com.google.android.gms.maps.SupportMapFragment;

import nl.easthome.ebikereader.Enums.DashboardGuiUpdateStates;
import nl.easthome.ebikereader.Objects.RideMeasurement;

public interface IRideRecordingGuiUpdate {
    void onNewRequestedGuiUpdate(DashboardGuiUpdateStates updateState, RideMeasurement rideMeasurement);

    SupportMapFragment getGoogleMap();


}
