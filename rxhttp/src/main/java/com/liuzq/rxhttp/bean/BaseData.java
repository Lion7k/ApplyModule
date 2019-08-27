package com.liuzq.rxhttp.bean;

/**
 * Created by liuzq on 2018/10/18.
 * 返回数据基类
 */

public class BaseData<T> {
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
