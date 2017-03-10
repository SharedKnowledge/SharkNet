package net.sharksystem.sharknet.chat;

import net.sharksystem.api.interfaces.Message;

/**
 * Created by j4rvis on 3/10/17.
 */

public class ChatMessageDataHolder {

    private Message message;
    private static ChatMessageDataHolder instance = new ChatMessageDataHolder();

    private ChatMessageDataHolder() {
    }

    public static ChatMessageDataHolder getInstance(){ return instance; }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void resetMessage(){
        this.message = null;
    }

}
