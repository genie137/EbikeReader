package nl.easthome.ebikereader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.ebikereader.Objects.Ride;
import nl.easthome.ebikereader.Objects.RideMeasurement;
import nl.easthome.ebikereader.Services.MappingService;
import nl.easthome.ebikereader.Services.RideRecordingService;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private MappingService mMappingService;
    private DashboardActivity.RideRecordingServiceConnection mRideRecordingServiceConnection;

	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.nav_view) NavigationView mNavigationView;
	@BindView(R.id.toolbar) Toolbar mToolbar;
	@BindView(R.id.dashboard_layout) LinearLayout mDashboardLayout;
	@BindView(R.id.dynamicArcView) DecoView mDecoView;
	@BindView(R.id.dashboard_content_holder) RelativeLayout mContentHolder;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
	@OnClick(R.id.fab) public void onRightFabButtonPress() {
        mRideRecordingServiceConnection.mRideRecordingService.startRecording(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		ButterKnife.bind(this);
		onCreateSetupNav();
		applyViewSizeChanges();
        mRideRecordingServiceConnection = new RideRecordingServiceConnection();
        bindService(new Intent(this, RideRecordingService.class), mRideRecordingServiceConnection, Context.BIND_AUTO_CREATE);
        mMappingService = new MappingService(this);
	}

    @Override
    protected void onDestroy() {
        unbindService(mRideRecordingServiceConnection);
        super.onDestroy();
    }

    @Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
//			if (isMyServiceRunning(RideRecordingService.class)) {
//				new MaterialDialog.Builder(this)
//						.title(R.string.dialog_quit_while_recording_title)
//						.content(R.string.dialog_quit_while_recording_content)
//						.positiveText(R.string.dialog_quit_while_recording_positive_button)
//						.negativeText(R.string.dialog_quit_while_recording_negative_button)
//						.onPositive(new MaterialDialog.SingleButtonCallback() {
//							@Override
//							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//								DashboardActivity.super.onBackPressed();
//							}
//						})
//						.show();
//			} else {
//				DashboardActivity.super.onBackPressed();
//			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_logout) {
			logoutApp();
		}

		mDrawerLayout.closeDrawer(GravityCompat.START);
		return true;
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

	private int pxToDp(int px){
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}
	public int dpToPx(int dp) {
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public void updateViews(final RideMeasurement measurement) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mMappingService.addPointToMap(measurement.getLocation());
			}
		});
	}

	public void restoreViews(final Ride ride) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mMappingService.restoreAllPointsOnMap(ride.getRideMeasurements());
			}
		});
	}

    public LinearLayout getRootView() {
        return mDashboardLayout;
    }


    private class RideRecordingServiceConnection implements ServiceConnection {
        RideRecordingService mRideRecordingService;
        boolean mIsServiceBound = false;


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RideRecordingService.RideRecordingBinder binder = (RideRecordingService.RideRecordingBinder) service;
            mRideRecordingService = binder.getService();
            mIsServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRideRecordingServiceConnection = null;
            mIsServiceBound = false;
        }
    }




}
