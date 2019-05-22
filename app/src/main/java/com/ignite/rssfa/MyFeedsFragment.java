package com.ignite.rssfa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ignite.rssfa.db.MyFeedsViewModel;
import com.ignite.rssfa.db.entity.Feed;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.internal.Util;

public class MyFeedsFragment extends Fragment {

    ListView mFeedList;
    List<Feed> feedList = new ArrayList<>();
    FloatingActionButton mAddFeed;
    private String mUrl;
    private boolean fromHome;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfeeds, container, false);
        mContext = inflater.getContext();
        mFeedList = view.findViewById(R.id.feedList);
        FeedAdapter adapter = new FeedAdapter(mContext, feedList);
        mFeedList.setAdapter(adapter);
        mAddFeed = view.findViewById(R.id.add_feed);
        SessionManager sessionManager = new SessionManager(mContext);
        fromHome = this.getArguments() != null && this.getArguments().getParcelableArrayList("feeds") != null;
        if (fromHome) {
            feedList = this.getArguments().getParcelableArrayList("feeds");
            adapter.setmFeedList(feedList);
            adapter.notifyDataSetChanged();
            mAddFeed.hide();
        } else {
            mAddFeed.show();
//            MyFeedsViewModel mFeedsViewModel = ViewModelProviders.of(this).get(MyFeedsViewModel.class);
            if (sessionManager.checkLogin()) {
                final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.Spinner);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                String token = sessionManager.getUserDetails().get(SessionManager.KEY_access_token);
                HttpRequest.getAllFeeds(token, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray feeds = new JSONArray(new String(responseBody));
                            for (int i = 0; i < feeds.length(); i++) {
                                JSONObject feedObj = feeds.getJSONObject(i);
                                Feed feed = new Feed(feedObj.getInt("id"), feedObj.get("title").toString(), feedObj.get("link").toString(),
                                        feedObj.get("description").toString(), feedObj.get("language").toString(),
                                        feedObj.get("pubDate").toString(), feedObj.get("rssURL").toString(), "", new ArrayList<>());
                                Parser parser = new Parser();
                                parser.onFinish(new OnTaskCompleted() {

                                    @Override
                                    public void onTaskCompleted(List<Article> list) {
                                        String image;
                                        for (int j = 0; j < list.size(); j++) {
                                            image = list.get(j).getImage();
                                            if (image != null) {
                                                feed.setImage(image);
                                                break;
                                            }
                                        }
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            adapter.addFeed(feed);
                                            adapter.notifyDataSetChanged();
                                            progressDialog.dismiss();
                                        });
                                    }

                                    //what to do in case of error
                                    @Override
                                    public void onError(Exception e) {
                                        Log.i("error parsing feed", e.getMessage());
                                        progressDialog.dismiss();
                                    }
                                });
                                parser.execute(feed.getRssUrl());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (responseBody != null)
                            Utils.logError(new Object() {
                            }.getClass().getName(), statusCode, new String(responseBody));
                        if (statusCode == 401) {
                            Utils.longToast(mContext, getResources().getString(R.string.Login_expired));
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }

        /*mFeedsViewModel.getAllFeeds().observe(this, new Observer<List<Feed>>() {
            @Override
            public void onChanged(@Nullable List<Feed> feeds) {

                feedList.clear();
                feedList.addAll(feeds);
                adapter.notifyDataSetChanged();
            }
        });
*/
        mFeedList.setOnItemClickListener((parent, view1, position, id) -> {
            Feed feed = feedList.get(position);
            openFeedDetail(feed, sessionManager.getUserDetails().get(SessionManager.KEY_access_token));
        });

        mFeedList.setOnItemLongClickListener((parent, view12, position, id) -> {
            Feed feed = feedList.get(position);
            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
            adb.setTitle(getResources().getString(R.string.delete));
            adb.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete) + feed.getTitle());
            final int positionToRemove = position;
            adb.setNegativeButton(getResources().getString(R.string.cancel), null);
            adb.setPositiveButton("Ok", (dialog, which) -> {
                HttpRequest.deleteFeed(feed.getUid(), sessionManager.getUserDetails().get(SessionManager.KEY_access_token), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        feedList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        Utils.shortToast(mContext, getResources().getString(R.string.feed_has_been_removed));
                        Log.i("OnSuccess", new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("OnFailure", new String(responseBody));
                    }
                });
            });
            adb.show();

            return true;
        });

        mAddFeed.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(getResources().getString(R.string.add_rss_feed_link));

            final EditText input = new EditText(mContext);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                final ProgressDialog progressDialog = new ProgressDialog(mContext,
                        R.style.com_facebook_auth_dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.adding_rss_feed));
                progressDialog.show();
                mUrl = input.getText().toString();
                if (sessionManager.checkLogin()) {
                    String token = sessionManager.getUserDetails().get(SessionManager.KEY_access_token);
                    HttpRequest.addFeed(mContext, token, mUrl, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject feedObj = new JSONObject(new String(responseBody));
                                Feed feed = new Feed(feedObj.getInt("id"), feedObj.get("title").toString(), feedObj.get("link").toString(),
                                        feedObj.get("description").toString(), feedObj.get("language").toString(),
                                        feedObj.get("pubDate").toString(), feedObj.get("rssURL").toString(), "", new ArrayList<>());

                                Parser parser = new Parser();
                                parser.onFinish(new OnTaskCompleted() {

                                    @Override
                                    public void onTaskCompleted(List<Article> list) {
                                        String image;
                                        for (int j = 0; j < list.size(); j++) {
                                            image = list.get(j).getImage();
                                            if (image != null) {
                                                feed.setImage(image);
                                                break;
                                            }
                                        }
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            adapter.addFeed(feed);
                                            adapter.notifyDataSetChanged();
                                            progressDialog.dismiss();
                                        });
                                    }

                                    //what to do in case of error
                                    @Override
                                    public void onError(Exception e) {
                                        Log.i("error parsing feed", e.getMessage());
                                    }
                                });
                                parser.execute(feed.getRssUrl());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                            Utils.shortToast(mContext, getString(R.string.rss_feed_added));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.w("scode:", String.valueOf(statusCode));
                            if (responseBody != null)
                                Log.w("failed", new String(responseBody));
                            progressDialog.dismiss();
                            Utils.shortToast(mContext, getString(R.string.failed_to_add_link));
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Utils.shortToast(mContext, getString(R.string.login_first));
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

            builder.show();
        });

        return view;
    }

    private void openFeedDetail(Feed feed, String token) {
        Intent intent = new Intent(mContext, FeedDetail.class);
        intent.putExtra("feed", feed);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
