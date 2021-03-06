package com.ignite.rssfa;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

final class HttpRequest {
    private static final String BASE_URL = "http://rsskeyapi.eu-west-3.elasticbeanstalk.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void login(String username, String password, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("login", username);
        params.put("password", password);
        client.get(getAbsoluteUrl("users/authentification"), params, handler);
    }

    public static void createUser(Context context, String username, String email, String password, AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("login", username);
            jsonParams.put("email", email);
            jsonParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            StringEntity entity = new StringEntity(jsonParams.toString());
            Log.i("test", username + " : " + email + " : " + password);
            client.post(context, getAbsoluteUrl("users/create"), entity, "application/json", handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void getAllFeeds(String token, AsyncHttpResponseHandler handler) {
        client.removeAllHeaders();
        client.addHeader("token", token);
        client.get(getAbsoluteUrl("feeds/user "), handler);
    }

    public static void getRandomArticles(AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteUrl("feeds/randompicker"), handler);
    }

    public static void addFeed(Context context, String token, String rssUrl, AsyncHttpResponseHandler handler) {
        JSONObject jsonParams = new JSONObject();
        client.setConnectTimeout(90000);
        client.setTimeout(90000);
        client.setResponseTimeout(90000);
        try {
            jsonParams.put("url", rssUrl);
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.addHeader("token", token);
            client.post(context, getAbsoluteUrl("feeds/user/add"), entity, "application/json", handler);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFeed(int id, String token, AsyncHttpResponseHandler handler) {
        client.removeAllHeaders();
        client.addHeader("token", token);
        client.delete(getAbsoluteUrl("feeds/user/delete/" + id), handler);
    }


    public static void getFeed(int id, String token, AsyncHttpResponseHandler handler) {
        client.removeAllHeaders();
        client.addHeader("token", token);
        client.get(getAbsoluteUrl("feeds/" + id), handler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
