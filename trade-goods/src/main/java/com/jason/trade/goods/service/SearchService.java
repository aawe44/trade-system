package com.jason.trade.goods.service;

import com.jason.trade.goods.db.model.Goods;

import java.util.List;

/**
 * Service for managing goods in Elasticsearch.
 */
public interface SearchService {

    /**
     * Add a Goods object to Elasticsearch.
     *
     * @param goods The Goods object to be added.
     */
    void addGoodsToES(Goods goods);

    /**
     * Search for Goods objects in Elasticsearch based on a keyword.
     *
     * @param keyword The keyword to search for in Goods.
     * @param from    The starting index for pagination.
     * @param size    The number of results to return.
     * @return A list of matching Goods objects.
     */
    List<Goods> searchGoodsList(String keyword, int from, int size);

}
