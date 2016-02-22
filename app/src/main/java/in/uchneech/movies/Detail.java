package in.uchneech.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class Detail extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener{
    private final String LOG_TAG = Detail.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_frag);
        Intent intent = getIntent();
        //Log.i(LOG_TAG,intent.getStringExtra("movieId" ));
        detailsFragment.updateContent(intent.getStringExtra("movieId"));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
