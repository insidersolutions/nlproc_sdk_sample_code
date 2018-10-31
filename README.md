SemanticAnalyzer SDK Sample Java Code
======================

![alt tag](http://semanticanalyzer.info/blog/wp-content/uploads/2014/09/cropped-SemanticAnalyzerLogoTransparentblog.png)

These code samples illustrate the usage of Lemmatizing and Sentiment Analysis SDKs 
for the Russian language.

To start using these technologies in your projects, you need to acquire the license. Get
in touch at dk@semanticanalyzer.info

Sentiment Analysis technology can be consumed as an API on mashape.com:

https://www.mashape.com/dmitrykey/russiansentimentanalyzer

Looking for Chinese sentiment analyzer? Check this one out: http://semanticanalyzer.info/blog/chinese-sentiment-analysis-fuxi-api/

# Lemmatizer features

Documentation in pdf form in [Russian](documentation/lemmatizer/russian/Лингвистический_компонент_Лемматайзер_для_русского_языка.pdf) and in [English](documentation/lemmatizer/english/Linguistic_component_Lemmatizer_for_the_Russian_language.pdf) is available.


The process of lemmatization constitutes in deriving lemma and a POS tag for a given surface form (word).
Because Russian is highly inflectional, it is very important to derive the word lemma and use it instead of
stem, which is more crude way of normalizing Russian.

The application area of the lemmatizer is very wide:

* information retrieval (we have a token filter for Lucene / Solr / Elasticsearch, contact us, if you need one)
* sentiment analysis (read on, if you are interested in this)
* machine translation: to avoid issues with sparse word forms space one can lemmatize them first before translating
* your project / research

## Dictionary size
The dictionary contains order of 100k lemmas, which translates to several million words, including
the grammatical cases as well as polysemic (multi-meaning, homonyms) words.

## Part of Speech (POS) tags
For each word, lemmatizer returns its POS tag. There can be many POS tags for a given word.

## User dictionary
If for a particular word you do not agree with the lemma and POS tag prediction, you can redefine this behaviour
in your personal user dictionary. It is done by establishing a link with an existing word, grammatical features
of which are the closest to your target. For instance, if to assume the lemmatizer does not recognize
the word инет (social media slang word from Internet), you define it via the linked word Интернет (Internet):

инет\tинтернет

(\t is the symbol of tabulation)

# Sentiment Analyzer features

Documentation in [pdf form](documentation/sentiment_analysis/russian/SemanticAnalyzer_sentiment_analysis_V2.1.1_Installation_And_Usage_Guide.pdf) in Russian is available.

## 3-way classification
The system returns one of the following labels for a given text (or sentence): NEUTRAL, POSITIVE, NEGATIVE.

## Object oriented sentiment detection
Most of the times, especially when monitoring a brand / person / company in the social / news media, it is
important to know the sentiment oriented to it. In the following example:

I like Phone1, but Phone2 is ugly.

we expect to get POSITIVE label for the object Phone1, and NEGATIVE for the object Phone2.

## Object synonyms
Because an object can be referred to using different words or word sequences (like "Android" or "Droid" etc),
the system supports describing the target object with an array of object synonyms. The first object synonym to be found
in the given text will trigger sentiment detection algorithm.

## Sentiment detection quality control
The quality can be controlled by overriding / introducing new sentiment words in the user polarity dictionaries.

# Topic API
The system for topical grouping of and search in unstructured content. Large-scale compatible: you can generate topics out of your text silos on as big a dataset as millions of texts. You create your personal data lake which is sealed with your mashape credentials.
In order to leverage the API you need to first load textual content into it: articles, tweets, blog posts -- what have you.

Use [TopicUploader](src/main/java/com/semanticanalyzer/TopicUploader.java) to load data from a MySQL DB. You need to modify settings of mybatis accordingly as well as com.semanticanalyzer.dao.ArticleEntry in accordance with your DB.

Access / subscribe to the API here: https://market.mashape.com/dmitrykey/topicapi