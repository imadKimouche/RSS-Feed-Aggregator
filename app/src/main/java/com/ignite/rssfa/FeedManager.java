package com.ignite.rssfa;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FeedManager {
    private static final String BASE_URL = "https://api.contify.com/v2.1/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private Context mContext;

    FeedManager(Context mContext) {
        this.mContext = mContext;
    }

    public static void getTopics(final Callback<List<String>> callback) {

        List<String> topics = new ArrayList<>();
        client.addHeader("Accept", "application/json");
        client.addHeader("APPID", "5f04e579");
        client.addHeader("APPSECRET", "439447ba1cd991c7bfcc4f04ddb9ab0f");
        client.get(getAbsoluteUrl("topics"), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("STATUS CODE", String.valueOf(statusCode));
                try {
                    JSONArray array = new JSONObject(new String(responseBody)).getJSONArray("standard");
                    for (int i = 0; i < array.length(); i++) {
                        topics.add((String) ((JSONObject) array.get(i)).get("name"));
                    }
                    if (callback != null) {
                        callback.onResponse(topics);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                callback.onResponse(null);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
