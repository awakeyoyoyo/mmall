package com.mmall.util;


import com.mmall.pojo.User;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //初始化
        //设置序列化

        //对象得所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认date转化timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空bean转json得错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //所有日期得格式都统一为 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略在json字符串仲存在，但是在java对象仲不存在对应属性得情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Pase object to String error", e);
            return null;
        }
    }

    public static <T> String objToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Pase object to String error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to object error", e);
            return null;
        }
    }
    public static <T> T stringToObj(String str, TypeReference<T> tTypeReference) {
        if (StringUtils.isEmpty(str) || tTypeReference == null) {
            return null;
        }
        try {
            return tTypeReference.getType().equals(String.class) ? (T) str : (T)objectMapper.readValue(str, tTypeReference);
        } catch (Exception e) {
            log.warn("Parse String to object error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str,  Class<?> collectionClasss,Class<?>... elementClasses) {
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClasss,elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to object error", e);
            return null;
        }
    }

    public static void main(String[] args) {
        User u1=new User();
        u1.setId(1);
        u1.setEmail("873864980@qq.com");
        User u2=new User();
        u2.setId(2);
        u2.setEmail("873864980@qq.com");
        List<User> userList=new ArrayList<User>();
        userList.add(u1);
        userList.add(u2);
        String u1Json=JsonUtil.objToString(u1);
        String u1JsonPertty=JsonUtil.objToStringPretty(u1);
        log.info("user1Json:{}",u1Json);
        log.info("user1JsonPertty:{}",u1JsonPertty);
        String userListStr=JsonUtil.objToStringPretty(userList);
        log.info("=============================");
        log.info(userListStr);


        User user=JsonUtil.stringToObj(u1Json,User.class);
        List<User> userListObj=JsonUtil.stringToObj(userListStr, new TypeReference<List<User>>() {
        });
        List<User> userListObj2=JsonUtil.stringToObj(userListStr,List.class,User.class);

        System.out.println("end");
    }

}
