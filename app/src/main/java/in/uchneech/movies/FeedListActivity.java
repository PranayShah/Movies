package in.uchneech.movies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

public class FeedListActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener{
   private final String LOG_TAG = FeedListActivity.class.getSimpleName();
// Constants
// The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    private boolean movieSelected = false;
    private Result result = null;
    private Boolean existsInFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Create the dummy account
        mAccount = CreateSyncAccount(this);

        //Log.i(LOG_TAG, String.valueOf(getResources().getConfiguration().smallestScreenWidthDp));
        //Log.i (LOG_TAG, String.valueOf(getResources().getConfiguration().screenWidthDp));
    }
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    /*@Override
    public boolean onPause (Bundle savedInstanceState)
    {
        super.onPause();
        movieSelected = false;
        return
    }*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.favourites)==null && movieSelected)
        {
            MenuItem item = menu.add(Menu.NONE, R.id.favourites, Menu.NONE, "Add to favourites");
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            if (existsInFav)
            {
                item.setIcon (android.R.drawable.btn_star_big_on).setEnabled(false);
            }
            else
            {
                item.setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                new DBAsync().execute(result);
                                return true;
                            }
                        }
                ).setIcon(android.R.drawable.btn_star);
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Parcelable parcel) {
//        Log.i(LOG_TAG, String.valueOf(Parcels.unwrap(parcel)));
        movieSelected = true;
        result = Parcels.unwrap(parcel);
        new DBAsyncRead().execute(result);
        if (findViewById(R.id.frag_container) == null)
        {
            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra("movieId", parcel);
            startActivity(intent);
        }
        else
        {
            DetailsFragment displayFrag = (DetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.details_frag);
            displayFrag.updateContent (parcel);
            //MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById((R.id.list_frag));

        }
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
//            Log.i (LOG_TAG, String.valueOf(newRowId));
            db.close();
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
            return temp > 0;
        }

        @Override
        protected void onPostExecute(Boolean existsInFavourites) {
                existsInFav = existsInFavourites;
            if (findViewById(R.id.frag_container) != null)
            {
                invalidateOptionsMenu();
            }

        }
    }


}
