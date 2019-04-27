package com.ignite.rssfa.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "favoriteRss")
public class RSS implements Parcelable {

    public RSS(int uid, String picture, String title, String text) {
        this.uid = uid;
        this.picture = picture;
        this.title = title;
        this.text = text;
    }

    @Ignore
    public RSS(String picture, String title, String text) {
        this.picture = picture;
        this.title = title;
        this.text = text;
    }

    public RSS(Parcel parcel) {
        uid = parcel.readInt();
        picture = parcel.readString();
        title = parcel.readString();
        text = parcel.readString();
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "picture")
    private String picture;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(picture);
        dest.writeString(title);
        dest.writeString(text);
    }

    public static final Parcelable.Creator<RSS> CREATOR = new Parcelable.Creator<RSS>() {

        @Override
        public RSS createFromParcel(Parcel parcel) {
            return new RSS(parcel);
        }

        @Override
        public RSS[] newArray(int size) {
            return new RSS[0];
        }
    };

    @Override
    public String toString() {
        return "{RSS:" + title +
                " text=" + text +
                "}";
    }
}
