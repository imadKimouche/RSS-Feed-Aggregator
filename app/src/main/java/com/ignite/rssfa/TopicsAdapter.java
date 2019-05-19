package com.ignite.rssfa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicHolder> {

    private List<Topic> mTopics;

    public TopicsAdapter(List<Topic> topics) {
        mTopics = topics;
    }

    @NonNull
    @Override

    public TopicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_topic, null);
        TopicHolder topicHolder = new TopicHolder(v);
        return topicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopicHolder topicHolder, int i) {
        Topic topic = mTopics.get(i);
        topicHolder.title.setText(topic.getTitle());
        if (topic.getImage().startsWith("http")) {
            new DownloadImageTask(topicHolder.image).execute(topic.getImage());
        } else {
            topicHolder.image.setImageResource(Integer.parseInt(topic.getImage()));
        }
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

    public static class TopicHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView title;

        private TopicHolder(View v) {
            super(v);
            this.image = v.findViewById(R.id.image);
            this.title = v.findViewById(R.id.title);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
