package com.semanticanalyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.semanticanalyzer.dao.ArticleEntry;
import com.semanticanalyzer.dao.SentimentDto;
import com.semanticanalyzer.dao.SentimentEntry;
import com.semanticanalyzer.mappers.ArticleEntryMapper;
import com.semanticanalyzer.mappers.SentimentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class SentimentTagger {
    private final static String mashapeKey = "[PUT YOUR MASHAPE KEY HERE]";

    private SentimentTagger() {}

    public static void main(String[] args) {
        if (args.length < 1) {
            log.info("Please specify the resource_id to load from the db");
            return;
        }

        SentimentTagger sentimentTagger = new SentimentTagger();
        sentimentTagger.tagSentiment(args[0]);
    }

    private SqlSessionFactory initSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            return new SqlSessionFactoryBuilder().build(inputStream, System.getenv("DB_ENVIRONMENT"));
        } catch (IOException e) {
            log.error("Error initializing SQL factory: {}", e);
            throw new RuntimeException(e);
        }
    }

    private void tagSentiment(String resourceId) {
        SqlSessionFactory sqlSessionFactory = initSqlSessionFactory();
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ArticleEntryMapper mapper = sqlSession.getMapper(ArticleEntryMapper.class);
            List<ArticleEntry> articleEntries = mapper.loadEntriesFromDB(resourceId);
            System.out.println("Loaded entries from DB: " + articleEntries.size());
            tagSentimentAndStoreInDB(articleEntries, sqlSession, resourceId);
        }
    }

    private void tagSentimentAndStoreInDB(List<ArticleEntry> articleEntries, SqlSession sqlSession, String resourceId) {
        for (ArticleEntry articleEntry : articleEntries) {
            tagAndStore(articleEntry, sqlSession, resourceId);
        }
    }

    private void tagAndStore(ArticleEntry articleEntry, SqlSession sqlSession, String resourceId) {
        SentimentDto sentimentDto = new SentimentDto();
        sentimentDto.setText(articleEntry.getDescription());
        sentimentDto.setObject_keywords("<placeholder>");
        sentimentDto.setInclude_strength(1);

        String textId = articleEntry.getId();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        SentimentMapper mapper = sqlSession.getMapper(SentimentMapper.class);

        // These code snippets use an open-source library. http://unirest.io/java
        HttpResponse<JsonNode> response;
        try {
            String body = gson.toJson(sentimentDto);

            //log.info("Sending body: {}", body);
            //System.out.println("Sending body: " + body);

            response = Unirest.post("https://russiansentimentanalyzer.p.mashape.com/rsa/sentiment/polarity/json/")
                    .header("X-Mashape-Key", mashapeKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(body)
                    .asJson();

            String sentimentLabel = response.getBody().getObject().getString("sentiment");
            double sentimentStrength = response.getBody().getObject().getDouble("strength");

            System.out.println("RussianSentimentAnalyzer response: " +
                    textId + ", " +
                    sentimentLabel + ", " +
                    sentimentStrength);

            SentimentEntry sentimentEntry = new SentimentEntry(textId, resourceId, sentimentLabel, sentimentStrength);

            mapper.insert(sentimentEntry);
            sqlSession.commit();

        } catch (UnirestException e) {
            log.error("Error while processing article={}", textId, e);
            e.printStackTrace();
        } catch (JSONException e) {
            log.error("Error while processing article={}", textId, e);
            System.out.println("Error while processing article=" + textId + " " + e.getMessage());
        }


    }

}
