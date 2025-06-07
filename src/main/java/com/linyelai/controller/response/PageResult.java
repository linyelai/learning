package com.linyelai.controller.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据类型
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 空分页结果
     */
    public static <T> PageResult<T> empty() {
        return empty(0, 0);
    }

    /**
     * 空分页结果
     */
    public static <T> PageResult<T> empty(int pageNum, int pageSize) {
        PageResult<T> page = new PageResult<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal(0);
        page.setPages(0);
        page.setList(Collections.emptyList());
        return page;
    }

    /**
     * 计算总页数
     */
    public int getPages() {
        if (pageSize == 0) {
            return 0;
        }
        return (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    }

    // getter和setter
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}