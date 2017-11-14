package net.sharksystem.sharknet.schnitzeljagd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
//TODO sidenav not visible!!
public class SchnitzeljagdMainActivity extends RxSingleBaseActivity<List<Schnitzeljagd>> {

    private ArrayList<Schnitzeljagd> schnitzelJagdList = new ArrayList<>();
    private Locator locator;
    private boolean admin_mode = false;
    private static final String pw = "nimda";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schnitzeljagd_main);
        setTitle("Schnitzeljagd");
        setProgressMessage("Loading Schnitzeljagden");

        FloatingActionButton addNewSchnitzeljagd = (FloatingActionButton) findViewById(R.id.addSchnitzeljagdButton);
        //addNewSchnitzeljagd.setVisibility(View.INVISIBLE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            this.locator = new Locator(this);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void generateDummySchnitzeljagden(){
        int size = 5;
        int size2 = 10;
        for(int i=0;i<size;i++){
            Schnitzeljagd jagd = new Schnitzeljagd("dummy name nr: " + i);
            for(int j=0;j<size2;j++){
                Schnitzel schnitzel = new Schnitzel(j,"dummy schnitzel: " + j, locator.getLastLocation());
                jagd.addSchnitzel(schnitzel);
            }
            schnitzelJagdList.add(jagd);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.schnitzeljagd_main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opt_admin:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setTitle("Admin Passwort:");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Als Admin anmelden fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(input.getText().toString().length() > 0){
                            if(input.getText().toString().equals(pw)){
                                activateFloatingActionButton();
                                admin_mode = true;
                            }
                            dialog.dismiss();
                        }
                        else{
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "Als Admin anmelden fehlgeschlagen, Passwort leer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return true;
            case R.id.opt_orte:
                return true;

            default: return super.onOptionsItemSelected(item);
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
