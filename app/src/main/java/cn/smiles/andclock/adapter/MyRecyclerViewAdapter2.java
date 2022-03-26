package cn.smiles.andclock.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.smiles.andclock.R;

public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder> {
    private final Context context;
    private List<ApplicationInfo> mDataset;
    private final PackageManager packageManager;
    private ItemClickListener mClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerViewAdapter2(Context context, List<ApplicationInfo> myDataset) {
        this.context = context;
        this.mDataset = myDataset;
        packageManager = context.getPackageManager();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerViewAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snippet_list_row, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ApplicationInfo applicationInfo = mDataset.get(position);

        holder.app_icon.setImageDrawable(applicationInfo.loadIcon(packageManager));
        holder.app_name.setText(applicationInfo.loadLabel(packageManager));
        holder.app_package.setText(applicationInfo.packageName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        // each data item is just a string in this case
        public ImageView app_icon;
        public TextView app_name;
        public TextView app_package;

        public ViewHolder(View v) {
            super(v);
            view = v;
            app_icon = v.findViewById(R.id.app_icon);
            app_name = v.findViewById(R.id.app_name);
            app_package = v.findViewById(R.id.app_package);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
