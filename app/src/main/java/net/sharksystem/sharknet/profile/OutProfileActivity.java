package net.sharksystem.sharknet.profile;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.broadcast.Dimension;
import net.sharkfw.knowledgeBase.broadcast.SemanticFilter;
import net.sharkfw.knowledgeBase.broadcast.SpatialFilter;
import net.sharkfw.knowledgeBase.inmemory.InMemoInterest;
import net.sharkfw.knowledgeBase.inmemory.InMemoSTSet;
import net.sharkfw.knowledgeBase.spatial.ISharkLocationProfile;
import net.sharksystem.api.models.Profile;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.account.AccountDetailActivity;
import net.sharksystem.sharknet.broadcast.BroadcastActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationActivity;
import net.sharksystem.sharknet.chat.ChatAnnotationTimeActivity;
import net.sharksystem.sharknet.data.dataprovider.SQLPolygonDataProvider;
import net.sharksystem.sharknet.location.LastLocationImpl;
import net.sharksystem.sharknet.locationprofile.PolygonLocationProfile;
import net.sharksystem.sharknet.locationprofile.SharkServiceBinder;
import net.sharksystem.sharknet.service.LocationProfilingService;
import net.sharksystem.sharknet.service.ServiceController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OutProfileActivity extends BaseActivity {

    private ImageButton saveButton;
    private EditText topicEditText;
    private EditText peerEditText;
    private EditText typeEditText;
    private EditText longitudeEditText;
    private EditText lattitudeEditText;
    private Switch activateLocationProfiling;
    private AppCompatSeekBar seekbarProfileThreshold;
    private long selectedStartTime = 0;
    private long selectedEndTime = 0;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_profile);
        if (profile == null) {
            profile = getSharkApp().getProfile();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageButton topicButton = (ImageButton) findViewById(R.id.imageButtonTopic);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",0);
                intent.putExtra("type","topic");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton typeButton = (ImageButton) findViewById(R.id.imageButtonType);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",0);
                intent.putExtra("type","type");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton approverButton = (ImageButton) findViewById(R.id.imageButtonApprover);
        approverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",0);
                intent.putExtra("type","approver");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton receiverButton = (ImageButton) findViewById(R.id.imageButtonReceiver);
        receiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",0);
                intent.putExtra("type","receiver");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton senderButton = (ImageButton) findViewById(R.id.imageButtonSender);
        senderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationActivity.class);
                intent.putExtra("purpose",0);
                intent.putExtra("type","sender");
                startActivityForResult(intent, 1);
            }
        });

        final ImageButton timeButton = (ImageButton) findViewById(R.id.imageButtonTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatAnnotationTimeActivity.class);
                intent.putExtra("purpose",0);
                startActivity(intent);
            }
        });

        final View dialogView = View.inflate(OutProfileActivity.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(OutProfileActivity.this).create();

        topicEditText = (EditText) findViewById(R.id.topic);
        try {
            topicEditText.setText(profile.getActiveOutInterest().getTopics().tags().nextElement().getSI()[0]);
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
                    interest = new InMemoInterest(topics, null, null, null, null, null, null, ASIPSpace.DIRECTION_OUT);
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                if (interest != null) {
                    profile.setActiveOutInterest(interest);
                    mApi.updateProfile(profile);
                    Toast.makeText(getApplicationContext(), "Added the out profile!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Could not save the Out RadialSpotLocationProfile!", Toast.LENGTH_LONG).show();
                }

                if (isNotDelete) {
                    if (spatialFilter == null) {
                        SharkServiceBinder exec = new ServiceController(OutProfileActivity.this, LocationProfilingService.class);
                        spatialFilter = new SpatialFilter(Dimension.SPATIAL, new PolygonLocationProfile(new SQLPolygonDataProvider(OutProfileActivity.this), new LastLocationImpl(OutProfileActivity.this), exec), ((double) seekbarProfileThreshold.getProgress()) / 100);
                        exec.start();
                        mApi.addSemanticFilter(spatialFilter);
                    }
                } else {
                    if (spatialFilter != null) {
                        ISharkLocationProfile locProfile = spatialFilter.getSharkLocationProfile();
                        if (locProfile instanceof PolygonLocationProfile) {
                            ((PolygonLocationProfile) locProfile).getSharkServiceBinder().stop();
                        }
                        mApi.removeSemanticFilter(spatialFilter);
                    }
                }
            }
        });

        activateLocationProfiling = findViewById(R.id.activateLocationProfiling);
        seekbarProfileThreshold = findViewById(R.id.seekbarProfileThreshold);
        seekbarProfileThreshold.setProgress(50);
        seekbarProfileThreshold.setEnabled(false);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), BroadcastActivity.class);
        startActivity(intent);
    }

    private SpatialFilter spatialFilter;
    private boolean isNotDelete = true;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);

        for (SemanticFilter filter : mApi.getAllSemanticFilters()) {
            if (filter instanceof SpatialFilter) {
                spatialFilter = (SpatialFilter) filter;
                activateLocationProfiling.setChecked(true);
                seekbarProfileThreshold.setEnabled(true);
                int threshold = (int) (100 * spatialFilter.getDecisionThreshold());
                ((TextView)findViewById(R.id.profileThresholdFull)).setText(threshold +"%");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    seekbarProfileThreshold.setProgress(threshold, true);
                } else {
                    seekbarProfileThreshold.setProgress(threshold);
                }
            }
        }

        activateLocationProfiling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isNotDelete = b;
                seekbarProfileThreshold.setEnabled(b);
            }
        });

        seekbarProfileThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView)findViewById(R.id.profileThresholdFull)).setText(i +"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (spatialFilter != null) {
                    spatialFilter.setDecisionThreshold(((double) seekBar.getProgress()) / 100);
                }
            }
        });
    }
}
