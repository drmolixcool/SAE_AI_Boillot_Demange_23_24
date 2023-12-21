package gps;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public record PointDegree(double latitude, double longitude) {

    public static PointDegree adresse(String adresse) throws IOException, InterruptedException {
        Gson gson = new Gson();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            String encoded_address = URLEncoder.encode(adresse, StandardCharsets.UTF_8);
            URI url = URI.create("https://api-adresse.data.gouv.fr/search/?q=" + encoded_address);
            HttpRequest request = HttpRequest.newBuilder(url).header("Accept-Encoding", "").build();
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            InputStream stream = response.body();
            if (response.headers().allValues("content-encoding").contains("gzip")) {
                stream = new GZIPInputStream(response.body());
            }
            String raw_data = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject data = gson.fromJson(raw_data, JsonObject.class);
            JsonArray array = data.getAsJsonArray("features");
            if (array == null) {
                return null;
            }
            JsonElement element = array.get(0);
            if (element == null) {
                return null;
            }
            JsonArray coordinates = element.getAsJsonObject().getAsJsonObject("geometry").getAsJsonArray("coordinates");
            Double latitude = coordinates.get(1).getAsDouble();
            Double longitude = coordinates.get(0).getAsDouble();
            return new PointDegree(latitude, longitude);
        }
    }

    public static PointDegree ici() throws IOException, InterruptedException {
        Gson gson = new Gson();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://ipapi.co/193.50.135.38/json")).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String raw_data = response.body();
            JsonObject data = gson.fromJson(raw_data, JsonObject.class);
            Double latitude = data.get("latitude").getAsDouble();
            Double longitude = data.get("longitude").getAsDouble();
            return new PointDegree(latitude, longitude);
        }
    }

    @Deprecated
    public PointDegree radians() {
        double lat = this.latitude * Math.PI / 180;
        double longitude = this.longitude * Math.PI / 180;
        return new PointDegree(lat, longitude);
    }
}
