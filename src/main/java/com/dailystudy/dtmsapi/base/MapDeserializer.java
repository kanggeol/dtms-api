package com.dailystudy.dtmsapi.base;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapDeserializer implements JsonDeserializer<DbMap> {
    @Override
    public DbMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DbMap map = (DbMap) read(json);
        if(map == null) {
            map = new DbMap();
        }

        return map;
    }

    public Object read(JsonElement in) {
        if(in.isJsonArray()){
            //JsonArray인 경우
            List<Object> list = new ArrayList<>();
            JsonArray arr = in.getAsJsonArray();
            for (JsonElement anArr : arr) {
                //JsonPrimitive 나올 떄까지 for문
                list.add(read(anArr));
            }
            return list;
        }else if(in.isJsonObject()){
            DbMap map = new DbMap();
            JsonObject obj = in.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
            for(Map.Entry<String, JsonElement> entry: entitySet){
                //JsonPrimitive 나올 떄까지 for문
                map.put(entry.getKey(), read(entry.getValue()));
            }
            return map;
        }else if( in.isJsonPrimitive()){
            JsonPrimitive prim = in.getAsJsonPrimitive();
            if(prim.isBoolean()){
                //true , fales 형으로
                return prim.getAsBoolean();
            }else if(prim.isString()){
                //String으로
                return prim.getAsString();
            }else if(prim.isNumber()){
                Number num = prim.getAsNumber();

                if(Math.ceil(num.doubleValue()) == num.longValue()) {
                    //소수점 버림, long형으로.
                    return num.longValue();
                } else{
                    //소수점 안버림, Double 형으로
                    return num.doubleValue();
                }
            }
        }
        return null;
    }
}
