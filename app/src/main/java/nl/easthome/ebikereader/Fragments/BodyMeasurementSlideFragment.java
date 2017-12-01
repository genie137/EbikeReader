package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.easthome.ebikereader.IntroActivity;
import nl.easthome.ebikereader.R;

public class BodyMeasurementSlideFragment extends Fragment {
    private IntroActivity mActivity;

    public BodyMeasurementSlideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_body_measurement_slide, container, false);
    }

}
