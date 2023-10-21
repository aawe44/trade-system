package com.jason.trade.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * Add a Goods object to Elasticsearch.
     *
     * @param goods The Goods object to be added.
     */
    @Override
    public void addGoodsToES(Goods goods) {
        try {
            // Serialize the 'goods' object to JSON
            String jsonData = JSON.toJSONString(goods);

            // Create an Elasticsearch IndexRequest
            IndexRequest indexRequest = new IndexRequest("goods")
                    .source(jsonData, XContentType.JSON);

            // Send the request and get the response
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

            // Log the result
            log.info("Added goods to Elasticsearch: {} | Result: {}", goods, response);
        } catch (Exception e) {
            // Handle any exceptions and log an error message
            log.error("Failed to add goods to Elasticsearch. SearchService addGoods error", e);
        }
    }


    /**
     * Search for Goods objects in Elasticsearch based on a keyword.
     *
     * @param keyword The keyword to search for in Goods.
     * @param from    The starting index for pagination.
     * @param size    The number of results to return.
     * @return A list of matching Goods objects.
     */
    @Override
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        try {
            // Create a search request for the "goods" index
            SearchRequest searchRequest = new SearchRequest("goods");

            // Create a search source builder
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // Define the query for the search
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "description", "keywords");
            searchSourceBuilder.query(queryBuilder);

            // Set the pagination parameters
            searchSourceBuilder.from(from).size(size);

            // Sort the results by "saleNum" in descending order
            searchSourceBuilder.sort("saleNum", SortOrder.DESC);

            // Attach the search source builder to the search request
            searchRequest.source(searchSourceBuilder);

            // Execute the search request and get the response
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // Log the search response
            log.info("Elasticsearch search response: {}", JSON.toJSONString(searchResponse));

            // Extract search hits and total count
            SearchHits searchHits = searchResponse.getHits();
            long totalNum = searchHits.getTotalHits().value;
            log.info("Search total number: {}", totalNum);

            // Map search hits to Goods objects
            List<Goods> goodsList = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                String sourceAsString = hit.getSourceAsString();
                Goods goods = JSON.parseObject(sourceAsString, Goods.class);
                goodsList.add(goods);
            }

            // Log the search result
            log.info("Search result: {}", JSON.toJSONString(goodsList));

            return goodsList;
        } catch (Exception e) {
            // Handle any exceptions and log an error message
            log.error("SearchService searchGoodsList error", e);
            return null;
        }
    }
}
