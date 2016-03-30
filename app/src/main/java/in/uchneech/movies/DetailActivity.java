package in.uchneech.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener{
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    Result itemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Intent intent = getIntent();
        Parcelable parcel = intent.getParcelableExtra("movieId");

        itemSelected = Parcels.unwrap(parcel);
        new DBAsyncRead().execute(itemSelected);
        collapsingToolbar.setTitle(itemSelected.title);

        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_frag);
//        Log.i(LOG_TAG, String.valueOf(itemSelected.getTitle()));
        if (parcel!=null) detailsFragment.updateContent(parcel);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class DBAsync extends AsyncTask<Result, Void, Void>
    {

        @Override
        protected Void doInBackground(Result... params) {
            // Gets the data repository in write mode
            Result movie = params[0];
            MovieOpenHelper movieOpenHelper = new MovieOpenHelper(getApplicationContext());
            SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put (MoviesContract.Movie._ID, movie.getId());
            values.put (MoviesContract.Movie.COLUMN_NAME_overview, movie.getOverview());
            values.put (MoviesContract.Movie.COLUMN_NAME_poster_path, movie.getPosterPath());
            values.put (MoviesContract.Movie.COLUMN_NAME_release_date, movie.getReleaseDate());
            values.put (MoviesContract.Movie.COLUMN_NAME_title, movie.getTitle());
            values.put (MoviesContract.Movie.COLUMN_NAME_vote_average, movie.getVoteAverage());
            // Insert the new row, returning the primary key value of the new row
            db.insert(
                    MoviesContract.Movie.TABLE_NAME,
                    null,
                    values);
            db.close();
//            Log.i (LOG_TAG, String.valueOf(newRowId));
            return null;
        }
    }
    class DBAsyncRead extends AsyncTask <Result, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Result... params) {
            MovieOpenHelper movieOpenHelper = new MovieOpenHelper(getApplicationContext());
            SQLiteDatabase db = movieOpenHelper.getReadableDatabase();
            String [] arg = new String[1];
            arg[0]= String.valueOf(params[0].id);
            Cursor c = db.query(
                    MoviesContract.Movie.TABLE_NAME,
                    null,
                    "id = ?",
                    arg,
                    null,
                    null,
                    null
            );
            int temp = c.getCount();
            c.close(); db.close();
            return temp> 0;
        }

        @Override
        protected void onPostExecute(Boolean existsInFavourites) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (existsInFavourites)
            {
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
            }
            else
            {
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DBAsync().execute(itemSelected);
                    }
                });
            }

        }
    }
}
