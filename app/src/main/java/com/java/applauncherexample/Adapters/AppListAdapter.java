package com.java.applauncherexample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.applauncherexample.Model.AppList;
import com.java.applauncherexample.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListHolder> implements Filterable {

    Context context;
    private List<AppList> appLists;
    private List<AppList> appListFilter;

    public AppListAdapter(Context context, List<AppList> appLists) {
        this.context = context;
        this.appLists = appLists;
        appListFilter = new ArrayList<>(appLists);
    }

    @NonNull
    @Override
    public AppListAdapter.AppListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.app_list_row, null);
        AppListHolder appListwHolder = new AppListHolder(view);
        return appListwHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.AppListHolder holder, int position) {

        holder.appIcon.setImageDrawable(appLists.get(position).icon);
        holder.appTitle.setText(appLists.get(position).label);
        holder.appPackage.setText(appLists.get(position).packageName);
        holder.activityName.setText("Launcher Activity :- "+appLists.get(position).mainActiivtyName);
        holder.verName.setText("Version Name :- "+appLists.get(position).versionName);
        holder.verCode.setText("Version Code :- "+appLists.get(position).versionCode);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appLists.get(position).packageName);
                context.startActivity( launchIntent );

            }
        });
    }

    @Override
    public int getItemCount() {
        return appLists.size();
    }

    @Override
    public Filter getFilter() {
        return filterItems;
    }

    private Filter filterItems = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<AppList> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(appListFilter);
            }else{

                String filterPattern  = constraint.toString().toLowerCase().trim();

                for(AppList item : appListFilter){

                    if(item.label.toString().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            appLists.clear();
            appLists.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    class AppListHolder extends RecyclerView.ViewHolder {
        // init the item view's
        private TextView appTitle, appPackage, activityName, verName, verCode;
        private ImageView appIcon;

        public AppListHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            // initializing our variables.
            appIcon = itemView.findViewById(R.id.imgAppIcon);
            appTitle = itemView.findViewById(R.id.tvAppTitle);
            appPackage = itemView.findViewById(R.id.tvAppPackage);
            activityName = itemView.findViewById(R.id.tvLaunchActivity);
            verName = itemView.findViewById(R.id.tvVerName);
            verCode = itemView.findViewById(R.id.tvVerCode);
        }
    }
}
