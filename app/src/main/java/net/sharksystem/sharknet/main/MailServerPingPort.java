package net.sharksystem.sharknet.main;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.ports.KnowledgePort;

/**
 * Created by j4rvis on 4/10/17.
 */

public class MailServerPingPort extends KnowledgePort {

    private OnMailServerPingListener mPingListener;

    public MailServerPingPort(SharkEngine se, OnMailServerPingListener listener) {
        super(se);
        mPingListener = listener;
    }

    public void deleteListeners(){
        mPingListener = null;
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {
        if (!SharkCSAlgebra.identical(NewProfileAddressFragment.MAIL_SERVER_PING_TYPE, message.getType()))
            return;
        if(mPingListener!=null) mPingListener.onPingSuccessful();
    }

    public interface OnMailServerPingListener {
        void onPingSuccessful();
    }
}
