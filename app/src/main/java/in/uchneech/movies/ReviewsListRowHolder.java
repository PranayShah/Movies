package in.uchneech.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ReviewsListRowHolder  extends RecyclerView.ViewHolder{
    private final String TAG = ReviewsListRowHolder.class.getSimpleName();
    protected TextView thumbnail;
    public ReviewsListRowHolder(View view, final List<Review> trailersItemList, final Context context) {
        super(view);
        this.thumbnail = (TextView) view.findViewById(R.id.rev);
//        Log.i(TAG, "http://img.youtube.com/vi/" + id + "/sddefault.jpg");
        /*Picasso.with(context).load("http://img.youtube.com/vi/"+id+"/sddefault.jpg")
                .into(this.thumbnail);*/
    }
}

