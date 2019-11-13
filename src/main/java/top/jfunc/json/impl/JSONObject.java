package top.jfunc.json.impl;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import top.jfunc.json.Json;
import top.jfunc.json.JsonArray;
import top.jfunc.json.JsonException;
import top.jfunc.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * @author xiongshiyan at 2018/6/10
 */
public class JSONObject extends BaseMapJSONObject {

    public JSONObject(Map<String , Object> map){
        super(map);
    }
    public JSONObject(){
    }
    public JSONObject(String jsonString){
        super(jsonString);
    }

    @Override
    protected Map<String, Object> str2Map(String jsonString) {
        Object parse = JSONValue.parse(jsonString);
        return (net.minidev.json.JSONObject)parse;
    }

    @Override
    protected String map2Str(Map<String, Object> map) {
        return JSONValue.toJSONString(map);
    }

    @Override
    public JsonObject parse(String jsonString) {
        this.map = str2Map(jsonString);
        return this;
    }

    @Override
    public <T> String serialize(T javaBean, boolean nullHold, String... ignoreFields) {
        JSONStyle jsonStyle = new JSONStyle();
        if(!nullHold){
            jsonStyle.ignoreNull();
        }
        return JSONValue.toJSONString(javaBean , jsonStyle);
    }

    @Override
    public <T> T deserialize(String jsonString, Class<T> clazz) {
        return JSONValue.parse(jsonString, clazz);
    }

    @Override
    public JsonObject getJsonObject(String key) {
        assertKey(key);
        //这里不能使用getJSONObject，因为每一种Json实现不一样，给出的JsonObject类型是不一致的。
        //这里就是各种JsonObject不能混用的原因
        Object temp = this.map.get(key);
        Object t = checkNullValue(key, temp);

        if(t instanceof Map){
            return new JSONObject((Map<String, Object>) t);
        }

        return (JsonObject) t;
    }

    @Override
    public JsonArray getJsonArray(String key) {
        assertKey(key);
        //这里不能使用getJSONObject，因为每一种Json实现不一样，给出的JsonObject类型是不一致的。
        //这里就是各种JsonObject不能混用的原因
        Object temp = this.map.get(key);
        Object t = checkNullValue(key, temp);

        if(t instanceof List){
            return new JSONArray((List<Object>)t);
        }
        return (JsonArray) t;
    }
    @Override
    public JsonObject fromMap(Map<String, Object> map) {
        return new JSONObject(map);
    }


    @Override
    public Json toJson(Object o) {
        if(o instanceof List){
            return new JSONArray((List<Object>) o);
        }
        if(o instanceof Map){
            return new JSONObject((Map<String, Object>) o);
        }
        try {
            return (Json) o;
        } catch (Exception e) {
            throw new JsonException("不能将非Json的对象转换为Json");
        }
    }
}
