package nl.easthome.ebikereader;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.nav_view) NavigationView mNavigationView;
	@BindView(R.id.toolbar) Toolbar mToolbar;
	@BindView(R.id.dashboard_layout) ConstraintLayout mDashboardLayout;
	@BindView(R.id.dashboard_chart) PieChart mPieChart;
	@OnClick(R.id.fab) public void onFabButtonPress(){
		Snackbar.make(mDashboardLayout, "Starting a new ride.", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
		populatePieChart();
	}

	private void populatePieChart() {
		mPieChart.setHoleRadius(75f);


		ArrayList<PieEntry> pieEntries = new ArrayList<>();

		pieEntries.add(new PieEntry(18.5f, "Green"));
		pieEntries.add(new PieEntry(26.7f, "Yellow"));
		pieEntries.add(new PieEntry(24.0f, "Red"));
		pieEntries.add(new PieEntry(30.8f, "Blue"));
		PieDataSet pieDataSet = new PieDataSet(pieEntries, "Performance");
		pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
		PieData pieData = new PieData(pieDataSet);

		mPieChart.setData(pieData);
		mPieChart.animateXY(1500,1500);


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		ButterKnife.bind(this);
		onCreateSetupNav();
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
}
