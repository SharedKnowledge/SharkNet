package net.sharksystem.sharknet.profile;

import android.os.Bundle;

import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;

public class ProfileSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.profile_settings_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
