package nl.easthome.ebikereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AntSensorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.activity_title_antsensor));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
