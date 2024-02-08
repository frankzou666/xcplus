package com.example.xcplusbase.base.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/*
  分页查询的结果统一封装模版
 */

@Data
@ToString
public class PageResult<T> implements Serializable {
    private List<T> items;
    private long counts;

    private long page;

    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageResult() {
    }
}
