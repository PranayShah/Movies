package in.uchneech.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private List<Videos> videosList;
    private OnFragmentInteractionListener mListener;
    private MyTrailersRecyclerViewAdapter adapter;
    RecyclerView mRecyclerView;
    public DetailsFragment() {
        // Required empty public constructor
        videosList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.content_scrolling, container, false);


//        DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details_frag);
        return view;
    }

    private void retroCall(Integer id) {

        //        Log.i (TAG, url);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(new StethoInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TmdbInterface service = retrofit.create(TmdbInterface.class);
        retrofit2.Call<VideosList> call = service.movieDetails(String.valueOf(id), "2c6a59512ceb6e441cc9a181f08974d2");

        call.enqueue(new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                VideosList feed = response.body();
//                Log.i(TAG, feed.getVideos().get(0).getName());
                /*if (page == 1) {
                    feedItemList.clear();
                    adapter.notifyDataSetChanged();
                }*/
                mRecyclerView = (RecyclerView) getView().findViewById(R.id.trailer_list);
                if (mRecyclerView != null) {
                    Context context = mRecyclerView.getContext();
                    LinearLayoutManager mLayout = new LinearLayoutManager(context);
                    mLayout.setAutoMeasureEnabled(true);
                    mRecyclerView.setLayoutManager(mLayout);
                    // Add the scroll listener
            /*mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    //Log.i(TAG, String.valueOf(page)+ " "+ String.valueOf(totalItemsCount));
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    retroCall(INIT_SORT, page+1);
                }
            });*/
                    Log.i(TAG, "found");
                    adapter = new MyTrailersRecyclerViewAdapter (context, videosList );
                    mRecyclerView.setAdapter(adapter);
                    /*if (savedInstanceState != null) {
                        retroCall(savedInstanceState.getInt("ID"));
                    }*/
                    Log.i (TAG, "Before"+ adapter.getItemCount());
                    videosList.addAll(feed.getVideos());
                    Log.i(TAG, videosList.get(0).getName());

                    adapter.notifyItemRangeInserted(adapter.getItemCount(), feed.getVideos().size());
                    Log.i(TAG, "After" + adapter.getItemCount());
                }

            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {
                t.printStackTrace();
            }
        });

        /*if (contributors != null) {
            for (FeedItem contributor : contributors) {
                System.out.println(
                        contributor.getId() + " (" + contributor.getThumbnail() + ")");
            }
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateContent(Parcelable parcel) {
        Result result = Parcels.unwrap(parcel);
        String movieId = String.valueOf( result.id);
        try {
            TextView date = (TextView) getView().findViewById(R.id.date),
                    overview = (TextView) getView().findViewById(R.id.overview), votes = (TextView) getView().findViewById(R.id.vote_average);
            ImageView poster = (ImageView) getActivity().findViewById(R.id.poster);

            if (result.release_date != null)
            {
                //Log.i(TAG, result.release_date);
                date.setText(result.release_date);
            }
            if (result.vote_average != null)
            {
                votes.setText(result.vote_average.toString());
            }
            if (result.overview != null)
            {
                overview.setText(result.overview);
            }
            if (result.poster_path != null)
            {
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+result.poster_path)
                        .into(poster);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        retroCall(result.getId());
        /*String url = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()
                .appendEncodedPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", BuildConfig.TMDB)
                .appendQueryParameter("append_to_response", "reviews").build().toString();
        Log.i(TAG, url);
        new AsyncHttpTask().execute(url);*/
    }

    private List parseResult(String result) {
        JSONObject response;
        JSONArray   posts;
        List temp = new ArrayList();
        try {
            response = new JSONObject(result);
            posts = response.optJSONArray("results");
            Log.i(TAG, posts.toString());

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.optJSONObject(i);
            temp.add(post.getString("key"));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }



    // Implementation of AsyncTask used to download JSON from tmdb
    public class AsyncHttpTask extends AsyncTask<String, Void, List> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected List doInBackground(String... params) {
            InputStream inputStream;
            List afterParse = null;
            HttpURLConnection urlConnection;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = urlConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    afterParse = parseResult(response.toString());
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            finally {
                inputStream = null; urlConnection = null;
            }

            return afterParse; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(List afterParse) {

            /*if (afterParse != null) { Log.i(TAG, String.valueOf(adapter.getItemCount()));
                if (str != null) {
                    //str.clear();
                    //str.addAll(afterParse) ;
                } else { Log.i (TAG, "NULL");}
                Log.i(TAG, String.valueOf(adapter.getItemCount()));Log.i(TAG, String.valueOf(str.size()));
                for (Object s : str) {Log.i(TAG, String.valueOf(s));}
                //adapter.notifyItemRangeInserted(adapter.getItemCount(), str.size());
//                adapter.notifyDataSetChanged();
                *//*ImageButton imageButton = (ImageButton) getView().findViewById(R.id.playIntent);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tostart = new Intent(Intent.ACTION_VIEW);
                        tostart.setDataAndType(Uri.parse(, "video*//**//*");
                        startActivity(tostart);
                    }
                });*//*
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }*/
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
