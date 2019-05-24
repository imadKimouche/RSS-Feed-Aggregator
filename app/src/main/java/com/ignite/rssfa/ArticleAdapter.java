package com.ignite.rssfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ignite.rssfa.db.AlreadyReadViewModel;
import com.ignite.rssfa.db.FavArticleViewModel;
import com.ignite.rssfa.db.SavedArticleViewModel;

import com.ignite.rssfa.AlreadyRead;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<RsskeeArticle> {

    private final List<RsskeeArticle> mArticleList;
    private AlreadyReadViewModel mAlreadyReadViewModel;
    private FavArticleViewModel mFavArticleViewModel;
    private SavedArticleViewModel mSavedArticleViewModel;
    private Context mContext;

    public ArticleAdapter(Context context, List<RsskeeArticle> articles) {
        super(context, 0, articles);
        mContext = context;
        mArticleList = articles;
        mAlreadyReadViewModel = ViewModelProviders.of((FragmentActivity) context).get(AlreadyReadViewModel.class);
        mFavArticleViewModel = ViewModelProviders.of((FragmentActivity) context).get(FavArticleViewModel.class);
        mSavedArticleViewModel = ViewModelProviders.of((FragmentActivity) context).get(SavedArticleViewModel.class);
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

        RsskeeArticle article = getItem(position);
        assert article != null;
        viewHolder.title.setText(article.getTitle());

        viewHolder.description.setText(String.format("%s %s / %s", mContext.getString(R.string.by), article.getAuthor(), Utils.dateDiff(mContext, article.getPubDate())));
        boolean alreadyRead = mAlreadyReadViewModel.exists(new AlreadyRead(article.getUrl()));
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
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            Activity activity = (FragmentActivity) mContext;
            final TextView favorite = new TextView(activity);
            final TextView saved = new TextView(activity);
            final LinearLayout layout = new LinearLayout(activity);
            favorite.setText(activity.getString(R.string.add_favorite));
            saved.setText(activity.getString(R.string.save_for_later));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    450, 100);

            layoutParams.setMargins(50, 40, 50, 30);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(favorite, layoutParams);
            layout.addView(saved, layoutParams);
            builder.setView(layout);
            AlertDialog ad = builder.show();

            favorite.setOnClickListener(v1 -> {
                mFavArticleViewModel.insert(article);
                ad.dismiss();
                Utils.longToast(mContext, activity.getString(R.string.added_favorite));
            });

            saved.setOnClickListener(v12 -> {
                mSavedArticleViewModel.insert(new RsskeeArticleSaved(article));
                ad.dismiss();
                Utils.longToast(mContext, activity.getString(R.string.article_saved_for_later));
            });
            return true;
        });

        ArticleViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(v -> {
            finalViewHolder.title.setTextColor(v.getResources().getColor(R.color.grey));
            finalViewHolder.description.setTextColor(v.getResources().getColor(R.color.grey));
            openRSSDetail(article);
        });

        return convertView;
    }

    private void openRSSDetail(RsskeeArticle article) {
        Intent intent = new Intent(mContext, RSSDetail.class);
        intent.putExtra("article", article);
        mContext.startActivity(intent);
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
}
