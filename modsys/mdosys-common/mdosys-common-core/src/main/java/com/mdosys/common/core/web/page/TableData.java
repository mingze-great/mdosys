package com.mdosys.common.core.web.page;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 *  PageUtils.startPage();
 *             List<Project> projectList = projectMapper.queryProjectByUserId(userId);
 *             PageInfo<Project> pageInfo = new PageInfo<>(projectList);
 *             return AjaxResult.success(pageInfo);
 * @param <T>
 */
public class TableData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public TableData() {
    }

    public TableData(Long total, List<T> list, boolean haveNextPage, Integer totalPages, Integer currentPage) {
        this.total = total;
        this.list = list;
        this.haveNextPage = haveNextPage;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    /** 总记录数 */
    private Long total;

    /** 是否还有下一页*/
    private boolean haveNextPage;

    /** 总记录页数*/
    private Integer totalPages;

    /** 当前页*/
    private Integer currentPage;

    /** 列表数据 */
    private List<?> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public boolean isHaveNextPage() {
        return haveNextPage;
    }

    public void setHaveNextPage(boolean haveNextPage) {
        this.haveNextPage = haveNextPage;
    }

    public Integer isTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
