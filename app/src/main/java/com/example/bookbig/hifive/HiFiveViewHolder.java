package com.example.bookbig.hifive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookbig.R;
import com.example.bookbig.chat.ChatActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HiFiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mHiFiveId,mHiFiveName;
    public ImageView mHiFiveImage;

    public HiFiveViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mHiFiveId = itemView.findViewById(R.id.hifiveId);
        mHiFiveName = itemView.findViewById(R.id.hifiveName);
        mHiFiveImage = itemView.findViewById(R.id.hifiveImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hifiveId",mHiFiveId.getText().toString());
        bundle.putString("name",mHiFiveName.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
