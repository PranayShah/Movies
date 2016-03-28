package in.uchneech.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface TmdbInterface {
    @GET("/3/movie/{id}/videos?append_to_response=reviews")
    Call<VideosList> movieDetails(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("/3/movie/{sort}")
    Call<MoviesList> discover (@Path("sort") String sort_by, @Query("page") Integer page, @Query("api_key") String apiKey);
}
