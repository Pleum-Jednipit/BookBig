package com.example.bookbig.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.bookbig.R;
import com.example.bookbig.hifive.HiFiveViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ChatViewHolder chatViewHolder = new ChatViewHolder(layoutView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        GradientDrawable gradientDrawable = (GradientDrawable) holder.mMessage.getBackground().mutate();
        if(chatList.get(position).getCurrentUser()){
            holder.mContainer.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            gradientDrawable.setColor(Color.rgb(244, 244, 244));
            //holder.mMessage.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else{
            holder.mContainer.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            gradientDrawable.setColor(Color.rgb(251, 201, 150));
            //holder.mMessage.setBackgroundColor(Color.parseColor("#2DB4CB"));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
