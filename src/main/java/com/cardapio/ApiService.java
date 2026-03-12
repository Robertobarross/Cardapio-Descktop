package com.cardapio;

import com.cardapio.model.Food;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Arrays;
import java.util.List;

public class ApiService {

    private static final String API_URL="http://localhost:8080/food";

    public static List<Food> getFoods(){

        try{

            HttpClient client=HttpClient.newHttpClient();

            HttpRequest request=HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response=
                    client.send(request,HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            Food[] foods = mapper.readValue(response.body(), Food[].class);

            return Arrays.asList(foods);

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }

}