package com.ignite.rssfa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TopicsFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    FloatingActionButton fabAddCat;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        mContext = getActivity().getApplicationContext();
        expListView = view.findViewById(R.id.navigationmenu);
        fabAddCat = view.findViewById(R.id.fab_add);

        retrieveTopics();

        fabAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        return view;
    }

    private void retrieveTopics() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");

        listDataChild.put(listDataHeader.get(0), top250);
    }

    private void addCategory() {
        listDataHeader = new ArrayList<String>();
        Intent intent = new Intent(mContext, AddCatActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                int picture = data.getIntExtra("picture", 0);
                listDataHeader.add(title);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

}
