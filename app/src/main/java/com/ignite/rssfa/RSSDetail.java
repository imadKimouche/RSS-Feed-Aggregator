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
import android.widget.TextView;
import android.widget.Toast;

import com.ignite.rssfa.db.FavRssViewModel;
import com.ignite.rssfa.db.entity.RSS;

import java.io.InputStream;

public class RSSDetail extends AppCompatActivity {

    private RsskeeArticle mArticle;
    FavRssViewModel mFavRssViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssdetail);
        Intent intent = getIntent();
        mArticle = intent.getParcelableExtra("article");
        setTitle(mArticle.getTitle());
        ImageView mPicture = findViewById(R.id.picture);
        TextView mTitle = findViewById(R.id.title);
        TextView mText = findViewById(R.id.text);
        mTitle.setText(mArticle.getTitle().equals("") ? "No title found" : mArticle.getTitle());
        mText.setText(mArticle.getContent().equals("") ? "No content found" : mArticle.getContent());
        if (!mArticle.getImage().equals("")) {
            new DownloadImageTask(mPicture).execute(mArticle.getImage());
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
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_save:
                Toast.makeText(this, "Article Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ab_favorite: {
                Boolean result = mFavRssViewModel.insert(mRss);
                item.setIcon(result ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
                Toast.makeText(this, result ? "Added Favorite" : "Removed Favorite", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }*/


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
