package com.csjbot.coshandler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.service.HandlerMsgService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Button bt_test = (Button) findViewById(R.id.bt_test);
//        startService(new Intent(this,HandlerMsgService.class));
//        bt_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Robot.getInstance().denyAction();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
