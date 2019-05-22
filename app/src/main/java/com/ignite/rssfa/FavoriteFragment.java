package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ignite.rssfa.db.FavArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    ListView mFavoriteList;
    List<RsskeeArticle> articleFavoriteList = new ArrayList<>();
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mContext = inflater.getContext();
        mFavoriteList = view.findViewById(R.id.favoriteList);
        ArticleAdapter adapter = new ArticleAdapter(mContext, articleFavoriteList);
        mFavoriteList.setAdapter(adapter);

        FavArticleViewModel mFavRssViewModel = ViewModelProviders.of(this).get(FavArticleViewModel.class);
        mFavRssViewModel.getAllFavRss().observe(this, articles -> {

            articleFavoriteList.clear();
            articleFavoriteList.addAll(articles);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
