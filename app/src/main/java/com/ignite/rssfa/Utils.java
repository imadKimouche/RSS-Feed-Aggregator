package com.ignite.rssfa;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void longToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static List<Topic> fillTopics() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("News", Integer.toString(R.drawable.ic_add), new ArrayList<>()));
        topics.add(new Topic("Science", "https://images.unsplash.com/photo-1486825586573-7131f7991bdd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", new ArrayList<>()));
        topics.add(new Topic("Health", "https://images.unsplash.com/photo-1485527172732-c00ba1bf8929?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", new ArrayList<>()));
        topics.add(new Topic("Technology", "https://images.unsplash.com/photo-1478358161113-b0e11994a36b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80", new ArrayList<>()));
        topics.add(new Topic("Environment", "https://images.unsplash.com/photo-1420666906485-bd11880afb89?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1674&q=80", new ArrayList<>()));
        topics.add(new Topic("Society", "https://images.unsplash.com/photo-1547944569-e62f69839c6e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=658&q=80", new ArrayList<>()));
        topics.add(new Topic("Strange", "https://images.unsplash.com/photo-1549773899-4720ed8c0c93?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=637&q=80", new ArrayList<>()));
        topics.add(new Topic("All", "https://images.unsplash.com/photo-1555679427-1f6dfcce943b?ixlib=rb-1.2.1&auto=format&fit=crop&w=675&q=80", new ArrayList<>()));
        return topics;
    }
}
