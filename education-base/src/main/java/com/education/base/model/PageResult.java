package com.education.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.io.Serializable;
/**
 * @program: onlineEducation
 * @description: 分页查询结果
 * @author: ShyZOra
 * @create: 2023-03-19 13:43
 **/
@Data
public class PageResult<T> implements Serializable{
    //数据列表
    private List<T> items;
    //总记录数
    private long counts;
    //当前页码
    private long page;
    //每页记录数
    private long pagesize;
    public PageResult(List<T> items, long counts, long page, long pageSize)
    {
        this.items = items;
        this.counts = counts;
        this.page = page;this.pagesize= pageSize;
    }
}
