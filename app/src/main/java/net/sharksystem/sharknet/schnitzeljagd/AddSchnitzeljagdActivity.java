package net.sharksystem.sharknet.schnitzeljagd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.util.Collections;
import net.sharksystem.sharknet.schnitzeljagd.Schnitzel;

public class AddSchnitzeljagdActivity extends AppCompatActivity {

    private int addIndex = 0;
    private TextView text;
    private EditText schnitzelDescription;
    private Button addButton;
    private Button finishButton;
    private Schnitzeljagd schnitzeljagd;
    private ListView listView;
    private ArrayAdapter<Schnitzel> arrayAdapter;
    private Locator locator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzeljagd);
        text = (TextView) findViewById(R.id.addSchnitzelText);
        schnitzelDescription = (EditText) findViewById(R.id.addSchnitzelDescription);
        addButton = (Button) findViewById(R.id.add_btn);
        listView = (ListView) findViewById(R.id.schnitzellist);
        finishButton = (Button) findViewById(R.id.finish_btn);
        schnitzeljagd = new Schnitzeljagd("bla");
        locator = new Locator(this);
        text.setText("Beschreibung des Ziels:");
        registerForContextMenu(listView);
        arrayAdapter = new ArrayAdapter<Schnitzel>(this, android.R.layout.simple_list_item_1, schnitzeljagd.getSchnitzel());
        listView.setAdapter(arrayAdapter);
        //listView.setEmptyView(findViewById(R.id.empty));
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

            Schnitzel schnitzel = new Schnitzel(addIndex, schnitzelDescription.getText().toString(), locator.getLastLocation());
            schnitzeljagd.addSchnitzel(schnitzel);
            addIndex++;
            schnitzelDescription.setText("");
            text.setText("Weg zum nächsten Schnitzel");
            Collections.sort(schnitzeljagd.getSchnitzel());
            //Collections.reverse(schnitzeljagd.getSchnitzel());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public void newSchnitzeljagdOnClick(View view) {
        if(addIndex +1 <2){
            finishButton.setError("Mindestens 2 Schnitzel notwendig!");
        }
        else if(schnitzelDescription.getText().toString().matches("")){
            resortSchnitzel();
            //TODO schnitzeljagd in DB
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
        else{
            addIndex++;
            Schnitzel schnitzel = new Schnitzel(addIndex, schnitzelDescription.getText().toString(), locator.getLastLocation());
            schnitzeljagd.addSchnitzel(schnitzel);

            resortSchnitzel();
            //TODO SJ in DB

            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.schnitzellist) {
            getMenuInflater().inflate(R.menu.schnitzel_list_menu, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.schnitzel_loeschen_menu){
            //TODO EDIT INDEX FOR THE FOLLOWING SCHNITZEL
            reIndexSchnitzel(info.position);
            schnitzeljagd.getSchnitzel().remove(info.position);
            addIndex--;
            if(addIndex == 0){
                text.setText("Beschreibung des Ziels:" );
            }
            arrayAdapter.notifyDataSetChanged();
        }
        else if(item.getItemId() == R.id.schnitzel_bearbeiten_menu){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setText(schnitzeljagd.getSchnitzel().get(info.position).getMessage());
            builder.setView(input);
            builder.setTitle("Edit");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Bearbeiten abgebrochen", Toast.LENGTH_SHORT).show();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(input.getText().toString().length() > 0){
                        schnitzeljagd.getSchnitzel().get(info.position).setMessage(input.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    else{
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Bearbeiten abgebrochen, da Text leer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return super.onContextItemSelected(item);
    }

    public void resortSchnitzel(){ //id -> size - id
        Collections.sort(schnitzeljagd.getSchnitzel());
        for (int i = 0; i< schnitzeljagd.getSchnitzel().size(); i++){
            schnitzeljagd.getSchnitzel().get(i).setIdx(schnitzeljagd.getSchnitzel().size()-i);
        }
        Collections.sort(schnitzeljagd.getSchnitzel());
    }

    public void reIndexSchnitzel(int start){ //start should equal idx of removed schnitzel
        Collections.sort(schnitzeljagd.getSchnitzel());
        Collections.reverse(schnitzeljagd.getSchnitzel());
        for (int i = schnitzeljagd.getSchnitzel().size() - start; i < schnitzeljagd.getSchnitzel().size(); i++){
            schnitzeljagd.getSchnitzel().get(i).setIdx(i-1);
        }
        Collections.reverse(schnitzeljagd.getSchnitzel());

    }


}
