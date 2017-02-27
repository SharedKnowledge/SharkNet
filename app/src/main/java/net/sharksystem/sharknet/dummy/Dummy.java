package net.sharksystem.sharknet.dummy;

/**
 * Created by viktorowich on 24/08/16.
 */

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
        String aliceTelephoneHome = "030 123456789";
        String aliceTelephoneMobile = "01177 123456789";
        String aliceNote = "This is just a simple note";

        String bobName = "Bob";
        String bobNickName = "Bob";
        String bobSI = "www.facebook.com/bob";
        String bobMail = "mail://bob.com";
        String bobTelephoneHome = "030 1234567891";
        String bobTelephoneMobile = "01177 1234567891";
        String bobNote = "This is just a simple note from Bob";

        String charlieName = "Charlie";
        String charlieNickName = "Charlie";
        String charlieSI = "www.facebook.com/charlie";
        String charlieMail = "mail://charlie.com";
        String charlieTelephoneHome = "030 1234567891";
        String charlieTelephoneMobile = "01177 1234567891";
        String charlieNote = "This is just a simple note from Charlie";

        String davidName = "David";
        String davidNickName = "Dave";
        String davidSI = "www.facebook.com/david";
        String davidMail = "mail://david.com";
        String davidTelephoneHome = "030 1234567891";
        String davidTelephoneMobile = "01177 1234567891";
        String davidNote = "This is just a simple note from David";

        String eliseName = "Elise";
        String eliseNickName = "Elli";
        String eliseSI = "www.facebook.com/elise";
        String eliseMail = "mail://elise.com";
        String eliseTelephoneHome = "030 1234567891";
        String eliseTelephoneMobile = "01177 1234567891";
        String eliseNote = "This is just a simple note from Elise";

        String frankName = "Frank";
        String frankNickName = "Frank";
        String frankSI = "www.facebook.com/frank";
        String frankMail = "mail://frank.com";
        String frankTelephoneHome = "030 1234567891";
        String frankTelephoneMobile = "01177 1234567891";
        String frankNote = "This is just a simple note from Frank";

        byte[] randomByte = new byte[20];
        InputStream stream = new ByteArrayInputStream(randomByte);

        SharkNetEngine engine = SharkNetEngine.getSharkNet();

        // Create contacts

        Contact alice = engine.newContact(aliceName, aliceSI);
//        alice.setEmail(aliceMail);
        alice.setNickname(aliceNickName);
        alice.addTelephoneNumber(aliceTelephoneHome);
        alice.addTelephoneNumber(aliceTelephoneMobile);
        alice.addNote(aliceNote);
        alice.setPublicKey("2983749283490982304823094820394802398402398402938409238409238409238487239");

        Contact bob = engine.newContact(bobName, bobSI);
//        bob.setEmail(bobMail);
        bob.setNickname(bobNickName);
        bob.addTelephoneNumber(bobTelephoneHome);
        bob.addTelephoneNumber(bobTelephoneMobile);
        bob.addNote(bobNote);
        bob.setPublicKey("209284092384j2j34ou23o");

        Contact charlie = engine.newContact(charlieName, charlieSI);
//        charlie.setEmail(charlieMail);
        charlie.setNickname(charlieNickName);
        charlie.addTelephoneNumber(charlieTelephoneHome);
        charlie.addTelephoneNumber(charlieTelephoneMobile);
        charlie.addNote(charlieNote);
        charlie.setPublicKey("20928409238asdfasdf4j2j34ou23o");


        Profile david = engine.newProfile(davidName, davidSI);
//        david.setEmail(davidMail);
        david.setNickname(davidNickName);
        david.addTelephoneNumber(davidTelephoneHome);
        david.addTelephoneNumber(davidTelephoneMobile);
        david.addNote(davidNote);
        david.setPublicKey("209284092384jasdfasdfasdfasdf2j34ou23o");


        Profile elise = engine.newProfile(eliseName, eliseSI);
//        elise.setEmail(eliseMail);
        elise.setNickname(eliseNickName);
        elise.addTelephoneNumber(eliseTelephoneHome);
        elise.addTelephoneNumber(eliseTelephoneMobile);
        elise.addNote(eliseNote);
        elise.setPublicKey("209284092384j2j34ou23o");


        Profile frank = engine.newProfile(frankName, frankSI);
//        frank.setEmail(frankMail);
        frank.setNickname(frankNickName);
        frank.addTelephoneNumber(frankTelephoneHome);
        frank.addTelephoneNumber(frankTelephoneMobile);
        frank.addNote(frankNote);
        frank.setPublicKey("20928asdfasdf4092384j2j34ou23o");


        engine.setActiveProfile(david, "password");

        ArrayList<Contact> aliceAndBob = new ArrayList<>();
        aliceAndBob.add(alice);
        aliceAndBob.add(bob);

        ArrayList<Contact> aliceAndBobAndCharlie = new ArrayList<>();
        aliceAndBobAndCharlie.add(alice);
        aliceAndBobAndCharlie.add(bob);
        aliceAndBobAndCharlie.add(charlie);

        ArrayList<Contact> aliceAndCharlie = new ArrayList<>();
        aliceAndCharlie.add(alice);
        aliceAndCharlie.add(charlie);

        ArrayList<Contact> bobAndCharlie = new ArrayList<>();
        bobAndCharlie.add(bob);
        bobAndCharlie.add(charlie);

        // ChatActivity initiation

        Chat aliceAndBobChat = engine.newChat(aliceAndBob);
        aliceAndBobChat.setTitle("ABD");

        Thread.sleep(10);
        Chat aliceAndBobAndCharlieChat = engine.newChat(aliceAndBobAndCharlie);
        aliceAndBobAndCharlieChat.setTitle("ABCD");
        Thread.sleep(10);
        Chat aliceAndCharlieChat = engine.newChat(aliceAndCharlie);
        aliceAndCharlieChat.setTitle("ACD");
        Thread.sleep(10);
        Chat bobAndCharlieChat = engine.newChat(bobAndCharlie);
        bobAndCharlieChat.setTitle("BCD");

        aliceAndBobChat.sendMessage(null, "Hallo Bob und David", null, alice);
        aliceAndBobChat.sendMessage(null, "Wie geht es euch beiden?", null, alice);
        aliceAndBobChat.sendMessage(null, "Hi mir geht es gut und dir?", null, bob);
        aliceAndBobChat.sendMessage(null, "Mir geht es auch gut! Was macht ihr so?", null);
        aliceAndBobChat.sendMessage(null, "Bestens ja! Ich bin gerade am SharkNet testen :D", null, alice);

        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, alice);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, bob);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, charlie);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null);

        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, charlie);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, charlie);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);

        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor consetetur sadipscing elitr", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, charlie);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, charlie);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
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