package com.example.powerise.db;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;


import java.util.Objects;

public class MorningListAdapter extends ListAdapter<Morning, MorningViewHolder> {

    public MorningListAdapter(@NonNull DiffUtil.ItemCallback<Morning> diffCallback) {
        super(diffCallback);
    }

    @Override
    public MorningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MorningViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(MorningViewHolder holder, int position) {
        Morning current = getItem(position);
        holder.bind(current.getMorning());
    }

    static public class MorningDiff extends DiffUtil.ItemCallback<Morning> {

        @Override
        public boolean areItemsTheSame(@NonNull Morning oldItem, @NonNull Morning newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Morning oldItem, @NonNull Morning newItem) {
            return Objects.equals(oldItem.getMorning(), newItem.getMorning());
        }
    }
}
