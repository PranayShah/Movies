package in.uchneech.movies;


import android.provider.BaseColumns;

public class MoviesContract {
    public MoviesContract () {}
    public static abstract class Movie implements BaseColumns {
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_NAME_release_date= "release_date";
        public static final String COLUMN_NAME_poster_path = "poster_path";
        public static final String COLUMN_NAME_title = "title";
        public static final String COLUMN_NAME_vote_average= "vote_average";
        public static final String COLUMN_NAME_overview = "overview";
        public static final String _ID = "id";
    }
}
