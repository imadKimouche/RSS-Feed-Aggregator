package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.ignite.rssfa.db.AppDatabase;
import com.ignite.rssfa.db.dao.FavoriteRssDao;
import com.ignite.rssfa.db.entity.RSS;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public Boolean insert(RSS rss) throws ExecutionException, InterruptedException {
        AsyncTask<RSS, Void, Boolean> task = new insertAsyncTask(mFavRssDao).execute(rss);
        Boolean result = task.get();
        return result;
    }

    private static class insertAsyncTask extends AsyncTask<RSS, Void, Boolean> {

        private FavoriteRssDao mAsyncTaskDao;

        insertAsyncTask(FavoriteRssDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RSS... params) {
            if (mAsyncTaskDao.findByTitle(params[0].getTitle()) != null) {
                mAsyncTaskDao.deleteByTitle(params[0].getTitle());
                return false;
            } else
                mAsyncTaskDao.insert(params[0]);
            return true;
        }

/*        @Override // Maybe use next
        protected void onPostExecute(Boolean result) {
            Log.i("DEBUG", result ? "Rss inserted" : "Rss Not inserted");
        }*/
    }

    public Boolean RssFavExists(RSS rss) throws ExecutionException, InterruptedException {
        AsyncTask<RSS, Void, Boolean> task = new existsAsyncTask(mFavRssDao).execute(rss);
        Boolean result = task.get();
        return result;
    }

    private static class existsAsyncTask extends AsyncTask<RSS, Void, Boolean> {

        private FavoriteRssDao mAsyncTaskDao;

        existsAsyncTask(FavoriteRssDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RSS... params) {
            return mAsyncTaskDao.findByTitle(params[0].getTitle()) != null;
        }
    }
}
