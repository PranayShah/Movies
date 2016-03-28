package in.uchneech.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener{
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
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
        Result itemSelected = Parcels.unwrap(parcel);
        collapsingToolbar.setTitle(itemSelected.title);
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_frag);
//        Log.i(LOG_TAG, String.valueOf(itemSelected.getTitle()));
        if (parcel!=null) detailsFragment.updateContent(parcel);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
