package gps;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Commune(int code, String nom, int population, PointDegree location) implements Comparable<Commune>, Serializable {

    public static List<Commune> communes() throws IOException, InterruptedException {
        ArrayList<Commune> communes = new ArrayList<>();
        Gson gson = new Gson();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://geo.api.gouv.fr/communes?fields=centre,population")).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String raw_data = response.body();
            JsonArray data = gson.fromJson(raw_data, JsonArray.class);
            for (int i = 0; i < data.asList().size(); i++) {
                JsonObject city = data.get(i).getAsJsonObject();

                String nom = city.get("nom").getAsString();
                Optional<Integer> population = Optional.ofNullable(city.get("population")).map(JsonElement::getAsInt);

                JsonArray coords = city.getAsJsonObject("centre").getAsJsonArray("coordinates");
                PointDegree point = new PointDegree(coords.get(1).getAsDouble(), coords.get(0).getAsDouble());

                communes.add(new Commune(i, nom, population.orElse(0), point));
            }
        }
        return communes;
    }

    @Override
    public int compareTo(Commune o) {
        return this.population - o.population;
    }

    public double distance(PointRadians other) {
        return new PointRadians(this.location).distance(other);
    }

    public double distance(Commune other) {
        return distance(new PointRadians(other.location));
    }

}
