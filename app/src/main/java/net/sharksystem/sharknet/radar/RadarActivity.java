package net.sharksystem.sharknet.radar;

import android.os.Bundle;

import net.sharksystem.sharknet.NavigationDrawerActivity;
import net.sharksystem.sharknet.R;

public class RadarActivity extends NavigationDrawerActivity {

    public static final String CHAT_ID = "CHAT_ID";

    private RadarListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.radar_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharkNetEngine.getSharkNet().addRadarListener(this);
//        ListView listView = (ListView) findViewById(R.id.radar_list_view);
//        mListAdapter = new RadarListAdapter(this);
//        listView.setAdapter(mListAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Contact contact = mListAdapter.getItem(position);
//
//                ArrayList<Contact> recipients = new ArrayList<>();
//                recipients.add(contact);
//                try {
//                    recipients.add(SharkNetEngine.getSharkNet().getMyProfile());
//                } catch (SharkKBException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Chat chat = SharkNetEngine.getSharkNet().newChat(recipients);
//                    Intent intent = new Intent(RadarActivity.this, ChatDetailActivity.class);
//                    intent.putExtra(CHAT_ID, chat.getID());
//                    startActivity(intent);
//                } catch (SharkKBException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
