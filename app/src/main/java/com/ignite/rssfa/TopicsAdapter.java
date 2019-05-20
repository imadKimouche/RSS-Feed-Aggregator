package com.ignite.rssfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicHolder> {

    private List<Topic> mTopics;
    private static ClickListener clickListener;
    private Context mContext;

    public TopicsAdapter(Context context, List<Topic> topics) {
        mContext = context;
        mTopics = topics;
    }

    @NonNull
    @Override

    public TopicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_topic, null);
        return new TopicHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicHolder topicHolder, int i) {
        Topic topic = mTopics.get(i);
        topicHolder.title.setText(topic.getTitle());
        int productImageId = topicHolder.image.getResources().getIdentifier(topic.getImage(), "drawable", mContext.getPackageName());
        Picasso.get()
                .load(productImageId)
                .fit()
                .centerCrop()
                .into(topicHolder.image);
    }

    @Override
    public int getItemCount() {
        return mTopics != null ? mTopics.size() : 0;
    }

    public List<Topic> getmTopics() {
        return mTopics;
    }

    public void setmTopics(List<Topic> mTopics) {
        this.mTopics = mTopics;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TopicsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    public static class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected ImageView image;
        protected TextView title;

        private TopicHolder(View v) {
            super(v);
            this.image = v.findViewById(R.id.image);
            this.title = v.findViewById(R.id.title);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }
}
