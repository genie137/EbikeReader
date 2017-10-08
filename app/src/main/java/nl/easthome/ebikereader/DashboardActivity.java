package nl.easthome.ebikereader;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import nl.easthome.ebikereader.Objects.Chat;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.nav_view) NavigationView mNavigationView;
	@BindView(R.id.toolbar) Toolbar mToolbar;
	@BindView(R.id.dashboard_layout) LinearLayout mDashboardLayout;
	@BindView(R.id.dynamicArcView) DecoView mDecoView;
	@BindView(R.id.dashboard_content_holder) RelativeLayout mContentHolder;
	@OnClick(R.id.fab) public void onFabButtonPress(){
		Snackbar.make(mDashboardLayout, "Starting a new ride.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
		populatePieChart();
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
		myRef.setValue(new Chat("lol", "howlol", "12"));
	}

	private void populatePieChart() {
		SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0)).setRange(0, 100, 0).setLineWidth(80f).build();
		int series1Index = mDecoView.addSeries(seriesItem1);
		mDecoView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
		mDecoView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
		mDecoView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		ButterKnife.bind(this);
		onCreateSetupNav();
		applyViewSizeChanges();
		mDecoView.configureAngles(180,0);
		mDecoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
									.setRange(0, 100, 100)
									.setInitialVisibility(true)
									.setLineWidth(80f)
									.build());
	}

	private void applyViewSizeChanges() {
		ViewTreeObserver vto = mDecoView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Log.d("ParentSize", "HEIGHT: " + pxToDp(mDashboardLayout.getHeight()) + ", WIDTH: " + mDashboardLayout.getWidth());
				Log.d("ContentSize", "HEIGHT: " + pxToDp(mContentHolder.getHeight()) + ", WIDTH: " + mContentHolder.getWidth());
				Log.d("ChartSize", "HEIGHT: " + pxToDp(mDecoView.getHeight()) + ", WIDTH: " + mDecoView.getWidth());
				ViewGroup.LayoutParams parentParams = mDashboardLayout.getLayoutParams();
				parentParams.height = mDashboardLayout.getHeight() + mDecoView.getHeight();
				mDashboardLayout.requestLayout();
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

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
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

	private void logoutApp() {
		Log.d("APP", "logout hit");
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

}
