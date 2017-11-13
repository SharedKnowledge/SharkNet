package net.sharksystem.sharknet.schnitzeljagd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import com.woxthebox.draglistview.DragListView;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.util.ArrayList;
import java.util.Collections;

public class AddSchnitzeljagdActivity extends AppCompatActivity {

    private int addIndex = 0;
    private TextView text;
    private EditText schnitzeljagdDescription;
    private Schnitzeljagd schnitzeljagd;
    private DragListView dragListView;
    private ArrayAdapter<Schnitzel> arrayAdapter;
    private Locator locator;


    private EditText schnitzelDescription;
    private ListView listview;
    private ArrayList<Schnitzel> schnitzellist;
    private Button finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzeljagd);
        text = (TextView) findViewById(R.id.addSchnitzelText);
        schnitzeljagdDescription = (EditText) findViewById(R.id.addSchnitzelJagdDescription);
        schnitzeljagd = new Schnitzeljagd("bla");
        locator = new Locator(this);
        text.setText("Beschreibung des Ziels:");
        registerForContextMenu(dragListView);
        arrayAdapter = new ArrayAdapter<Schnitzel>(this, android.R.layout.simple_list_item_1, schnitzeljagd.getSchnitzel());
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(getApplicationContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(getApplicationContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.list_item, R.id.image, false);
        //dragListView.setAdapter(listAdapter); //TODO implement ItemAdapter
        dragListView.setCanDragHorizontally(false);
    }

    public void generateDummySchnitzel(){
        int size = 10;
        for(int i = 0; i < size ; i++){
            schnitzeljagd.addSchnitzel( new Schnitzel(i,"Dummy Schnitzel Nr.: " + i, locator.getLastLocation()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK){
                Schnitzel schnitzel = (Schnitzel) data.getSerializableExtra("schnitzel");
                schnitzel.setIdx(addIndex);
                schnitzeljagd.addSchnitzel(schnitzel);
                addIndex++;
                //TODO update listview
            }
            else if(requestCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Schnitzeljagderstellung abgebrochen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void newSchnitzeljagdOnClick(View view) { //todo remove
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
        //if(v.getId() == R.id.schnitzellist) {
        //    getMenuInflater().inflate(R.menu.schnitzel_list_menu, menu);
        //}
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
