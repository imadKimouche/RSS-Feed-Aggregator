package com.ignite.rssfa;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ignite.rssfa.db.FavRssViewModel;
import com.ignite.rssfa.db.entity.RSS;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    ListView mFavoriteList;
    List<RSS> rssFavoriteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mFavoriteList = view.findViewById(R.id.favoriteList);
        RSSAdapter adapter = new RSSAdapter(getActivity().getApplicationContext(), rssFavoriteList);
        mFavoriteList.setAdapter(adapter);

        FavRssViewModel mFavRssViewModel = ViewModelProviders.of(this).get(FavRssViewModel.class);
        mFavRssViewModel.getAllFavRss().observe(this, new Observer<List<RSS>>() {
            @Override
            public void onChanged(@Nullable List<RSS> rssList) {

                rssFavoriteList.clear();
                rssFavoriteList.addAll(rssList);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
