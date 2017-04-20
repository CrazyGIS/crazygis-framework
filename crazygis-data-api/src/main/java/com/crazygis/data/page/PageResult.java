package com.crazygis.data.page;

import java.util.List;

/**
 * Created by William on 2014/12/13.
 */
public class PageResult<T> {

    //数据总行数
    private long total;

    private List<T> items;

    public PageResult(long total){
        this.total = total;
    }

    public PageResult(long total, List<T> items){
        this.total = total;
        this.items = items;
    }

    /**
     * 获取总行数
     * @return
     */
    public long getTotal() {
        return total;
    }

    /**
     * 获取查询的结果集
     * @return
     */
    public List<T> getItems(){
        return this.items;
    }

}
