package net.sharksystem.sharknet.dummy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;

import com.thedeanda.lorem.LoremIpsum;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Contact;

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
    private final AssetManager assets;
    private String[] maleProfilePictures;
    private String[] femaleProfilePictures;

    private String[] siArray = new String[]{"http://www.facebook.de/", "http://www.google-plus.de/", "http://www.snapchat.com/", "http://www.instagram.de/", "http://www.xing.de/", "http://www.linkedin.de/"};

    public DummyContactGenerator(Context context) {
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
        String name, pictureName;
        InputStream picture = null;
        Random random = new Random(System.currentTimeMillis());

        if (isFemale) {
            name = loremIpsum.getNameFemale();
            pictureName = femaleProfilePictures[random.nextInt(femaleProfilePictures.length)];
            try {
                picture = assets.open("pictures/female/" + pictureName);
            } catch (IOException e) {
                L.d(e.getMessage(), this);
                e.printStackTrace();
            }
        } else {
            name = loremIpsum.getNameMale();
            pictureName = maleProfilePictures[random.nextInt(maleProfilePictures.length)];
            try {
                picture = assets.open("pictures/male/" + pictureName);
            } catch (IOException e) {
                L.d(e.getMessage(), this);
                e.printStackTrace();
            }
        }

        String mail = "mail@" + name.toLowerCase().replace(" ", "") + ".de";

        Contact contact = new Contact(name, mail);
        contact.setImage(BitmapFactory.decodeStream(picture));

        SharkNetApi.getInstance().addContact(contact);
        return contact;
    }

}
