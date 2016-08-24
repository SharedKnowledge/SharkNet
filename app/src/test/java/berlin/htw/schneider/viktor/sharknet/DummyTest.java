package berlin.htw.schneider.viktor.sharknet;

import net.sharkfw.knowledgeBase.SharkKBException;

import net.sharksystem.api.interfaces.*;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Profile;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DummyTest
{
    ImplSharkNet sharkNet;
    @Before
    public void setUp() throws SharkKBException {
        sharkNet = new ImplSharkNet();
        sharkNet.fillWithDummyData();
        sharkNet.setProfile(sharkNet.getProfiles().get(1),"");
        System.out.println("%%%%%%%%%%%% "+sharkNet.getMyProfile().getNickname());
    }


    @Test
    public void checkProfiles()
    {
        List<Profile> profiles = sharkNet.getProfiles();
        System.out.println("%%%%%%%%%%%% Profiles:"+profiles.size());
        assertNotNull(profiles);
        assertNotEquals(0,profiles.size());
    }

    @Test
    public void checkFeeds()
    {
        List<Feed> feeds = sharkNet.getFeeds(false);
        System.out.println("%%%%%%%%%%%% Feeds:"+feeds.size());
        assertNotNull(feeds);
    }
    @Test
    public void checkContacts()
    {
        List<Contact> contacts = sharkNet.getContacts();
        System.out.println("%%%%%%%%%%%% Contacts:"+contacts.size());
        assertNotNull(contacts);
    }
    @Test
    public void checkChats()
    {
        List<Chat> chats = sharkNet.getChats();
        System.out.println("%%%%%%%%%%%% Chats:"+chats.size());
        assertNotNull(chats);
    }

    @Test
    public void checkNewContact()
    {
    }





}