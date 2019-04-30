package com.ignite.rssfa;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Arrays;

public class FeedManager {
    private static final String BASE_URL = "https://api.contify.com/v2.1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getTopics() {
        client.get(getAbsoluteUrl("topics"), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.i("REQUEST START", "REQUEST STARTING");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.i("STATUS CODE", String.valueOf(statusCode));
                Log.i("Body", Arrays.toString(responseBody));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("STATUS CODE", String.valueOf(statusCode));
                Log.i("Body", Arrays.toString(responseBody));
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
