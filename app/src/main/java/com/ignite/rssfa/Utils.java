package com.ignite.rssfa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ignite.rssfa.db.entity.Feed;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {
    public static void longToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static List<Topic> fillTopics() {
        List<Topic> topics = new ArrayList<>();

        topics.add(makeTopic());
        topics.add(new Topic("Science", Integer.toString(R.drawable.science), new ArrayList<>()));
        topics.add(new Topic("Health", Integer.toString(R.drawable.health), new ArrayList<>()));
        topics.add(new Topic("Technology", Integer.toString(R.drawable.tek), new ArrayList<>()));
        topics.add(new Topic("Environment", Integer.toString(R.drawable.env), new ArrayList<>()));
        topics.add(new Topic("Society", Integer.toString(R.drawable.soc), new ArrayList<>()));
        topics.add(new Topic("Strange", Integer.toString(R.drawable.strange), new ArrayList<>()));
        topics.add(new Topic("All", Integer.toString(R.drawable.solid), new ArrayList<>()));

        return topics;
    }

    private static Topic makeTopic() {
        Feed feed = new Feed("Google News", "", "", "en", "", "https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx1YlY4U0FtVnVHZ0pWVXlnQVAB?hl=en-US&gl=US&ceid=US:en", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Google_News_icon.png/221px-Google_News_icon.png");
        Feed feed1 = new Feed("Reddit - World News", "", "", "en", "", "https://www.reddit.com/r/worldnews/.rss", "https://www.redditstatic.com/new-icon.png");
        Feed feed2 = new Feed(" The New York Times News", "", "", "en", "", "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/328e7c8732133.560c2513bab89.gif");

        return new Topic("News", Integer.toString(R.drawable.news), new ArrayList<Feed>() {{
            add(feed);
            add(feed1);
            add(feed2);
        }});
    }

    public static void logError(String className, int statusCode, String error) {
        Log.w(className, statusCode + " " + error);
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                mIcon11 = BitmapFactory.decodeStream(in, null, options);
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

    public static void hideSoftKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String dateDiff(Context context, String stringDate) {
        if (stringDate != null && !stringDate.isEmpty() && !stringDate.equals("null")) {
            try {
                stringDate = stringDate.substring(8, 10) + "/" + stringDate.substring(4, 7) + "/" + stringDate.substring(24, 28);
                String pattern = "dd/MMM/yyyy";
                SimpleDateFormat parserSDF = new SimpleDateFormat(pattern, Locale.ENGLISH);
                Date date = parserSDF.parse(stringDate);
                DateTime start = new DateTime(date.getTime());
                long days = Days.daysBetween(start.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).getDays();
                if (days > 30) {
                    if (days > 365) {
                        return days / 365 + context.getString(R.string.year_abv);
                    }
                    return days / 30 + " m";
                }
                return days + context.getString(R.string.days_abv);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
