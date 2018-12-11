package com.humor.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author zhangshaoze
 * @date 2018/11/21 2:49 PM
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String obj2String(T obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T string2Obj(String content,Class<T> clas){
        try {
            return objectMapper.readValue(content,clas);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
