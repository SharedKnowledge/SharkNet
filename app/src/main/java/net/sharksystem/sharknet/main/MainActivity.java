package net.sharksystem.sharknet.main;

import android.os.Bundle;

import net.sharkfw.system.L;
import net.sharksystem.sharknet.ParentActivity;

public class MainActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.setLogLevel(L.LOGLEVEL_ALL);


    }
}
