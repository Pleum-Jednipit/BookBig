package com.example.bookbig.bookcover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bookbig.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookcoverImageAdapter extends RecyclerView.Adapter<BookcoverViewHolder> {
    private List<Bookcover> bookcoverList;
    private Context context;

    public BookcoverImageAdapter(List<Bookcover> bookcoverList, Context context) {
        this.bookcoverList = bookcoverList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookcoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookcover,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        BookcoverViewHolder bookcoverViewHolder = new BookcoverViewHolder(layoutView);
        return bookcoverViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookcoverViewHolder holder, int position) {
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
