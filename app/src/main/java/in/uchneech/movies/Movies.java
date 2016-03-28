package in.uchneech.movies;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class Movies extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}