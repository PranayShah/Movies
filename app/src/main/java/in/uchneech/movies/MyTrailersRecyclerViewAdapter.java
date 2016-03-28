package in.uchneech.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersListRowHolder> {
    private String TAG = MyTrailersRecyclerViewAdapter.class.getSimpleName();
    private List<Videos> trailerItemList;
    private Context mContext;

    public MyTrailersRecyclerViewAdapter(Context context, List<Videos> trailerItemList) {
        this.trailerItemList = trailerItemList;
        this.mContext = context;
        /*Log.i(TAG, "In constructor");
        if (trailerItemList!=null){for (Videos s : trailerItemList) {Log.i(TAG, String.valueOf(s.getKey()));}} else {Log.i(TAG, "Null");}
        Log.i(TAG, "Out constructor");*/
    }

    @Override
    public TrailersListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        Log.i(TAG, "In onCreate"); Log.i (TAG, String.valueOf(viewGroup)); Log.i(TAG, String.valueOf(i));
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_view, viewGroup, false);
//        if (trailerItemList!=null){for (Videos s : trailerItemList) {Log.i(TAG, String.valueOf(s.getName()));}} else {Log.i(TAG, "Null");}
//        Log.i (TAG, "Out onCreate");
        return new TrailersListRowHolder(v, trailerItemList, mContext);
    }

    @Override
    public void onBindViewHolder(TrailersListRowHolder trailersListRowHolder, int i) {
//        Log.i(TAG, "In onBind"); Log.i (TAG, String.valueOf(trailersListRowHolder)); Log.i(TAG, String.valueOf(i));
//        if (trailerItemList!=null){for (Object s : trailerItemList) {Log.i(TAG, String.valueOf(s));}} else {Log.i(TAG, "Null");}
        Videos trailer = trailerItemList.get(i);
//        Log.i(TAG, String.valueOf(trailerItemList.get(i)));
        if (trailer!= null ) {
//            Log.i(TAG, trailer.getId());
            Picasso.with(mContext).load("http://img.youtube.com/vi/" + trailer.getKey() + "/sddefault.jpg")
                    .into(trailersListRowHolder.thumbnail);
        }
//        Log.i (TAG, "Out onBind");
    }

    @Override
    public int getItemCount() {
//        Log.i (TAG, String.valueOf(trailerItemList)); Log.i (TAG, String.valueOf (trailerItemList.size()));
        return (null != trailerItemList )? trailerItemList.size() : 0;
    }
}
