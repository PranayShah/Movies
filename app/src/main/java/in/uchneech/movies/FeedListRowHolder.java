package in.uchneech.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.util.List;

public class FeedListRowHolder extends RecyclerView.ViewHolder{
//    private final String LOG_TAG = FeedListRowHolder.class.getSimpleName();
    protected ImageView thumbnail;
    public FeedListRowHolder(View view, final MoviesFragment.OnListFragmentInteractionListener fragListener, final List<Result> feedItemList) {
        super(view);
        view.setOnClickListener(new View.OnClickListener(){
            public  void onClick (View v)
            {
                fragListener.onListFragmentInteraction(Parcels.wrap(feedItemList.get(getLayoutPosition())));
            }
        });
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

    }
}
