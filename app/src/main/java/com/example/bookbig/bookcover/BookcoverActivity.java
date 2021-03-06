package com.example.bookbig.bookcover;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.R;
import com.example.bookbig.chat.ChatActivity;
import com.example.bookbig.hifive.HiFiveActivity;
import com.example.bookbig.registration.AddFirstBookCoverActivity;
import com.example.bookbig.registration.GenderRegistrationActivity;
import com.example.bookbig.setting.NewBookcoverActivity;
import com.example.bookbig.setting.UserInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;

public class BookcoverActivity extends AppCompatActivity {
    private static final String TAG = "Gallery : ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mBookcverImageAdapter;
    private RecyclerView.LayoutManager mBookcoverLayoutManager;
    private List<Bookcover> resultBookcover = new ArrayList<>();
    private FloatingActionButton mFloatingActionButton;
    private FirestoreOperation firestoreOperation;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcover);
        firestoreOperation = new FirestoreOperation();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle = findViewById(R.id.profileName);
        mTitle.setText("My Bookcovers");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookcoverActivity.this, UserInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getUserBookCover(firestoreOperation.getCurrentUserId());


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mFloatingActionButton = findViewById(R.id.floating_action_button);

        mBookcoverLayoutManager = new GridLayoutManager(BookcoverActivity.this,3);
        mRecyclerView.setLayoutManager(mBookcoverLayoutManager);
        mBookcverImageAdapter = new BookcoverImageAdapter(getDataSetBookcover(), BookcoverActivity.this);
        mRecyclerView.setAdapter(mBookcverImageAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookcoverActivity.this, NewBookcoverActivity.class);
                startActivity(intent);
//                finish();
            }
        });

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
                        mBookcverImageAdapter.notifyDataSetChanged();
                        }
                    });
    }

    private List<Bookcover> getDataSetBookcover() {
        return resultBookcover;
    }


}
