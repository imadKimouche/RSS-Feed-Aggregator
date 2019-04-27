package com.ignite.rssfa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.ignite.rssfa.db.entity.RSS;

public class HomeFragment extends Fragment {

    ListView mFeedList;
    List<RSS> rssList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFeedList = view.findViewById(R.id.feedList);

        rssList.add(new RSS("picture", "RSS1", "rss feed text"));
        rssList.add(new RSS("picture", "RSS2", "rss feed text"));
        rssList.add(new RSS("picture", "RSS3", "rss feed text"));


        RSSAdapter adapter = new RSSAdapter(getActivity().getApplicationContext(), rssList);
        mFeedList.setAdapter(adapter);

        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                RSS rss = (RSS) parent.getItemAtPosition(position);
                openRSSDetail(rss);
            }
        });

        return view;
    }

    private void openRSSDetail(RSS rss) {
        Intent intent = new Intent(getActivity(), RSSDetail.class);
        intent.putExtra("rss", rss);
        startActivity(intent);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
