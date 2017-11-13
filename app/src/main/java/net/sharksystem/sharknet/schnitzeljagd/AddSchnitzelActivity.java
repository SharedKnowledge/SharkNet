package net.sharksystem.sharknet.schnitzeljagd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.util.ArrayList;

public class AddSchnitzelActivity extends AppCompatActivity {

    private ArrayList<Ort> orte = new ArrayList<>();
    private Locator locator;
    private TextView text;
    private EditText schnitzelDescription;
    private Button addButton;
    private ListView ortListView;
    private ArrayAdapter<Ort> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzel);
        this.locator = new Locator(this);
        text = (TextView) findViewById(R.id.addSchnitzelText);
        schnitzelDescription = (EditText) findViewById(R.id.addSchnitzelDescription);
        addButton = (Button) findViewById(R.id.add_btn);
        ortListView = (ListView) findViewById(R.id.addSchnitzelOrtList);
        generateDummyOrte();
        arrayAdapter = new ArrayAdapter<Ort>(this, android.R.layout.simple_list_item_1, orte);
        ortListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void generateDummyOrte(){
        int size = 15;
        for(int i = 0; i < size ; i++){
            orte.add(new Ort("Dummy Ort Name Nr.: " + i, locator.getLastLocation()));
        }
    }

    public void newSchnitzelOnClick(View view) {
        if(schnitzelDescription.getText().toString().matches("")){
            schnitzelDescription.setError("Darf nicht leer sein");
        }
        else {
            if (locator.getLastLocation() == null) {
                Toast.makeText(getApplicationContext(), "location null", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            Schnitzel schnitzel = new Schnitzel(0, schnitzelDescription.getText().toString(), locator.getLastLocation());
            intent.putExtra("schnitzel", schnitzel);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
