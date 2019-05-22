package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ignite.rssfa.RsskeeArticleSaved;
import com.ignite.rssfa.db.AppDatabase;
import com.ignite.rssfa.db.dao.SavedArticleDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SavedArticleRepository {
    private SavedArticleDao mSavedArticleDao;
    private LiveData<List<RsskeeArticleSaved>> mSavedArticles;

    SavedArticleRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mSavedArticleDao = db.SavedArticleDao();
        mSavedArticles = mSavedArticleDao.getAll();
    }

    public LiveData<List<RsskeeArticleSaved>> getAllSavedArticles() {
        return mSavedArticles;
    }

    public Boolean insert(RsskeeArticleSaved article) throws ExecutionException, InterruptedException {
        AsyncTask<RsskeeArticleSaved, Void, Boolean> task = new SavedArticleRepository.insertAsyncTask(mSavedArticleDao).execute(article);
        return task.get();
    }

    private static class insertAsyncTask extends AsyncTask<RsskeeArticleSaved, Void, Boolean> {

        private SavedArticleDao mAsyncTaskDao;

        insertAsyncTask(SavedArticleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RsskeeArticleSaved... params) {
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

    public Boolean savedArticleExists(RsskeeArticleSaved article) throws ExecutionException, InterruptedException {
        AsyncTask<RsskeeArticleSaved, Void, Boolean> task = new SavedArticleRepository.existsAsyncTask(mSavedArticleDao).execute(article);
        return task.get();
    }

    private static class existsAsyncTask extends AsyncTask<RsskeeArticleSaved, Void, Boolean> {

        private SavedArticleDao mAsyncTaskDao;

        existsAsyncTask(SavedArticleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RsskeeArticleSaved... params) {
            return mAsyncTaskDao.findByTitle(params[0].getTitle()) != null;
        }
    }
}
