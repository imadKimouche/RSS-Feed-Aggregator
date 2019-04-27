package com.ignite.rssfa;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;

import android.content.Context;

@RunWith(MockitoJUnitRunner.class)

public class RSSAdapterTest {

    private RSS mRss1;
    private RSS mRss2;
    private RSSAdapter mAdapter;

    @Before
    public void setUp() {
        ArrayList<RSS> data = new ArrayList<>();
        Context context = mock(Context.class);

        mRss1 = new RSS("picture1", "rss1", "rss1 text");
        mRss2 = new RSS("picture2", "rss2", "rss1 text");
        data.add(mRss1);
        data.add(mRss2);

        mAdapter = new RSSAdapter(context, data);
    }

    @Test
    public void testAdapterNotNull() {
        assertNotNull(mAdapter);
    }

    @Test
    public void testAdapterContentLength() {
        assertEquals(2, mAdapter.getCount());
    }
/*
    @Test
    public void testGetView() {
        View view = mAdapter.getView(0, null, null);
//        System.out.println("test");

        TextView title = view
                .findViewById(R.id.title);

        TextView text = view
                .findViewById(R.id.text);

        ImageView picture = view
                .findViewById(R.id.picture);

        assertNotNull("View is null. ", view);
        assertNotNull("Name TextView is null. ", title);
        assertNotNull("Number TextView is null. ", text);
        assertNotNull("Photo ImageView is null. ", picture);

    }*/
}