package com.ignite.rssfa;

public class RSS {
    private String picture;
    private String title;
    private String text;

    public RSS(String picture, String title, String text) {
        this.picture = picture;
        this.title = title;
        this.text = text;
    }

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
}
