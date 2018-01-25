package net.sharksystem.sharknet.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dustin Feurich on 28.09.2017.
 */

public class ChatAnnotationActivity extends BaseActivity {

    private SharkApp application;
    private EditText textSI;
    private EditText textName;
    private EditText textAddress;
    private ListView list;
    private ListView relationlist;
    private ViewFlipper vf;
    private List<String> name = new ArrayList<String>();
    private List<String> si = new ArrayList<String>();
    private List<String> address = new ArrayList<String>();
    private Map<String, HashMap<String, String>> relations = new HashMap<String, HashMap<String, String>>();
    private SharedPreferences mPrefs;
    private int purpose = -1;
    private String type = "";

    public class ChatAnnotationObject {
        public List<String> name = new ArrayList<String>();
        public List<String> si = new ArrayList<String>();
        public List<String> address = new ArrayList<String>();
        public Map<String, HashMap<String, String>> relations = new HashMap<String, HashMap<String, String>>();
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

    public class CustomRelationList extends ArrayAdapter<String> {

        private final Activity context;
        private final List<String> name;
        private final List<String> si;
        private final String semanticobject;


        public CustomRelationList(Activity context,
                          List<String> name, List<String> si, String semanticobject) {
            super(context, R.layout.single_chat_annotation_relation, si);
            this.context = context;
            this.name = name;
            this.si = si;
            this.semanticobject = semanticobject;

        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.single_chat_annotation_relation, null, true);
            TextView txtName = (TextView) rowView.findViewById(R.id.SingleChatAnnotationName);

            TextView txtSi = (TextView) rowView.findViewById(R.id.SingleChatAnnotationSubjectIdentifier);

            TextView txtRelation = (TextView) rowView.findViewById(R.id.SingleChatAnnotationRelation);
            txtName.setText(name.get(position));

            txtSi.setText(si.get(position));
            String relation = "";
            try {
                relation = relations.get(semanticobject).get(si.get(position));
            } catch (Exception e) {
            }
            txtRelation.setText(relation);
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mPrefs  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            purpose=extra.getInt("purpose");
            type=extra.getString("type");
        }
        String json = "";
        if (type.equals("topic")) {
            if (purpose == 0) json = mPrefs.getString("EntryProfileTopics", "");
            else if (purpose == 1) json = mPrefs.getString("ChatAnnotationList", "");
        }
        else if (type.equals("type")) {
            if (purpose == 0) json = mPrefs.getString("EntryProfileTypes", "");
            else if (purpose == 1) json = mPrefs.getString("ChatAnnotationTypeList", "");
        }
        if (!json.equals("")) {
            ChatAnnotationObject obj = gson.fromJson(json, ChatAnnotationObject.class);
            name=obj.name;
            si=obj.si;
            address=obj.address;
            relations=obj.relations;
        }
        setLayoutResource(R.layout.chat_annotation_viewflipper);
        vf = (ViewFlipper) findViewById(R.id.chat_annotation_viewflipper);
        textSI = (EditText) findViewById(R.id.annotation_si);
        textName = (EditText) findViewById(R.id.annotation_name);
        textAddress = (EditText) findViewById(R.id.annotation_address);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();
        if (type.equals("topic")) {
            ((TextView) findViewById(R.id.chat_annotation_title)).setText("List of Topic Annotations");
            ((EditText) findViewById(R.id.annotation_address)).setVisibility(View.GONE);
        }
        else if (type.equals("type")){
            ((TextView) findViewById(R.id.chat_annotation_title)).setText("List of Type Annotations");
            ((EditText) findViewById(R.id.annotation_address)).setVisibility(View.GONE);
        }
//        Bundle extra = getIntent().getExtras();
//        if (extra != null) {
//            textSI.setText(extra.getString(BroadcastActivity.EXTRA_MESSAGE));
//        }
        final CustomList adapter = new
                CustomList(this, name, si);
        list=(ListView)findViewById(R.id.annotation_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                vf.setDisplayedChild(1);
                final int[] chosennumber=new int[1];
                chosennumber[0] = -1;
                TextView specTitle = (TextView) findViewById(R.id.chat_annotation_specific_title);
                specTitle.setText("Si:" + si.get(position) + ", Name:" + name.get(position));
                List<String> relationnames = new ArrayList<String>(name);
                relationnames.remove(position);
                final List<String> relationsis = new ArrayList<String>(si);
                relationsis.remove(position);
                final List<String> relationaddresses = new ArrayList<String>(address);
                relationaddresses.remove(position);
                final EditText relationname = (EditText) findViewById(R.id.relation_name);
                final List<String> relationnamesfinal = relationnames;
                final List<String> relationsisfinal = relationsis;
                final List<String> relationaddressesfinal = relationaddresses;
                final CustomRelationList relationadapter = new
                        CustomRelationList(ChatAnnotationActivity.this, relationnames, relationsis, si.get(position));
                relationlist=(ListView)findViewById(R.id.annotation_connection_list);
                relationlist.setAdapter(relationadapter);
                relationlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int relationposition, long id) {
                        chosennumber[0]=relationposition;
                        ((TextView) findViewById(R.id.object_title)).setText("Si:" + relationsisfinal.get(relationposition) + ", Name:" + relationnamesfinal.get(relationposition));
                    }});
                relationlist.setLongClickable(true);
                relationlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        if (relations.get(si.get(position))==null) {
                            relations.put(si.get(position),new HashMap<String, String>());
                        }
                        relations.get(si.get(position)).remove(relationsis.get(pos));
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        ChatAnnotationObject cao = new ChatAnnotationObject();
                        cao.si=si;
                        cao.name=name;
                        cao.address=address;
                        cao.relations=relations;
                        String json = gson.toJson(cao);
                        if (type.equals("topic")) {
                            if (purpose==0) prefsEditor.putString("EntryProfileTopics", json);
                            else if (purpose==1) prefsEditor.putString("ChatAnnotationList", json);
                        }
                        else if (type.equals("type")) {
                            if (purpose==0) prefsEditor.putString("EntryProfileTypes", json);
                            else if (purpose==1) prefsEditor.putString("ChatAnnotationTypeList", json);
                        }
                        prefsEditor.commit();
                        relationadapter.notifyDataSetChanged();
                        Toast.makeText(ChatAnnotationActivity.this, "Entry deleted",
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
                final FloatingActionButton relationSaveButton = (FloatingActionButton) findViewById(R.id.imageButtonRelationSave);
                relationSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(relationname.getText().toString())&&chosennumber[0]!=-1) {
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            if (relations.get(si.get(position))==null) {
                                relations.put(si.get(position),new HashMap<String, String>());
                            }
                            relations.get(si.get(position)).put(relationsis.get(chosennumber[0]),relationname.getText().toString());
                            relationadapter.notifyDataSetChanged();
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            ChatAnnotationObject cao = new ChatAnnotationObject();
                            cao.si=si;
                            cao.name=name;
                            cao.address=address;
                            cao.relations=relations;
                            String json = gson.toJson(cao);
                            if (type.equals("topic")) {
                                if (purpose==0) prefsEditor.putString("EntryProfileTopics", json);
                                else if (purpose==1) prefsEditor.putString("ChatAnnotationList", json);
                            }
                            else if (type.equals("type")) {
                                if (purpose==0) prefsEditor.putString("EntryProfileTypes", json);
                                else if (purpose==1) prefsEditor.putString("ChatAnnotationTypeList", json);
                            }
                            prefsEditor.commit();
                            System.out.println("_________ " + textSI.getText() + " ___________BBBB");
                            //finish();
                        }
                        else {
                            vf.setDisplayedChild(0);
                        }

                    }
                });
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
                cao.address=address;
                cao.relations=relations;
                String json = gson.toJson(cao);
                if (type.equals("topic")) {
                    if (purpose==0) prefsEditor.putString("EntryProfileTopics", json);
                    else if (purpose==1) prefsEditor.putString("ChatAnnotationList", json);
                }
                else if (type.equals("type")) {
                    if (purpose==0) prefsEditor.putString("EntryProfileTypes", json);
                    else if (purpose==1) prefsEditor.putString("ChatAnnotationTypeList", json);
                }
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
                    address.add(textAddress.getText().toString());
                    adapter.notifyDataSetChanged();
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    ChatAnnotationObject cao = new ChatAnnotationObject();
                    cao.si=si;
                    cao.name=name;
                    cao.address=address;
                    cao.relations=relations;
                    String json = gson.toJson(cao);
                    if (type.equals("topic")) {
                        if (purpose==0) prefsEditor.putString("EntryProfileTopics", json);
                        else if (purpose==1) prefsEditor.putString("ChatAnnotationList", json);
                    }
                    else if (type.equals("type")) {
                        if (purpose==0) prefsEditor.putString("EntryProfileTypes", json);
                        else if (purpose==1) prefsEditor.putString("ChatAnnotationTypeList", json);
                    }
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
