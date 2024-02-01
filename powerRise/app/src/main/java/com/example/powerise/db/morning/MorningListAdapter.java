package com.example.powerise.db.morning;

import android.content.Context;
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
        holder.bind(current.getMorning(holder.itemView.getContext()));  // Übergebe den Kontext der RecyclerView-Elementansicht
    }


    static public class MorningDiff extends DiffUtil.ItemCallback<Morning> {
        private final Context context;

        public MorningDiff(Context context) {
            this.context = context;
        }

        @Override
        public boolean areItemsTheSame(@NonNull Morning oldItem, @NonNull Morning newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Morning oldItem, @NonNull Morning newItem) {
            return Objects.equals(oldItem.getMorning(context), newItem.getMorning(context));
        }
    }


}
