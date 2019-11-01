package com.example.bookbig.hifive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.MainActivity;
import com.example.bookbig.Profile;
import com.example.bookbig.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HiFiveActivity extends AppCompatActivity {
    private static final String TAG = "HiFive : ";
    private Button mSwipe;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mHiFiveAdapter;
    private RecyclerView.LayoutManager mHiFiveLayoutManager;
    private List<HiFive> resultHiFive = new ArrayList<>();
    private FirestoreOperation firestoreOperation;
    private EditText mSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi_five);

        firestoreOperation = new FirestoreOperation();
        getHifive();
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mSearchBar = findViewById(R.id.searchBar);
        mSwipe = findViewById(R.id.swipe);
        mSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HiFiveActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mHiFiveLayoutManager = new LinearLayoutManager(HiFiveActivity.this);
        mRecyclerView.setLayoutManager(mHiFiveLayoutManager);
        mHiFiveAdapter = new HiFiAdapter(getDataSetHiFi(), HiFiveActivity.this);
        mRecyclerView.setAdapter(mHiFiveAdapter);
    }



    // Can't code in FirestoreOperation becuase bug in onCallback!!!
    private void getHifive(){
        firestoreOperation.getHifiveRef()
                .whereArrayContains("userId", firestoreOperation.getCurrentUserId())
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
                                Log.d("TEST",document.getData().toString());
                                ArrayList<String> id = (ArrayList<String>) document.get("userId");
                                String hifiveId = document.getData().get("hifiveId").toString();
                                for (int i = 0; i < id.size(); i++) {
                                    if (id.get(i).equals(firestoreOperation.getCurrentUserId())) {
                                        id.remove(i);
                                        i--;
                                    } else {
                                        getUserProfile(id.get(i),hifiveId);
                                    }
                                }
                            }
                        }

                    }
                });
    }


    private void getUserProfile(final String userId, final String hifiveId){
        final List<Profile> profileList = new ArrayList<>();
        firestoreOperation.getUserAccountRef()
                        .document(userId)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }
                                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                        ? "Local" : "Server";

                                if (snapshot != null && snapshot.exists()) {
                                    Log.d(TAG,snapshot.getData().toString());
                                    String name = snapshot.getData().get("name").toString();
                                    String userId = snapshot.getData().get("userId").toString();
                                    Profile profile = new Profile(name,userId);
                                    profileList.add(profile);
                                    HiFive hiFive = new HiFive(profile.getName(),hifiveId);
                                    resultHiFive.add(hiFive);
                                    mHiFiveAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, source + " data: null");
                                }
                            }
                        });
            }


    private List<HiFive> getDataSetHiFi() {
        return resultHiFive;
    }
}




