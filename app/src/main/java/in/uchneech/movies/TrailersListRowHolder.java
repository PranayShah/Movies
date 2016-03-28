package in.uchneech.movies;
import com.google.android.youtube.player.YouTubeIntents;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class TrailersListRowHolder extends RecyclerView.ViewHolder{
        private final String TAG = TrailersListRowHolder.class.getSimpleName();
    protected ImageView thumbnail;
    public TrailersListRowHolder(View view, final List<Videos> trailersItemList, final Context context) {
        super(view);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String version = YouTubeIntents.getInstalledYouTubeVersionName(v.getContext());
                if (version != null) {
                    if (YouTubeIntents.canResolvePlayVideoIntent (v.getContext()))
                    {
                        context.startActivity(YouTubeIntents.createPlayVideoIntent(v.getContext(), trailersItemList.get(getLayoutPosition()).getKey()));
                    }
                    else
                    {
                        Log.w (TAG, "Can not resolve");
                    }
                }
                else
                {
                    Log.w (TAG, "Youtube not intalled");
                }

            }
        });
        this.thumbnail = (ImageView) view.findViewById(R.id.trailerView);
//        Log.i(TAG, "http://img.youtube.com/vi/" + id + "/sddefault.jpg");
        /*Picasso.with(context).load("http://img.youtube.com/vi/"+id+"/sddefault.jpg")
                .into(this.thumbnail);*/
    }
}
