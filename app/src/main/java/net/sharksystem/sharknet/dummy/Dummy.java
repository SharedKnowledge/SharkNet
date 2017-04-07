package net.sharksystem.sharknet.dummy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
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
    private static SharkNetApi mApi;

    public static void createDummyData(Context context, SharkNetApi api) throws SharkKBException, JSONException, InterruptedException, IOException {

        ArrayList<Contact> contacts = new ArrayList<>();

        DummyContactGenerator dummyContactGenerator = new DummyContactGenerator(context, api);

        Random random = new Random(System.currentTimeMillis());

        int numberOfContacts = 8;
        for (int i = 0; i < numberOfContacts; i++){
            contacts.add(dummyContactGenerator.newContact());
        }

        mApi = api;

        api.setAccount(dummyContactGenerator.newContact());

        Contact account = api.getAccount();

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
        Chat chat1 = new Chat(account, firstChat);
        chat1.setTitle("Erster Chat");
        chat1.setImage(BitmapFactory.decodeStream(assets.open("pictures/groups/group01.jpg")));
        Chat chat2 = new Chat(account, secondChat);
        chat2.setTitle("Freitag?!");
        chat2.setImage(BitmapFactory.decodeStream(assets.open("pictures/groups/group02.jpg")));
        Chat chat3 = new Chat(account, thirdChat);
        chat3.setTitle("PewPewPew");
        chat3.setImage(BitmapFactory.decodeStream(assets.open("pictures/groups/group03.jpg")));
        Chat chat4 = new Chat(account, contacts);
        chat4.setTitle("Allesamt");
        chat4.setImage(BitmapFactory.decodeStream(assets.open("pictures/groups/group04.jpg")));
        Chat chat5 = new Chat(account, contacts.get(2));
        Chat chat6 = new Chat(account, contacts.get(5));
        Chat chat7 = new Chat(account, contacts.get(4));
        Chat chat8 = new Chat(account, contacts.get(7));

        chats.add(chat1);
        chats.add(chat2);
        chats.add(chat3);
        chats.add(chat4);
        chats.add(chat5);
        chats.add(chat6);
        chats.add(chat7);
        chats.add(chat8);

        L.d("Chats created");

        Lorem lorem = LoremIpsum.getInstance();

        for (Chat chat : chats) {
            List<Contact> contactList = chat.getContacts();
            contactList.add(chat.getOwner());
            int numberOfMessages = random.nextInt(20);
            long date = lastWeek;
            for (int i = 0; i < numberOfMessages; i++){
                Contact contact = contactList.get(random.nextInt(contactList.size()));
                date = Dummy.getNextRandomDate(date);
                Message message = new Message(contact);
                message.setContent(lorem.getWords(2, 20));
                message.setDate(new Date(date));
                chat.addMessage(message);
            }
            api.addChat(chat);
        }

        L.d("Chats filled with messages");

        SharkPkiStorage pkiStorage = (SharkPkiStorage) api.getSharkEngine().getPKIStorage();

        try {
            pkiStorage.setPkiStorageOwner(account.getTag());
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
            keys.add(pkiStorage.addUnsignedKey(next.getKey().getTag(), next.getValue().getPublic(), sign * (randomDays+1) * hour + today));
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
                if(!SharkCSAlgebra.identical(key.getOwner(), next.getKey().getTag())){
                    if((new Random().nextInt(100) % 2)==0){
                        pkiStorage.sign(key, next.getKey().getTag(), next.getValue().getPrivate());
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