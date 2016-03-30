package in.uchneech.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoviesDB.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesContract.Movie.TABLE_NAME + " (" +
                    MoviesContract.Movie._ID + " INTEGER PRIMARY KEY," +
                    MoviesContract.Movie.COLUMN_NAME_poster_path + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.Movie.COLUMN_NAME_release_date  + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.Movie.COLUMN_NAME_title + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.Movie.COLUMN_NAME_vote_average  + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.Movie.COLUMN_NAME_overview + TEXT_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesContract.Movie.TABLE_NAME;
    public MovieOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

