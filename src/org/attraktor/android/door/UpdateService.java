package org.attraktor.android.door;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (StatusWidgetProvider.thread != null)
            StatusWidgetProvider.thread.start();
    }
}
