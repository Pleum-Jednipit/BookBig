package com.example.bookbig.bookcover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookbig.R;
import com.example.bookbig.bookcover.Bookcover;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookcoverAdapter extends ArrayAdapter {
    private List<Bookcover> bookcoverList;
    private Context context;


    public BookcoverAdapter(Context context, int resource, List<Bookcover> bookcaseList) {
        super(context, resource, bookcaseList);
    }



    public View getView(int position, View convertView, ViewGroup parent){
        Bookcover bookcover = (Bookcover) getItem(position);


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }


        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(bookcover.getName());
        Glide.with(getContext()).load(bookcover.getPhotoUrl()).into(image);

        return convertView;
    }


}
