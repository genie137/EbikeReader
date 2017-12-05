package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.R;

//todo implement loding items from firebase
public class BodyMeasurementSlideFragment extends Fragment {
    @BindView(R.id.weight_input) EditText mUserWeight;
    @BindView(R.id.height_input) EditText mUserHeight;


    public BodyMeasurementSlideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_body_measurement_slide, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    public double getUserWeight() {
        return Double.parseDouble(mUserWeight.getText().toString());
    }

    public double getUserHeight() {
        return Double.parseDouble(mUserHeight.getText().toString());
    }
}
