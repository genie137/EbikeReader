package nl.easthome.ebikereader.Dialogs;
import android.Manifest;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import nl.easthome.ebikereader.R;


public class FixLocationSnackbarOnClick implements View.OnClickListener {

    private Activity mActivity;
    private View mLayoutForSnackbar;

    public FixLocationSnackbarOnClick(Activity activity, View layoutForSnackbar) {
        mActivity = activity;
        mLayoutForSnackbar = layoutForSnackbar;
    }

    @Override
    public void onClick(View view) {
        Dexter.withActivity(mActivity).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(mActivity, R.string.permission_for_location_granted, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(mActivity, R.string.permission_for_location_on_denied, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                final PermissionToken token1 = token;
                Snackbar snackbar1 = Snackbar.make(mLayoutForSnackbar, R.string.permission_for_location_rationale, Snackbar.LENGTH_LONG);
                snackbar1.setAction(R.string.snackbar_button_fix_this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        token1.continuePermissionRequest();
                    }
                }).show();
            }
        }).check();
    }
}
