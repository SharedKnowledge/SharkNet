package net.sharksystem.sharknet.schnitzeljagd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.sharksystem.sharknet.R;

import java.util.ArrayList;

public class Schnitzelorte extends AppCompatActivity {

    private ArrayList<OrtInterface> orte = new ArrayList<>();
    private ListView ortList;
    private ArrayAdapter<OrtInterface> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schnitzelorte);
        setTitle("verfügbare Orte:");
        ortList = (ListView) findViewById(R.id.schnitzel_orte_list);
        generateDummyOrte();
        arrayAdapter = new ArrayAdapter<OrtInterface>(this, android.R.layout.simple_list_item_1, orte);
        ortList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    public void generateDummyOrte(){
        int size = 20;
        for(int i = 0;i<size;i++){
            OrtInterface ort = new Ort("dummy name: " + i);
            orte.add(ort);
        }
    }
}
