SemanticAnalyzer SDK Sample Java Code
======================

These code samples illustrate the usage of Lemmatizing and Sentiment Analysis SDKs 
for the Russian language.

To start using these technologies in your projects, you need to acquire the license. Get
in touch at dk@semanticanalyzer.info

Sentiment Analysis technology can be tried out in the form of API on mashape.com:

https://www.mashape.com/dmitrykey/russiansentimentanalyzer

# Lemmatizer features
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
The quality can be controlled by overriding / introducing new sentiment words in user polarity dictionaries.
