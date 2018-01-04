package com.rosinante.firestorerealtimechat.realtime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.util.ui.ImeHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rosinante.firestorerealtimechat.R;
import com.rosinante.firestorerealtimechat.holder.ChatHolder;
import com.rosinante.firestorerealtimechat.realtime.model.Chat;
import com.rosinante.firestorerealtimechat.util.SignInResultNotifier;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealtimeChatActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    protected static final Query sChatQuery =
            FirebaseDatabase.getInstance().getReference().child("chats").limitToLast(50);

    @BindView(R.id.recycler_chat)
    RecyclerView mRecyclerView;

    @BindView(R.id.sendButton)
    Button mSendButton;

    @BindView(R.id.edit_text_message)
    EditText mMessageEdit;

    @BindView(R.id.text_empty)
    TextView mEmptyListMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImeHelper.setImeOnDoneListener(mMessageEdit, new ImeHelper.DonePressedListener() {
            @Override
            public void onDonePressed() {
                onSendClick();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        }
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        mSendButton.setEnabled(isSignedIn());
        mMessageEdit.setEnabled(isSignedIn());

        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        } else {
            Toast.makeText(this, "Signing In.....", Toast.LENGTH_SHORT).show();
            auth.signInAnonymously().addOnCompleteListener(new SignInResultNotifier(this));
        }
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.sendButton)
    public void onSendClick() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = "User " + uid.substring(0, 6);

        onAddMessage(new Chat(name, mMessageEdit.getText().toString(), uid));

        mMessageEdit.setText("");
    }

    protected RecyclerView.Adapter newAdapter() {
        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(sChatQuery, Chat.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
                holder.bind(model);
            }

            @Override
            public void onDataChanged() {
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    protected void onAddMessage(Chat chat) {
        sChatQuery.getRef().push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference reference) {
                if (error != null) {

                }
            }
        });
    }
}
