package berlin.htw.schneider.viktor.sharknet;

import net.sharkfw.knowledgeBase.SharkKBException;

import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.*;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Profile;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DummyTest
{
    SharkNet mSharkNet;
    @Before
    public void setUp() throws SharkKBException, JSONException {
        mSharkNet = SharkNetEngine.getSharkNet();
        Dummy.createDummyData();
        mSharkNet.setActiveProfile(mSharkNet.getProfiles().get(1),"");
        System.out.println("%%%%%%%%%%%% "+mSharkNet.getMyProfile().getNickname());
    }


    @Test
    public void checkProfiles() throws SharkKBException {
        List<Profile> profiles = mSharkNet.getProfiles();
        System.out.println("%%%%%%%%%%%% Profiles:"+profiles.size());
        assertNotNull(profiles);
        assertNotEquals(0,profiles.size());
    }

    @Test
    public void checkFeeds() throws SharkKBException {
        List<Feed> feeds = mSharkNet.getFeeds(false);
        System.out.println("%%%%%%%%%%%% Feeds:"+feeds.size());
        assertNotNull(feeds);
    }
    @Test
    public void checkContacts() throws SharkKBException {
        List<Contact> contacts = mSharkNet.getContacts();
        System.out.println("%%%%%%%%%%%% Contacts:"+contacts.size());
        assertNotNull(contacts);
    }
    @Test
    public void checkChats() throws SharkKBException {
        List<Chat> chats = mSharkNet.getChats();
        System.out.println("%%%%%%%%%%%% Chats:"+chats.size());
        assertNotNull(chats);
    }

    @Test
    public void checkNewContact()
    {
    }





}