package net.sharksystem.sharknet.dummy;

/**
 * Created by viktorowich on 24/08/16.
 */

import android.content.Context;
import android.content.res.AssetManager;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Profile;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by j4rvis on 24.08.16.
 */
public class Dummy {

    static long hour = 1000 * 60 * 60;
    static long day = 24 * hour;
    static long week = 7 * day;
    static long today = System.currentTimeMillis();
    static long tomorrow = today + day;
    static long nextWeek = today + week;
    static long yesterday = today - day;
    static long lastWeek = today - week;

    public static void createDummyData(Context context) throws SharkKBException, JSONException, InterruptedException, IOException {

        ArrayList<Contact> contacts = new ArrayList<>();
        ArrayList<Profile> profiles = new ArrayList<>();

        DummyContactGenerator dummyContactGenerator = new DummyContactGenerator(context);

        Random random = new Random(System.currentTimeMillis());

        int numberOfContacts = 8;
        for (int i = 0; i < numberOfContacts; i++){
            contacts.add(dummyContactGenerator.newContact());
        }

        profiles.add(dummyContactGenerator.newProfile(true));
        profiles.add(dummyContactGenerator.newProfile(false));

        SharkNetEngine engine = SharkNetEngine.getSharkNet();

        // Set male profile as active
        engine.setActiveProfile(profiles.get(1), "password");
        Profile activeProfile = engine.getMyProfile();

        // TODO Now generate dummy chats...

        ArrayList<Chat> chats = new ArrayList<>();

        ArrayList<Contact> firstChat = new ArrayList<>();
        firstChat.add(contacts.get(0));
        firstChat.add(contacts.get(1));
        firstChat.add(contacts.get(2));
        ArrayList<Contact> secondChat = new ArrayList<>();
        secondChat.add(contacts.get(0));
        secondChat.add(contacts.get(1));
        secondChat.add(contacts.get(2));
        secondChat.add(contacts.get(3));
        secondChat.add(contacts.get(4));
        ArrayList<Contact> thirdChat = new ArrayList<>();
        thirdChat.add(contacts.get(0));
        thirdChat.add(contacts.get(1));
        thirdChat.add(contacts.get(2));
        thirdChat.add(contacts.get(3));

        AssetManager assets = context.getResources().getAssets();
        chats.add(engine.newChat(firstChat));
        chats.get(chats.size()-1).setTitle("Erster Chat");
        chats.get(chats.size()-1).setPicture(assets.open("pictures/groups/group01.jpg"), "image/jpg");
        chats.add(engine.newChat(secondChat));
        chats.get(chats.size()-1).setTitle("Was machen wir am Freitag?");
        chats.get(chats.size()-1).setPicture(assets.open("pictures/groups/group02.jpg"), "image/jpg");
        chats.add(engine.newChat(thirdChat));
        chats.get(chats.size()-1).setTitle("PewPew");
        chats.add(engine.newChat(new ArrayList<>(contacts)));
        chats.get(chats.size()-1).setTitle("Alle zusammen");
        chats.get(chats.size()-1).setPicture(assets.open("pictures/groups/group04.jpg"), "image/jpg");
        chats.add(engine.newChat(contacts.get(5)));
        chats.add(engine.newChat(contacts.get(2)));
        chats.add(engine.newChat(contacts.get(6)));
        chats.add(engine.newChat(contacts.get(7)));
        chats.add(engine.newChat(contacts.get(1)));

        Lorem lorem = LoremIpsum.getInstance();

        for (Chat chat : chats) {
            List<Contact> contactList = chat.getContacts();
            int numberOfMessages = random.nextInt(20);
            long date = lastWeek;
            for (int i = 0; i < numberOfMessages; i++){
                Contact contact = contactList.get(random.nextInt(contactList.size()));
                date = Dummy.getNextRandomDate(date);
                if(contact.equals(activeProfile)){
                    chat.sendMessage(null, lorem.getWords(2, 20), null, date);
                } else {
                    chat.sendMessage(null, lorem.getWords(2, 20), null, contact, date);
                }
            }
        }
    }

    private static long getNextRandomDate(long min){
        long max = System.currentTimeMillis();
        Random random = new Random(max);
        return min + random.nextInt((int) (max - min));
    }

    public static void createDummyPkiData() {

        SharkPkiStorage pkiStorage = (SharkPkiStorage) SharkNetEngine.getSharkNet().getSharkEngine().getPKIStorage();

        SharkNetEngine sharkNetEngine = SharkNetEngine.getSharkNet();
        try {
            pkiStorage.setPkiStorageOwner(sharkNetEngine.getMyProfile().getPST());
            pkiStorage.generateNewKeyPair(1000*60*60*24*365);
        } catch (SharkKBException | NoSuchAlgorithmException | IOException e) {
            L.e(e.getMessage());
        }

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        L.d("KeyGenerator initiated.", Dummy.class.toString());

        String kName = "Karl";
        String kSI = "st:k";
        String kAddr = "tcp://shark.net/k";
        PeerSemanticTag kTag = InMemoSharkKB.createInMemoPeerSemanticTag(kName, kSI, kAddr);
        KeyPair kKeyPair = keyPairGenerator.generateKeyPair();

        String lName = "Louis";
        String lSI = "st:l";
        String lAddr = "tcp://sharl.net/l";
        PeerSemanticTag lTag = InMemoSharkKB.createInMemoPeerSemanticTag(lName, lSI, lAddr);
        KeyPair lKeyPair = keyPairGenerator.generateKeyPair();

        String mName = "Marc";
        String mSI = "st:m";
        String mAddr = "tcp://sharm.net/m";
        PeerSemanticTag mTag = InMemoSharkKB.createInMemoPeerSemanticTag(mName, mSI, mAddr);
        KeyPair mKeyPair = keyPairGenerator.generateKeyPair();

        String nName = "Ned";
        String nSI = "st:n";
        String nAddr = "tcp://sharn.net/n";
        PeerSemanticTag nTag = InMemoSharkKB.createInMemoPeerSemanticTag(nName, nSI, nAddr);
        KeyPair nKeyPair = keyPairGenerator.generateKeyPair();

        String oName = "Olaf";
        String oSI = "st:o";
        String oAddr = "tcp://sharo.net/o";
        PeerSemanticTag oTag = InMemoSharkKB.createInMemoPeerSemanticTag(oName, oSI, oAddr);
        KeyPair oKeyPair = keyPairGenerator.generateKeyPair();

        L.d("Keys generated", Dummy.class.toString());

        SharkPublicKey kKey = pkiStorage.addUnsignedKey(kTag, kKeyPair.getPublic(), tomorrow);
        SharkPublicKey lKey = pkiStorage.addUnsignedKey(lTag, lKeyPair.getPublic(), tomorrow + hour);
        SharkPublicKey mKey = pkiStorage.addUnsignedKey(mTag, mKeyPair.getPublic(), yesterday);
        SharkPublicKey nKey = pkiStorage.addUnsignedKey(nTag, nKeyPair.getPublic(), nextWeek);
        SharkPublicKey oKey = pkiStorage.addUnsignedKey(oTag, oKeyPair.getPublic(), lastWeek);
        L.d("Keys added", Dummy.class.toString());
        try {
            // Signed by myself
            pkiStorage.sign(kKey);
            pkiStorage.sign(lKey);
            pkiStorage.sign(nKey);

            // Signed by others
            pkiStorage.sign(lKey, kTag, kKeyPair.getPrivate());
            pkiStorage.sign(mKey, kTag, kKeyPair.getPrivate());
            pkiStorage.sign(oKey, kTag, kKeyPair.getPrivate());

            pkiStorage.sign(mKey, lTag, lKeyPair.getPrivate());
            pkiStorage.sign(nKey, lTag, lKeyPair.getPrivate());
            pkiStorage.sign(oKey, lTag, lKeyPair.getPrivate());

            pkiStorage.sign(kKey, mTag, mKeyPair.getPrivate());
            pkiStorage.sign(nKey, mTag, mKeyPair.getPrivate());
            pkiStorage.sign(oKey, mTag, mKeyPair.getPrivate());
            pkiStorage.sign(lKey, mTag, mKeyPair.getPrivate());

            pkiStorage.sign(kKey, nTag, nKeyPair.getPrivate());
            pkiStorage.sign(lKey, nTag, nKeyPair.getPrivate());
            pkiStorage.sign(oKey, nTag, nKeyPair.getPrivate());

            pkiStorage.sign(kKey, oTag, oKeyPair.getPrivate());
            pkiStorage.sign(nKey, oTag, oKeyPair.getPrivate());

        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        L.d("Keys signed", Dummy.class.toString());

    }
}