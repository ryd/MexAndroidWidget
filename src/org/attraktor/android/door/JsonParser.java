package org.attraktor.android.door;

import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
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

    public String message;
    public int status = -1;
    
    public void parse(JSONObject root) {
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
                Log.e("MexWidget", "failed to parse data - " + e.getMessage());
            }
        } else {
            message = ERROR_MSG_FETCH;
        }

        if (status == -1)
            status = R.drawable.ic_unknown;
    }
}
