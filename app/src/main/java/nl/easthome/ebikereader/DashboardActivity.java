package nl.easthome.ebikereader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.antpluslibary.Exceptions.NoDeviceConfiguredException;
import nl.easthome.antpluslibary.Exceptions.NotImplementedException;
import nl.easthome.ebikereader.Implementations.RideRecordingServiceConnection;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private static final String LOGTAG = "DashboardActivity";
    private RideRecordingServiceConnection mRideRecordingServiceConnection;
    private Intent mRideRecordingIntent;
    private RideRecordingService mRideRecordingService;

	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.nav_view) NavigationView mNavigationView;
	@BindView(R.id.toolbar) Toolbar mToolbar;
	@BindView(R.id.dashboard_layout) LinearLayout mDashboardLayout;
	@BindView(R.id.dynamicArcView) DecoView mDecoView;
	@BindView(R.id.dashboard_content_holder) RelativeLayout mContentHolder;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
	@OnClick(R.id.fab) public void onRightFabButtonPress() {
		mRideRecordingService = mRideRecordingServiceConnection.getRideRecordingService();

		if (mRideRecordingService.isRecording()) {
            mRideRecordingService.stopRecording();
			mFloatingActionButton.setImageResource(R.drawable.ic_play_circle_outline_white_36dp);
		}
		else {
			try {
                mRideRecordingService.startRecording(this);
			} catch (NotImplementedException nie) {
				nie.printStackTrace();
			} catch (NoDeviceConfiguredException ndce) {
                showNoDeviceConfiguredExceptionDialog(ndce.getMessage());
			} catch (SecurityException se){
			    se.printStackTrace();
            }
			populatePieChart();
			mFloatingActionButton.setImageResource(R.drawable.ic_stop_white_36dp);
		}
	}
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOGTAG, "onCreate");
		setContentView(R.layout.activity_dashboard);
		ButterKnife.bind(this);
		onCreateSetupNav();
		applyViewSizeChanges();
        mRideRecordingServiceConnection = new RideRecordingServiceConnection();
        mRideRecordingIntent = new Intent(this, RideRecordingService.class);
        bindService(mRideRecordingIntent, mRideRecordingServiceConnection, Context.BIND_AUTO_CREATE);
    }
	@Override protected void onDestroy() {
		Log.d(LOGTAG, "onDestroy");
		if (!mRideRecordingService.isRecording()){
            stopService(mRideRecordingIntent);
        }
        unbindService(mRideRecordingServiceConnection);
        super.onDestroy();
    }
    @Override public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			if (mRideRecordingService.isRecording()) {
				new MaterialDialog.Builder(this)
						.title(R.string.dialog_quit_while_recording_title)
						.content(R.string.dialog_quit_while_recording_content)
						.positiveText(R.string.dialog_quit_while_recording_positive_button)
						.negativeText(R.string.dialog_quit_while_recording_negative_button)
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								finish();
							}
						})
						.show();
			} else {
				finish();
			}
		}
	}
	@Override protected void onPause() {
		Log.d(LOGTAG, "onPause");
		super.onPause();
	}
    @Override protected void onRestart() {
        Log.d(LOGTAG, "onRestart");
        super.onRestart();
    }
    @Override public boolean onNavigationItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.nav_ant_sensors) {
		    startActivity(new Intent(this, AntSensorActivity.class));
        } else if (id == R.id.nav_logout) {
			logoutApp();
			//TODO remove user specific firebase data
		}

		mDrawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}
    private void showNoDeviceConfiguredExceptionDialog(String message){
        new MaterialDialog.Builder(this)
                .title("Missing Sensor")
                .content("We cant start recording because we need a sensor which you haven't paired yet.  \n "+ message)
                .positiveText("Pair Sensor")
                .negativeText("Dismiss")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(DashboardActivity.this, AntSensorActivity.class);
                        startActivity(i);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }
	private void populatePieChart() {
		SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0)).setRange(0, 100, 0).setLineWidth(80f).build();
		int series1Index = mDecoView.addSeries(seriesItem1);
		mDecoView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
		mDecoView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
		mDecoView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());
	}
	private void applyViewSizeChanges() {
		ViewTreeObserver vto = mDecoView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ViewGroup.LayoutParams parentParams = mDashboardLayout.getLayoutParams();
				parentParams.height = mDashboardLayout.getHeight() + mDecoView.getHeight();
				mDashboardLayout.requestLayout();
				mDecoView.configureAngles(180, 0);
				mDecoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
											.setRange(0, 100, 100)
											.setInitialVisibility(true)
											.setLineWidth(80f)
											.build());
				mDecoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

			}
		});
	}
	private void onCreateSetupNav() {
		setSupportActionBar(mToolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		mNavigationView.setNavigationItemSelectedListener(this);
	}
	private void logoutApp() {
		FirebaseAuth.getInstance().signOut();
		finish();
	}
    public LinearLayout getRootView() {
        return mDashboardLayout;
    }

}
