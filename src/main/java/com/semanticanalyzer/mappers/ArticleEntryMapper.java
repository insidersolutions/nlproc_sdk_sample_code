package com.semanticanalyzer.mappers;

import com.semanticanalyzer.dao.ArticleEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleEntryMapper {
    /**
     * Load Article Entries for the DB.
     *
     * @return list of Article entries
     */
    List<ArticleEntry> loadEntriesFromDB(@Param("resourceId") String resourceId);
}
