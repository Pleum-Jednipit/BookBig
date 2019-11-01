package com.example.bookbig.hifive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookbig.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HiFiAdapter extends RecyclerView.Adapter<HiFiveViewHolder> {
    private List<HiFive> hiFiveList;
    private Context context;

    public HiFiAdapter(List<HiFive> hiFiveList, Context context) {
        this.hiFiveList = hiFiveList;
        this.context = context;
    }

    @NonNull
    @Override
    public HiFiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hifi,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        HiFiveViewHolder hiFiveViewHolder = new HiFiveViewHolder(layoutView);
        return hiFiveViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HiFiveViewHolder holder, int position) {
        holder.mHiFiveName.setText(hiFiveList.get(position).getName());
        holder.mHiFiveId.setText(hiFiveList.get(position).getHifiveId());
//        Glide.with(context).load(hiFiveList.get(position))
    }

    @Override
    public int getItemCount() {
        return hiFiveList.size();
    }

    public void filterList(ArrayList<HiFive> filteredList) {
        hiFiveList = filteredList;
        notifyDataSetChanged();
    }
}
