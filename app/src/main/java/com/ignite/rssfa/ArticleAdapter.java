package com.ignite.rssfa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import com.prof.rssparser.Article;

public class ArticleAdapter extends ArrayAdapter<RsskeeArticle> {

    private final List<RsskeeArticle> mArticleList;

    public ArticleAdapter(Context context, List<RsskeeArticle> articles) {
        super(context, 0, articles);
        mArticleList = articles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rss, parent, false);
        }

        ArticleViewHolder viewHolder = (ArticleViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ArticleViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.description = convertView.findViewById(R.id.text);
            viewHolder.picture = convertView.findViewById(R.id.picture);
            convertView.setTag(viewHolder);
        }

        // gets the item at the position 'position' of the list 'list[com.ignite.rssfa.db1.entity.RSS]'
        RsskeeArticle article = getItem(position);
        viewHolder.title.setText(article.getTitle());
        viewHolder.description.setText(article.getDescription());
        if (article.getImage() != null) {
            if (article.getImage().startsWith("http")) {
                new DownloadImageTask(viewHolder.picture).execute(article.getImage());
            }
        }
        return convertView;
    }

    private class ArticleViewHolder {
        public TextView title;
        public TextView description;
        public TextView content;
        public TextView author;
        public ImageView picture;
    }

    @Override
    public int getCount() {
        return mArticleList != null ? mArticleList.size() : 0;
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
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                mIcon11 = BitmapFactory.decodeStream(in, null, options);
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
