package com.ignite.rssfa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCatActivity extends AppCompatActivity {

    private int mSelectedImage;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);
        TextInputEditText text = findViewById(R.id.title);
        FloatingActionButton fab_accept = findViewById(R.id.accept);

        int[] imageList = new int[]{
                R.drawable.ic_topic, R.drawable.ic_bookmark
        };

        // Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < imageList.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("image", Integer.toString(imageList[i]));
            aList.add(hm);
        }
        String[] from = {"image"};
        int[] to = {R.id.image};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.imagelistitem, from, to);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedImage = i;

            }
        });

        fab_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle = text.getText().toString();
                Intent intent = new Intent();
                Log.i("putting", String.valueOf(mSelectedImage));
                intent.putExtra("picture", mSelectedImage);
                intent.putExtra("title", mTitle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
