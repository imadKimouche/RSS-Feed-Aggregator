package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ignite.rssfa.db.dao.MyFeedsDao;
import com.ignite.rssfa.db.entity.Feed;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyFeedsRepository {
    private MyFeedsDao mFeedsDao;
    private LiveData<List<Feed>> mFeedList;

    MyFeedsRepository(Application application) {
        com.ignite.rssfa.db.AppDatabase db = com.ignite.rssfa.db.AppDatabase.getInstance(application);
        mFeedsDao = db.MyFeedsDao();
        mFeedList = mFeedsDao.getAll();
    }

    LiveData<List<Feed>> getAllFeeds() {
        return mFeedList;
    }

    public Boolean insert(Feed feed) throws ExecutionException, InterruptedException {
        AsyncTask<Feed, Void, Boolean> task = new insertAsyncTask(mFeedsDao).execute(feed);
        Boolean result = task.get();
        return result;
    }

    private static class insertAsyncTask extends AsyncTask<Feed, Void, Boolean> {

        private MyFeedsDao mAsyncTaskDao;

        insertAsyncTask(MyFeedsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Feed... params) {
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

    public Boolean feedExists(Feed feed) throws ExecutionException, InterruptedException {
        AsyncTask<Feed, Void, Boolean> task = new existsAsyncTask(mFeedsDao).execute(feed);
        Boolean result = task.get();
        return result;
    }

    private static class existsAsyncTask extends AsyncTask<Feed, Void, Boolean> {

        private MyFeedsDao mAsyncTaskDao;

        existsAsyncTask(MyFeedsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Feed... params) {
            return mAsyncTaskDao.findByTitle(params[0].getTitle()) != null;
        }
    }
}
