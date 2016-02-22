package in.uchneech.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class FeedListActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener{
//    private final String LOG_TAG = FeedListActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Log.i(LOG_TAG, String.valueOf(getResources().getConfiguration().smallestScreenWidthDp));
        //Log.i (LOG_TAG, String.valueOf(getResources().getConfiguration().screenWidthDp));
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
    public void onListFragmentInteraction(String movieId) {
        if (findViewById(R.id.frag_container) != null)
        {
            Intent intent = new Intent(this, Detail.class);
            intent.putExtra("movieId", movieId);
            startActivity(intent);
        }
        else
        {
            DetailsFragment displayFrag = (DetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.details_frag);
            displayFrag.updateContent (movieId);
            //MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById((R.id.list_frag));

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
