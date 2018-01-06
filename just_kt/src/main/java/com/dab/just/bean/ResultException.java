package com.dab.just.bean;

/**
 * Created by dab on 2018/1/4 0004 17:51
 */

public class ResultException extends  Exception{
    public ResultException(String message, int code) {
        super(message);
        this.code = code;
    }
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
