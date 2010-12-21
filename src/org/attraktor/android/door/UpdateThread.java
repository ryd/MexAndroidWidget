package org.attraktor.android.door;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class UpdateThread extends Thread {
    private final JsonRequest jsonRequest = new JsonRequest();
    private final JsonParser jsonParser = new JsonParser();

    private final AppWidgetManager awm;
    private final RemoteViews remoteViews;
    private final ComponentName widget;

    public UpdateThread(Context context) {
        super(UpdateThread.class.getName());

        awm = AppWidgetManager.getInstance(context);
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.status_widget);
        widget = new ComponentName(context, StatusWidgetProvider.class);
    }

    @Override
    public void run() {
        super.run();
        updateWidget();
    }

    private void updateWidget() {
        jsonParser.parse(jsonRequest.httpJsonGet());

        remoteViews.setTextViewText(R.id.widget_text, jsonParser.message);
        remoteViews.setImageViewResource(R.id.widget_door_status,
                jsonParser.status);

        awm.updateAppWidget(widget, remoteViews);
    }
}
