package com.semanticanalyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.semanticanalyzer.dao.ArticleEntry;
import com.semanticanalyzer.mappers.ArticleEntryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class TopicUploader {
    private final static String mashapeKey = "[PUT YOUR MASHAPE TOPIC API KEY HERE]";

    private TopicUploader() {}

    public static void main(String[] args) {

        if (args.length < 1) {
            log.info("Please specify the resource_id to load from the db");
            return;
        }

        TopicUploader topicUploader = new TopicUploader();

        topicUploader.uploadDBEntries(args[0]);
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

    private void uploadDBEntries(String resourceId) {
        SqlSessionFactory sqlSessionFactory = initSqlSessionFactory();
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ArticleEntryMapper mapper = sqlSession.getMapper(ArticleEntryMapper.class);
            List<ArticleEntry> articleEntries = mapper.loadEntriesFromDB(resourceId);
            System.out.println("Loaded entries from DB: " + articleEntries.size());
            uploadArticlesToTopicAPI(articleEntries);
        }
    }

    private void uploadArticlesToTopicAPI(List<ArticleEntry> articleEntries) {
        articleEntries.forEach(
                this::uploadArticleToTopicAPI
        );
    }

    private void uploadArticleToTopicAPI(ArticleEntry x) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            String body = "[" + gson.toJson(x) + "]";

            System.out.println("Sending body for id: " + x.getId());

            HttpResponse<JsonNode> response = Unirest.post("https://dmitrykey-insiderapi-v1.p.mashape.com/articles/uploadJson")
                    .header("X-Mashape-Key", mashapeKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(body)
                    .asJson();

            System.out.println("TopicAPI response:" + response.getBody().toString());
        } catch (UnirestException e) {
            log.error("Error: {}", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

}
