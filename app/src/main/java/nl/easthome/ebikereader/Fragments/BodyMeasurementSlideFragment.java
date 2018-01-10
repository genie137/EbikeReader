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
public class BodyMeasurementSlideFragment extends Fragment implements ISlidePolicy {
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


    public int getUserWeight() {
        return Integer.valueOf(mUserWeight.getText().toString());
    }

    public int getUserHeight() {
        return Integer.valueOf(mUserHeight.getText().toString());
    }

    @Override
    public boolean isPolicyRespected() {
        return (!mUserWeight.getText().toString().equals("") && !mUserHeight.getText().toString().equals(""));
    }

    //TODO move text to strings.xml
    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please enter the data in order to continue.", Toast.LENGTH_LONG).show();
    }

    public void fillFields(UserDetails userDetails) {
            mUserHeight.setText(String.valueOf(userDetails.getUserHeight()));
            mUserWeight.setText(String.valueOf(userDetails.getUserWeight()));
    }
}
