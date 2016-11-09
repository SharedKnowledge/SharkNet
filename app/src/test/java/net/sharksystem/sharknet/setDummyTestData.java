package net.sharksystem.sharknet;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
//import net.sharksystem.api.dummy_impl.*;
import net.sharksystem.api.interfaces.*;
import net.sharksystem.api.interfaces.Chat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Created by viktorowich on 13/08/16.
 */
public class setDummyTestData
{
/*
    @Test
    public void fillWithDummyData() throws  SharkKBException
    {
        ImplSharkNet s = new ImplSharkNet();

        //Timestamps erzeugen um Messages und Feeds mit von der aktuellen Uhrzeit abweichend zu erzeugen
        java.util.Date fiveMinAgo = new Date(System.currentTimeMillis()-5*60*1000);
        Timestamp time5ago = new Timestamp(fiveMinAgo.getTime());

        java.util.Date fiveMinAfter = new Date(System.currentTimeMillis()+5*60*1000);
        Timestamp time5after = new Timestamp(fiveMinAfter.getTime());

        java.util.Date sevenMinAgo = new Date(System.currentTimeMillis()-100*60*1000);
        Timestamp time7ago = new Timestamp(fiveMinAgo.getTime());

        java.util.Date sevenMinAfter = new Date(System.currentTimeMillis()+100*60*1000);
        Timestamp time7after = new Timestamp(fiveMinAfter.getTime());

        java.util.Date oneDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000);
        Timestamp timeOneDayAgo = new Timestamp(oneDayAgo.getTime());

        java.util.Date twoDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000*2);
        Timestamp timeTwoDayAgo = new Timestamp(twoDayAgo.getTime());

        java.util.Date now = new Date(System.currentTimeMillis());
        Timestamp timenow = new Timestamp(now.getTime());

        //Anlegen von Profilen
        net.sharksystem.api.interfaces.ProfileActivity bob_p = s.newProfile("bob", "bobsDevice");
        assertEquals(1,s.getProfiles().size());
        net.sharksystem.api.interfaces.ProfileActivity alice_p  = s.newProfile("alice", "alicesDevice");
        assertEquals(2,s.getProfiles().size());

        //Aktives Profil ist Alice
       // s.setProfile(alice_p, "");
        s.setActiveProfile(alice_p, "");
        assertEquals("alice",s.getMyProfile().getNickname());

        //Kontakte von Alice und Bob laden
        Contact alice = alice_p;
        Contact bob = bob_p;

        //Kontakte von Alice setzen
        Contact alice_bob = s.newContact(bob.getNickname(), bob.getUID(), bob.getPublicKey());
        assertNotEquals(0,s.getContacts().size());
        Contact alice_charles = s.newContact("charles", "charlesuid", "charlespublickey");
        Contact alice_dean = s.newContact("dean", "deanuid", "deanpublickey");
        Contact alice_erica = s.newContact("erica", "ericauid", "ericapublickey");
        Contact alice_frank = s.newContact("frank", "frankuid", "frankpublickey");

        //Letzter Wifi-Kontakt mit Charles setzen
        alice_charles.setLastWifiContact(timenow);


        //Anlegen von Chats
        //1. Kontaktlisten anlegen
        List<Contact> grouprecipients = new ArrayList<>();
        grouprecipients.add(bob);
        grouprecipients.add(alice_charles);
        grouprecipients.add(alice_dean);
        grouprecipients.add(alice_erica);
        grouprecipients.add(alice_frank);
        List<Contact> charlesrecipient = new ArrayList<>();
        charlesrecipient.add(alice_charles);
        List<Contact> bobandcharlesrecipients = new ArrayList<>();
        bobandcharlesrecipients.add(bob);
        bobandcharlesrecipients.add(alice_charles);

        //2.Chats mit Kontaktlisten anelgen
        net.sharksystem.api.interfaces.ChatActivity chatgroup = s.newChat(grouprecipients);
        net.sharksystem.api.interfaces.ChatActivity chatcharles  = s.newChat(charlesrecipient);
        net.sharksystem.api.interfaces.ChatActivity chatbobandcharles  = s.newChat(bobandcharlesrecipients);

        //3. Senden von Nachrichten
        chatgroup.sendMessage(new ImplContent("Hallo zusammen", alice_p));
        chatgroup.sendMessage(new ImplContent("Eine tolle Idee", alice_p));
        chatgroup.sendMessage(new ImplContent("Ich erstelle ein Umfage wann der beste Termin ist", alice_p));

        //4. Nachrichten hinzufügen die Alice bekommen hat
        Message m1 = new ImplMessage(new ImplContent("Wir wollen eine WG-Party veranstalten", alice_p), time5ago, bob, s.getMyProfile(), grouprecipients, false, false);
        DummyDB.getInstance().addMessage(m1, chatgroup);
        Message m2 = new ImplMessage(new ImplContent("Willkommen in unserer Gruppe", alice_p), timeOneDayAgo, bob, s.getMyProfile(), grouprecipients, false, false);
        DummyDB.getInstance().addMessage(m2, chatgroup);
        Message m3 = new ImplMessage(new ImplContent("Hallo zusammen", alice_p), timeTwoDayAgo, bob, s.getMyProfile(), grouprecipients, false, false);
        DummyDB.getInstance().addMessage(m3, chatgroup);

        // vote
        Content content = new ImplContent("", alice_p);
        Voting votedummy = content.addVoting("Wann soll die nächste WG-Party stattfinden?", true);
        List<String> answersdummy = Arrays.asList(
                "Montag",
                "Dienstag",
                "Mittwoch",
                "Donnerstag",
                "Freitag",
                "Samstag",
                "Sonntag"
        );
        votedummy.addAnswers(answersdummy);

        HashMap<String, Contact> answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(4), alice_p);
        votedummy.vote(answersmap);
        answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(5), alice_dean);
        votedummy.vote(answersmap);
        answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(6), alice_frank);
        votedummy.vote(answersmap);

        answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(4), alice_bob);
        votedummy.vote(answersmap);
        answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(5), alice_erica);
        votedummy.vote(answersmap);

        answersmap = new HashMap<>();
        answersmap.put(answersdummy.get(4), alice_charles);
        votedummy.vote(answersmap);

        chatgroup.sendMessage(content);

        //Senden von Nachrichten aus dem ChatActivity 2 und 3
        chatcharles.sendMessage(new ImplContent("Hallo Charles", alice_p));
        chatcharles.sendMessage(new ImplContent("Wie geht es dir?", alice_p));
        chatcharles.sendMessage(new ImplContent("Es freut mich das du auch bei SharkNet bist", alice_p));
        chatbobandcharles.sendMessage(new ImplContent("Treffen wir uns in der Mensa?", alice_p));


        //Setzen von Encryted und Signiert usw auf True
        List<Message> chat1_m = chatgroup.getMessages(true);
        for(Message m : chat1_m){
            m.setEncrypted(true);
            m.setSigned(true);
            m.setDirectReceived(true);
            m.setVerified(true);
        }



        // Interessen anlegen, Fussball wird unter Sport eingeordnet
        Interest i1 = new ImplInterest(alice);
        TXSemanticTag si1 = i1.addInterest("sport", "https://de.wikipedia.org/wiki/Sport");
        TXSemanticTag si2 = i1.addInterest("fußball", "https://de.wikipedia.org/wiki/Fußball");
        i1.moveInterest(si1, si2);

        //Bob hat nur das interesse shark
        Interest i2 = new ImplInterest(bob);
        TXSemanticTag si3 = i2.addInterest("shark", "www.sharknet.de");

//		bob.getInterests().addInterest(si1);
//		bob.getInterests().addInterest(si2);
//		bob.getInterests().addInterest(si3);


        //Feeds anlegen
        Feed f1 = s.newFeed(new ImplContent("this is the fist feed of sharkNet", alice_p), i2, bob);
        Feed f2 = s.newFeed(new ImplContent("i <3 football", alice_p), i1, alice);
        Feed f3 = s.newFeed(new ImplContent("portugal is european champion", alice_p), i1, alice);

        //Comments an die Fees anhängen
        f1.newComment(new ImplContent("this is amazing", alice_p), alice);
        f1.newComment(new ImplContent("i know", alice_p), bob);

        // Dislike eines Comments
        f1.getComments(true).get(0).setDisliked(true);

        //Blacklist anlegen, Alice hat ihren Exfreund auf der Liste
        alice_p.getBlacklist().add(new ImplContact("alice exboyfriend", "hotboy@elitepartner.com", "", alice_p));


//Bobs stuff
        s.setProfile(bob_p, "");
        Contact peter = s.newContact("peter", "dagobert@entenhausen.de", "foo");
        List<Contact> recipients = new LinkedList<>();
        recipients.add(peter);
        ChatActivity bob_peter = s.newChat(recipients);
        Message m_peter_bob = new ImplMessage(new ImplContent("hallo bob", bob_p), time5ago, peter, s.getMyProfile(), grouprecipients, false, false);
        DummyDB.getInstance().addMessage(m_peter_bob, bob_peter);
        bob_peter.sendMessage(new ImplContent("hallo peter", bob_p));


        Feed bob_feed1 = s.newFeed(new ImplContent("bob thinks shark net is amazing", bob_p), i2, bob);

        bob_feed1.newComment(new ImplContent("Peter thinks so too", bob_p), peter);
        s.getFeeds(5, 10, true);

        bob_p.getBlacklist().add(new ImplContact("bad hacker", "bad@hacker.com", "", bob_p));



        time7ago = Timestamp.valueOf("2012-04-06 09:01:10");
        time7after = Timestamp.valueOf("2017-04-06 09:01:10");


        //Generate a Content and add a Voting
        Content c_test = new ImplContent("foo", bob_p);
        ImplVoting vote = c_test.addVoting("what is foo", false);

        //Generate a list of Answers
        List<String> testanswers = new LinkedList<>();
        testanswers.add("fooans1");
        testanswers.add("fooans2");
        testanswers.add("fooans3");

        //Add the Answers to the voting
        vote.addAnswers(testanswers);


        //Get a HashMap to add the contact to the voting
        HashMap<String, Contact> answers = vote.getAnswers();
        answers.put(testanswers.get(2), alice);

        //Return the Hasmap with the Voting
        vote.vote(answers);

        //Get a Hash Map with Answers an List of Content to See the Voting
        HashMap<String, List<Contact>> voting_finished = vote.getVotings();


        TestListener foolistener = new TestListener();
        s.addListener(alice_p, foolistener);

        Message mlistener1 = new ImplMessage(new ImplContent("received through listener - bob to alice", bob_p), time5ago, bob, s.getMyProfile(), grouprecipients, false, false);
        Message mlistener2 = new ImplMessage(new ImplContent("received through listener - alice to bob", bob_p), time5ago, alice, s.getMyProfile(), charlesrecipient, false, false);


        s.informMessage(mlistener2);
        s.informMessage(mlistener1);
        s.exchangeContactNFC();


        //Set Default ProfileActivity to alice

        s.setProfile(alice_p, "");
    }
    */

}
