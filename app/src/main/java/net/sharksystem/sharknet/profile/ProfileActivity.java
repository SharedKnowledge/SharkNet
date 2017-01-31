package net.sharksystem.sharknet.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

public class ProfileActivity extends NavigationDrawerActivity implements View.OnClickListener {

    private Button detail, keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.content_profile);

        detail = (Button) findViewById(R.id.pro_edit_button);
        keys = (Button) findViewById(R.id.pro_keys_button);
        detail.setOnClickListener(this);
        keys.setOnClickListener(this);
        //TODO soll möglich machen ein neues Profil erstellen zu könnne
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent settings = new Intent(this, ProfileSettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.pro_edit_button:
                intent = new Intent(this, ProfileDetailActivity.class);
                startActivity(intent);
                break;
//TODO: nocht nicht in der API drin
//            case R.id.pro_interests_button:
//                intent = new Intent(this, ProfileDetailInterestsActivity.class);
//                startActivity(intent);
//                break;

            case R.id.pro_keys_button:
                intent = new Intent(this, ProfileKeys.class);
                startActivity(intent);

        }

    }
}
