package net.sharksystem.sharknet.schnitzeljagd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

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
        //FloatingActionButton addNewSchnitzeljagd = (FloatingActionButton) findViewById(R.id.addSchnitzeljagdButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            this.locator = new Locator(this);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
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
        //TODO addnew Activity
        Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
    }
}
