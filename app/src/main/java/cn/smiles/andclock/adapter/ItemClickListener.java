package cn.smiles.andclock.adapter;


import android.view.View;

// parent activity will implement this method to respond to click events
public interface ItemClickListener {
    void onItemClick(View view, int position);
}
