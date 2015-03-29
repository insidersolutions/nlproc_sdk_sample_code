package com.semanticanalyzer;

import info.semanticanalyzer.sentiment.Enumerations;
import info.semanticanalyzer.sentiment.SentimentEngine;
import info.semanticanalyzer.sentiment.SentimentException;
import info.semanticanalyzer.sentiment.SynonymSentiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic unit tests for the {@link info.semanticanalyzer.sentiment.SentimentEngine} class
 *
 * @author Dmitry Kan
 * Date: 19.03.14
 * Time: 20:10
 */
public class SentimentEngineTest {

    SentimentEngine sentimentEngine;
    
    public static void main(String[] args) throws IOException, RuntimeException {
    	SentimentEngineTest sentimentTest = new SentimentEngineTest();
    	sentimentTest.run();
    }
    
    private void run() {
    	this.testclassifySentenceSimplePositive();
    	this.testclassifySentenceSimplePositiveWithPunct();
    	this.testclassifySentenceNeutralWithOppositeConjunction();
    	this.testclassifySentencePositiveWithOppositeConjunctionWithKeywords();
    	this.testdetectPolarityOfTextForSynonyms();
	}

	public SentimentEngineTest() {
    	sentimentEngine = new SentimentEngine(new File("conf/sentiment-module.properties"));
	}
	
    private void info(String msg) {
    	System.out.println("INFO " + msg);
    }
    
    public void testclassifySentenceSimplePositive() throws RuntimeException {
        Enumerations.Sentiment sentiment = sentimentEngine.detectPolarityOfText("Сегодня хорошая погода");
        if (!Enumerations.Sentiment.POSITIVE.equals(sentiment)) {
        	throw new RuntimeException("testclassifySentenceSimplePositive failed");
        }
        info("testclassifySentenceSimplePositive passed");
    }

    public void testclassifySentenceSimplePositiveWithPunct() throws RuntimeException {
        Enumerations.Sentiment sentiment = sentimentEngine.detectPolarityOfText("Сегодня хорошая погода!");
        if (!Enumerations.Sentiment.POSITIVE.equals(sentiment)) {
        	throw new RuntimeException("testclassifySentenceSimplePositiveWithPunct failed");
        }
        info("testclassifySentenceSimplePositiveWithPunct passed");
    }

    public void testclassifySentenceNeutralWithOppositeConjunction() throws RuntimeException {
        Enumerations.Sentiment sentiment = sentimentEngine.detectPolarityOfText("Мне понравился новый iPhone, но вот GalaxyS неудобный.");
        if (!Enumerations.Sentiment.NEUTRAL.equals(sentiment)) {
        	throw new RuntimeException("testclassifySentenceNeutralWithOppositeConjunction failed");
        }
        info("testclassifySentenceNeutralWithOppositeConjunction passed");
    }

    public void testclassifySentencePositiveWithOppositeConjunctionWithKeywords() throws RuntimeException {
        Enumerations.Sentiment sentiment;
		try {
			sentiment = sentimentEngine.detectPolarityOfTextForKeywords("Мне понравился новый iPhone, но вот GalaxyS неудобный.", Collections.singletonList("iPhone"));
		} catch (SentimentException e) {
			throw new RuntimeException(e.getMessage());
		}
        if (!Enumerations.Sentiment.POSITIVE.equals(sentiment)) {
        	throw new RuntimeException("testclassifySentencePositiveWithOppositeConjunctionWithKeywords failed");
        }

        try {
			sentiment = sentimentEngine.detectPolarityOfTextForKeywords("Мне понравился новый iPhone, но вот GalaxyS неудобный.", Collections.singletonList("GalaxyS"));
		} catch (SentimentException e) {
			throw new RuntimeException(e.getMessage());
		}
        if (!Enumerations.Sentiment.NEGATIVE.equals(sentiment)) {
        	throw new RuntimeException("testclassifySentencePositiveWithOppositeConjunctionWithKeywords failed");
        }
        info("testclassifySentencePositiveWithOppositeConjunctionWithKeywords passed");
    }

    public void testdetectPolarityOfTextForSynonyms() throws RuntimeException {
        List<String> synonym1 = new ArrayList<String>();
        synonym1.add("Nokia");
        synonym1.add("Lumia");

        List<String> synonym2 = new ArrayList<String>();
        synonym2.add("Lumia");
        synonym2.add("920");

        List<List<String>> synonyms = new ArrayList<List<String>>();
        synonyms.add(synonym1);
        synonyms.add(synonym2);

        SynonymSentiment synonymSentiment;
		try {
			synonymSentiment = sentimentEngine.detectPolarityOfTextForSynonyms("Смартфон Nokia Lumia 920 не имеет компромиссов. " +
			        "Его уже сегодня можно назвать передовым устройством.", synonyms);
		} catch (SentimentException e) {
			throw new RuntimeException(e.getMessage());		}

        if(!Enumerations.Sentiment.POSITIVE.equals(synonymSentiment.getSentimentTag())) {
        	throw new RuntimeException("testdetectPolarityOfTextForSynonyms failed");
        }
        info("testdetectPolarityOfTextForSynonyms passed");
    }
}
