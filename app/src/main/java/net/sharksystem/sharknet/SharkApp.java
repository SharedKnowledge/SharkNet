package net.sharksystem.sharknet;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;


/**
 * Created by j4rvis on 3/13/17.
 */

public class SharkApp extends MultiDexApplication {

    private static SharkApp instance;
    private Chat chat = null;
    private Broadcast broadcast = null;
    private Contact contact = null;
    private Message message = null;
    private Contact account = null;

    private boolean isDummy = false;

    public SharkApp() {        instance = this;
        MultiDex.install(this);
    }

    public static Context getContext() {
        return instance;
    }

    public Contact getAccount() {
        return account;
    }

    public void setAccount(Contact account) {
        this.account = account;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void resetChat() {
        this.chat = null;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void resetContact() {
        this.contact = null;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void resetMessage() {
        this.message = null;
    }

    public void activateDummy() {
        isDummy = true;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public Broadcast getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }
}
