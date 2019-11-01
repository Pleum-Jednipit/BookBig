package com.example.bookbig.bookcover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookbig.R;
import com.example.bookbig.chat.ChatActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookcoverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mBookcoverId,mBookcoverName;
    public ImageView mBookcoverImage;

    public BookcoverViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mBookcoverId = itemView.findViewById(R.id.hifiveId);
        mBookcoverName = itemView.findViewById(R.id.hifiveName);
        mBookcoverImage = itemView.findViewById(R.id.hifiveImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("bookcoverId",mBookcoverId.getText().toString());
        bundle.putString("name",mBookcoverName.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
