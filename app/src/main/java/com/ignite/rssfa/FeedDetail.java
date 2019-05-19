package com.ignite.rssfa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ignite.rssfa.db.entity.Feed;
import com.ignite.rssfa.db.entity.RSS;

import java.util.List;

public class FeedDetail extends AppCompatActivity {

    ListView mArticleList;
    private Feed mFeed;
    private List<RsskeeArticle> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);
        Intent intent = getIntent();
        mFeed = intent.getParcelableExtra("feed");
        setTitle(mFeed.getTitle());

        mArticleList = findViewById(R.id.articleList);
        mArticles = mFeed.getArticles();
        Log.i("article", String.valueOf(mArticles.size()));
        ArticleAdapter adapter = new ArticleAdapter(this, mArticles);
        mArticleList.setAdapter(adapter);

        mArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                RsskeeArticle article = mArticles.get(position);
                openRSSDetail(article);
            }
        });
    }

    private void openRSSDetail(RsskeeArticle article) {
        Intent intent = new Intent(this, RSSDetail.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }
}