package com.liuzq.searchbox.data;

public class BaseData<T> {

    private T data; //搜索参数
    private String search; // 搜索字段

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
