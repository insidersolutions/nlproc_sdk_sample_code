package com.semanticanalyzer;


import info.semanticanalyzer.morph.ru.MorphAnalyzer;
import info.semanticanalyzer.morph.ru.MorphAnalyzerConfig;
import info.semanticanalyzer.morph.ru.MorphAnalyzerLoader;
import info.semanticanalyzer.morph.ru.PartOfSpeech;
import info.semanticanalyzer.tok.GenericFlexTokenizer;
import info.semanticanalyzer.tok.Token;
import info.semanticanalyzer.tok.Tokenizer;
import info.semanticanalyzer.util.Charsets;
import info.semanticanalyzer.util.IOUtils;
import info.semanticanalyzer.morph.ru.MorphDesc;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

/**
 * Generic unit tests for the {@link info.semanticanalyzer.morph.ru.MorphAnalyzer} class
 *
 * @author Dmitry Kan
 * Date: 19.03.14
 * Time: 20:00
 */
public class LemmatizerRuTest {

    MorphAnalyzer analyzer;
    
    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        LemmatizerRuTest lemmatizerRuTest = new LemmatizerRuTest();
        lemmatizerRuTest.run();
    }
    
    public LemmatizerRuTest() throws IOException {
    	File propeFile = new File("conf/lemmatizer-ru.properties");
        Properties properties = new Properties();
        properties.load(new StringReader(IOUtils.readFile(propeFile, Charsets.UTF_8)));
		this.analyzer = MorphAnalyzerLoader.load(new MorphAnalyzerConfig(properties));
	}
    
    public void run() throws RuntimeException {
    	this.testFindLemma_unknownWord();
    	this.testFindLemma_estrella();
    	this.testFindLemma();
    	this.testFindMostFrequentLemma();
    	this.testLemmatizePhrase();
    	this.testBlogPostExample();
    }
    
    public void testFindLemma_unknownWord() throws RuntimeException {
      if (analyzer.analyzeBest("vasdfsмою") != null)
    	  throw new RuntimeException("testFindLemma_unknownWord failed");
      info("testFindLemma_unknownWord passed");
    }
    
    private void info(String msg) {
    	System.out.println("INFO " + msg);
    }

	public void testFindLemma_estrella() throws RuntimeException {
      if (!analyzer.analyze("эстрелла").isEmpty())
    		  throw new RuntimeException("testFindLemma_estrella failed");
      info("testFindLemma_estrella passed");
    }

    public void testFindLemma() throws RuntimeException {
    	
    	List<MorphDesc> l = analyzer.analyze("мою квартиру");
        info("Lemmata of 'мою':");
        for (MorphDesc desc : l) {
            info(desc.getLemma().toString());
        }

        l = analyzer.analyze("данную");
        info("Lemmata of 'данную':");
        for (MorphDesc desc : l) {
            info(desc.getLemma().toString());
        }

        if(!PartOfSpeech.ADJECTIVE.equals(l.get(0).getPos())) {
        	throw new RuntimeException("testFindLemma failed: " + l.get(0).getPos());
        }
        if (!"данный".equals(l.get(0).getLemma())) {
        	throw new RuntimeException("testFindLemma failed: " + l.get(0).getLemma());
        }
        if (!PartOfSpeech.VERB.equals(l.get(1).getPos())) {
        	throw new RuntimeException("testFindLemma failed: " + l.get(1).getPos());
        }
        if (!"дать".equals(l.get(1).getLemma())) {
        	throw new RuntimeException("testFindLemma failed: " + l.get(1).getLemma());
        }
        info("testFindLemma passed");
    }

    public void testFindMostFrequentLemma() throws RuntimeException {
        MorphDesc l = analyzer.analyzeBest("мою");

        if (l.getLemma() != null) {
            info("Most frequent lemma of 'мою' is " + l.getLemma());
        }

        if (!"мой".equals(l.getLemma())) {
        	throw new RuntimeException("testFindMostFrequentLemma failed: " + l.getLemma());
        }
        if (!PartOfSpeech.PRONOUN_ADJECTIVE.equals(l.getPos())) {
        	throw new RuntimeException("testFindMostFrequentLemma failed: " + l.getPos());
        }
        info("testFindMostFrequentLemma passed");
    }
    
    public void testLemmatizePhrase() throws RuntimeException {
    	String phrase = "мою квартиру";

    	Tokenizer tokenizer = new GenericFlexTokenizer(new StringReader(phrase), true);
        Token reusableToken = Token.newReusableToken();
        try {
			while ( (reusableToken = tokenizer.getNextToken(reusableToken)) != null) {
			    String token = reusableToken.getText();
			    MorphDesc l = analyzer.analyzeBest(token);
		        if (l.getLemma() != null) {
		            info("Most frequent lemma of '" + token + "' is " + l.getLemma());
		        }			    
			    
			}
		} catch (IOException e) {
			throw new RuntimeException("testLemmatizePhrase failed: " + e.getMessage());
		}
		
		info("testLemmatizePhrase passed");
    }
    
    public void testBlogPostExample() throws RuntimeException {
    	String phrase = "рестораны Москвы";
    	
    	Tokenizer tokenizer = new GenericFlexTokenizer(new StringReader(phrase.toLowerCase()), true);
    	Token reusableToken = Token.newReusableToken();
    	try {
    		while ( (reusableToken = tokenizer.getNextToken(reusableToken)) != null ) {
    			String token = reusableToken.getText();
    			MorphDesc morphDescription = analyzer.analyzeBest(token);
    			if (morphDescription != null && morphDescription.getLemma() != null) {
    				info("Most frequent lemma of '" + token + "' is " + morphDescription.getLemma());
    				info("Its POS tag: " + morphDescription.getPos());
    			}
    		}
    	} catch (IOException e) {
    		throw new RuntimeException("testBlogPostExample failed: " + e.getMessage());
    	}
    }
}
