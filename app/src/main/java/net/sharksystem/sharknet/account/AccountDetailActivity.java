package net.sharksystem.sharknet.account;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Contact;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.RxSingleBaseActivity;

public class AccountDetailActivity extends RxSingleBaseActivity<Contact> {

    private EditText mName;
    private EditText mMail;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPopServer;
    private EditText mSmtpServer;
    private Button mTestMail;
    private TextView mServerTestStatus;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.account_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProgressMessage(R.string.account_progress);

        mImage = (ImageView) findViewById(R.id.contact_image);

        mName = (EditText) findViewById(R.id.editText_account_name);
        mMail = (EditText) findViewById(R.id.editText_Account_mail);
        mUsername = (EditText) findViewById(R.id.editText_account_username);
        mPassword = (EditText) findViewById(R.id.editText_account_password);
        mPopServer = (EditText) findViewById(R.id.editText_account_pop_server);
        mSmtpServer = (EditText) findViewById(R.id.editText_account_smtp_server);

        mTestMail = (Button) findViewById(R.id.button_account_test_mail);
        mServerTestStatus = (TextView) findViewById(R.id.textView_server_test_status);
    }

    @Override
    protected Contact doOnBackgroundThread() throws Exception {
        return mApi.getAccount();
    }

    @Override
    protected void doOnUIThread(Contact contact) {
        if(contact==null) return;

        mName.setText(contact.getName());
        mImage.setImageBitmap(contact.getImage());


    }

    @Override
    protected void doOnError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
