package com.ignite.rssfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.ignite.rssfa.db.entity.RSS;

public class RSSAdapter extends ArrayAdapter<RSS> {

    private final List<RSS> mRSSList;

    public RSSAdapter(Context context, List<RSS> RSSs) {
        super(context, 0, RSSs);
        mRSSList = RSSs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rss, parent, false);
        }

        RSSViewHolder viewHolder = (RSSViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new RSSViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.text = convertView.findViewById(R.id.text);
            viewHolder.picture = convertView.findViewById(R.id.picture);
            convertView.setTag(viewHolder);
        }

        // gets the item at the position 'position' of the list 'list[com.ignite.rssfa.db1.entity.RSS]'
        RSS RSS = getItem(position);
        viewHolder.title.setText(RSS.getTitle());
        viewHolder.text.setText(RSS.getText());
        //viewHolder.picture.setImageDrawable();

        return convertView;
    }

    private class RSSViewHolder {
        public TextView title;
        public TextView text;
        public ImageView picture;
    }

    @Override
    public int getCount() {
        return mRSSList != null ? mRSSList.size() : 0;
    }
}
