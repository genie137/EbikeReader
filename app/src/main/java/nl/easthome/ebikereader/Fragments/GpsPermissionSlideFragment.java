package nl.easthome.ebikereader.Fragments;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlidePolicy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.R;

public class GpsPermissionSlideFragment extends Fragment implements ISlidePolicy {
    boolean isPermissionGiven = false;
    @BindView(R.id.gpsPermissionButton) Button mGpsButton;
    @OnClick(R.id.gpsPermissionButton) public void onGpsButtonPress(){
        askPermissions();
    }

    public GpsPermissionSlideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps_permission_slide, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void askPermissions() {
        mGpsButton.setEnabled(false);
        mGpsButton.setText("Working...");
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isPermissionGiven = true;
                mGpsButton.setText("Permission given, Thanks!");
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                System.out.println("DENIED");
                mGpsButton.setText("Please change permission in app settings. \n Give Permission");
                mGpsButton.setEnabled(true);
                mGpsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                mGpsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askPermissions();
                    }
                });
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                mGpsButton.setText("We really need that, can you please accept? - Give Permission");
                mGpsButton.setEnabled(true);
                System.out.println("RATIONALE");
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {

                System.out.println("ERROR " + error.toString());
                mGpsButton.setText("Something happened, can you try again? - Give Permission");
                mGpsButton.setEnabled(true);
            }
        }).onSameThread().check();
    }

    @Override
    public boolean isPolicyRespected() {
        return isPermissionGiven;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please accept the permission before continuing.", Toast.LENGTH_LONG).show();
    }
}
