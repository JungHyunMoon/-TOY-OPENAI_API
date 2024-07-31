package kr.co.direa.direachatbot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class GptService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final HttpClient client = HttpClient.newHttpClient();

    public CompletableFuture<String> getGptResponse(String prompt) {
        JSONObject json = new JSONObject();
        json.put("model", "ft:gpt-3.5-turbo-0125:personal::9o1yZoGC");

        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);

        json.put("messages", messages);
        json.put("max_tokens", 150);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        return responseObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    } catch (Exception e) {
                        System.out.println("API Response: " + response);  // 응답 출력
                        throw new RuntimeException("Error parsing API response", e);
                    }
                });
    }
}