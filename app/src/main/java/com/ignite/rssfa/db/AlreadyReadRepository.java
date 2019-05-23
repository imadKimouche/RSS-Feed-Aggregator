package com.ignite.rssfa.db;

import android.app.Application;
import android.os.AsyncTask;

import com.ignite.rssfa.AlreadyRead;
import com.ignite.rssfa.db.dao.AlreadyReadDao;
import com.ignite.rssfa.db.AppDatabase;

import java.util.concurrent.ExecutionException;

public class AlreadyReadRepository {
    private AlreadyReadDao mAlreadyReadDao;

    AlreadyReadRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mAlreadyReadDao = db.AlreadyReadDao();
    }

    public Boolean insert(AlreadyRead article) throws ExecutionException, InterruptedException {
        AsyncTask<AlreadyRead, Void, Boolean> task = new insertAsyncTask(mAlreadyReadDao).execute(article);
        return task.get();
    }

    private static class insertAsyncTask extends AsyncTask<AlreadyRead, Void, Boolean> {

        private AlreadyReadDao mAsyncTaskDao;

        insertAsyncTask(AlreadyReadDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final AlreadyRead... params) {
            if (mAsyncTaskDao.findByUrl(params[0].getUrl()) != null) {
                return false;
            } else
                mAsyncTaskDao.insert(params[0]);
            return true;
        }
    }

    public Boolean alreadyReadExists(AlreadyRead article) throws ExecutionException, InterruptedException {
        AsyncTask<AlreadyRead, Void, Boolean> task = new existsAsyncTask(mAlreadyReadDao).execute(article);
        return task.get();
    }

    private static class existsAsyncTask extends AsyncTask<AlreadyRead, Void, Boolean> {

        private AlreadyReadDao mAsyncTaskDao;

        existsAsyncTask(AlreadyReadDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final AlreadyRead... params) {
            return mAsyncTaskDao.findByUrl(params[0].getUrl()) != null;
        }
    }

    public int count() throws ExecutionException, InterruptedException {
        AsyncTask<AlreadyRead, Void, Integer> task = new countAsyncTask(mAlreadyReadDao).execute();
        return task.get();
    }

    private static class countAsyncTask extends AsyncTask<AlreadyRead, Void, Integer> {

        private AlreadyReadDao mAsyncTaskDao;

        countAsyncTask(AlreadyReadDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(final AlreadyRead... params) {
            return mAsyncTaskDao.countAlreadyRead();
        }
    }
}
