package net.sharksystem.sharknet.dummy;

/**
 * Created by viktorowich on 24/08/16.
 */

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

/**
 * Created by j4rvis on 24.08.16.
 */
public class Dummy {

    public static void createDummyData() throws SharkKBException, JSONException, InterruptedException {

        String aliceName = "Alice";
        String aliceNickName = "Ali";
        String aliceSI = "www.facebook.com/alice";
        String aliceMail = "mail://alice.com";

        String bobName = "Bob";
        String bobNickName = "Bob";
        String bobSI = "www.facebook.com/bob";
        String bobMail = "mail://bob.com";

        String charlieName = "Charlie";
        String charlieNickName = "Charlie";
        String charlieSI = "www.facebook.com/charlie";
        String charlieMail = "mail://charlie.com";

        String davidName = "David";
        String davidNickName = "Dave";
        String davidSI = "www.facebook.com/david";
        String davidMail = "mail://david.com";

        String eliseName = "Elise";
        String eliseNickName = "Elli";
        String eliseSI = "www.facebook.com/elise";
        String eliseMail = "mail://elise.com";

        String frankName = "Frank";
        String frankNickName = "Frank";
        String frankSI = "www.facebook.com/frank";
        String frankMail = "mail://frank.com";

        byte[] randomByte = new byte[20];
        InputStream stream = new ByteArrayInputStream(randomByte);

        SharkNetEngine engine = SharkNetEngine.getSharkNet();

        // Create contacts

        Contact alice = engine.newContact(aliceName, aliceSI);
//        alice.setEmail(aliceMail);
        alice.setNickname(aliceNickName);
        alice.setPublicKey("2983749283490982304823094820394802398402398402938409238409238409238487239");

        Contact bob = engine.newContact(bobName, bobSI);
//        bob.setEmail(bobMail);
        bob.setNickname(bobNickName);
        bob.setPublicKey("209284092384j2j34ou23o");

        Contact charlie = engine.newContact(charlieName, charlieSI);
//        charlie.setEmail(charlieMail);
        charlie.setNickname(charlieNickName);
        charlie.setPublicKey("20928409238asdfasdf4j2j34ou23o");


        Profile david = engine.newProfile(davidName, davidSI);
//        david.setEmail(davidMail);
        david.setNickname(davidNickName);
        david.setPublicKey("209284092384jasdfasdfasdfasdf2j34ou23o");


        Profile elise = engine.newProfile(eliseName, eliseSI);
//        elise.setEmail(eliseMail);
        elise.setNickname(eliseNickName);
        elise.setPublicKey("209284092384j2j34ou23o");

        Profile frank = engine.newProfile(frankName, frankSI);
//        frank.setEmail(frankMail);
        frank.setNickname(frankNickName);
        frank.setPublicKey("20928asdfasdf4092384j2j34ou23o");

        // Set my profile as active
        engine.setActiveProfile(david, "password");

        ArrayList<Contact> aliceAndBob = new ArrayList<>();
        aliceAndBob.add(alice);
        aliceAndBob.add(bob);
        aliceAndBob.add(engine.getMyProfile());

        ArrayList<Contact> aliceAndBobAndCharlie = new ArrayList<>();
        aliceAndBobAndCharlie.add(alice);
        aliceAndBobAndCharlie.add(bob);
        aliceAndBobAndCharlie.add(charlie);
        aliceAndBobAndCharlie.add(engine.getMyProfile());

        ArrayList<Contact> aliceAndCharlie = new ArrayList<>();
        aliceAndCharlie.add(alice);
        aliceAndCharlie.add(charlie);
        aliceAndCharlie.add(engine.getMyProfile());

        ArrayList<Contact> bobAndCharlie = new ArrayList<>();
        bobAndCharlie.add(bob);
        bobAndCharlie.add(charlie);
        bobAndCharlie.add(engine.getMyProfile());

        // Chat initiation

        Chat aliceAndBobChat = engine.newChat(aliceAndBob);
        aliceAndBobChat.setTitle("Erster Chat");
        Chat aliceAndBobAndCharlieChat = engine.newChat(aliceAndBobAndCharlie);
        aliceAndBobAndCharlieChat.setTitle("Alle zusammen!");
        Chat aliceAndCharlieChat = engine.newChat(aliceAndCharlie);
        aliceAndCharlieChat.setTitle("Let's roll");
        Chat bobAndCharlieChat = engine.newChat(bobAndCharlie);
        bobAndCharlieChat.setTitle("Freitagabend");

        Lorem lorem = LoremIpsum.getInstance();

        aliceAndBobChat.sendMessage(null, lorem.getWords(3, 20), null, alice);
        aliceAndBobChat.sendMessage(null, lorem.getWords(3, 20), null, alice);
        aliceAndBobChat.sendMessage(null, lorem.getWords(3, 20), null, bob);
        aliceAndBobChat.sendMessage(null, lorem.getWords(3, 20), null);
        aliceAndBobChat.sendMessage(null, lorem.getWords(3, 20), null, alice);

        aliceAndBobAndCharlieChat.sendMessage(null, lorem.getWords(3, 20), null);
        aliceAndBobAndCharlieChat.sendMessage(null, lorem.getWords(3, 20), null, alice);
        aliceAndBobAndCharlieChat.sendMessage(null, lorem.getWords(3, 20), null, bob);
        aliceAndBobAndCharlieChat.sendMessage(null, lorem.getWords(3, 20), null, charlie);
        aliceAndBobAndCharlieChat.sendMessage(null, lorem.getWords(3, 20), null);

        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null, alice);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null, charlie);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null, alice);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null, charlie);
        aliceAndCharlieChat.sendMessage(null, lorem.getWords(3, 40), null, alice);

        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null, bob);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null, charlie);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null, bob);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null, bob);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null, charlie);
        bobAndCharlieChat.sendMessage(null, lorem.getWords(3, 10), null);
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

        long hour = 1000 * 60 * 60;
        long day = 24 * hour;
        long week = 7 * day;
        long today = System.currentTimeMillis();
        long tomorrow = today + day;
        long nextWeek = today + week;
        long yesterday = today - day;
        long previousWeek = today - week;

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
        SharkPublicKey oKey = pkiStorage.addUnsignedKey(oTag, oKeyPair.getPublic(), previousWeek);
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