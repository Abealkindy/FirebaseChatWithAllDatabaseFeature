package com.rosinante.firestorerealtimechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rosinante.firestorerealtimechat.firestore.FirestoreChatActivity;
import com.rosinante.firestorerealtimechat.realtime.RealtimeChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_realtime)
    Button buttonRealtime;
    @BindView(R.id.button_firestore)
    Button buttonFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_realtime, R.id.button_firestore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_realtime:
                startActivity(new Intent(MainActivity.this, RealtimeChatActivity.class));
                break;
            case R.id.button_firestore:
                startActivity(new Intent(MainActivity.this, FirestoreChatActivity.class));
                break;
        }
    }
}
