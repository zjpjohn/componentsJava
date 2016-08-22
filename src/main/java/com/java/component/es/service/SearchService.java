package com.java.component.es.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.java.component.es.bean.BaseBean;
import com.java.component.es.bean.SearchCondition;
import com.java.component.es.enums.DataFilter;
import com.java.component.es.enums.SearchLogic;
import com.java.component.es.enums.SearchType;
import com.java.component.es.utils.EsUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.java.component.es.service
 * User: zjprevenge
 * Date: 2016/8/22
 * Time: 19:13
 */

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    @Resource
    private TransportClient transportClient;

    private String hightlightCSS;

    public TransportClient getTransportClient() {
        return transportClient;
    }

    public void setTransportClient(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    public String getHightlightCSS() {
        return hightlightCSS;
    }

    public void setHightlightCSS(String hightlightCSS) {
        this.hightlightCSS = hightlightCSS;
    }

    /**
     * 创建索引
     *
     * @param index 索引名称
     * @param type  类型
     * @param datas 数据
     * @param <V>
     * @return
     */
    public <V extends BaseBean> boolean createIndex(String index, String type, Class<V> clazz, List<V> datas) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index), "index must not be empty...");
        Preconditions.checkArgument(StringUtils.isNotBlank(type), "type must not be empty...");
        Preconditions.checkArgument(datas != null && datas.size() > 0, "datas must not be empty...");
        if (!isExistType(index, type)) {
            createIndexEmpty(index, type, clazz);
        }
        BulkRequestBuilder bulk = transportClient.prepareBulk();
        for (V data : datas) {
            try {
                IndexRequest request = transportClient.prepareIndex(index, type)
                        .setId(data.getId().toString())
                        .setSource(EsUtils.getXBuidler(data))
                        .request();
                bulk.add(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        BulkResponse response = bulk.execute().actionGet();
        return !response.hasFailures();
    }

    /**
     * 判断指定索引的类型是存在
     *
     * @param index 索引
     * @param type  类型
     * @return
     */
    public boolean isExistType(String index, String type) {
        TypesExistsResponse existsResponse = transportClient.admin()
                .indices()
                .typesExists(
                        new TypesExistsRequest(new String[]{index}, type))
                .actionGet();
        return existsResponse.isExists();
    }

    /**
     * 创建索引结构，空索引
     *
     * @param index 索引
     * @param type  类型
     * @param clazz 数据里
     * @param <V>
     * @return
     */
    private <V extends BaseBean> boolean createIndexEmpty(String index, String type, Class<V> clazz) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(type)
                    .startObject("_timestamp")
                    .field("enable", true)
                    .field("store", "no")
                    .field("index", "not_analyzed")
                    .endObject();
            builder = builder.startObject("properties");
            for (Field field : clazz.getDeclaredFields()) {
                com.java.component.es.annotation.Field annotation = field.getAnnotation(com.java.component.es.annotation.Field.class);
                if (annotation != null) {
                    builder = builder.startObject(field.getName())
                            .field("type", field.getClass().getSimpleName().toLowerCase())
                            .field("store", annotation.store())
                            .field("analyzer", annotation.analyzer())
                            .field("index", annotation.index())
                            .endObject();
                }
            }
            builder = builder.endObject().endObject().endObject();
            CreateIndexRequestBuilder prepareCreate = transportClient.admin().indices().prepareCreate(index);
            prepareCreate.addMapping(type, builder);
            prepareCreate.execute().actionGet();
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除数据
     *
     * @param index
     * @param type
     * @param ids
     * @return
     */
    public boolean deleteIndex(String index, String type, List<String> ids) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index), "index must not be empty...");
        Preconditions.checkArgument(StringUtils.isNotBlank(type), "type must not be empty... ");
        Preconditions.checkArgument(ids != null && ids.size() > 0, "ids must not be empty...");
        BulkRequestBuilder bulk = transportClient.prepareBulk();
        for (String id : ids) {
            DeleteRequestBuilder delete = transportClient.prepareDelete(index, type, id);
            bulk.add(delete);
        }
        BulkResponse response = bulk.execute().actionGet();
        return !response.hasFailures();
    }

    /**
     * 更新索引数据
     *
     * @param index 索引
     * @param type  类型
     * @param ids   id集合
     * @param datas 数据集
     * @param <V>
     * @return
     */
    public <V extends BaseBean> boolean updateIndex(String index, String type, List<String> ids, List<V> datas) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index), "index must not be empty...");
        Preconditions.checkArgument(StringUtils.isNotBlank(type), "type must not be empty...");
        Preconditions.checkArgument(datas != null && datas.size() > 0, "datas must not be empty...");
        BulkRequestBuilder bulk = transportClient.prepareBulk();
        for (V data : datas) {
            try {
                UpdateRequestBuilder update = transportClient.prepareUpdate(index, type, data.getId().toString())
                        .setSource(EsUtils.getXBuidler(data));
                bulk.add(update);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        BulkResponse response = bulk.execute().actionGet();
        return !response.hasFailures();
    }

    /**
     * 创建range查询条件
     *
     * @param fieldName 查询字段值
     * @param values    范围
     * @return
     */
    private RangeQueryBuilder createRangeQueryBuilder(String fieldName,
                                                      Object[] values) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fieldName), "fieldName must not be empty...");
        Preconditions.checkState(EsUtils.checkValue(values), "values must not be empty...");

        boolean timeType = false;
        if (EsUtils.isDate(values[0]) && EsUtils.isDate(values[1])) {
            timeType = true;
        }
        String start, end;
        if (timeType) {
            start = EsUtils.formatDateFromDate(values[0]);
            end = EsUtils.formatDateFromDate(values[1]);
        } else {
            start = values[0].toString();
            end = values[1].toString();
        }
        return QueryBuilders.rangeQuery(fieldName).from(start).to(end);
    }

    /**
     * 创建过滤器
     *
     * @param searchLogic
     * @param queryBuilder
     * @param searchContent
     * @param filterContent
     * @return
     */
    private QueryBuilder createFilterQuery(SearchLogic searchLogic,
                                           QueryBuilder queryBuilder,
                                           Map<String, SearchCondition> searchContent,
                                           Map<String, SearchCondition> filterContent) {

        Iterator<Map.Entry<String, SearchCondition>> iterator = searchContent.entrySet().iterator();
        AndFilterBuilder andFilterBuilder = null;
        while (iterator.hasNext()) {
            Map.Entry<String, SearchCondition> entry = iterator.next();
            SearchCondition searchCondition = entry.getValue();
            if (!EsUtils.checkValue(searchCondition.getData())) {
                continue;
            }
            if (searchCondition.getDataFilter() == DataFilter.exists) {
                //被搜索为存在过滤
                ExistsFilterBuilder existsFilter = FilterBuilders.existsFilter(entry.getKey());
                if (andFilterBuilder == null) {
                    andFilterBuilder = FilterBuilders.andFilter(existsFilter);
                } else {
                    andFilterBuilder = andFilterBuilder.add(existsFilter);
                }
            }
        }

        //没有过滤条件
        if (filterContent == null || filterContent.isEmpty()) {
            return QueryBuilders.filteredQuery(queryBuilder, andFilterBuilder);
        }
        //构造过滤条件
        QueryFilterBuilder queryFilterBuilder = FilterBuilders.queryFilter(createQueryBuilder(filterContent, searchLogic));
        return QueryBuilders.filteredQuery(queryBuilder, FilterBuilders.andFilter(andFilterBuilder, queryFilterBuilder));
    }


    /**
     * 创建搜索条件
     *
     * @param searchContent 搜索条件
     * @param searchLogic   搜索逻辑
     * @return
     */
    private QueryBuilder createQueryBuilder(Map<String, SearchCondition> searchContent,
                                            SearchLogic searchLogic) {
        if (searchContent == null || searchContent.isEmpty()) {
            return null;
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Map.Entry<String, SearchCondition>> iterator = searchContent.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SearchCondition> entry = iterator.next();
            String field = entry.getKey();
            SearchCondition searchCondition = entry.getValue();
            if (!EsUtils.checkValue(searchCondition.getData())) {
                continue;
            }
            QueryBuilder queryBuilder = createSingleFieldQueryBuilder(field, searchCondition);
            if (queryBuilder != null) {
                if (searchLogic == SearchLogic.should) {
                    boolQueryBuilder = boolQueryBuilder.should(queryBuilder);
                } else if (searchLogic == SearchLogic.must) {
                    boolQueryBuilder = boolQueryBuilder.must(queryBuilder);
                }
            }
        }
        return boolQueryBuilder;
    }


    /**
     * 创建单个字段的查询条件
     *
     * @param field
     * @param searchCondition
     * @return
     */
    private QueryBuilder createSingleFieldQueryBuilder(String field,
                                                       SearchCondition searchCondition) {
        //区间搜索
        if (searchCondition.getSearchType() == SearchType.range) {
            return createRangeQueryBuilder(field, searchCondition.getData());
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Object value : searchCondition.getData()) {
            QueryBuilder queryBuilder = null;
            String formatValue = value.toString().trim().replace("*", "");
            if (searchCondition.getSearchType() == SearchType.term) {
                queryBuilder = QueryBuilders.termQuery(field, formatValue)
                        .boost(searchCondition.getBoost());
            } else if (searchCondition.getSearchType() == SearchType.query_String) {
                if (formatValue.length() == 1) {
                    if (Pattern.matches("[0-9]", formatValue)) {
                        formatValue = "*" + formatValue + "*";
                    }
                }
                QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(formatValue)
                        .minimumShouldMatch(searchCondition.getQueryStringPrecision());
                queryBuilder = queryStringQueryBuilder.field(field).boost(searchCondition.getBoost());
            }
            if (searchCondition.getSearchLogic() == SearchLogic.should) {
                boolQueryBuilder = boolQueryBuilder.should(queryBuilder);
            } else if (searchCondition.getSearchLogic() == SearchLogic.must) {
                boolQueryBuilder = boolQueryBuilder.must(queryBuilder);
            }
        }
        return boolQueryBuilder;
    }

    public List<Map<String, Object>> complexSearch(String[] indexNames,
                                                   Map<String, SearchCondition> mustSearchCondition,
                                                   Map<String, SearchCondition> shouldSearchCondition,
                                                   int from,
                                                   int offset,
                                                   String sortField,
                                                   String sortType) {
        Preconditions.checkArgument(offset <= 0, "offset must greater than zero...");
        //创建must查询条件
        QueryBuilder mustQuery = createQueryBuilder(mustSearchCondition, SearchLogic.must);
        //创建should查询条件
        QueryBuilder shouldQuery = createQueryBuilder(shouldSearchCondition, SearchLogic.should);
        if (mustQuery == null && shouldQuery == null) {
            return null;
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (mustQuery != null) {
            boolQuery.must(mustQuery);
        }
        if (shouldQuery != null) {
            boolQuery.should(shouldQuery);
        }
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(indexNames)
                .setSearchType(org.elasticsearch.action.search.SearchType.DEFAULT)
                .setQuery(boolQuery)
                .setFrom(from)
                .setSize(offset)
                .setExplain(true);
        if (sortField != null
                && !sortField.isEmpty()
                && sortType != null
                && !sortType.isEmpty()) {
            SortOrder sortOrder = sortType.equals("desc") ? SortOrder.DESC : SortOrder.ASC;
            searchRequestBuilder.addSort(sortField, sortOrder);
        }
        searchRequestBuilder = createHighlight(searchRequestBuilder, mustSearchCondition);
        searchRequestBuilder = createHighlight(searchRequestBuilder, shouldSearchCondition);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return getSearchResult(searchResponse);
    }


    /**
     * 解析搜索结果
     *
     * @param response
     * @return
     */
    private List<Map<String, Object>> getSearchResult(SearchResponse response) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : searchHit.getSource().entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
            Map<String, HighlightField> highlightFieldMap = searchHit.highlightFields();
            for (Map.Entry<String, HighlightField> entry : highlightFieldMap.entrySet()) {
                Text[] texts = entry.getValue().fragments();
                if (texts.length == 1) {
                    resultMap.put(entry.getKey(), texts[0].toString());
                } else {
                    log.warn("搜索结果中高亮显示结果出现多条数据");
                }
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 创建高亮显示
     *
     * @param searchRequestBuilder
     * @param searchConditionMap
     * @return
     */
    private SearchRequestBuilder createHighlight(SearchRequestBuilder searchRequestBuilder,
                                                 Map<String, SearchCondition> searchConditionMap) {
        Set<Map.Entry<String, SearchCondition>> entrySet = searchConditionMap.entrySet();
        for (Map.Entry<String, SearchCondition> entry : entrySet) {
            String field = entry.getKey();
            SearchCondition searchCondition = entry.getValue();
            if (!EsUtils.checkValue(searchCondition.getData())) {
                continue;
            }
            //是高亮显示
            if (searchCondition.isHighlight()) {
                searchRequestBuilder.addHighlightedField(field, 1000)
                        .setHighlighterPreTags("<" + this.hightlightCSS.split(",") + ">")
                        .setHighlighterPostTags("</" + hightlightCSS.split(",") + ">");
            }
        }
        return searchRequestBuilder;
    }

    /**
     * 自动补全
     *
     * @param indexNames 索引名称
     * @param field      字段
     * @param value
     * @param count
     * @return
     */
    public List<String> getSuggest(String[] indexNames,
                                   String field,
                                   String value,
                                   int count) {
        return null;
    }

    /**
     * 分组查询
     *
     * @param indexName
     * @param mustCondition
     * @param shouldCondition
     * @param groupFields
     * @return
     */
    public Map<String, String> group(String indexName,
                                     Map<String, SearchCondition> mustCondition,
                                     Map<String, SearchCondition> shouldCondition,
                                     String[] groupFields) {
        return null;
    }


    /**
     * 分组查询
     *
     * @param indexName
     * @param queryBuilder
     * @param groupFields
     * @return
     */
    private Map<String, String> group(String indexName, QueryBuilder queryBuilder, String[] groupFields) {
        return null;
    }

    /**
     * 搜索数量
     *
     * @param indexName       索引名称
     * @param mustCondition   must条件
     * @param shouldCondition should条件
     * @return
     */
    public long searchCount(String indexName,
                            Map<String, SearchCondition> mustCondition,
                            Map<String, SearchCondition> shouldCondition) {
        return 0;
    }

}
