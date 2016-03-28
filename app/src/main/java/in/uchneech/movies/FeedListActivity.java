package in.uchneech.movies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Parcelable parcel) {
        Log.i(LOG_TAG, String.valueOf(Parcels.unwrap(parcel)));
        if (findViewById(R.id.frag_container) != null)
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
}
