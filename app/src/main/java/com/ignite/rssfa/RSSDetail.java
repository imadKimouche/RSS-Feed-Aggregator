package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ignite.rssfa.AlreadyRead;
import com.ignite.rssfa.db.AlreadyReadViewModel;
import com.ignite.rssfa.db.FavArticleViewModel;
import com.ignite.rssfa.db.SavedArticleViewModel;

import java.text.MessageFormat;
import java.util.Objects;

public class RSSDetail extends AppCompatActivity {

    private RsskeeArticle mArticle;
    private FavArticleViewModel mFavArticleViewModel;
    private SavedArticleViewModel mSavedArticleViewModel;
    private AlreadyReadViewModel mAlreadyReadViewModel;
    private TextView mTitle;
    private TextView mContent;
    private LinearLayout mBackground;
    private Button visitWebSite;
    private TextView mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssdetail);
        Intent intent = getIntent();
        mArticle = intent.getParcelableExtra("article");
        ImageView mPicture = findViewById(R.id.picture);
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.text);
        mBackground = findViewById(R.id.background);
        visitWebSite = findViewById(R.id.visitWebSite);
        mDate = findViewById(R.id.date);
        mTitle.setText(mArticle.getTitle().equals("") ? "No title found" : mArticle.getTitle());
        mDate.setText(MessageFormat.format("{0}{1}{2}", mArticle.getPubDate(), mArticle.getPubDate().isEmpty() ? " By " : " by ", mArticle.getAuthor()));
        mContent.setText(mArticle.getContent().equals("") ? "No content found" : mArticle.getContent());
        if (mArticle.getImage() != null) {
            if (mArticle.getImage().startsWith("http")) {
                new Utils.DownloadImageTask(mPicture).execute(mArticle.getImage());
            }
        }
        mFavArticleViewModel = ViewModelProviders.of(this).get(FavArticleViewModel.class);
        mSavedArticleViewModel = ViewModelProviders.of(this).get(SavedArticleViewModel.class);
        mAlreadyReadViewModel = ViewModelProviders.of(this).get(AlreadyReadViewModel.class);
        AlreadyRead article = new AlreadyRead(mArticle.getUrl());
        mAlreadyReadViewModel.insert(article);

        visitWebSite.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.getUrl()));
            startActivity(browserIntent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rss_detail_action_bar, menu);
        MenuItem item = menu.findItem(R.id.switchDM);
        MenuItem favorite = menu.findItem(R.id.ab_favorite);
        MenuItem saved = menu.findItem(R.id.ab_save);
        item.setActionView(R.layout.show_protected_switch);
        Switch switchAB = item
                .getActionView().findViewById(R.id.switchAB);
        switchAB.setChecked(false);

        switchAB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBackground.setBackgroundColor(getResources().getColor(R.color.black));
                mTitle.setTextColor(getResources().getColor(R.color.white));
                mDate.setTextColor(getResources().getColor(R.color.white));
                mContent.setTextColor(getResources().getColor(R.color.white));
            } else {
                mBackground.setBackgroundColor(getResources().getColor(R.color.white));
                mTitle.setTextColor(getResources().getColor(R.color.black));
                mDate.setTextColor(getResources().getColor(R.color.black));
                mContent.setTextColor(getResources().getColor(R.color.black));
            }
        });
        favorite.setIcon(mFavArticleViewModel.exists(mArticle) ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
        saved.setIcon(mSavedArticleViewModel.exists(new RsskeeArticleSaved(mArticle)) ? R.drawable.ic_bookmark_enabled : R.drawable.ic_bookmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean result;
        switch (item.getItemId()) {
/*            case android.R.id.ab_return: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }*/
            case R.id.ab_save:
                RsskeeArticleSaved savedArticle = new RsskeeArticleSaved(mArticle);
                result = mSavedArticleViewModel.insert(savedArticle);
                item.setIcon(result ? R.drawable.ic_bookmark_enabled : R.drawable.ic_bookmark);
                Toast.makeText(this, "Article Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ab_favorite: {
                result = mFavArticleViewModel.insert(mArticle);
                item.setIcon(result ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
                Toast.makeText(this, result ? "Added Favorite" : "Removed Favorite", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.ab_share: {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mArticle.getUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mArticle.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                break;
            }
        }
        return true;
    }
}
