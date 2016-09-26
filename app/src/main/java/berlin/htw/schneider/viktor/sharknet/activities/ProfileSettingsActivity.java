package berlin.htw.schneider.viktor.sharknet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import berlin.htw.schneider.viktor.sharknet.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner sync = (Spinner) findViewById(R.id.sync_spinner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
