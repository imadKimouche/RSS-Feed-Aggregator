package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.ignite.rssfa.db.entity.RSS;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public Boolean insert(RSS rss) {
        Boolean result = false;
        try {
            result = mRepository.insert(rss);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean exists(RSS rss) {
        Boolean result = false;
        try {
            result = mRepository.RssFavExists(rss);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}