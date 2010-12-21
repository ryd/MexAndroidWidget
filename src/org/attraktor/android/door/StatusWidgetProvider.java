package org.attraktor.android.door;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class StatusWidgetProvider extends AppWidgetProvider {
    private final static long RATE = 5 * 60 * 1000; // every 5 minutes
    private final static int alarmType = AlarmManager.ELAPSED_REALTIME;
    private AlarmManager am = null;
    private PendingIntent pi = null;
    public static UpdateThread thread = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager awm,
            int[] appWidgetIds) {
        thread = new UpdateThread(context);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        pi = PendingIntent.getBroadcast(context, 0, new Intent(context,
                UpdateService.class), 0);
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(alarmType, System.currentTimeMillis(), RATE, pi);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        am.cancel(pi);
        
        am = null;
        pi = null;
        thread = null;
    }
}
