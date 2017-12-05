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
public class BikeInfoSlideFragment extends Fragment {
    @BindView(R.id.model_input) EditText mBikeModel;
    @BindView(R.id.km_input) EditText mKmInput;
    @BindView(R.id.battery_input) EditText mBattWatt;
    @BindView(R.id.maxspeed_input) EditText mMaxSpeed;
    @BindView(R.id.wheelcircum_input) EditText mWheelCircum;

    public BikeInfoSlideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bike_info_slide, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    public String getBikeModel() {
        return mBikeModel.getText().toString();
    }

    public double getBikeKmRange() {
        return Double.parseDouble(mKmInput.getText().toString());
    }

    public double getBikeBattWatt() {
        return Double.parseDouble(mBattWatt.getText().toString());
    }

    public double getBikeMaxSpeed() {
        return Double.parseDouble(mMaxSpeed.getText().toString());
    }

    public double getBikeWheelCircumference() {
        return Double.parseDouble(mWheelCircum.getText().toString());
    }
}
