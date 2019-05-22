package com.ignite.rssfa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ignite.rssfa.db.entity.Feed;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    ListView mFeedList;
    List<RsskeeArticle> articleList = new ArrayList<>();
    List<Topic> mTopics = new ArrayList<>();
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = inflater.getContext();
        mFeedList = view.findViewById(R.id.feedList);
        RecyclerView rv = view.findViewById(R.id.topicsList);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTopics = Utils.fillTopics();
        TopicsAdapter adapterTopic = new TopicsAdapter(mContext, mTopics);
        rv.setAdapter(adapterTopic);
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapterTopic.setOnItemClickListener(new TopicsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Topic topic = mTopics.get(position);
                List<Feed> feeds = topic.getmFeeds();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("feeds", new ArrayList<>(feeds));
                MyFeedsFragment myFeedsFragment = new MyFeedsFragment();
                myFeedsFragment.setArguments(bundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFeedsFragment).addToBackStack("HomeFragment").commit();
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        HttpRequest.getRandomArticles(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray articles = new JSONArray(new String(responseBody));
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);
                        String content = article.get("description").toString();
                        String desc = content.substring(0, (content.length() > 100) ? 100 : content.length());
                        RsskeeArticle articleToLoad = new RsskeeArticle(article.get("title").toString(), desc, content, "", "", article.get("author").toString(), article.get("link").toString());
                        articleList.add(articleToLoad);
                        ArticleAdapter adapter = new ArticleAdapter(mContext, articleList);
                        mFeedList.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    Utils.shortToast(mContext, "Couldn't get the articles!");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                RsskeeArticle article = (RsskeeArticle) parent.getItemAtPosition(position);
                openRSSDetail(article);
            }
        });

        return view;
    }

    private void openRSSDetail(RsskeeArticle article) {
        Intent intent = new Intent(mContext, RSSDetail.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void openFeedDetail(com.ignite.rssfa.db.entity.Feed feed) {
        Intent intent = new Intent(mContext, MyFeedsFragment.class);
        startActivity(intent);
    }
}
