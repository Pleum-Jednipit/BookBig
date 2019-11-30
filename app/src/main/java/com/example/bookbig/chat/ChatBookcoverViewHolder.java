package com.example.bookbig.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookbig.R;
import com.example.bookbig.chat.ChatActivity;
import com.example.bookbig.setting.NewBookcoverActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatBookcoverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mBookcoverName,mBookcoverId;
    public ImageView mBookcoverImage;

    public ChatBookcoverViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mBookcoverName = itemView.findViewById(R.id.bookName);
        mBookcoverImage = itemView.findViewById(R.id.bookcoverImage);
        mBookcoverId = itemView.findViewById(R.id.bookcoverId);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatBookcoverDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("bookcoverId",mBookcoverId.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}

