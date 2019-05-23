package com.ignite.rssfa;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "alreadyRead")
public class AlreadyRead implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "url")
    private String url;

    public AlreadyRead(String url) {
        this.url = url;
    }

    public AlreadyRead(RsskeeArticleSaved article) {
        this.url = article.getUrl();
    }

    private AlreadyRead(Parcel in) {
        url = in.readString();
    }

    public static final Creator<AlreadyRead> CREATOR = new Creator<AlreadyRead>() {
        @Override
        public AlreadyRead createFromParcel(Parcel in) {
            return new AlreadyRead(in);
        }

        @Override
        public AlreadyRead[] newArray(int size) {
            return new AlreadyRead[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    @Override
    public String toString() {

        return "Url: " + url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
