package net.sharksystem.sharknet.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.sharkfw.system.L;
import net.sharksystem.sharknet.R;

public class MainActivity extends AppCompatActivity
        implements StartupFragment.StartupFragmentButtonListener,
            NewProfileFragment.NewProfileFragmentButtonListener,
            NewProfileAddressFragment.NewProfileAddressFragmentButtonListener{

    private StartupFragment mStartupFragment;
    private NewProfileFragment mNewProfileFragment;
    private NewProfileAddressFragment mNewProfileAddressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        L.setLogLevel(L.LOGLEVEL_ALL);

        if (savedInstanceState != null) {
            return;
        }

        mStartupFragment = new StartupFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mStartupFragment).commit();
    }

    @Override
    public void onCreateNewProfileSelected() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNewProfileFragment = new NewProfileFragment();
        transaction.replace(R.id.fragment_container, mNewProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreateDummyDataSelected() {
        Toast.makeText(this, "Create the dummy data and load the chat activity.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsePreviousProfileSelected() {
        Toast.makeText(this, "Load the profile and load the chat activity.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNextFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNewProfileAddressFragment = new NewProfileAddressFragment();
        transaction.replace(R.id.fragment_container, mNewProfileAddressFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackToStartupFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mNewProfileFragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateProfile() {

    }

    @Override
    public void onBackToNewProfileFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mNewProfileAddressFragment).commit();
        getSupportFragmentManager().popBackStack();
    }
}
