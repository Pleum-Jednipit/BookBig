package com.example.bookbig.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.R;
import com.example.bookbig.bookcover.Bookcover;
import com.example.bookbig.bookcover.BookcoverActivity;
import com.example.bookbig.bookcover.BookcoverImageAdapter;
import com.example.bookbig.setting.NewBookcoverActivity;
import com.example.bookbig.setting.UserInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatBookcoverActivity extends AppCompatActivity {
    private static final String TAG = "Gallery : ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mBookcoverImageAdapter;
    private RecyclerView.LayoutManager mBookcoverLayoutManager;
    private List<Bookcover> resultBookcover = new ArrayList<>();
    private FloatingActionButton mFloatingActionButton;
    private FirestoreOperation firestoreOperation;
    private String userId,hifiveId,name;
    private TextView mProfileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcover);
        firestoreOperation = new FirestoreOperation();

        userId = getIntent().getExtras().getString("userId");
        hifiveId = getIntent().getExtras().getString("hifiveId");
        name = getIntent().getExtras().getString("name");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProfileName = findViewById(R.id.profileName);
        mProfileName.setText(name + "'s Bookcovers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatBookcoverActivity.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hifiveId",hifiveId);
                bundle.putString("name",name);
                bundle.putString("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getUserBookCover(userId);


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mFloatingActionButton = findViewById(R.id.floating_action_button);
        mFloatingActionButton.hide();
        mBookcoverLayoutManager = new GridLayoutManager(ChatBookcoverActivity.this,3);
        mRecyclerView.setLayoutManager(mBookcoverLayoutManager);
        mBookcoverImageAdapter = new ChatBookcoverImageAdapter(getDataSetBookcover(), ChatBookcoverActivity.this);
        mRecyclerView.setAdapter(mBookcoverImageAdapter);

    }


    public void getUserBookCover(String userId) {
        firestoreOperation.getBookcoverRef()
                .whereEqualTo("userId",userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        resultBookcover.clear();
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String bookcoverId = document.getData().get("bookcoverId").toString();
                                String name = document.getData().get("name").toString();
                                String description = document.getData().get("description").toString();
                                String userId = document.getData().get("userId").toString();
                                String photoUrl = document.getData().get("photoUrl").toString();
                                String bookcoverType = document.getData().get("bookcoverType").toString();
                                Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId,bookcoverType);
                                resultBookcover.add(bookcover);
                            }
                        }
                        mBookcoverImageAdapter.notifyDataSetChanged();
                    }
                });
    }

    private List<Bookcover> getDataSetBookcover() {
        return resultBookcover;
    }


}

