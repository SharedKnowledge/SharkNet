package net.sharksystem.sharknet.schnitzeljagd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.sharksystem.api.locator.Locator;
import net.sharksystem.api.models.Schnitzeljagd;
import net.sharksystem.sharknet.R;


public class AddSchnitzeljagdActivity extends AppCompatActivity {

    private int addIndex = 0;
    private TextView text;
    private EditText schnitzelDescription;
    private Button addButton;
    private Button finishButton;
    private Schnitzeljagd schnitzeljagd;
    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schnitzeljagd);
    }

    public void newSchnitzelOnClick(View view) {
    }

    public void newSchnitzeljagdOnClick(View view) {
    }
}
