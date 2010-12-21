package org.attraktor.android.door;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class JsonRequest {
    private final static String N = "n";
    private final static String URL = "http://winkekatzen.r3l4x.de/api/status";

    private final static HttpClient httpclient = AndroidHttpClient
            .newInstance("MexAndroidWidget");

    public JSONObject httpJsonGet() {
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
            Log.e("MexWidget", "failed to fetch data - " + e.getMessage());
        } finally {
            httpGet.abort();
        }
        return null; // error
    }
}
