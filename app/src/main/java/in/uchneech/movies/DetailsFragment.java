package in.uchneech.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
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

    public void updateContent(String movieId) {
        String url = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()
                .appendEncodedPath(movieId)
                .appendQueryParameter("api_key", BuildConfig.TMDB).build().toString();
        Log.i(LOG_TAG, url);
        new AsyncHttpTask().execute(url);
    }

    private JSONObject parseResult(String result) {
        JSONObject response = null;
        try {
            response = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void UIupdater(JSONObject response) {
        DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details_frag);

        try {
            TextView date = (TextView) getView().findViewById(R.id.date),
                    overview = (TextView) getView().findViewById(R.id.overview), votes = (TextView) getView().findViewById(R.id.vote_average);
            ImageView poster = (ImageView) getView().findViewById(R.id.poster);

            if (! response.isNull("release_date"))
            {
                date.setText(response.getString("release_date"));
            }
            if (! response.isNull("vote_average"))
            {
                votes.setText(response.getString("vote_average"));
            }
            if (! response.isNull("overview"))
            {
                overview.setText(response.getString("overview"));
            }
            if (! response.isNull("poster_path"))
            {
                Picasso.with(detailsFragment.getContext()).load("http://image.tmdb.org/t/p/w185"+response.getString("poster_path"))
                        .into(poster);

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Implementation of AsyncTask used to download JSON from tmdb
    public class AsyncHttpTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
//            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream inputStream;
            JSONObject afterParse = null;
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
                Log.d(LOG_TAG, e.getLocalizedMessage());
            }
            finally {
                inputStream = null; urlConnection = null;
            }

            return afterParse; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(JSONObject afterParse) {

//            setProgressBarIndeterminateVisibility(false);
            //Log.i (TAG, result.toString());
            /* Download complete. Lets update UI */
            Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
            if (afterParse != null) {
                if (!afterParse.isNull("title"))
                {
                    try {
                        myToolbar.setTitle(afterParse.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                UIupdater(afterParse);
            } else {
                Log.e(LOG_TAG, "Failed to fetch data!");
            }
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
