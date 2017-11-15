package net.sharksystem.sharknet.schnitzeljagd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.schnitzeljagd.locator.Locator;

import java.util.ArrayList;
import java.util.Collections;

public class AddSchnitzeljagdActivity extends AppCompatActivity {

    private int addIndex = 0;
    private EditText schnitzeljagdDescription;
    private Schnitzeljagd schnitzeljagd;
    private DragListView dragListView;
    private Locator locator;
    private MySwipeRefreshLayout refreshLayout;
    private ItemAdapter listAdapter;
    private FloatingActionButton fab;

    private ImageButton finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzeljagd);

        schnitzeljagdDescription = (EditText) findViewById(R.id.addSchnitzelJagdDescription);
        schnitzeljagd = new Schnitzeljagd("bla");
        locator = new Locator(this);
        fab = (FloatingActionButton)findViewById(R.id.addSchnitzeljagdButton) ;
        finishButton = (ImageButton) findViewById(R.id.schnitzeljagd_add_header);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(schnitzeljagdDescription.getText().toString().equals("") || schnitzeljagdDescription.getText().toString().equals("Beschreibung der Schnitzeljagd")){
                    schnitzeljagdDescription.setError("Bitte Namen der Schnitzeljagd eingeben");
                }
                else if(schnitzeljagd.getSchnitzel().size()<=2){
                    Toast.makeText(getApplicationContext(),"Zu wenig Schnitzel erstellt. Mindestens 2 erstellen und erneut versuchen!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    schnitzeljagd.setDescription(schnitzeljagdDescription.getText().toString());
                    intent.putExtra("schnitzeljagd", schnitzeljagd);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        generateDummySchnitzel();
        refreshLayout = (MySwipeRefreshLayout) findViewById(R.id.schnitzel_swipe_refresh_layout);
        dragListView = (DragListView) findViewById(R.id.addSchnitzelJagdDragList);
        dragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        dragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {
                //refreshLayout.setEnabled(true);
                //Toast.makeText(getApplicationContext(), "Start - position: " + position + " & Start-Idx: " + schnitzeljagd.getSchnitzel().get(position), Toast.LENGTH_SHORT).show();
                //TODO move schnitzel from
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    //refreshLayout.setEnabled(true);
                    //Toast.makeText(getApplicationContext(), "End - position: " + toPosition + " & End-Idx: " + schnitzeljagd.getSchnitzel().get(toPosition), Toast.LENGTH_SHORT).show();
                    //TODO move schnitzel from to

                }
            }
        });
        //refreshLayout.setScrollingView(dragListView);
        //refreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.cardview_light_background));
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
//            }
//        });
        dragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {
                //refreshLayout.setEnabled(true);
            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                //refreshLayout.setEnabled(true);
                if (swipedDirection == ListSwipeItem.SwipeDirection.LEFT){
                    Schnitzel schnitzel = (Schnitzel)item.getTag();
                    int pos = dragListView.getAdapter().getPositionForItem(schnitzel);
                    dragListView.getAdapter().removeItem(pos);
                }
            }

            @Override
            public void onItemSwiping(ListSwipeItem item, float swipedDistanceX) {
                super.onItemSwiping(item, swipedDistanceX);
            }
        });
        dragListView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ItemAdapter(schnitzeljagd.getSchnitzel(), R.layout.schnitzel_list_item, R.id.schnitzel_list_item_text, false);
        dragListView.setAdapter(listAdapter,false);
        dragListView.setCanDragHorizontally(false);
        dragListView.setCustomDragItem(new DragItem(getApplicationContext(), R.layout.schnitzel_list_item));
        //listAdapter.notifyDataSetChanged();
    }

    public void generateDummySchnitzel(){
        int size = 10;
        for(int i = 0; i < size ; i++){
            schnitzeljagd.addSchnitzel( new Schnitzel(i,"Dummy Schnitzel Nr.: " + i, locator.getLastLocation()));
            addIndex++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK){
                try {
                    Schnitzel schnitzel = (Schnitzel) data.getParcelableExtra("schnitzel");
                    schnitzel.setIdx(addIndex);
                    schnitzeljagd.addSchnitzel(schnitzel);
                    addIndex++;
                    listAdapter.notifyDataSetChanged();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
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
            //finishButton.setError("Mindestens 2 Schnitzel notwendig!");
        }
        else if(schnitzeljagdDescription.getText().toString().matches("")){
            resortSchnitzel();
            //TODO schnitzeljagd in DB
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
        else{
            addIndex++;
            Schnitzel schnitzel = new Schnitzel(addIndex, schnitzeljagdDescription.getText().toString(), locator.getLastLocation());
            schnitzeljagd.addSchnitzel(schnitzel);

            resortSchnitzel();
            //TODO SJ in DB

            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.schnitzel_loeschen_menu){
            //TODO EDIT INDEX FOR THE FOLLOWING SCHNITZEL
            reIndexSchnitzel(info.position);
            schnitzeljagd.getSchnitzel().remove(info.position);
            addIndex--;
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


    public void fabClicked(View view) {
        final Intent intent = new Intent(this, AddSchnitzelActivity.class);
        startActivityForResult(intent, 1);
    }
}
