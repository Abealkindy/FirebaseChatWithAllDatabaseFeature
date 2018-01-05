package com.rosinante.firestorerealtimechat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.rosinante.firestorerealtimechat.firestore.FirestoreChatActivity
import com.rosinante.firestorerealtimechat.realtime.RealtimeChatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_firestore.setOnClickListener {
            startActivity(Intent(MainActivity@ this, FirestoreChatActivity::class.java))
        }
        button_realtime.setOnClickListener {
            startActivity(Intent(MainActivity@ this, RealtimeChatActivity::class.java))
        }
    }

}
