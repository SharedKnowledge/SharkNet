package net.sharksystem.sharknet;

import android.app.Application;

import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.interfaces.Profile;

/**
 * Created by j4rvis on 3/13/17.
 */

public class SharkApp extends Application {

    public Chat chat = null;
    public Contact contact = null;
    public Profile profile = null;
    public Message message = null;

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void resetProfile() {
        this.profile = null;
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
