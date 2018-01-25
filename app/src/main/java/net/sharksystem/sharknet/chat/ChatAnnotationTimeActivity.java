package net.sharksystem.sharknet.chat;

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
import android.widget.ImageButton;
import android.widget.TimePicker;

import net.sharksystem.sharknet.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChatAnnotationTimeActivity extends AppCompatActivity {

    private long selectedStartTime = 0;
    private long selectedEndTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_annotation_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final View dialogView = View.inflate(ChatAnnotationTimeActivity.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ChatAnnotationTimeActivity.this).create();

        ((TimePicker) dialogView.findViewById(R.id.time_picker)).setIs24HourView(true);

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
                        DateFormat formatter = new SimpleDateFormat("dd. MMMM yyyy - HH:mm");
                        String dateFormatted = formatter.format(new Date(selectedStartTime));
                        startButton.setText("Start Time:       "+String.valueOf(dateFormatted));
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
                        DateFormat formatter = new SimpleDateFormat("dd. MMMM yyyy - HH:mm");
                        String dateFormatted = formatter.format(new Date(selectedEndTime));
                        endButton.setText("End Time:       "+String.valueOf(dateFormatted));
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        final ImageButton saveButton = (ImageButton) findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
                startActivity(intent);
            }
        });

    }

}
