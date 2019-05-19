package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ignite.rssfa.db.entity.Feed;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyFeedsViewModel extends AndroidViewModel {

    private com.ignite.rssfa.db.MyFeedsRepository mRepository;

    private LiveData<List<Feed>> mFeedList;

    public MyFeedsViewModel(Application application) {
        super(application);
        mRepository = new com.ignite.rssfa.db.MyFeedsRepository(application);
        mFeedList = mRepository.getAllFeeds();
    }

    public LiveData<List<Feed>> getAllFeeds() {
        return mFeedList;
    }

    public Boolean insert(Feed feed) {
        Boolean result = false;
        try {
            result = mRepository.insert(feed);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean exists(Feed feed) {
        Boolean result = false;
        try {
            result = mRepository.feedExists(feed);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}