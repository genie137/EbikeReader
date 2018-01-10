package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Objects.UserDetails;
import nl.easthome.ebikereader.R;

//todo implement loding items from firebase
public class BikeInfoSlideFragment extends Fragment implements ISlidePolicy{
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

    public int getBikeKmRange() {
        return Integer.valueOf(mKmInput.getText().toString());
    }

    public int getBikeBattWatt() {
        return Integer.valueOf(mBattWatt.getText().toString());
    }

    public int getBikeMaxSpeed() {
        return Integer.valueOf(mMaxSpeed.getText().toString());
    }

    public int getBikeWheelCircumference() {
        return Integer.valueOf(mWheelCircum.getText().toString());
    }

    @Override
    public boolean isPolicyRespected() {
        return (!mBikeModel.getText().toString().equals("") && !mKmInput.getText().toString().equals("") && !mBattWatt.getText().toString().equals("") && !mMaxSpeed.getText().toString().equals("") && !mWheelCircum.getText().toString().equals(""));
    }

    //TODO move text to strings.xml
    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please enter the data in order to continue.", Toast.LENGTH_LONG).show();
    }

    public void fillFields(UserDetails userDetails) {
        mBikeModel.setText(userDetails.getBikeModel());
        mKmInput.setText(String.valueOf(userDetails.getBikeKmRange()));
        mBattWatt.setText(String.valueOf(userDetails.getBikeBattWatt()));
        mMaxSpeed.setText(String.valueOf(userDetails.getBikeMaxSpeed()));
        mWheelCircum.setText(String.valueOf(userDetails.getBikeWheelCircumference()));

    }
}
