package com.java.applauncherexample.BroadcastReceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.java.applauncherexample.R;

public class AppUnInstallReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent!=null){
            String packageName = intent.getData().toString();
            int lastIndxDot = packageName.lastIndexOf(':')+1;
            if (lastIndxDot != -1) {
                packageName = packageName.substring(lastIndxDot);
            }
            //Log.e("packageName:- ", packageName);

            if(intent.getAction().equals(intent.ACTION_PACKAGE_REMOVED)){
                sentNotification(context, "App Uninstalled",  packageName);
           }else if(intent.getAction().equals(intent.ACTION_PACKAGE_ADDED)){
                String appName = getAppNameFromPkgName(context, packageName);
                sentNotification(context, "App Installed",  appName);
            }
        }
    }

    public static String getAppNameFromPkgName(Context context, String Packagename) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(Packagename, PackageManager.GET_META_DATA);
            String appName = (String) packageManager.getApplicationLabel(info);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return " No Name";
        }
    }

    public void sentNotification(Context context, String action, String detail){

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify=new Notification.Builder
                (context.getApplicationContext()).setContentTitle(action).setContentText(detail).
                setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, notify);
    }
}
