package in.uchneech.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsListRowHolder> {
    private String TAG = MyReviewsRecyclerViewAdapter.class.getSimpleName();
    private List<Review> trailerItemList;
    private Context mContext;

    public MyReviewsRecyclerViewAdapter(Context context, List<Review> trailerItemList) {
        this.trailerItemList = trailerItemList;
        this.mContext = context;
//        Log.i(TAG, "In constructor");
//        if (trailerItemList!=null){for (Review s : trailerItemList) {Log.i(TAG, String.valueOf(s.getContent()));}} else {Log.i(TAG, "Null");}
//        Log.i(TAG, "Out constructor");
    }

    @Override
    public ReviewsListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        Log.i(TAG, "In onCreate"); Log.i (TAG, String.valueOf(viewGroup)); Log.i(TAG, String.valueOf(i));
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_view, viewGroup, false);
//        if (trailerItemList!=null){for (Review s : trailerItemList) {Log.i(TAG, String.valueOf(s.getContent()));}} else {Log.i(TAG, "Null");}
//        Log.i (TAG, "Out onCreate");
        return new ReviewsListRowHolder(v, trailerItemList, mContext);
    }

    @Override
    public void onBindViewHolder(ReviewsListRowHolder trailersListRowHolder, int i) {
//        Log.i(TAG, "In onBind"); Log.i (TAG, String.valueOf(trailersListRowHolder)); Log.i(TAG, String.valueOf(i));
//        if (trailerItemList!=null){for (Review s : trailerItemList) {Log.i(TAG, String.valueOf(s.getContent()));}} else {Log.i(TAG, "Null");}
        Review trailer = trailerItemList.get(i);
//        Log.i(TAG, String.valueOf(trailerItemList.get(i)));
        if (trailer!= null ) {
//            Log.i(TAG, trailer.getContent());
            trailersListRowHolder.thumbnail.setText(trailer.getAuthor()+ " says " + trailer.getContent());
        }
//        Log.i (TAG, "Out onBind");
    }

    @Override
    public int getItemCount() {
//        Log.i (TAG, String.valueOf(trailerItemList)); Log.i (TAG, String.valueOf (trailerItemList.size()));
        return (null != trailerItemList )? trailerItemList.size() : 0;
    }
}

