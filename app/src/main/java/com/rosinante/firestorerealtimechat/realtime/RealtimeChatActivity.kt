package com.rosinante.firestorerealtimechat.realtime

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.firebase.ui.auth.util.ui.ImeHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import com.rosinante.firestorerealtimechat.R
import com.rosinante.firestorerealtimechat.holder.ChatHolder
import com.rosinante.firestorerealtimechat.realtime.model.Chat
import com.rosinante.firestorerealtimechat.util.SignInResultNotifier


import kotlinx.android.synthetic.main.activity_chat.*

class RealtimeChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private val isSignedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recycler_chat.setHasFixedSize(true)
        recycler_chat.layoutManager = LinearLayoutManager(this)
        sendButton.setOnClickListener {
            onSendClick()
        }
        ImeHelper.setImeOnDoneListener(edit_text_message) { onSendClick() }
    }

    public override fun onStart() {
        super.onStart()
        if (isSignedIn) {
            attachRecyclerViewAdapter()
        }
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        sendButton.isEnabled = isSignedIn
        edit_text_message.isEnabled = isSignedIn

        if (isSignedIn) {
            attachRecyclerViewAdapter()
        } else {
            Toast.makeText(this, "Signing In.....", Toast.LENGTH_SHORT).show()
            auth.signInAnonymously().addOnCompleteListener(SignInResultNotifier(this))
        }
    }

    private fun attachRecyclerViewAdapter() {
        val adapter = newAdapter()

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recycler_chat.smoothScrollToPosition(adapter.itemCount)
            }
        })

        recycler_chat.adapter = adapter
    }


    fun onSendClick() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val name = "User " + uid.substring(0, 6)

        onAddMessage(Chat(name, edit_text_message.text.toString(), uid))

        edit_text_message.setText("")
    }

    private fun newAdapter(): RecyclerView.Adapter<*> {
        val options = FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(sChatQuery, Chat::class.java)
                .setLifecycleOwner(this)
                .build()

        return object : FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
                return ChatHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_item, parent, false))
            }

            override fun onBindViewHolder(holder: ChatHolder, position: Int, model: Chat) {
                holder.bind(model)
            }

            override fun onDataChanged() {
                text_empty.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }

    private fun onAddMessage(chat: Chat) {
        sChatQuery.ref.push().setValue(chat) { error, _ ->
            if (error != null) {

            }
        }
    }

    companion object {

        private val sChatQuery = FirebaseDatabase.getInstance().reference.child("chats").limitToLast(50)
    }
}
