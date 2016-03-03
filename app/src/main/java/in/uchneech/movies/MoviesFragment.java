package in.uchneech.movies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static String INIT_SORT = "popularity.desc";
    private static int page = 1;
    private final OkHttpClient client = new OkHttpClient();
    private List<FeedItem> feedItemList;

    MyMovieRecyclerViewAdapter adapter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesFragment() {
        feedItemList = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public static MoviesFragment newInstance(int columnCount) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        LinearLayoutManager linearLayoutManager; GridLayoutManager gridLayoutManager = null;
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                linearLayoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(linearLayoutManager);
            } else {
                gridLayoutManager = new GridLayoutManager(context, mColumnCount);
                mRecyclerView.setLayoutManager(gridLayoutManager);
            }
            // Add the scroll listener
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    run(callAdapterUpdater(INIT_SORT, page));
                }
            });

            adapter = new MyMovieRecyclerViewAdapter(MoviesFragment.this.getContext(), feedItemList, mListener);
            mRecyclerView.setAdapter(adapter);
            if (savedInstanceState != null)
            {
                run(callAdapterUpdater(savedInstanceState.getString("INIT_SORT"), savedInstanceState.getInt("page")));
            }
            else
            {
                run(callAdapterUpdater(INIT_SORT, page));
            }

        }
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("INIT_SORT", INIT_SORT);
        savedInstanceState.putInt("page", page);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    private String callAdapterUpdater(String initSort, int page) {

        //        Log.i (TAG, url);
        String url = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon()
                .appendQueryParameter("sort_by", initSort)
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("api_key", BuildConfig.TMDB).build().toString(); Log.i(TAG, url); return url;
    }

    public void run(String url)  {
        int postsLength = 0;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                //System.out.println(response.body().string());
                String myResponse =  response.body().string();
                //Do something with response
                //...

                MoviesFragment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Handle UI here

                    }
                });
            }
        });
    }

    private int parseResult(String result) {
        JSONArray posts = null;
        try {
            JSONObject response = new JSONObject(result);
            posts = response.optJSONArray("results");
            //Log.i(TAG, posts.toString());
            /*Initialize array if null*/
            /*if (null == feedItemList) {

            }*/
            feedItemList = new ArrayList<>();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);

                FeedItem item = new FeedItem();
                item.setId(post.optString("id"));
                item.setThumbnail(post.optString("poster_path"));
                feedItemList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return posts != null? posts.length(): 0;
        }
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.popularity_asc :INIT_SORT= "popularity.asc"; run(callAdapterUpdater("popularity.asc", 1)); return true;
            case R.id.popularity_desc : INIT_SORT= "popularity.desc"; run(callAdapterUpdater("popularity.desc", 1)); return true;
            case R.id.vote_average_asc : INIT_SORT= "vote_average.asc"; run(callAdapterUpdater("vote_average.asc", 1)); return true;
            case R.id.vote_average_desc : INIT_SORT= "vote_average.desc"; run(callAdapterUpdater("vote_average.desc", 1)); return true;
            default: return super.onOptionsItemSelected(item);
        }

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String id);
    }


    // Implementation of AsyncTask used to download JSON from tmdb
    /*public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream;
            Integer result = 0;
            HttpURLConnection urlConnection;

            try {
                *//* forming th java.net.URL object *//*
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                *//**//* for Get request *//**//*
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                *//**//* 200 represents HTTP OK *//**//*
                if (statusCode ==  200) { Log.i (TAG, "200");
                    inputStream = urlConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            finally {
                inputStream = null; urlConnection = null;
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

//            setProgressBarIndeterminateVisibility(false);
            //Log.i (TAG, result.toString());
            *//* Download complete. Lets update UI *//*
            if (result == 1) {
                adapter.notifyItemRangeInserted(adapter.getItemCount(), 20);
                Log.i(TAG, "Yay");
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }*/
}
