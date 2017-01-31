package net.sharksystem.sharknet.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class ProfileSettingsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.content_profile_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
