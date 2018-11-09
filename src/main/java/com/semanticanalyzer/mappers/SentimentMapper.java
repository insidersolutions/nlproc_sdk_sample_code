package com.semanticanalyzer.mappers;

import com.semanticanalyzer.dao.SentimentEntry;
import org.apache.ibatis.annotations.Param;

public interface SentimentMapper {
    void insert(@Param("item") SentimentEntry sentimentEntry);
}
