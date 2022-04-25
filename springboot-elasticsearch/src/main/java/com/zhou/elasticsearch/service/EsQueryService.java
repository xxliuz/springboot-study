package com.zhou.elasticsearch.service;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/25 14:11
 * @Description:
 */
public interface EsQueryService {

    /** 根据查询参数，从ES查询多条document
     *
     * @param index      索引
     * @param type       类型
     * @param params     查询参数
     *               查询条件为满足map中所有条件的交集
     * @return
     */
    Map<String, Object> getSingleQueryResult(String index, String type, Map<String, Object> params);

    /** 根据查询参数，从ES查询多条document
     *
     * @param index      索引
     * @param type       类型
     * @param sortMap     排序参数
     * @param pagingMap  分页参数
     * @param params     查询参数
     *               查询条件为满足map中所有条件的交集
     * @return
     */
    Map<String, Object> getMultiQueryResult(String index, String type, Map<String, SortOrder> sortMap, Map<String,String> pagingMap, Map<String, Object> params);


    /**
     * 根据查询参数，从ES滚动查询所有document
     * @param esIndex
     * @param params
     * @param sortField
     * @param returnFields
     * @return
     */
    Map<String, Object> scrollQueryResult(String esIndex, Map<String, Object> params, String sortField, String[] returnFields);

    /**
     *
     * @param index
     * @param doc
     * @param queryBuilder
     * @param script
     * @param isRefresh
     * @return
     */
    boolean updateByQuery(String index, String doc, QueryBuilder queryBuilder, Script script, Boolean isRefresh);

    /**
     *  创建索引
     * @param index       索引名称
     * @param type        索引类型
     * @param request     创建索引的REQUEST
     * @throws IOException
     */
    void createIndex(String index, String type, CreateIndexRequest request) throws IOException;

    /**
     * 删除索引
     * @param index 索引名称
     * @throws IOException
     */
    void deleteIndex(String index)throws IOException;

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    boolean existsIndex(String index)throws IOException ;
}
