package com.ignite.rssfa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.List;

import static com.ignite.rssfa.FeedManager.getTopics;

public class TopicsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private FeedManager mFeedManager;
    private ListView mTopics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        mTopics = view.findViewById(R.id.topicsList);
        mFeedManager = new FeedManager(getActivity().getApplicationContext());

        getTopics(new Callback<List<String>>() {
            public void onResponse(List<String> response) {
                if (response != null) {
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, response);
                    mTopics.setAdapter(ad);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSwipeLayout = view.findViewById(R.id.swiperefresh);
        mSwipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getTopics(new Callback<List<String>>() {
            public void onResponse(List<String> response) {
                if (response != null) {
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, response);
                    mTopics.setAdapter(ad);
                    mSwipeLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });
    }
}
