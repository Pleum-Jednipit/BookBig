package com.example.bookbig.bookcover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bookbig.R;

import java.util.ArrayList;
import java.util.List;

public class BookcoverActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mBookcverImageAdapter;
    private RecyclerView.LayoutManager mBookcoverLayoutManager;
    private List<Bookcover> resultBookcover = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcover);


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mBookcoverLayoutManager = new LinearLayoutManager(BookcoverActivity.this);
        mRecyclerView.setLayoutManager(mBookcoverLayoutManager);
        mBookcverImageAdapter = new BookcoverImageAdapter(getDataSetHiFi(), BookcoverActivity.this);
        mRecyclerView.setAdapter(mBookcverImageAdapter);
    }

    private List<Bookcover> getDataSetHiFi() {
        return resultBookcover;
    }


}
