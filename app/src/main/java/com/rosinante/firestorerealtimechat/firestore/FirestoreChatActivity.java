package com.rosinante.firestorerealtimechat.firestore;

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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rosinante.firestorerealtimechat.R;
import com.rosinante.firestorerealtimechat.firestore.model.Chat;
import com.rosinante.firestorerealtimechat.holder.ChatHolder;
import com.rosinante.firestorerealtimechat.util.SignInResultNotifier;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirestoreChatActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final CollectionReference chatCollection = FirebaseFirestore.getInstance().collection("chats");
    private static final Query chatQuery = chatCollection.orderBy("timestamp").limit(50);

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    @BindView(R.id.text_empty)
    TextView textEmpty;
    @BindView(R.id.edit_text_message)
    EditText editTextMessage;
    @BindView(R.id.recycler_chat)
    RecyclerView recyclerChat;
    @BindView(R.id.sendButton)
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setTitle("Firestore Chat");
        recyclerChat.setHasFixedSize(true);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        ImeHelper.setImeOnDoneListener(editTextMessage, new ImeHelper.DonePressedListener() {
            @Override
            public void onDonePressed() {
                onSendClick();
            }
        });
    }

    @Override
    protected void onStart() {
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
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        sendButton.setEnabled(isSignedIn());
        editTextMessage.setEnabled(isSignedIn());
        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        } else {
            Toast.makeText(this, "Signed In....", Toast.LENGTH_SHORT).show();
            firebaseAuth.signInAnonymously().addOnCompleteListener(new SignInResultNotifier(this));
        }
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerChat.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        recyclerChat.setAdapter(adapter);
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @OnClick(R.id.sendButton)
    public void onSendClick() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = "User " + uid.substring(0, 6);
        onAddMessage(new Chat(name, editTextMessage.getText().toString(), uid));
        editTextMessage.setText("");
    }

    protected RecyclerView.Adapter newAdapter() {
        FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions.Builder<Chat>()
                        .setQuery(chatQuery, Chat.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirestoreRecyclerAdapter<Chat, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder,
                                            int position,
                                            @NonNull Chat model) {
                holder.bind(model);
            }

            @Override
            public void onDataChanged() {
                textEmpty.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    protected void onAddMessage(Chat chat) {
        chatCollection.add(chat).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}
