package com.zhou.elasticsearch.service;

import com.zhou.elasticsearch.dao.EsDao;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: zhou.liu
 * @Date: 2022/4/25 14:12
 * @Description:
 */
@Slf4j
@Service
public class EsQueryServiceImpl implements EsQueryService {

    @Autowired
    private EsDao esDao;

    @Override
    public Map<String, Object> getSingleQueryResult(String index, String type, Map<String, Object> params) {
        QueryBuilder queryBuilder = generateAndQueryBuilderByMap(params);
        SearchResponse searchResponse = esDao.get(index,type,queryBuilder,1);
        if(searchResponse.getHits().getTotalHits().value == 1){
            return searchResponse.getHits().getHits()[0].getSourceAsMap();
        }else {
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getMultiQueryResult(String index, String type, Map<String, SortOrder> sortMap, Map<String, String> pagingMap, Map<String, Object> params) {
        QueryBuilder queryBuilder = generateAndQueryBuilderByMap(params);
        int limit = Integer.parseInt(pagingMap.get("limit"));
        int offset =Integer.parseInt(pagingMap.get("offset"));
        SearchResponse searchResponse = esDao.get(index, type, queryBuilder,sortMap,offset,limit);
        return getResultMap(convertSearchResponse(searchResponse),searchResponse.getHits().getTotalHits().value);
    }

    @Override
    public Map<String, Object> scrollQueryResult(String esIndex, Map<String, Object> params, String sortField, String[] returnFields) {
        QueryBuilder queryBuilder = generateAndQueryBuilderByMap(params);
        SearchResponse searchResponse = esDao.getByInitialScroll(esIndex,"doc",sortField,queryBuilder,returnFields, 10000, 5000);
        List<Map<String,Object>> result = new ArrayList<>();
        while(searchResponse.getHits().getHits().length != 0){
            result.addAll(convertSearchResponse(searchResponse));
            searchResponse = esDao.getByScroll(searchResponse.getScrollId(), 5000);
        }

        return getResultMap(result,searchResponse.getHits().getTotalHits().value);
    }


    @Override
    public boolean updateByQuery(String index, String doc, QueryBuilder queryBuilder, Script script, Boolean isRefresh) {
        return  esDao.update(index, doc, queryBuilder, script, isRefresh);
    }

    @Override
    public void createIndex(String index, String type, CreateIndexRequest request) throws IOException {
        esDao.createIndex(index,type,request);
    }

    @Override
    public void deleteIndex(String index) throws IOException {
        esDao.deleteIndex(index);
    }

    @Override
    public boolean existsIndex(String index) throws IOException {
        return esDao.existsIndex(index);
    }

    /**
     * 根据传入的Map，把需要查询的参数进行且关联，返回es查询条件
     * @param paramsMap
     * @return
     */
    private QueryBuilder generateAndQueryBuilderByMap(Map<String, Object> paramsMap){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            if (entry.getValue() == null) {
                break;
            }
            boolQueryBuilder.should(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
        }
        return boolQueryBuilder.must().size() == 0 ? null: boolQueryBuilder;
    }

    private  List<Map<String,Object>> convertSearchResponse(SearchResponse searchResponse){
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> mapResult  = hit.getSourceAsMap();
            list.add(mapResult);
        }
        return list;
    }

    private Map<String,Object> getResultMap(List<Map<String,Object>> result, long totalSize){
        Map<String, Object> resultMap = new HashMap<>(2);
        log.info("search result count is {}", totalSize);
        resultMap.put("total",totalSize);
        resultMap.put("rows", result);
        return resultMap;
    }

}
