package net.sharksystem.sharknet.schnitzeljagd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

import java.util.ArrayList;
import java.util.List;
//TODO sidenav not visible!!
public class SchnitzeljagdMainActivity extends RxSingleBaseActivity<List<Schnitzeljagd>> {

    private ArrayList<Schnitzeljagd> schnitzelJagdList = new ArrayList<>();
    //public static DBSchnitzeljagdHelper dbhelper;
    private Locator locator;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schnitzeljagd_main);
        setProgressMessage("Loading Schnitzeljagden");
        //dbhelper = new DBSchnitzeljagdHelper(getApplicationContext());
        FloatingActionButton addNewSchnitzeljagd = (FloatingActionButton) findViewById(R.id.addSchnitzeljagdButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            this.locator = new Locator(this);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected List<Schnitzeljagd> doOnBackgroundThread() throws Exception {
        return schnitzelJagdList;
    }

    @Override
    protected void doOnUIThread(List<Schnitzeljagd> messages) {

    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    public void fabClicked(View view) {
        final Intent intent = new Intent(this, AddSchnitzeljagdActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK){
                //TODO
            }
            else if(requestCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Schnitzeljagderstellung abgebrochen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //User just granted permission in permission dialog
                    this.locator = new Locator(this);
                } else {
                    Toast.makeText(SchnitzeljagdMainActivity.this, "Permission denied to get location", Toast.LENGTH_SHORT).show();
                    //TODO kill app (app won't work without location permission)
                }
                return;
            }
        }
    }
}
