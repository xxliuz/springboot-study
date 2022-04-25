package com.zhou.elasticsearch.dao;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/24 18:02
 * @Description:
 */
@Slf4j
@Service
public class EsRestClientDaoImpl implements EsDao {

    @Autowired
    RestHighLevelClient restHighLevelClient;


    @Override
    public boolean insert(String index, String type, String id, Map<String, Object> source) {
        UpdateRequest updateRequest = new UpdateRequest(index,id).doc(source);
        updateRequest.upsert(source);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = updateResponse.getResult();
            log.info("insert updateResponse :"+result.toString());
            return result == DocWriteResponse.Result.CREATED || result == DocWriteResponse.Result.UPDATED;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return false;
    }

    private SearchResponse dealSearchRequest(SearchRequest searchRequest) {
        try {
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public SearchResponse getByInitialScroll(String index, String type, String sortField, QueryBuilder queryBuilder, String[] returnFields, int size, int millisecond) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(size).sort(sortField,SortOrder.ASC).fetchSource(returnFields,null).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder).scroll(new TimeValue(millisecond));
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse getByScroll(String scrollId, long millisecond) {
        SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
        searchScrollRequest.scroll(new TimeValue(millisecond));
        try {
            return restHighLevelClient.scroll(searchScrollRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(size).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, String sortField, SortOrder order, int startIndex, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).sort(sortField,order).from(startIndex).size(size).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, AggregationBuilder agg, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).aggregation(agg).size(size).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, List<AggregationBuilder> aggList, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(size).trackTotalHits(true);
        for (AggregationBuilder agg : aggList){
            searchSourceBuilder.aggregation(agg);
        }
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, String[] returnFields, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(size).fetchSource(returnFields,null).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, Map<String, SortOrder> sortMap, int startIndex, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).from(startIndex).size(size).trackTotalHits(true);
        for (Map.Entry<String, SortOrder> entry : sortMap.entrySet()) {
            searchSourceBuilder.sort(entry.getKey(), entry.getValue());
        }
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    @Override
    public GetResponse get(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index,id);
        try {
            return restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public SearchResponse get(String index, String type, QueryBuilder queryBuilder, String[] returnFields, Map<String, SortOrder> sortMap, int startIndex, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).fetchSource(returnFields,null).from(startIndex).size(size);

        for (Map.Entry<String, SortOrder> entry : sortMap.entrySet()) {
            searchSourceBuilder.sort(entry.getKey(), entry.getValue());
        }
        searchRequest.source(searchSourceBuilder);
        return dealSearchRequest(searchRequest);
    }

    private boolean dealUpdateRequest(UpdateByQueryRequest request) {
        try {
            BulkByScrollResponse bulkResponse = restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
            return bulkResponse.getUpdated() > 0;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public boolean update(String index, String type, QueryBuilder queryBuilder, Script script, Boolean isRefresh) {
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index);
        updateByQueryRequest.setAbortOnVersionConflict(false).setQuery(queryBuilder).setScript(script).setRefresh(isRefresh);
        return dealUpdateRequest(updateByQueryRequest);
    }

    @Override
    public boolean update(String index, String type, String id, Script script, Boolean isRefresh) {
        UpdateRequest updateRequest = new UpdateRequest(index,id).script(script);

        if(isRefresh){
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result =  updateResponse.getResult();
            return result == DocWriteResponse.Result.CREATED || result == DocWriteResponse.Result.UPDATED;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void createIndex(String index, String type, CreateIndexRequest request) throws IOException {
        log.info("source:" + request.toString());
        if (!existsIndex(index)) {
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            log.info("索引创建结果：" + response.isAcknowledged());
        } else {
            log.warn("索引：{}，已经存在，不能再创建。", index);
        }
    }

    @Override
    public void deleteIndex(String index) throws IOException{
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        if (restHighLevelClient.indices().exists(getIndexRequest,RequestOptions.DEFAULT)) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            log.info("source:" + deleteIndexRequest.toString());
            restHighLevelClient.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
        }
    }

    @Override
    public boolean existsIndex(String index) throws IOException{
        GetIndexRequest request = new GetIndexRequest(index);
        log.info("source:" + request.toString());
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        log.debug("existsIndex: " + exists);
        return exists;
    }
}
