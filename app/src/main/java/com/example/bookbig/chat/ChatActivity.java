package com.example.bookbig.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.R;
import com.example.bookbig.hifive.HiFiveActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "Chat : ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private String hifiveId;
    private String name,userId;
    private List<Chat> resultChat;
    private EditText mMessage;
    private Button mSend;
    private FirestoreOperation firestoreOperation;
    private NestedScrollView mScrollView;
    private TextView mProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        firestoreOperation = new FirestoreOperation();
        hifiveId = getIntent().getExtras().getString("hifiveId");
        name = getIntent().getExtras().getString("name");
        userId = getIntent().getExtras().getString("userId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProfileName = findViewById(R.id.profileName);
        mProfileName.setText(name);
        mProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ChatBookcoverActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hifiveId",hifiveId);
                bundle.putString("name",name);
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, HiFiveActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mScrollView = findViewById(R.id.scrollView);
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        },100);
        resultChat = new ArrayList<>();
        Log.d("TEST","Match ID: " + hifiveId);
        getMessage();
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mMessage = findViewById(R.id.message);
        mSend = findViewById(R.id.send);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(hifiveId,mMessage.getText().toString());
                mMessage.setText(null);
            }
        });

        mMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }


    private void getMessage(){
        firestoreOperation.getHifiveRef()
                .document(hifiveId)
                .collection("message")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                Log.d(TAG,document.getData().toString());
                                String message = document.getData().get("message").toString();
                                String sendByUser = document.getData().get("sendByUser").toString();
                                String timestamp = document.getData().get("timestamp").toString();


                                Boolean currentUserBoolean = false;
                                if(sendByUser.equals(firestoreOperation.getCurrentUserId())){
                                    currentUserBoolean = true;
                                }
                                Chat chat = new Chat(message,currentUserBoolean,timestamp);
                                if(resultChat.contains(chat)){
                                    Log.d(TAG,"Same!!!");
                                }else{
                                    Log.d("TAG","Add :" + chat.toString());
                                    resultChat.add(chat);
                                }
                            }
                        }
                        mChatAdapter.notifyDataSetChanged();
                        mScrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        },100);

                    }
                });

    }

    public void sendMessage(String hifiveId, String message) {
        if (!message.isEmpty()) {
            Map<String, Object> messageMap = new HashMap<>();
            Long tsLong = System.currentTimeMillis()/1000;
            String timestamp = tsLong.toString();
//            String timestamp = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            messageMap.put("hifiveId",hifiveId);
            messageMap.put("sendByUser", firestoreOperation.getCurrentUserId());
            messageMap.put("message", message);
            messageMap.put("timestamp",timestamp);
            String messageId = firestoreOperation.getHifiveRef().document(hifiveId).collection("message").document().getId();
            Log.d(TAG,"In sendMessage");
            firestoreOperation.getHifiveRef()
                    .document(hifiveId)
                    .collection("message")
                    .document(messageId)
                    .set(messageMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"Success to add message");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Fail to add message");
                }
            });
        }
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        },100);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        },100);
    }



    private List<Chat> getDataSetChat() {
        return resultChat;
    }


}
