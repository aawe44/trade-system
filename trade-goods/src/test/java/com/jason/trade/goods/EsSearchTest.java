package com.jason.trade.goods;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.model.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSearchTest {

    @Autowired
    private RestHighLevelClient client;


    public void contextLoads() {
    }


    @Test
    public void esTest() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("http://127.0.0.1", 9200, "http")
                )
        );

        System.out.println(JSON.toJSONString(client));
    }

    @Test
    public void addDoc() throws Exception {
        Person person = Person.builder()
                .id("125")
                .name("AndyChang")
                .address("HongKong")
                .age(18).build();

        String data = JSON.toJSONString(person);
        System.out.println(data);
        IndexRequest request =
                new IndexRequest("person")
                        .id(person.getId())
                        .source(data, XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());

    }

    @Test
    public void matchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("person");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();

        searchSourceBuilder.query(query);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        SearchHits searchHits = searchResponse.getHits();
        long totalNum = searchHits.getTotalHits().value;
        System.out.println("total number: " + totalNum);

        List<Person> personList = Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSourceAsString)
                .map(sourceAsString -> JSON.parseObject(sourceAsString, Person.class))
                .collect(Collectors.toList());

        System.out.println(JSON.toJSONString(personList));


    }

    @Test
    public void termAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("person");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermQueryBuilder query = QueryBuilders.termQuery("address", "HongKong");
        searchSourceBuilder.query(query);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        SearchHits searchHits = searchResponse.getHits();
        long totalNum = searchHits.getTotalHits().value;
        System.out.println("Total Number：" + totalNum);

        List<Person> personList = Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSourceAsString)
                .map(sourceAsString -> JSON.parseObject(sourceAsString, Person.class))
                .collect(Collectors.toList());

        System.out.println(JSON.toJSONString(personList));
    }


    @Test
    public void queryString() throws IOException {

        SearchRequest searchRequest = new SearchRequest("person");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryStringQueryBuilder query = QueryBuilders.queryStringQuery("HongKong OR Taiwan").field("name").field("address").defaultOperator(Operator.OR);

        searchSourceBuilder.query(query);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        SearchHits searchHits = searchResponse.getHits();
        long totalNum = searchHits.getTotalHits().value;
        System.out.println("Total Number：" + totalNum);

        List<Person> personList = Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSourceAsString)
                .map(sourceAsString -> JSON.parseObject(sourceAsString, Person.class))
                .collect(Collectors.toList());

        System.out.println(JSON.toJSONString(personList));
    }


    @Test
    public void queryMatch() throws IOException {

        SearchRequest searchRequest = new SearchRequest("person");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("name", "*Andy*");
        WildcardQueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery("address", "*Tai*");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.should(queryBuilder1);
        boolQueryBuilder.should(queryBuilder2);

        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse));

        SearchHits searchHits = searchResponse.getHits();
        long totalNum = searchHits.getTotalHits().value;
        System.out.println("Total Number：" + totalNum);

        List<Person> personList = Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSourceAsString)
                .map(sourceAsString -> JSON.parseObject(sourceAsString, Person.class))
                .collect(Collectors.toList());

        System.out.println(JSON.toJSONString(personList));
    }
}
