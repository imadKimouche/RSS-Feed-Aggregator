package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.ignite.rssfa.db.AppDatabase;
import com.ignite.rssfa.db.dao.FavoriteRssDao;
import com.ignite.rssfa.db.entity.RSS;
import java.util.List;

public class FavRssRepository {
    private FavoriteRssDao mFavRssDao;
    private LiveData<List<RSS>> mFavRssList;

    FavRssRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mFavRssDao = db.FavRssDao();
        mFavRssList = mFavRssDao.getAll();
    }

    LiveData<List<RSS>> getAllFavRss() {
        return mFavRssList;
    }

    public void insert (RSS rss) {
        new insertAsyncTask(mFavRssDao).execute(rss);
    }

    private static class insertAsyncTask extends AsyncTask<RSS, Void, Void> {

        private FavoriteRssDao mAsyncTaskDao;

        insertAsyncTask(FavoriteRssDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RSS... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
