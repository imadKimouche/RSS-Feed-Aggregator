package com.ignite.rssfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RSSAdapter extends ArrayAdapter<RSS> {

    public RSSAdapter(Context context, List<RSS> RSSs) {
        super(context, 0, RSSs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rss,parent, false);
        }

        RSSViewHolder viewHolder = (RSSViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RSSViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.text = convertView.findViewById(R.id.text);
            viewHolder.picture = convertView.findViewById(R.id.picture);
            convertView.setTag(viewHolder);
        }

        RSS RSS = getItem(position);
        viewHolder.title.setText(RSS.getTitle());
        viewHolder.text.setText(RSS.getText());
        //viewHolder.picture.setImageDrawable();

        return convertView;
    }

    private class RSSViewHolder{
        public TextView title;
        public TextView text;
        public ImageView picture;
    }
}
