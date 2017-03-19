package net.sharksystem.sharknet.chat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.sharknet.ParentActivity;
import net.sharksystem.sharknet.R;

import java.util.List;

public class ChatSettingsActivity extends ParentActivity {

    private List<Contact> contacts;
    private ListView lv;
    private EditText editText;
    private ImageView imageView;
    private String title;
    private Bitmap bitmap;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.chat_new_activity);
        setOptionsMenu(R.menu.chat_settings_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chat = getSharkApp().getChat();

        try {
            contacts = chat.getContacts();

            ChatNewConListAdapter chatListAdapter = new ChatNewConListAdapter(this, R.layout.chat_settings_contact_line_item, contacts);
            lv = (ListView) findViewById(R.id.con_list_view);
            lv.setAdapter(chatListAdapter);

            editText = (EditText) findViewById(R.id.chat_new_title);
            imageView = (ImageView) findViewById(R.id.chat_new_image);

            title = chat.getTitle();
            editText.setText(title);

            bitmap = BitmapFactory.decodeStream(chat.getPicture().getInputStream());
            imageView.setImageBitmap(bitmap);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                try {

                    String s = editText.getText().toString();
                    if (!s.isEmpty() && !s.equals(title)) {
                        chat.setTitle(s);
                    }
                    // TODO Check if bitmap has changed and update the bitmap

                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                getSharkApp().setChat(chat);
                startActivity(new Intent(ChatSettingsActivity.this, ChatDetailActivity.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }


    }

}
