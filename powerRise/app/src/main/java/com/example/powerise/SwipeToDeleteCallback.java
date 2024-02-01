package com.example.powerise;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerise.db.morning.MorningListAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final MorningListAdapter adapter;
    private final Drawable icon;
    private final ColorDrawable background;

    public SwipeToDeleteCallback(MorningListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_delete); // Adjust to your icon
        this.background = new ColorDrawable(Color.RED); // Adjust to your background color
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; // Adjust to your needs

        if (dX > 0) { // Swipe to the right (Delete)
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            icon.setBounds(itemView.getLeft() + backgroundCornerOffset, itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2, itemView.getLeft() + backgroundCornerOffset + icon.getIntrinsicWidth(), itemView.getBottom() - (itemView.getHeight() - icon.getIntrinsicHeight()) / 2);
        } else if (dX < 0) { // Swipe to the left (Delete)
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            icon.setBounds(itemView.getRight() - backgroundCornerOffset - icon.getIntrinsicWidth(), itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2, itemView.getRight() - backgroundCornerOffset, itemView.getBottom() - (itemView.getHeight() - icon.getIntrinsicHeight()) / 2);
        } else {
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
