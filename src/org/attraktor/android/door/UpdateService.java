package org.attraktor.android.door;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateService extends Service {
    private final static String ERROR_MSG_FETCH = "Unable to fetch data.";
    private final static String ERROR_MSG_PARSE = "Unable to parse data.";
    private final static String JSON_DOOR_STATE = "doorStateChange";
    private final static String JSON_NET_STATE = "networkStateChange";
    private final static String JSON_STATE = "bState";
    private final static String JSON_NET_WLAN = "iwlanClients";
    private final static String JSON_NET_LAN = "iClients";
    private final static String TRUE = "true";
    private final static String FALSE = "false";
    private final static String MSG_LAN = " - LAN:";
    private final static String MSG_WLAN = "WLAN:";
    private final static String N = "n";
    private final static String URL = "http://winkekatzen.r3l4x.de/api/status";

    private final static HttpClient httpclient = AndroidHttpClient
            .newInstance("MexAndroidWidget");

    public static String message;
    public static int status = -1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        final JSONObject root = httpJsonGet();
        if (root != null) {
            message = ERROR_MSG_PARSE;
            try {
                Object o;

                if ((o = root.getJSONObject(JSON_DOOR_STATE)) != null)
                    o = ((JSONObject) o).getString(JSON_STATE);

                if (o != null && o.toString().equals(TRUE)) {
                    status = R.drawable.ic_unlocked;
                } else if (o != null && o.toString().equals(FALSE)) {
                    status = R.drawable.ic_locked;
                }

                final JSONObject jo = root.getJSONObject(JSON_NET_STATE);

                String client;
                if (jo != null
                        && (client = jo.getString(JSON_NET_WLAN)) != null)
                    message = MSG_WLAN + client;

                if (jo != null && (client = jo.getString(JSON_NET_LAN)) != null)
                    message = message + MSG_LAN + client;
            } catch (Exception e) {
            }
        } else {
            message = ERROR_MSG_FETCH;
        }

        if (status == -1)
            status = R.drawable.ic_unknown;

        updateWidget();
    }

    private void updateWidget() {
        final AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        final RemoteViews remoteViews = new RemoteViews(getApplicationContext()
                .getPackageName(), R.layout.status_widget);
        final ComponentName thisWidget = new ComponentName(
                getApplicationContext(), StatusWidgetProvider.class);

        remoteViews.setTextViewText(R.id.widget_text, message);
        remoteViews.setImageViewResource(R.id.widget_door_status, status);

        mgr.updateAppWidget(thisWidget, remoteViews);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private JSONObject httpJsonGet() {
        final HttpGet httpGet = new HttpGet(URL);
        try {
            final HttpEntity entity = httpclient.execute(httpGet).getEntity();
            if (entity == null)
                return null;

            final InputStream instream = entity.getContent();
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(instream), 2048);

            String line = null;
            final StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line + N);

            instream.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
        } finally {
            httpGet.abort();
        }
        return null; // error
    }
}
