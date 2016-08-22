package com.java.component.es.bean;

import com.java.component.es.enums.DataFilter;
import com.java.component.es.enums.SearchLogic;
import com.java.component.es.enums.SearchType;

import java.io.Serializable;

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
 * Module Desc:com.java.component.es.bean
 * User: zjprevenge
 * Date: 2016/8/22
 * Time: 23:21
 */

public class SearchCondition implements Serializable {
    private static final long serialVersionUID = 2969486263860970700L;

    private SearchType searchType = SearchType.query_String;

    private SearchLogic searchLogic = SearchLogic.must;

    private DataFilter dataFilter = DataFilter.exists;

    private String queryStringPrecision = "100";

    private float boost = 1.0f;

    private boolean highlight = false;

    private Object[] data;

    public SearchCondition() {
    }

    public SearchCondition(Builder builder) {
        this.boost = builder.boost;
        this.dataFilter = builder.dataFilter;
        this.highlight = builder.highlight;
        this.searchLogic = builder.searchLogic;
        this.searchType = builder.searchType;
        this.queryStringPrecision = builder.queryStringPrecision;
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private SearchType searchType;

        private SearchLogic searchLogic;

        private DataFilter dataFilter;

        private String queryStringPrecision;

        private float boost;

        private boolean highlight;

        private Object[] data;

        public Builder() {
        }

        public Builder searchType(SearchType searchType) {
            this.searchType = searchType;
            return this;
        }

        public Builder queryStringPrecision(String queryStringPrecision) {
            this.queryStringPrecision = queryStringPrecision;
            return this;
        }

        public Builder searchLogic(SearchLogic searchLogic) {
            this.searchLogic = searchLogic;
            return this;
        }

        public Builder dataFilter(DataFilter dataFilter) {
            this.dataFilter = dataFilter;
            return this;
        }

        public Builder boost(float boost) {
            this.boost = boost;
            return this;
        }

        public Builder highlight(boolean highlight) {
            this.highlight = highlight;
            return this;
        }

        public Builder data(Object[] data) {
            this.data = data;
            return this;
        }

        public SearchCondition build() {
            return new SearchCondition(this);
        }
    }

    public String getQueryStringPrecision() {
        return queryStringPrecision;
    }

    public void setQueryStringPrecision(String queryStringPrecision) {
        this.queryStringPrecision = queryStringPrecision;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public SearchLogic getSearchLogic() {
        return searchLogic;
    }

    public void setSearchLogic(SearchLogic searchLogic) {
        this.searchLogic = searchLogic;
    }

    public DataFilter getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    public float getBoost() {
        return boost;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
