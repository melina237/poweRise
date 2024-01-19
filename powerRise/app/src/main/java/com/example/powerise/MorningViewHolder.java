package com.example.powerise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MorningViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;

    private MorningViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String record) {
        wordItemView.setText(record);

    }

    static MorningViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new MorningViewHolder(view);
    }
}
