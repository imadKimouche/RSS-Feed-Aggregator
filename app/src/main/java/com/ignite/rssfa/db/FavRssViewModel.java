package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.ignite.rssfa.db.entity.RSS;

import java.util.List;

public class FavRssViewModel extends AndroidViewModel {

    private com.ignite.rssfa.db.FavRssRepository mRepository;

    private LiveData<List<RSS>> mFavRssList;

    public FavRssViewModel(Application application) {
        super(application);
        mRepository = new com.ignite.rssfa.db.FavRssRepository(application);
        mFavRssList = mRepository.getAllFavRss();
    }

    public LiveData<List<RSS>> getAllFavRss() {
        return mFavRssList;
    }

    public void insert(RSS rss) {
        mRepository.insert(rss);
    }
}