package net.sharksystem.sharknet.dummy;

import android.content.Context;
import android.content.res.AssetManager;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

        L.d("Contacts created");

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

        L.d("Chats created");

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

        L.d("Chats filled with messages");

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
            return;
        }

        L.d("PKI initiated");

        HashMap<Contact, KeyPair> contactKeyPairHashMap = new HashMap<>();
        for (Contact contact : contacts) {
            contactKeyPairHashMap.put(contact, keyPairGenerator.generateKeyPair());
        }

        L.d("KeyPair generated for each contact");

        ArrayList<SharkPublicKey> keys = new ArrayList<>();
        Iterator<Map.Entry<Contact, KeyPair>> iterator = contactKeyPairHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Contact, KeyPair> next = iterator.next();
            int randomDays = new Random(System.currentTimeMillis()).nextInt(100);
            int sign = (randomDays % 2)==0 ? 1 : -1;
            keys.add(pkiStorage.addUnsignedKey(next.getKey().getPST(), next.getValue().getPublic(), sign * (randomDays+1) * hour + today));
        }

        L.d("SharkPublicKeys created for each contact");

        pkiStorage.sign(keys.get(0));
        pkiStorage.sign(keys.get(1));
        pkiStorage.sign(keys.get(2));

        L.d("Some keys signed by activeProfile");

        L.d("Start random key signing between foreign keys");

        Iterator<Map.Entry<Contact, KeyPair>> iteratorAgain = contactKeyPairHashMap.entrySet().iterator();
        while (iteratorAgain.hasNext()){
            Map.Entry<Contact, KeyPair> next = iteratorAgain.next();
            for (SharkPublicKey key : keys) {
                if(!SharkCSAlgebra.identical(key.getOwner(), next.getKey().getPST())){
                    if((new Random().nextInt(100) % 2)==0){
                        pkiStorage.sign(key, next.getKey().getPST(), next.getValue().getPrivate());
                    }
                }
            }
        }
    }

    private static long getNextRandomDate(long min){
        long max = System.currentTimeMillis();
        Random random = new Random(max);
        return min + random.nextInt((int) (max - min));
    }
}