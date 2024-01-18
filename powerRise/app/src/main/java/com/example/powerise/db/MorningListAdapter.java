package com.example.powerise.db;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

public class MorningListAdapter extends ListAdapter<com.example.powerise.db.Morning, com.example.powerise.db.MorningViewHolder> {

    public MorningListAdapter(@NonNull DiffUtil.ItemCallback<com.example.powerise.db.Morning> diffCallback) {
        super(diffCallback);
    }

    @Override
    public com.example.powerise.db.MorningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return com.example.powerise.db.MorningViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(com.example.powerise.db.MorningViewHolder holder, int position) {
        com.example.powerise.db.Morning current = getItem(position);
        holder.bind(current.getmorning());
    }

    static class morningDiff extends DiffUtil.ItemCallback<com.example.powerise.db.Morning> {

        @Override
        public boolean areItemsTheSame(@NonNull com.example.powerise.db.Morning oldItem, @NonNull com.example.powerise.db.Morning newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull com.example.powerise.db.Morning oldItem, @NonNull com.example.powerise.db.Morning newItem) {
            return Objects.equals(oldItem.getmorning(), newItem.getmorning());
        }
    }
}
