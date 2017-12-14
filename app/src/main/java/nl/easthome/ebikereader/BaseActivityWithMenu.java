package nl.easthome.ebikereader;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import nl.easthome.ebikereader.Helpers.UserLogout;

public class BaseActivityWithMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private @IdRes int menuID;

    public void setContent(@LayoutRes int layoutResID, @IdRes int menuID){
        setContentView(layoutResID);
        this.menuID = menuID;
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity_with_menu_layout, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.base_activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.base_activity_toolbar);
        navigationView = (NavigationView) findViewById(R.id.base_activity_nav_view);

        if (useToolbar())
        {
            setSupportActionBar(toolbar);
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();
    }

    /**
     * Helper method that can be used by child classes to
     * specify that they don't want a {@link Toolbar}
     * @return true
     */
    protected boolean useToolbar()
    {
        return true;
    }

    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        if( useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            fullLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white));
        }
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */
    protected boolean useDrawerToggle()
    {
        return true;
    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(menuID);
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case(R.id.nav_dashboard):
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            case(R.id.nav_ant_sensors):
                startActivity(new Intent(this, AntSensorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case(R.id.nav_history):
                startActivity(new Intent(this, RideHistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case(R.id.nav_logout):
                UserLogout.showUserLogoutDialogs(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
