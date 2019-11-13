package top.jfunc.json.impl;

import net.minidev.json.JSONValue;
import top.jfunc.json.Json;
import top.jfunc.json.JsonArray;
import top.jfunc.json.JsonException;
import top.jfunc.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiongshiyan at 2018/6/11
 */
public class JSONArray extends BaseListJSONArray {
    public JSONArray(List<Object> list){
        super(list);
    }
    public JSONArray(){
    }
    public JSONArray(String arrayString){
        super(arrayString);
    }

    @Override
    public List<Object> str2List(String arrayString) {
        Object parse = JSONValue.parse(arrayString);
        return (net.minidev.json.JSONArray)parse;
    }
    @Override
    public JsonArray parse(String jsonString) {
        this.list = str2List(jsonString);
        return this;
    }

    @Override
    public String toString() {
        //需要针对JsonObject/JsonArray处理
        Map<Integer , Json> map = new HashMap<>();
        int size = size();
        for (int i = 0; i < size; i++) {
            Object o = list.get(i);
            if(o instanceof JsonObject || o instanceof JsonArray){
                map.put(i , (Json) o);
            }
        }
        map.forEach((k,v)->{
            list.remove((int)k);
            list.add(k , v.unwrap());
        });

        return JSONValue.toJSONString(list);
    }
    @Override
    public JsonObject getJsonObject(int index) {
        assertIndex(index , size());
        Object opt = list.get(index);
        if(opt instanceof Map){
            return new JSONObject((Map<String, Object>) opt);
        }
        return (JsonObject) opt;
    }

    @Override
    public JsonArray getJsonArray(int index) {
        assertIndex(index , size());
        Object opt = list.get(index);
        if(opt instanceof List){
            return new JSONArray((List<Object>) opt);
        }
        return (JsonArray) opt;
    }

    @Override
    public JsonArray fromList(List<Object> list) {
        this.list = list;
        return this;
    }

    @Override
    public Json toJson(Object o) {
        try {
            return (Json) o;
        } catch (Exception e) {
            throw new JsonException("不能将非Json的对象转换为Json");
        }
    }
}
