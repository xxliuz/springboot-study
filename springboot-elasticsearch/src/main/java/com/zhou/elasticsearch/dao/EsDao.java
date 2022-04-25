package com.zhou.elasticsearch.dao;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/24 18:02
 * @Description: esDao
 */
public interface EsDao {

    /**
     * 更新插入内容
     * @param index     索引index
     * @param type      类型type
     * @param id        id
     * @param source    添加内容
     * @return  更新插入结果
     */
    boolean insert(String index, String type, String id, Map<String, Object> source);

    /**
     * 查询并生成scrollId
     * @param index index
     * @param type type
     * @param queryBuilder 查询条件
     * @param size 大小
     * @param millisecond 分页存在时间
     * @return
     */
    SearchResponse getByInitialScroll(String index, String type, String sortField, QueryBuilder queryBuilder, String[] returnFields, int size, int millisecond);


    /**
     * 根据scrollId滚动查询
     * @param scrollId
     * @param millisecond
     * @return
     */
    SearchResponse getByScroll(String scrollId,long millisecond);


    /**
     * 查询内容
     *
     * @param queryBuilder 查询条件
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder, int size);

    /**
     * 根据字段排序查询内容
     *
     * @param queryBuilder 查询条件
     * @param sortField        排序字段
     * @param order        排序方式
     * @param startIndex   起始索引
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder, String sortField, SortOrder order,
                       int startIndex, int size);

    /**
     * 根据查询条件和聚合条件查询
     *
     * @param queryBuilder 查询条件
     * @param agg          聚合条件
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder, AggregationBuilder agg, int size);

    /**
     * 根据查询条件和聚合条件查询
     *
     * @param queryBuilder 查询条件
     * @param aggList      聚合条件
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder, List<AggregationBuilder> aggList,
                       int size);
    /**
     * 返回指定字段查询内容
     *
     * @param queryBuilder 查询条件
     * @param returnFields 返回字段
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder,String[] returnFields, int size);
    /**
     * 根据查询条件和字段排序查询
     *
     * @param queryBuilder 查询条件
     * @param sortMap      排序字段Map
     * @param startIndex   起始索引
     * @param size         返回列表大小
     * @return
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder, Map<String, SortOrder> sortMap,
                       int startIndex, int size);

    /**
     * 获取记录
     * @param index 索引
     * @param type  类型
     * @param id    id
     * @return      记录返回值
     */
    GetResponse get(String index, String type, String id);



    /**
     * 获取记录
     * @param returnFields    返回的字段
     * @return      记录返回值
     */
    SearchResponse get(String index, String type, QueryBuilder queryBuilder,String[] returnFields, Map<String, SortOrder> sortMap,
                       int startIndex, int size);


    /**
     *
     * @param index        index
     * @param type         type
     * @param queryBuilder 查询条件
     * @param script       执行脚本条件
     * @param isRefresh    是否立刻刷新
     * @return
     */
    boolean update(String index, String type, QueryBuilder queryBuilder, Script script, Boolean isRefresh);

    /**
     *
     * @param index     索引index
     * @param type      类型type
     * @param id        id
     * @param script    脚本
     * @param isRefresh    是否立刻刷新
     * @return          返回
     */
    boolean update(String index, String type, String id, Script script, Boolean isRefresh);

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
