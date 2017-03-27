package net.sharksystem.sharknet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleNavigationDrawerActivity;

import java.util.List;

public class ChatActivity extends RxSingleNavigationDrawerActivity<List<Chat>> {

    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();

        setProgressMessage("Lade Chats..");

        startSubscription();
    }

    private void configureLayout() {
        setLayoutResource(R.layout.chat_activity);
        setTitle("Chats");
        mChatListAdapter = new ChatListAdapter(this, getSharkApp());
        RecyclerView mChatRecyclerView = (RecyclerView) findViewById(R.id.chats_recylcer_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mChatListAdapter);

        activateFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, ChatNewActivity.class));
            }
        });
    }

    @Override
    protected List<Chat> doOnBackgroundThread() throws SharkKBException {
//        List<Chat> chats = SharkNetEngine.getSharkNet().getChats();
//        ArrayList<ChatDataHolder> list = new ArrayList<>();
//        for (Chat chat : chats) {
//            String name = chat.getTitle();
//            if (name == null || name.isEmpty()) {
//                name = "";
//                List<Contact> contacts = chat.getContactsWithoutMe();
//                for (Contact next : contacts) {
//                    if (!name.isEmpty()) {
//                        name += ", ";
//                    }
//                    name += next.getName();
//                }
//            }
//
//            // message
//            String message = "";
//            try {
//                List<Message> messages = chat.getMessages(false);
//                if (messages.size() > 0) {
//                    Message last_msg = messages.get(messages.size() - 1);
//                    String content = last_msg.getContent().getMessage();
//                    String sender = last_msg.getSender().getNickname();
//                    message = sender + ":" + content;
//                }
//            } catch (SharkKBException e) {
//                e.printStackTrace();
//            }
//
//            // Image
//            Bitmap image = null;
//            int imageId = 0;
//            if (chat.getPicture().getLength() > 0) {
//                image = BitmapFactory.decodeStream(chat.getPicture().getInputStream());
//            } else {
//                imageId = R.drawable.ic_group_white_24dp;
//            }
//            list.add(new ChatDataHolder(chat, image, imageId, name, message));
//        }
//        return list;
        return SharkNetApi.getInstance().getChats();
    }

    @Override
    protected void doOnUIThread(List<Chat> chats) {
        mChatListAdapter.setChats(chats);
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }
}
