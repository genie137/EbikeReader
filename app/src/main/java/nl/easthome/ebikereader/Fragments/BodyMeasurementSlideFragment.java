package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.easthome.ebikereader.IntroActivity;
import nl.easthome.ebikereader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BodyMeasurementSlideFragment extends Fragment {
    private IntroActivity mActivity;

    public BodyMeasurementSlideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_measurement_slide, container, false);
    }

    public BodyMeasurementSlideFragment setCorrespondingActivity(IntroActivity activity){
        mActivity = activity;
        return this;
    }


}
