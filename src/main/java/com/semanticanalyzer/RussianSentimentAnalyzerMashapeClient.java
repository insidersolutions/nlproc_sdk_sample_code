package com.semanticanalyzer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by dmitry on 3/29/15.
 */
public class RussianSentimentAnalyzerMashapeClient {

    private final static String mashapeKey = "[PUT_YOUR_MASHAPE_KEY_HERE]";

    public static void main(String[] args) throws UnirestException {

        String textToAnnotate = "'ВТБ кстати неплохой банк)'";
        String targetObject = "'ВТБ'";

        // These code snippets use an open-source library. http://unirest.io/java
        HttpResponse<JsonNode> response = Unirest.post("https://russiansentimentanalyzer.p.mashape.com/ru/sentiment/polarity/json/")
                .header("X-Mashape-Key", mashapeKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{'text':" + textToAnnotate + ",'object_keywords':" + targetObject + ",'output_format':'json'}")
                .asJson();

        System.out.println("Input text = " + textToAnnotate + "\n" + "Target object:" + targetObject);
        System.out.println("RussianSentimentAnalyzer response:" + response.getBody().toString());
    }
}
