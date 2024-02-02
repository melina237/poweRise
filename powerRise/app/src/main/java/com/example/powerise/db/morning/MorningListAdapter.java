package com.example.powerise.db.morning;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerise.db.RoomDatabase;

import java.util.Objects;

public class MorningListAdapter extends ListAdapter<Morning, MorningViewHolder> {

    private final Context context;

    public MorningListAdapter(@NonNull DiffUtil.ItemCallback<Morning> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public MorningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MorningViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(MorningViewHolder holder, int position) {
        Morning current = getItem(position);
        holder.bind(current.getMorning(context));
    }

    public Context getContext() {
        return context;
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

    public void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Morning morningToDelete = getItem(position);

            new DeleteMorningAsyncTask(context).execute(morningToDelete);
        }
    }

    private static class DeleteMorningAsyncTask extends AsyncTask<Morning, Void, Void> {
        private Context context;

        DeleteMorningAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Morning... mornings) {
            // Perform the delete operation in the background
            RoomDatabase.getDatabase(context).morningDao().deleteMorning(mornings[0]);
            return null;
        }
    }

}
