package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.ignite.rssfa.AlreadyRead;
import com.ignite.rssfa.db.AlreadyReadRepository;

import java.util.concurrent.ExecutionException;

public class AlreadyReadViewModel extends AndroidViewModel {

    private AlreadyReadRepository mRepository;

    public AlreadyReadViewModel(Application application) {
        super(application);
        mRepository = new AlreadyReadRepository(application);
    }

    public Boolean insert(AlreadyRead article) {
        Boolean result = false;
        try {
            result = mRepository.insert(article);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean exists(AlreadyRead article) {
        Boolean result = false;
        try {
            result = mRepository.alreadyReadExists(article);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int count() {
        int result = 0;
        try {
            result = mRepository.count();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}