package com.ignite.rssfa;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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

public class RSSDetail extends AppCompatActivity {

    private RSS mRss;
    FavRssViewModel mFavRssViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssdetail);
        Intent intent = getIntent();
        mRss = intent.getParcelableExtra("rss");
        setTitle(mRss.getTitle());
        ImageView mPicture = findViewById(R.id.picture);
        TextView mTitle = findViewById(R.id.title);
        TextView mText = findViewById(R.id.text);
        mTitle.setText(mRss.getTitle());
        mText.setText(mRss.getText());
        mFavRssViewModel = ViewModelProviders.of(this).get(FavRssViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rss_detail_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setIcon(mFavRssViewModel.exists(mRss) ? R.drawable.ic_favorite_enabled : R.drawable.ic_favorite);
        return true;
    }

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
    }
}
