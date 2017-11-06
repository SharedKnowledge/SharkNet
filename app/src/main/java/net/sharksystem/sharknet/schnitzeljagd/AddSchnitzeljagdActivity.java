package net.sharksystem.sharknet.schnitzeljagd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.api.locator.Locator;
import net.sharksystem.api.models.Schnitzel;
import net.sharksystem.api.models.Schnitzeljagd;
import net.sharksystem.sharknet.R;

import java.util.Collections;

public class AddSchnitzeljagdActivity extends AppCompatActivity {

    private int addIndex = 0;
    private TextView text;
    private TextView numberOfSchnitzel;
    private EditText schnitzelDescription;
    private Button addButton;
    private Button finishButton;
    private Schnitzeljagd schnitzeljagd;
    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzeljagd);
        text = (TextView) findViewById(R.id.addSchnitzelText);
        numberOfSchnitzel = (TextView) findViewById(R.id.addSchnitzelID);
        schnitzelDescription = (EditText) findViewById(R.id.addSchnitzelDescription);
        addButton = (Button) findViewById(R.id.add_btn);
        finishButton = (Button) findViewById(R.id.finish_btn);
        schnitzeljagd = new Schnitzeljagd("bla");
        locator = new Locator(this);

    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        finish();
    }
    public void newSchnitzelOnClick(View view) {
        if(schnitzelDescription.getText().toString().matches("")){
            schnitzelDescription.setError("Darf nicht leer sein");
        }
        else{
            if(locator.getLastLocation() == null){
                Toast.makeText(getApplicationContext(),"location null", Toast.LENGTH_SHORT).show();
            }
            Schnitzel schnitzel = new Schnitzel(addIndex, text.getText().toString(), locator.getLastLocation());
            schnitzeljagd.addSchnitzel(schnitzel);
            addIndex++;
            schnitzelDescription.setText("");
            numberOfSchnitzel.setText(addIndex);
        }
    }

    public void newSchnitzeljagdOnClick(View view) {
        if(addIndex +1 <2){
            finishButton.setError("Mindestens 2 Schnitzel notwendig!");
        }
        else if(schnitzelDescription.getText().toString().matches("")){
            //TODO schnitzeljagd in DB
        }
        else{
            addIndex++;
            Schnitzel schnitzel = new Schnitzel(addIndex, text.getText().toString(), locator.getLastLocation());
            schnitzeljagd.addSchnitzel(schnitzel);

            sortSchnitzel();
            //TODO SJ in DB

            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void sortSchnitzel(){ //id -> size - id
        Collections.sort(schnitzeljagd.getSchnitzel());
        for (int i = 0; i< schnitzeljagd.getSchnitzel().size(); i++){
            schnitzeljagd.getSchnitzel().get(i).setIdx(schnitzeljagd.getSchnitzel().size()-i);
        }
        Collections.sort(schnitzeljagd.getSchnitzel());
    }
}
