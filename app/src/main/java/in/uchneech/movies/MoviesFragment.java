package in.uchneech.movies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.facebook.stetho.okhttp3.StethoInterceptor;

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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static String INIT_SORT = "popular";
    private static int page = 1;
    private MoviesList feed;
    private List<Result> feedItemList;
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
        savedInstanceState.putString("INIT_SORT", INIT_SORT);
        savedInstanceState.putInt("page", page);
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
                    //Log.i(TAG, String.valueOf(page)+ " "+ String.valueOf(totalItemsCount));
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    retroCall(INIT_SORT, page + 1);
                }
            });

            adapter = new MyMovieRecyclerViewAdapter(MoviesFragment.this.getContext(), feedItemList, mListener);
            mRecyclerView.setAdapter(adapter);
            if (savedInstanceState != null)
            {
//                Log.i (TAG, "saved"); Log.i (TAG,savedInstanceState.getString("INIT_SORT")); Log.i (TAG, String.valueOf (savedInstanceState.getInt ("page")));
                if (savedInstanceState.getString("INIT_SORT") == "favourites")
                {
                    new DBAsyncRead().execute();
                }
                else
                {
                    retroCall(savedInstanceState.getString("INIT_SORT"), savedInstanceState.getInt("page"));
                }

            }
            else {
//                Log.i(TAG, INIT_SORT); Log.i (TAG, String.valueOf(page));
                if (INIT_SORT == "favourites")
                {
                    new DBAsyncRead().execute();
                }
                else
                {
                    retroCall(INIT_SORT, page);
                }

            }

        }
        return view;
    }

    private void retroCall(String initSort, final int page) {

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
        retrofit2.Call<MoviesList> call = service.discover(initSort, page, BuildConfig.TMDB);

        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response != null) {
                    feed = response.body();
                    if (feed != null) {

                        if (page == 1) {
                            feedItemList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        feedItemList.addAll(feed.getResults());
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), feed.getResults().size());
                    } else {
                        Log.i(TAG, "Feed null");
                    }
                } else {
                    Log.i(TAG, "Response null");
                }

            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected)
        inflater.inflate(R.menu.menu_list, menu);
        else inflater.inflate (R.menu.menu_no_internet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        item.setChecked(true);
        switch (item.getItemId())
        {
            case R.id.popularity :INIT_SORT = "popular";
                retroCall("popular", 1);  return true;
            case R.id.vote : INIT_SORT= "top_rated"; retroCall("top_rated", 1); return true;
            case R.id.favouritesMenuItem : INIT_SORT= "favourites"; favouritesChosen(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void favouritesChosen() {
        new DBAsyncRead().execute();
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
        void onListFragmentInteraction(Parcelable parcel);
    }

    class DBAsyncRead extends AsyncTask<Void, Void, List<Result>>
    {

        @Override
        protected List<Result> doInBackground(Void... params) {
            MovieOpenHelper movieOpenHelper = new MovieOpenHelper(getContext());
            SQLiteDatabase db = movieOpenHelper.getReadableDatabase();
            Cursor c = db.query(
                    MoviesContract.Movie.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            c.moveToFirst();
            List <Result> results = new ArrayList<>();
            for (int i=1; i<= c.getCount(); i++)
            {
                Result result = new Result();
                result.setId(c.getInt(c.getColumnIndex(MoviesContract.Movie._ID)));
//                Log.i(TAG, String.valueOf(result.id));
                result.setOverview(c.getString(c.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_overview)));
                result.setPosterPath(c.getString(c.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_poster_path)));
                result.setReleaseDate(c.getString(c.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_release_date)));
                result.setTitle(c.getString(c.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_title)));
                result.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_vote_average)));
                results.add(result);
                c.moveToNext();
            }
            c.close();
            return results;
        }

        @Override
        protected void onPostExecute(List<Result> results) {
            feedItemList.clear(); adapter.notifyDataSetChanged();
            feedItemList.addAll(results);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), results.size());
        }
    }
}
