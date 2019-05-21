package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ignite.rssfa.db.FavRssViewModel;
import com.ignite.rssfa.db.entity.RSS;

import java.io.InputStream;

public class RSSDetail extends AppCompatActivity {

    private RsskeeArticle mArticle;
    FavRssViewModel mFavRssViewModel;
    private TextView mTitle;
    private TextView mContent;
    private RelativeLayout mBackground;
    private boolean toggled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssdetail);
        Intent intent = getIntent();
        mArticle = intent.getParcelableExtra("article");
        setTitle(mArticle.getTitle());
        ImageView mPicture = findViewById(R.id.picture);
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.text);
        mBackground = findViewById(R.id.background);
        mTitle.setText(mArticle.getTitle().equals("") ? "No title found" : mArticle.getTitle());
        mContent.setText(mArticle.getContent().equals("") ? "No content found" : mArticle.getContent());
        if (mArticle.getImage() != null) {
            if (mArticle.getImage().startsWith("http")) {
                new Utils.DownloadImageTask(mPicture).execute(mArticle.getImage());
            }
        }
        //mFavRssViewModel = ViewModelProviders.of(this).get(FavRssViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rss_detail_action_bar, menu);
        MenuItem item = menu.getItem(0);
        //item.setIcon(mFavRssViewModel.exists(mRss) ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
/*            case R.id.ab_save:
                Toast.makeText(this, "Article Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ab_favorite: {
                Boolean result = mFavRssViewModel.insert(mRss);
                item.setIcon(result ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
                Toast.makeText(this, result ? "Added Favorite" : "Removed Favorite", Toast.LENGTH_SHORT).show();
                break;
            }*/
            case R.id.ab_share: {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mArticle.getUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mArticle.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                break;
            }
            case R.id.ab_darkMode: {
                if (!toggled) {
                    mBackground.setBackgroundColor(getResources().getColor(R.color.black));
                    mTitle.setTextColor(getResources().getColor(R.color.white));
                    mContent.setTextColor(getResources().getColor(R.color.white));
                    toggled = true;
                } else {
                    mBackground.setBackgroundColor(getResources().getColor(R.color.white));
                    mTitle.setTextColor(getResources().getColor(R.color.black));
                    mContent.setTextColor(getResources().getColor(R.color.black));
                    toggled = false;

                }
                break;
            }
        }
        return true;
    }
}
