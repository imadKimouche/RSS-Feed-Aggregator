package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import com.ignite.rssfa.db.AlreadyReadViewModel;
import com.prof.rssparser.Article;

public class SavedArticleAdapter extends ArrayAdapter<RsskeeArticleSaved> {

    private final List<RsskeeArticleSaved> mArticleList;
    private Context mContext;
    private AlreadyReadViewModel mAlreadyReadViewModel;

    public SavedArticleAdapter(Context context, List<RsskeeArticleSaved> articles) {
        super(context, 0, articles);
        mContext = context;
        mArticleList = articles;
        mAlreadyReadViewModel = ViewModelProviders.of((FragmentActivity) context).get(AlreadyReadViewModel.class);
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

        RsskeeArticleSaved article = getItem(position);
        viewHolder.title.setText(article.getTitle());
        viewHolder.description.setText(String.format("%s %s / %s", mContext.getString(R.string.by), article.getAuthor(), Utils.dateDiff(mContext, article.getPubDate())));
        boolean alreadyRead = mAlreadyReadViewModel.exists(new com.ignite.rssfa.AlreadyRead(article.getUrl()));
        viewHolder.title.setTextColor(alreadyRead ?
                convertView.getResources().getColor(R.color.grey) :
                convertView.getResources().getColor(R.color.black));
        viewHolder.description.setTextColor(alreadyRead ?
                convertView.getResources().getColor(R.color.grey) :
                convertView.getResources().getColor(R.color.black));
        if (article.getImage() != null) {
            if (article.getImage().startsWith("http")) {
                new Utils.DownloadImageTask(viewHolder.picture).execute(article.getImage());
            }
        }

        convertView.setOnLongClickListener(v -> {
            Log.w("click", "clicked");
            return true;
        });

        convertView.setOnClickListener(v -> {
            RsskeeArticleSaved savedArticle = mArticleList.get(position);
            openRSSDetail(savedArticle);
        });

        return convertView;
    }

    private class ArticleViewHolder {
        public TextView title;
        private TextView description;
        public TextView content;
        public TextView author;
        public ImageView picture;
    }

    @Override
    public int getCount() {
        return mArticleList != null ? mArticleList.size() : 0;
    }

    private void openRSSDetail(RsskeeArticleSaved savedArticle) {
        RsskeeArticle article = new RsskeeArticle(savedArticle);
        Intent intent = new Intent(mContext, RSSDetail.class);
        intent.putExtra("article", article);
        mContext.startActivity(intent);
    }
}
