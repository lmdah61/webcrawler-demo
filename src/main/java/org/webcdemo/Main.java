package org.webcdemo;

import com.google.gson.Gson;
import org.webcdemo.model.SearchRequest;
import org.webcdemo.model.SearchResults;
import org.webcdemo.service.SearchService;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;

public class Main {
    private static final SearchService searchService = new SearchService();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        port(4567);

        post("/crawl", (req, res) -> {
            SearchRequest request = gson.fromJson(req.body(), SearchRequest.class);
            String baseUrl = System.getenv("BASE_URL");
            SearchResults results = searchService.startSearch(request, baseUrl);
            res.status(200);
            res.type("application/json");
            return gson.toJson(results);
        });

        get("/crawl/:id", (req, res) -> {
            String id = req.params(":id");
            SearchResults results = searchService.getSearchResults(id);

            if (results != null) {
                res.status(200);
                res.type("application/json");
                return gson.toJson(results);
            } else {
                res.status(404);
                return "Search not found";
            }
        });
    }
}
