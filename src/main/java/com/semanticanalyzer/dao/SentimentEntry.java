package com.semanticanalyzer.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentimentEntry {
    public String articleId;
    public String resourceId;
    public String sentimentLabel;
    public Double sentimentStrength;
}
