package com.aswipe_menu.student_like.swipemenu.ListView;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.aswipe_menu.student_like.swipemenu.R;

public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    // change title dependin on consumption or history
    private int change_title;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Movie> movieItems, int change_title) {
        this.activity = activity;
        this.movieItems = movieItems;
        this.change_title = change_title;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title_todo = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        Movie m = movieItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // change title depending on consumption or history
        if (change_title == 0) {
            title_todo.setText(m.getTitle());
        }
        else {
            String text = "<font color=#C62828>remove </font>";
            title_todo.setText(Html.fromHtml(text + m.getTitle()));
        }

        // rating
        rating.setText("product: " + String.valueOf(m.getRating()));

        // genre
        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);

        // release year
        year.setText(String.valueOf(m.getYear()));

        return convertView;
    }

}
