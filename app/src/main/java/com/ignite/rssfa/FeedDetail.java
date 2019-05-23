package com.ignite.rssfa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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

public class FeedDetail extends AppCompatActivity {

    ListView mArticleList;
    private Feed mFeed;
    private List<RsskeeArticle> mArticles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);
        Intent intent = getIntent();
        mFeed = intent.getParcelableExtra("feed");
        String token = intent.getStringExtra("token");
        setTitle(mFeed.getTitle());
        mArticleList = findViewById(R.id.articleList);
        ArticleAdapter adapter = new ArticleAdapter(this, mArticles);
        mArticleList.setAdapter(adapter);
        final ProgressDialog progressDialog = new ProgressDialog(FeedDetail.this, R.style.Spinner);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        HttpRequest.getFeed(mFeed.getUid(), token, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject feedObj;
                try {
                    if (responseBody.length == 0) {
                        return;
                    }
                    feedObj = new JSONObject(new String(responseBody));
                    JSONArray articles = feedObj.getJSONArray("items");
                    RsskeeArticle article;
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject ja = articles.getJSONObject(i);
                        article = new RsskeeArticle(ja.get("title").toString(), ja.get("description").toString(), "", "", "", ja.get("author").toString(), ja.get("link").toString());
                        mArticles.add(article);
                    }
                    Parser parser = new Parser();
                    parser.onFinish(new OnTaskCompleted() {

                        @Override
                        public void onTaskCompleted(List<Article> list) {
                            for (int j = 0; j < list.size(); j++) {
                                mArticles.get(j).setImage(list.get(j).getImage());
                            }
                            new Handler(Looper.getMainLooper()).post(() -> {
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.i("error parsing feed", e.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                    parser.execute(mFeed.getRssUrl());
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                if (responseBody != null)
                    Log.w("getFeed", new String(responseBody));
            }
        });

        mArticleList.setOnItemClickListener((parent, view, position, id) -> {
            RsskeeArticle article = mArticles.get(position);
            openRSSDetail(article);
        });
    }

    private void openRSSDetail(RsskeeArticle article) {
        Intent intent = new Intent(this, RSSDetail.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }
}