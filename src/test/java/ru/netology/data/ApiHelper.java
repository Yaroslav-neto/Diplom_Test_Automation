package ru.netology.data;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;

public class ApiHelper {

    public static HttpResponse<String> sendPostRequestPay(String json) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/pay"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendPostRequestPayCredit(String json) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/credit"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static String createJsonBody(DataHelper.FullCardData data) {
        return "{"
                + "\"number\": \"" + data.getNumber() + "\","
                + "\"year\": \"" + data.getYear() + "\","
                + "\"month\": \"" + data.getMonth() + "\","
                + "\"holder\": \"" + data.getOwner() + "\","
                + "\"cvc\": \"" + data.getCvc() + "\""
                + "}";
    }
}