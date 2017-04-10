package net.sharksystem.sharknet.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 3/18/17.
 */

public class NewProfileAddressFragment extends Fragment implements View.OnClickListener, MailServerPingPort.OnMailServerPingListener {

    public static SemanticTag MAIL_SERVER_PING_TYPE = InMemoSharkKB.createInMemoSemanticTag("Server-Ping", "si:ping");
    NewProfileAddressFragmentButtonListener mListener;
    private EditText mEditAddress;
    private EditText mEditPassword;
    private EditText mEditUsername;
    private EditText mEditIncoming;
    private EditText mEditOutgoing;
    private TextView mTextServerStatus;
    private Button mCreateContact;
    private AndroidSharkEngine mEngine;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;

    @Override
    public void onPingSuccessful() {
        L.d("Ping successful!", this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextServerStatus.setText("Ping successful!");
                mCreateContact.setVisibility(View.VISIBLE);
                mEngine.stopMail();
                if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Checking MailServer");
        mHandler = new Handler();

        try {
            mListener = (NewProfileAddressFragmentButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewProfileAddressFragmentButtonListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_new_account_mail_fragment, container, false);

        mCreateContact = (Button) view.findViewById(R.id.next_fragment);
        mCreateContact.setOnClickListener(this);
        mCreateContact.setVisibility(View.GONE);

        view.findViewById(R.id.previous_fragment).setOnClickListener(this);
        view.findViewById(R.id.buttonTestServer).setOnClickListener(this);

        mEditAddress = (EditText) view.findViewById(R.id.editTextMailAddress);
        mEditPassword = (EditText) view.findViewById(R.id.editTextMailPassword);
        mEditUsername = (EditText) view.findViewById(R.id.editTextMailUsername);
        mEditIncoming = (EditText) view.findViewById(R.id.editTextIncoming);
        mEditOutgoing = (EditText) view.findViewById(R.id.editTextOutgoing);

        mEditAddress.setText("mail@j4rvis.de");
        mEditPassword.setText("password1");
        mEditUsername.setText("j4rvisde-0001");
        mEditIncoming.setText("pop.udag.de");
        mEditOutgoing.setText("smtp.udag.de");

        mTextServerStatus = (TextView) view.findViewById(R.id.textViewTestServerStatus);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTestServer:
                L.d("Status checking", this);
                // TODO check if fields are empty
                SharkNetApi api = ((MainActivity) getActivity()).getApi();
                mEngine = api.getSharkEngine();

                String mailAddress = mEditAddress.getText().toString();
                String mailPassword = mEditPassword.getText().toString();
                String mailUsername = mEditUsername.getText().toString();
                String mailIncomingHost = mEditIncoming.getText().toString();
                String mailOutgoingHost = mEditOutgoing.getText().toString();

                // TODO to be removed
                PeerSemanticTag owner = InMemoSharkKB.createInMemoPeerSemanticTag("Owner", "si:owner", Protocols.MAIL_PREFIX + mailAddress);
                mEngine.setEngineOwnerPeer(owner);

                mEngine.setMailConfiguration(mailOutgoingHost, mailUsername, mailPassword, false, mailIncomingHost, mailUsername, mailAddress, mailPassword, 3, false);
                new MailServerPingPort(api.getSharkEngine(), this);
                api.pingMailServer(MAIL_SERVER_PING_TYPE, owner);
                mProgressDialog.show();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                            Toast.makeText(NewProfileAddressFragment.this.getContext(), "Ping was not successful. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                mHandler.postDelayed(runnable, 5000);
                break;
            case R.id.next_fragment:
                // TODO add contact details
                mListener.onCreateProfile(null);
                break;
            case R.id.previous_fragment:
                mListener.onBackToNewProfileFragment();
                break;
            default:
                break;
        }
    }

    public interface NewProfileAddressFragmentButtonListener {
        void onCreateProfile(Contact contact);
        void onBackToNewProfileFragment();
    }
}
