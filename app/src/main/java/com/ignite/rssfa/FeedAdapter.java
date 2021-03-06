package com.ignite.rssfa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import com.ignite.rssfa.db.entity.Feed;

public class FeedAdapter extends ArrayAdapter<Feed> {

    private List<Feed> mFeedList;

    public FeedAdapter(Context context, List<Feed> feeds) {
        super(context, 0, feeds);
        mFeedList = feeds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rss, parent, false);
        }

        FeedViewHolder viewHolder = (FeedViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new FeedViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.description = convertView.findViewById(R.id.text);
            viewHolder.picture = convertView.findViewById(R.id.picture);
            convertView.setTag(viewHolder);
        }

        Feed feed = mFeedList.get(position);
        if (feed != null) {
            viewHolder.title.setText(feed.getTitle());
            viewHolder.description.setText(feed.getDescription());
            if (feed.getImage() != null && !feed.getImage().equals("")) {
                new Utils.DownloadImageTask(viewHolder.picture).execute(feed.getImage());
            }
        }


        return convertView;
    }

    private class FeedViewHolder {
        public TextView title;
        public TextView description;
        public ImageView picture;
    }

    @Override
    public int getCount() {
        return mFeedList != null ? mFeedList.size() : 0;
    }

    public void setmFeedList(List<Feed> feeds) {
        mFeedList = feeds;
    }

    public void addFeed(Feed feed) {
        mFeedList.add(feed);
    }

}
