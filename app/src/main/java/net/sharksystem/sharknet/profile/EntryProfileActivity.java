package net.sharksystem.sharknet.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoInterest;
import net.sharkfw.knowledgeBase.inmemory.InMemoSTSet;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Profile;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.account.AccountDetailActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationTimeActivity;
import net.sharksystem.sharknet.chat.ChatDetailActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EntryProfileActivity extends BaseActivity {

    private ImageButton saveButton;
    private EditText topicEditText;
    private EditText peerEditText;
    private EditText typeEditText;
    private EditText longitudeEditText;
    private EditText lattitudeEditText;
    private long selectedStartTime = 0;
    private long selectedEndTime = 0;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_profile);
        if (profile == null) {
            profile = getSharkApp().getProfile();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final View dialogView = View.inflate(EntryProfileActivity.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(EntryProfileActivity.this).create();

        topicEditText = (EditText) findViewById(R.id.topic);
        try {
        //if (profile != null && profile.getActiveEntryInterest() != null && profile.getActiveEntryInterest().getTopics() !=null && profile.getActiveEntryInterest().getTopics() != null && profile.getActiveEntryInterest().getTopics().tags().hasMoreElements()) {
            topicEditText.setText(profile.getActiveEntryInterest().getTopics().tags().nextElement().getSI()[0]);
        //}
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        peerEditText = (EditText) findViewById(R.id.peer);
        typeEditText = (EditText) findViewById(R.id.type);
        longitudeEditText = (EditText) findViewById(R.id.longitude);
        lattitudeEditText = (EditText) findViewById(R.id.lattitude);

        final Button startButton = (Button) findViewById(R.id.buttonStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        selectedStartTime = calendar.getTimeInMillis();
                        DateFormat formatter = new SimpleDateFormat("dd. MMM. yyyy : HH:mm");
                        String dateFormatted = formatter.format(new Date(selectedStartTime));
                        startButton.setText(String.valueOf(dateFormatted));
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        final Button endButton = (Button) findViewById(R.id.buttonEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        selectedEndTime = calendar.getTimeInMillis();
                        DateFormat formatter = new SimpleDateFormat("dd. MMM. yyyy : HH:mm");
                        String dateFormatted = formatter.format(new Date(selectedEndTime));
                        endButton.setText(String.valueOf(dateFormatted));
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


        saveButton = (ImageButton) findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountDetailActivity.class);
                Profile profile = new Profile(mApi.getAccount().getTag());
                ASIPInterest interest = null;
                STSet topics = new InMemoSTSet();
                try {
                    topics.createSemanticTag("Topic", topicEditText.getText().toString());
                    interest = new InMemoInterest(topics, null, null, null, null, null, null, ASIPSpace.DIRECTION_IN);
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                if (interest != null) {
                    profile.setActiveEntryInterest(interest);
                    mApi.updateProfile(profile);
                    Toast.makeText(getApplicationContext(), "Added the entry profile!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Could not save the Entry RadialSpotLocationProfile!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

    }


}
