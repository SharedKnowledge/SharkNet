package net.sharksystem.sharknet.dummy;

import android.content.Context;
import android.content.res.AssetManager;

import com.thedeanda.lorem.LoremIpsum;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by j4rvis on 3/7/17.
 */

public class DummyContactGenerator {

    private final LoremIpsum loremIpsum;
    private final SharkNetEngine sharkNet;
    private final AssetManager assets;
    private String[] maleProfilePictures;
    private String[] femaleProfilePictures;

    private String[] siArray = new String[]{"http://www.facebook.de/", "http://www.google-plus.de/", "http://www.snapchat.com/", "http://www.instagram.de/", "http://www.xing.de/", "http://www.linkedin.de/"};

    public DummyContactGenerator(Context context) {
        sharkNet = SharkNetEngine.getSharkNet();
        loremIpsum = LoremIpsum.getInstance();
        assets = context.getResources().getAssets();
        try {
            femaleProfilePictures = assets.list("pictures/female");
            maleProfilePictures = assets.list("pictures/male");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Contact newContact(){
        return newContact(new Random(System.currentTimeMillis()).nextInt(100) % 2 == 0);
    }

    public Contact newContact(boolean isFemale) {
        Contact contact = null;
        String firstName, lastName, nickName, pictureName;
        InputStream picture = null;
        Random random = new Random(System.currentTimeMillis());

        if (isFemale) {
            firstName = loremIpsum.getFirstNameFemale();
            lastName = loremIpsum.getLastName();
            nickName = loremIpsum.getNameFemale();
            pictureName = femaleProfilePictures[random.nextInt(femaleProfilePictures.length)];
            try {
                picture = assets.open("pictures/female/" + pictureName);
            } catch (IOException e) {
                L.d(e.getMessage(), this);
                e.printStackTrace();
            }
        } else {
            firstName = loremIpsum.getFirstNameMale();
            lastName = loremIpsum.getLastName();
            nickName = loremIpsum.getNameMale();
            pictureName = maleProfilePictures[random.nextInt(maleProfilePictures.length)];
            try {
                picture = assets.open("pictures/male/" + pictureName);
            } catch (IOException e) {
                L.d(e.getMessage(), this);
                e.printStackTrace();
            }
        }

        String fullName = firstName + " " + lastName;
        String[] sis = generateSI(fullName);
        String[] addresses = new String[random.nextInt(3) + 1];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = "tcp://" + "0.0.0.0";
        }
        try {
            contact = sharkNet.newContact(InMemoSharkKB.createInMemoPeerSemanticTag(fullName, sis, addresses));
            contact.setName(fullName);
            contact.setNickname(nickName);
            if(picture!=null){
                contact.setPicture(picture, pictureName, "jpg");
            } else {
                L.d("Picture is null!", this);
            }
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
        return contact;
    }

    public Profile newProfile(boolean isFemale) {
        Profile profile = null;
        String firstName, lastName, nickName, pictureName;
        InputStream picture = null;
        Random random = new Random(System.currentTimeMillis());

        if (isFemale) {
            firstName = loremIpsum.getFirstNameFemale();
            lastName = loremIpsum.getLastName();
            nickName = loremIpsum.getNameFemale();
            pictureName = femaleProfilePictures[random.nextInt(femaleProfilePictures.length)];
            try {
                picture = assets.open("pictures/female/" + pictureName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            firstName = loremIpsum.getFirstNameMale();
            lastName = loremIpsum.getLastName();
            nickName = loremIpsum.getNameMale();
            pictureName = maleProfilePictures[random.nextInt(maleProfilePictures.length)];
            try {
                picture = assets.open("pictures/male/" + pictureName);
            } catch (IOException e) {
                L.d(e.getMessage(), this);
                e.printStackTrace();
            }
        }

        String fullName = firstName + " " + lastName;
        String[] sis = generateSI(fullName);
        String[] addresses = new String[random.nextInt(3) + 1];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = loremIpsum.getEmail();
        }
        try {
            profile = sharkNet.newProfile(InMemoSharkKB.createInMemoPeerSemanticTag(fullName, sis, addresses));
            profile.setName(fullName);
            profile.setNickname(nickName);
            if(picture!=null){
                profile.setPicture(picture, pictureName, "jpg");
            } else {
                L.d("Picture is null!", this);
            }
            profile.setPicture(picture, pictureName, "jpg");
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
        return profile;
    }

    private String[] generateSI(String name) {
        String encode = "";
        try {
            encode = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Random random = new Random(System.currentTimeMillis());
        int length = random.nextInt(siArray.length) + 1;
        String[] sis = new String[length];

        for (int i = 0; i < length; i++) {
            sis[i] = siArray[i] + encode;
        }
        return sis;
    }
}
