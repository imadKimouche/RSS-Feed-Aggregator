package com.ignite.rssfa;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfeeds, container, false);
        mFeedList = view.findViewById(R.id.feedList);
        FeedAdapter adapter = new FeedAdapter(getActivity(), feedList);
        mFeedList.setAdapter(adapter);
        mAddFeed = view.findViewById(R.id.add_feed);
        SessionManager sessionManager = new SessionManager(getActivity());

        if (this.getArguments() != null && this.getArguments().getParcelableArrayList("feeds") != null) {
            feedList = this.getArguments().getParcelableArrayList("feeds");
            adapter.setmFeedList(feedList);
            adapter.notifyDataSetChanged();
            mAddFeed.hide();
        } else {
            mAddFeed.show();
            MyFeedsViewModel mFeedsViewModel = ViewModelProviders.of(this).get(MyFeedsViewModel.class);
            if (sessionManager.checkLogin()) {
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
                                        feed.setImage(list.get(0).getImage());
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            adapter.addFeed(feed);
                                            adapter.notifyDataSetChanged();
                                        });
                                    }

                                    //what to do in case of error
                                    @Override
                                    public void onError(Exception e) {
                                        Log.i("error parsing feed", e.getMessage());
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
                        Utils.logError(new Object() {
                        }.getClass().getName(), statusCode, new String(responseBody));
                        if (statusCode == 401) {
                            Utils.longToast(getActivity(), "Login expired, please logout and log back in");
                        }
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
            feed.setArticles(new ArrayList<>());
            Parser parser = new Parser();
            parser.onFinish(new OnTaskCompleted() {

                @Override
                public void onTaskCompleted(List<Article> list) {
                    feed.setArticlesFromParser(list);
                    openFeedDetail(feed);
                }

                @Override
                public void onError(Exception e) {
                    Log.i("error parsing feed", e.getMessage());
                }
            });
            parser.execute(feed.getRssUrl());
        });

        mFeedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Feed feed = feedList.get(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + feed.getTitle());
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", (dialog, which) -> {
                    HttpRequest.deleteFeed(feed.getUid(), sessionManager.getUserDetails().get(SessionManager.KEY_access_token), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            feedList.remove(positionToRemove);
                            adapter.notifyDataSetChanged();
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
            }
        });

        mAddFeed.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add RSS Feed Link");

            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                mUrl = input.getText().toString();
                if (sessionManager.checkLogin()) {
                    String token = sessionManager.getUserDetails().get(SessionManager.KEY_access_token);
                    HttpRequest.addFeed(getActivity(), token, mUrl, new AsyncHttpResponseHandler() {
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
                                        feed.setImage(list.get(0).getImage());
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            adapter.addFeed(feed);
                                            adapter.notifyDataSetChanged();
                                        });
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.i("error parsing feed", e.getMessage());
                                    }
                                });
                                parser.execute(mUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Utils.shortToast(getActivity(), "RSS link added");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Utils.shortToast(getActivity(), "Failed to add link");
                        }
                    });
                } else {
                    Utils.shortToast(getActivity(), "Login first(Menu -> My Account)");
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        return view;
    }

    private void openFeedDetail(Feed feed) {
        Intent intent = new Intent(getActivity(), FeedDetail.class);
        intent.putExtra("feed", feed);
        startActivity(intent);
    }
}
