package in.uchneech.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class FeedListRowHolder extends RecyclerView.ViewHolder{
//    private final String LOG_TAG = FeedListRowHolder.class.getSimpleName();
    protected ImageView thumbnail;
    public FeedListRowHolder(View view, final MoviesFragment.OnListFragmentInteractionListener fragListener, final List<FeedItem> feedItemList) {
        super(view);
        view.setOnClickListener(new View.OnClickListener(){
            public  void onClick (View v)
            {
                fragListener.onListFragmentInteraction(feedItemList.get(getLayoutPosition()).getId());
            }
        });
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
    }
}
