package org.attraktor.android.door;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class StatusWidgetProvider extends AppWidgetProvider {
    private final static long RATE = 300000;

    @Override
    public void onUpdate(Context context, AppWidgetManager awm,
            int[] appWidgetIds) {
        AlarmManager mgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, UpdateService.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), RATE, pi);
    }
}
