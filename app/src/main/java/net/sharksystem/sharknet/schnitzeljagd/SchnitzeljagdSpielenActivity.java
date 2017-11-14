package net.sharksystem.sharknet.schnitzeljagd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.sharksystem.sharknet.R;

public class SchnitzeljagdSpielenActivity extends AppCompatActivity {

    private Schnitzeljagd schnitzeljagd;
    private int schnitzelIndex = 0;
    private TextView textView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schnitzeljagd_spielen);
        schnitzeljagd = (Schnitzeljagd) getIntent().getSerializableExtra("schnitzeljagd");
        textView = (TextView) findViewById(R.id.schnitzeljagd_spielen_text);
        textView.setText(schnitzeljagd.getSchnitzel().get(schnitzelIndex).getMessage());
        button =  (Button) findViewById(R.id.schnitzeljagd_spielen_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(schnitzelIndex == schnitzeljagd.getSchnitzel().size() -1 ){
                    textView.setText("Alle schnitzel gefunden JUHU");
                    schnitzelIndex++;
                    button.setText("Fertig");
                }
                else if(schnitzelIndex == schnitzeljagd.getSchnitzel().size()){
                    setResult(RESULT_OK);
                    finish();
                }
                else if(schnitzelIndex < schnitzeljagd.getSchnitzel().size() -1){
                    schnitzelIndex++;
                    textView.setText(schnitzeljagd.getSchnitzel().get(schnitzelIndex).getMessage());
                }
            }
        });
    }
}
