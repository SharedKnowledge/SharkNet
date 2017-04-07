package net.sharksystem.sharknet;

import android.app.Application;
import android.content.Context;

import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;


/**
 * Created by j4rvis on 3/13/17.
 */

public class SharkApp extends Application {

    private Chat chat = null;
    private Contact contact = null;
    private Message message = null;
    private Contact account = null;

    private static SharkApp instance;

    public SharkApp() {
        instance = this;
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
}
