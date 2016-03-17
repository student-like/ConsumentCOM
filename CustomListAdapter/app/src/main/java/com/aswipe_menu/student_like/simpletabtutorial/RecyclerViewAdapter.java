package com.aswipe_menu.student_like.simpletabtutorial;

import android.app.Activity;
import android.location.GpsStatus;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aswipe_menu.student_like.simpletabtutorial.Fragments.FragmentConsume;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    static String LOG_TAG = RecyclerViewAdapter.class.getSimpleName();

    public List<Product> data;
    public Activity activity;
    public FragmentConsume.OnFragmentInteractionListener mListener;

    public RecyclerViewAdapter(Activity activity, List<Product>  data, FragmentConsume.OnFragmentInteractionListener mListener){
        this.activity = activity;
        this.data = data;
        this.mListener = mListener;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        ViewHolder vh = new ViewHolder(v, this.activity, this.mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        //Log.i(LOG_TAG, "infos: receiving data from Product m position "+position+"...");

        // get all data (amount, type, brand) from product class...
        Product m = data.get(position);

        holder.title.setText(m.getTitle());
        holder.rating.setText("whatever " + String.valueOf(m.getRating()));

        // --------------------- thumbnail image ---------------------
        holder.thumbnail.setImageDrawable(m.getContentDrawable(activity, position));

    }

    @Override
    public int getItemCount() {
        //Log.i(LOG_TAG, "infos: get Item count "+data.size()+"...");
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // define new ConsumeFragment for communication
        FragmentConsume newConsume = new FragmentConsume();

        // definitions ins list_row
        public TextView title;
        public TextView rating;
        public ImageView thumbnail;

        public Activity activity;
        public FragmentConsume.OnFragmentInteractionListener mListener;

        public ViewHolder(View v, Activity activity, FragmentConsume.OnFragmentInteractionListener mListener) {
            super(v);

            this.activity = activity;
            this.mListener = mListener;
            //Log.i(LOG_TAG, "infos: creating ViewholDER...");

            title = (TextView) v.findViewById(R.id.title);
            rating = (TextView) v.findViewById(R.id.rating);
            // set img from drawable
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "infos: item was clicked -> RecyclerViewAdapter nr " + getAdapterPosition() + "...");

            // send position to FragmentConsume.java
            newConsume.clickAction(getAdapterPosition(), activity, mListener);
        }
    }
}