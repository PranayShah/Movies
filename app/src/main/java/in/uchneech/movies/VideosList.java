package in.uchneech.movies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideosList {

    private Integer id;
    @SerializedName("results") private List<Videos> results = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Videos> getVideos() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setVideos(List<Videos> results) {
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

