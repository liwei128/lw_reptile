package com.abner.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;



/**
 * @author wei.li
 * Created On 2017-11-29 11:48
 */
public class JsonUtil {
    private static Logger logger=Logger.getLogger(JsonUtil.class);

    public static String toString(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Bean转Json异常",e);
        }
        return "";
    }

    public static <T> T toBean(String json, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,clazz);
        } catch (IOException e) {
            logger.error("Json转Bean异常",e);
        }
        return null;
    }

    public static <T> List<T> toList(String json,Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("Json转List异常",e);
        }
        return null;
    }

}
