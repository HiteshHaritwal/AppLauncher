package com.java.applauncherexample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.java.applauncherexample.Adapters.AppListAdapter;
import com.java.applauncherexample.Model.AppList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppListActivity extends AppCompatActivity {

    @BindView(R.id.lst_app)
    RecyclerView lstApp;
    Context context;

    List<AppList> appLists;
    AppListAdapter appListAdapter;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_activity);
        ButterKnife.bind(this);
        context = AppListActivity.this;
        appLists = getAppList(context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        lstApp.setLayoutManager(linearLayoutManager);

        appListAdapter = new AppListAdapter(context, appLists);
        lstApp.setAdapter(appListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);

        MenuItem searchApp = menu.findItem(R.id.app_search);
        SearchView searchView = (SearchView)  searchApp.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                appListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    public List<AppList> getAppList(Context context) {
        progressbar.setVisibility(View.VISIBLE);
        PackageManager pManager = context.getPackageManager();

        List<AppList> appLists = new ArrayList<AppList>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allApps = pManager.queryIntentActivities(i, 0);


        for (ResolveInfo ri : allApps) {
            AppList app = new AppList();
            app.label = ri.loadLabel(pManager).toString();
            app.packageName = ri.activityInfo.packageName;

            app.icon = ri.activityInfo.loadIcon(pManager);
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(ri.activityInfo.packageName, 0);

                Intent intent = pManager.getLaunchIntentForPackage(ri.activityInfo.packageName);
                String activity = intent.getComponent().getClassName();
                int lastIndxDot = activity.lastIndexOf('.');
                if (lastIndxDot != -1) {
                    app.mainActiivtyName = activity.substring(lastIndxDot);
                }
                app.versionName = pInfo.versionName;
                app.versionCode = pInfo.versionCode;


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appLists.add(app);

        }
        Collections.sort(appLists, new Comparator<AppList>() {
            @Override
            public int compare(AppList o1, AppList o2) {
                return o1.label.compareTo(o2.label);
            }
        });
        progressbar.setVisibility(View.GONE);
        return appLists;
    }
}
