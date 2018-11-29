package com.freelycar.saas.basic.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页查询结果集对象
 * @author tangwei - Toby
 * @date 2018/11/28
 * @email toby911115@gmail.com
 */
public class PaginationRJO {
    /**
     * 返回数据列表
     */
    private List data;

    /**
     * 每页显示个数
     */
    private int pageSize;

    /**
     * 当前页码
     * 注：后端JPA的Pageable是从0开始算第一页，前端是从1开始，所以构造函数中需要加1
     */
    private int currentPage;

    /**
     * 数据总数
     */
    private long total;

    /**
     * 分页总数
     */
    private int pageCount;

    /**
     *
     * @param pageResult
     * @return
     */
    public static PaginationRJO of(Page pageResult) {
        return new PaginationRJO(pageResult);
    }

    /**
     * 构造函数
     *
     * @param pageResult
     */
    private PaginationRJO(Page pageResult) {
        this.data = pageResult.getContent();
        this.pageSize = pageResult.getSize();
        this.currentPage = pageResult.getNumber() + 1;
        this.total = pageResult.getTotalElements();
        this.pageCount = pageResult.getTotalPages();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PaginationRJO{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", total=" + total +
                ", pageCount=" + pageCount +
                '}';
    }
}
