package net.sharksystem.sharknet.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;

import java.io.IOException;

public class ProfileDetailActivity extends BaseActivity {

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.profile_detail_activity);
        setOptionsMenu(R.menu.profile_detail_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            profile = SharkNetEngine.getSharkNet().getMyProfile();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        EditText nickname = (EditText) findViewById(R.id.contact_nickname);
        try {
            nickname.setText(profile.getNickname());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        EditText name = (EditText) findViewById(R.id.contact_name);
        name.setText("leer");

        EditText email = (EditText) findViewById(R.id.contact_email);
        email.setText("leer");

        EditText phone = (EditText) findViewById(R.id.con_phone_edit);
        phone.setText("leer");

        EditText note = (EditText) findViewById(R.id.con_not_edit);
        note.setText("leer");
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                ImageView imageView = (ImageView) findViewById(R.id.con_profile_image);
                EditText nickname = (EditText) findViewById(R.id.con_nickname_edit);
                EditText name = (EditText) findViewById(R.id.con_name_edit);
                EditText email = (EditText) findViewById(R.id.con_email_edit);
                EditText phone = (EditText) findViewById(R.id.con_phone_edit);
                EditText note = (EditText) findViewById(R.id.con_not_edit);

                net.sharksystem.sharknet.api.ProfileActivity myprofile =  SharkNetEngine.getSharkNet.getMyProfile();
                assert nickname != null;
                //SharkNetEngine.getSharkNet.newContact(nickname.getText().toString(),"234234234","public key lkajljk234234");
                Contact mycontact = SharkNetEngine.getSharkNet.getMyProfile().getContact();
                mycontact.setNickname(nickname.getText().toString());
                //TODO: mycontact.setPicture();
                //TODO: mycontact.setUID();
                //TODO: mycontact.setPublicKey();
                myprofile.setContact(mycontact);
                finish();
            }
        });
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_save:
                ImageView imageView = (ImageView) findViewById(R.id.contact_image);
                EditText nickname = (EditText) findViewById(R.id.contact_nickname);
                EditText name = (EditText) findViewById(R.id.contact_name);
                EditText email = (EditText) findViewById(R.id.contact_email);
                EditText phone = (EditText) findViewById(R.id.con_phone_edit);
                EditText note = (EditText) findViewById(R.id.con_not_edit);

                net.sharksystem.api.interfaces.Profile myprofile = null;
                try {
                    myprofile = SharkNetEngine.getSharkNet().getMyProfile();
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                assert nickname != null;
                //SharkNetEngine.getSharkNet.newContact(nickname.getText().toString(),"234234234","public key lkajljk234234");

                try {
                    myprofile.setNickname(nickname.getText().toString());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                //TODO: mycontact.setPicture();
                //TODO: mycontact.setUID(); soll raus
                //TODO: mycontact.setPublicKey();

                finish();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void loadImage(View view) {
        int PICK_IMAGE_REQUEST = 1;

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int PICK_IMAGE_REQUEST = 1;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.contact_image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
