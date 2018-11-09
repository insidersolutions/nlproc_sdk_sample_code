package com.semanticanalyzer.dao;

import lombok.Data;
import lombok.Setter;

@Data
public class SentimentDto {
    public String text;
    String object_keywords;
    int include_strength;
}
