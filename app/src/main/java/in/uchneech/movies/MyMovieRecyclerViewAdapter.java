package in.uchneech.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    private String LOG_TAG = MyMovieRecyclerViewAdapter.class.getSimpleName();
    private List<Result> feedItemList;
    private MoviesFragment.OnListFragmentInteractionListener fragListener;
    private Context mContext;

    public MyMovieRecyclerViewAdapter(Context context, List<Result> feedItemList, MoviesFragment.OnListFragmentInteractionListener fragListener) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.fragListener = fragListener;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_movie, viewGroup, false);
        return new FeedListRowHolder(v, fragListener, feedItemList);
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        Result feedItem = feedItemList.get(i);
        //Log.i(LOG_TAG, String.valueOf(feedItem.getId()));
        if (feedItem.getPosterPath()!= null ) {Log.i (LOG_TAG, feedItem.getTitle());}
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185"+feedItem.getPosterPath())
                .into(feedListRowHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
