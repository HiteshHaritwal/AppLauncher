package com.java.applauncherexample;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;

import com.java.applauncherexample.BroadcastReceivers.AppUnInstallReceiver;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnHome)
    ImageView btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        BroadcastReceiver br = new AppUnInstallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);

        registerReceiver(br, intentFilter);

    }

    @OnClick(R.id.btnHome)
    public void onViewClicked() {

        Intent intAppLst = new Intent(this, AppListActivity.class);
        startActivity(intAppLst);
    }
}