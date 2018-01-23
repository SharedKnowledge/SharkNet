package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import net.sharksystem.api.models.Message;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.SharkApp;
import net.sharksystem.sharknet.broadcast.BroadcastActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dustin Feurich on 28.09.2017.
 */

public class ChatAnnotationActivity extends BaseActivity {

    private SharkApp application;
    private EditText textSI;
    private EditText textName;
    private ListView list;
    private ViewFlipper vf;
    private List<String> name = new ArrayList<String>();
    private List<String> si = new ArrayList<String>();
    private SharedPreferences mPrefs;

    public class ChatAnnotationObject {
        public List<String> name = new ArrayList<String>();
        public List<String> si = new ArrayList<String>();
    }

    public class CustomList extends ArrayAdapter<String> {

        private final Activity context;
        private final List<String> name;
        private final List<String> si;
        public CustomList(Activity context,
                          List<String> name, List<String> si) {
            super(context, R.layout.single_chat_annotation, si);
            this.context = context;
            this.name = name;
            this.si = si;

        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.single_chat_annotation, null, true);
            TextView txtName = (TextView) rowView.findViewById(R.id.SingleChatAnnotationName);

            TextView txtSi = (TextView) rowView.findViewById(R.id.SingleChatAnnotationSubjectIdentifier);
            txtName.setText(name.get(position));

            txtSi.setText(si.get(position));
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mPrefs  = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("ChatAnnotationList", "");
        if (!json.equals("")) {
            ChatAnnotationObject obj = gson.fromJson(json, ChatAnnotationObject.class);
            name=obj.name;
            si=obj.si;
        }
        setLayoutResource(R.layout.chat_annotation_viewflipper);
        vf = (ViewFlipper) findViewById(R.id.chat_annotation_viewflipper);
        textSI = (EditText) findViewById(R.id.annotation_si);
        textName = (EditText) findViewById(R.id.annotation_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            textSI.setText(extra.getString(BroadcastActivity.EXTRA_MESSAGE));
        }
        final CustomList adapter = new
                CustomList(this, name, si);
        list=(ListView)findViewById(R.id.annotation_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                vf.setDisplayedChild(1);
                TextView specTitle = (TextView) findViewById(R.id.chat_annotation_specific_title);
                specTitle.setText("Si:" + si.get(position) + ", Name:" + name.get(position));
            }
        });
        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                name.remove(pos);
                si.remove(pos);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                ChatAnnotationObject cao = new ChatAnnotationObject();
                cao.si=si;
                cao.name=name;
                String json = gson.toJson(cao);
                prefsEditor.putString("ChatAnnotationList", json);
                prefsEditor.commit();
                adapter.notifyDataSetChanged();
                Toast.makeText(ChatAnnotationActivity.this, "Entry deleted",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
        final FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                if (!TextUtils.isEmpty(textSI.getText().toString())) {
                    if (si.contains(textSI.getText().toString())) return;
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    si.add(textSI.getText().toString());
                    name.add(textName.getText().toString());
                    adapter.notifyDataSetChanged();
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    ChatAnnotationObject cao = new ChatAnnotationObject();
                    cao.si=si;
                    cao.name=name;
                    String json = gson.toJson(cao);
                    prefsEditor.putString("ChatAnnotationList", json);
                    prefsEditor.commit();
                    returnIntent.putExtra("result", textSI.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    System.out.println("_________ " + textSI.getText() + " ___________BBBB");
                    //finish();
                }
                else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }

            }
        });

        //final TextView textView = (TextView) findViewById(R.id.text_view_top);
        //textView.setText(intent.getStringExtra(ChatDetailActivity.EXTRA_MESSAGE));
    }
}
