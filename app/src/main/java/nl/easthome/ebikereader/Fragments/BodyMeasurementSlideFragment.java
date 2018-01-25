package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.easthome.ebikereader.Objects.UserDetails;
import nl.easthome.ebikereader.R;

public class BodyMeasurementSlideFragment extends Fragment implements ISlidePolicy {
    @BindView(R.id.weight_input) EditText mUserWeight;
    @BindView(R.id.height_input) EditText mUserHeight;
    @BindView(R.id.age_input)
    EditText mUserAge;
    @BindView(R.id.gender_spinner)
    Spinner mGenderSpinner;

    private String userGender;

    public BodyMeasurementSlideFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_body_measurement_slide, container, false);
        ButterKnife.bind(this, v);
        userGender = getResources().getStringArray(R.array.genders)[0];
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userGender = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userGender = getResources().getStringArray(R.array.genders)[0];
            }
        });

        return v;
    }

    @Override
    public boolean isPolicyRespected() {
        return (!mUserWeight.getText().toString().equals("") && !mUserHeight.getText().toString().equals("") && !mUserAge.getText().toString().equals(""));
    }

    //TODO move text to strings.xml
    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), R.string.body_measurement_slide_not_all_fields_error, Toast.LENGTH_LONG).show();
    }

    public void fillFields(UserDetails userDetails) {
        mUserHeight.setText(String.valueOf(userDetails.getUserHeight()));
        mUserWeight.setText(String.valueOf(userDetails.getUserWeight()));
        mUserAge.setText(String.valueOf(userDetails.getUserAge()));
        String[] genderArray = getResources().getStringArray(R.array.genders);
        for (int i = 0; i < genderArray.length; i++) {
            String value = genderArray[i];
            if (value.equals(userDetails.getUserGender())) {
                mGenderSpinner.setSelection(i);
            }
        }
    }

    public int getUserAge() {
        return Integer.valueOf(mUserAge.getText().toString());
    }

    public int getUserWeight() {
        return Integer.valueOf(mUserWeight.getText().toString());
    }

    public int getUserHeight() {
        return Integer.valueOf(mUserHeight.getText().toString());
    }

    public String getUserGender() {
        return userGender;
    }
}
