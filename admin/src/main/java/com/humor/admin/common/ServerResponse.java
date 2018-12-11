package com.humor.admin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by humor
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;
    private long count;

    public ServerResponse(){}

    private ServerResponse(int code){
        this.code = code;
    }
    private ServerResponse(int status,T data){
        this.code = status;
        this.data = data;
    }

    private ServerResponse(int code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }


}
