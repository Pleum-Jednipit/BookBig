package com.example.bookbig.chat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bookbig.R;
import com.example.bookbig.bookcover.Bookcover;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatBookcoverImageAdapter extends RecyclerView.Adapter<ChatBookcoverViewHolder> {
    private List<Bookcover> bookcoverList;
    private Context context;

    public ChatBookcoverImageAdapter(List<Bookcover> bookcoverList, Context context) {
        this.bookcoverList = bookcoverList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatBookcoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookcover,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChatBookcoverViewHolder bookcoverViewHolder = new ChatBookcoverViewHolder(layoutView);
        return bookcoverViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBookcoverViewHolder holder, int position) {
        holder.mBookcoverId.setText(bookcoverList.get(position).getBookcoverId());
        holder.mBookcoverName.setText(bookcoverList.get(position).getName());
        holder.mBookcoverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(bookcoverList.get(position).getPhotoUrl()).into(holder.mBookcoverImage);
    }

    @Override
    public int getItemCount() {
        return bookcoverList.size();
    }

}
