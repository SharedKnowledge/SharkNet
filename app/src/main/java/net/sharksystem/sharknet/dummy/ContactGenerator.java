package net.sharksystem.sharknet.dummy;

import com.thedeanda.lorem.LoremIpsum;

import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Profile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by j4rvis on 3/7/17.
 */

public class ContactGenerator {

    private final LoremIpsum loremIpsum;
    private String[] siArray = new String[]{
            "http://www.facebook.de/",
            "http://www.google-plus.de/",
            "http://www.snapchat.com/",
            "http://www.instagram.de/",
            "http://www.xing.de/",
            "http://www.linkedin.de/"
    };

    public ContactGenerator() {
        loremIpsum = LoremIpsum.getInstance();
    }

    public Contact newContact(boolean isFemale) {
        if(isFemale){
            String firstName = loremIpsum.getFirstNameFemale();
            String lastName = loremIpsum.getLastName();
            String fullName = firstName + " " + lastName;
            String nickName = loremIpsum.getNameFemale();
            String[] sis = generateSI(fullName);


        } else {

        }
    }

    public Profile newProfile(boolean isFemale) {

    }


    private String[] generateSI(String name){
        String encode = "";
        try {
            encode = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Random random = new Random(System.currentTimeMillis());
        int length = random.nextInt(siArray.length);
        String[] sis = new String[length];

        for (int i = 0; i < length; i++){
            sis[i] = siArray[i] + encode;
        }
        return sis;
    }


}
