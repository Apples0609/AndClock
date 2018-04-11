package cn.smiles.andclock.adapter;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.smiles.andclock.R;
import cn.smiles.andclock.tools.Ktools;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private final Activity activity;
    private List<ApplicationInfo> mDataset;
    private final PackageManager packageManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerViewAdapter(Activity activity, List<ApplicationInfo> myDataset) {
        this.activity = activity;
        this.mDataset = myDataset;
        packageManager = activity.getPackageManager();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snippet_list_row, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ApplicationInfo applicationInfo = mDataset.get(position);

        holder.app_icon.setImageDrawable(applicationInfo.loadIcon(packageManager));
        holder.app_name.setText(applicationInfo.loadLabel(packageManager));
        if ((applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
            // It is a system app
            holder.app_package.setText(Html.fromHtml(applicationInfo.packageName + "<font color='red'>*</font>"));
        } else {
            // It is installed by the user
            holder.app_package.setText(applicationInfo.packageName);
        }
        holder.view.setOnClickListener(v -> {
            if (activity.getPackageName().equals(applicationInfo.packageName)) {
                Toast.makeText(activity, "^_^点击自己^_^", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean b = Ktools.runAPPByPackageName(activity, applicationInfo.packageName);
            if (!b) {
                Toast.makeText(activity, "此APP没有Launcher Activity", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        // each data item is just a string in this case
        public ImageView app_icon;
        public TextView app_name;
        public TextView app_package;

        ViewHolder(View v) {
            super(v);
            view = v;
            app_icon = v.findViewById(R.id.app_icon);
            app_name = v.findViewById(R.id.app_name);
            app_package = v.findViewById(R.id.app_package);
        }
    }

}
