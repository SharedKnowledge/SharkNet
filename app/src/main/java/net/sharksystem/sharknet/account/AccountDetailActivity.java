package net.sharksystem.sharknet.account;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.system.L;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;
import net.sharksystem.sharknet.main.MailServerPingPort;

import java.io.IOException;

import static net.sharksystem.sharknet.main.NewProfileAddressFragment.MAIL_SERVER_PING_TYPE;

public class AccountDetailActivity extends RxSingleBaseActivity<Contact> implements View.OnClickListener, MailServerPingPort.OnMailServerPingListener {

    private static final int PICK_IMAGE_REQUEST = 1777;

    private EditText mName;
    private EditText mMail;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPopServer;
    private EditText mSmtpServer;
    private Button mTestMail;
    private TextView mServerTestStatus;
    private ImageView mImage;
    private Bitmap mBitmap;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;
    private AndroidSharkEngine mEngine;
    private MailServerPingPort mServerPingPort;
    private boolean mMailSuccessfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.account_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setOptionsMenu(R.menu.account_menu);

        setProgressMessage(R.string.account_progress);

        mImage = (ImageView) findViewById(R.id.contact_image);
        mName = (EditText) findViewById(R.id.editText_account_name);

        mMail = (EditText) findViewById(R.id.editText_account_mail);
        mUsername = (EditText) findViewById(R.id.editText_account_username);
        mPassword = (EditText) findViewById(R.id.editText_account_password);
        mPopServer = (EditText) findViewById(R.id.editText_account_pop_server);
        mSmtpServer = (EditText) findViewById(R.id.editText_account_smtp_server);

        mTestMail = (Button) findViewById(R.id.button_account_test_mail);
        mServerTestStatus = (TextView) findViewById(R.id.textView_server_test_status);

        mTestMail.setOnClickListener(this);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected Contact doOnBackgroundThread() throws Exception {
        return mApi.getAccount();
    }

    @Override
    protected void doOnUIThread(Contact contact) {
        if (contact == null) return;

        mName.setText(contact.getName());
        mImage.setImageBitmap(contact.getImage());

        Settings settings = mApi.getSettings();

        if(settings.getMailAddress() == null || settings.getMailAddress().isEmpty()){
            settings.setMailAddress(contact.getEmail());
        }

        mMail.setText(settings.getMailAddress());
        mUsername.setText(settings.getMailUsername());
        mPassword.setText(settings.getMailPassword());
        mPopServer.setText(settings.getMailPopServer());
        mSmtpServer.setText(settings.getMailSmtpServer());
    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Checking MailServer");
        mHandler = new Handler();

        mEngine = mApi.getSharkEngine();
        mEngine.stopMail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.button_account_save:
                L.d("Saved Account", this);
                if(mMailSuccessfull){
                    Settings settings = new Settings();
                    settings.setMailAddress(mMail.getText().toString());
                    settings.setMailUsername(mUsername.getText().toString());
                    settings.setMailPassword(mPassword.getText().toString());
                    settings.setMailPopServer(mPopServer.getText().toString());
                    settings.setMailSmtpServer(mSmtpServer.getText().toString());
                    mApi.setSettings(settings);
                }

                Contact account = mApi.getAccount();
                account.setName(mName.getText().toString());
                account.setEmail(mMail.getText().toString());
                if(mBitmap!=null) account.setImage(mBitmap);

                mApi.setAccount(account);

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPingSuccessful() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mServerTestStatus.setText("Ping successful!");
                mEngine.stopMail();
                mMailSuccessfull = true;
                mServerPingPort.deleteListeners();
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_account_test_mail:

                String mailAddress = mMail.getText().toString();
                String mailPassword = mPassword.getText().toString();
                String mailUsername = mUsername.getText().toString();
                String mailIncomingHost = mPopServer.getText().toString();
                String mailOutgoingHost = mSmtpServer.getText().toString();

                // TODO to be removed
                PeerSemanticTag owner = InMemoSharkKB.createInMemoPeerSemanticTag("Owner", "si:owner", Protocols.MAIL_PREFIX + mailAddress);
                mEngine.setEngineOwnerPeer(owner);

                if(mServerPingPort == null){
                    mServerPingPort = new MailServerPingPort(mApi.getSharkEngine(), this);
                }

                // Reset MailConfiguration @startup
                mEngine.setMailConfiguration(mailOutgoingHost, mailUsername, mailPassword, false, mailIncomingHost, mailUsername, mailAddress, mailPassword, 3, false);
                mApi.pingMailServer(MAIL_SERVER_PING_TYPE, owner);
                mProgressDialog.show();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AccountDetailActivity.this, "Ping was not successful. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                mHandler.postDelayed(runnable, 10000);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mBitmap = bitmap;
                mImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
