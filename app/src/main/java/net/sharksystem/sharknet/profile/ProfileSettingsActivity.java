package net.sharksystem.sharknet.profile;

import android.os.Bundle;

import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class ProfileSettingsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.profile_settings_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
