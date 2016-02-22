package in.uchneech.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    //private String LOG_TAG = MyMovieRecyclerViewAdapter.class.getSimpleName();
    private List<FeedItem> feedItemList;
    private MoviesFragment.OnListFragmentInteractionListener fragListener;
    private Context mContext;

    public MyMovieRecyclerViewAdapter(Context context, List<FeedItem> feedItemList, MoviesFragment.OnListFragmentInteractionListener fragListener) {
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
        FeedItem feedItem = feedItemList.get(i);

        Picasso.with(mContext).load(feedItem.getThumbnail())
                .into(feedListRowHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
