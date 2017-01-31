package net.sharksystem.sharknet;

import net.sharksystem.sharknet.chat.ChatActivity;
import net.sharksystem.sharknet.contact.ContactsActivity;
import net.sharksystem.sharknet.inbox.InboxActivity;
import net.sharksystem.sharknet.nfc.NFCActivity;
import net.sharksystem.sharknet.profile.ProfileActivity;
import net.sharksystem.sharknet.radar.RadarActivity;

/**
 * Created by mn-io on 22.01.16.
 */
public class SideNav {

//    public final static Object[][] system_modules = new Object[][]{
//            new Object[]{R.string.sidenav_item_settings, SettingsActivity.class},
//            new Object[]{R.string.sidenav_item_log, LogActivity.class},
//    };

    public final static Object[][] modules = new Object[][]{
            new Object[]{R.string.sidenav_item_chats, ChatActivity.class},
            new Object[]{R.string.sidenav_item_contacts, ContactsActivity.class},
            new Object[]{R.string.sidenav_item_inbox, InboxActivity.class},
            new Object[]{R.string.sidenav_item_profile, ProfileActivity.class},
            new Object[]{R.string.sidenav_item_radar, RadarActivity.class},
            new Object[]{R.string.sidenav_item_nfc, NFCActivity.class},
    };
}
