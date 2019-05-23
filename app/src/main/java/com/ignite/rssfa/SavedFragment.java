package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ignite.rssfa.db.FavArticleViewModel;
import com.ignite.rssfa.db.SavedArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private Context mContext;
    ListView mSavedList;
    List<RsskeeArticleSaved> articleSavedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        mContext = inflater.getContext();
        mSavedList = view.findViewById(R.id.savedList);
        SavedArticleAdapter adapter = new SavedArticleAdapter(mContext, articleSavedList);
        mSavedList.setAdapter(adapter);

        SavedArticleViewModel mSavedArticleViewModel = ViewModelProviders.of(this).get(SavedArticleViewModel.class);
        mSavedArticleViewModel.getAllSavedArticles().observe(this, articles -> {

            articleSavedList.clear();
            articleSavedList.addAll(articles);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
