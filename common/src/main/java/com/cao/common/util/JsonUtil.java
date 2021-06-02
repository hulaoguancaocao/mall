package com.cao.common.util;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JsonUtil {
    private static final Gson gson;
    private static final Gson caseGson;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";//date format


    static {
        gson = new Gson();

        GsonBuilder caseGsonBuilder = new GsonBuilder();
        caseGsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>() {
        }.getType(), new MapDeserializerDoubleAsIntFix());
        caseGsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        caseGsonBuilder.setDateFormat(DATE_FORMAT);
        caseGson = caseGsonBuilder.create();
    }


    private JsonUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * transfer java object to json str
     *
     * @param src target obj
     * @return json str
     */
    public static String toJSONString(Object src) {
        return gson.toJson(src);
    }

    /**
     * transfer json str o java obj
     *
     * @param json  json str
     * @param clazz target type
     * @param <T>   generic type
     * @return java obj
     */
    public static <T> T convert(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static Gson getCaseGson() {
        return caseGson;
    }

    public static class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {

        @Override
        @SuppressWarnings("unchecked")
        public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return (Map<String, Object>) read(json);
        }

        public Object read(JsonElement in) {

            if (in.isJsonArray()) {
                List<Object> list = new ArrayList<>();
                JsonArray arr = in.getAsJsonArray();
                for (JsonElement anArr : arr) {
                    list.add(read(anArr));
                }
                return list;
            } else if (in.isJsonObject()) {
                Map<String, Object> map = new LinkedTreeMap<>();
                JsonObject obj = in.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
                for (Map.Entry<String, JsonElement> entry : entitySet) {
                    map.put(entry.getKey(), read(entry.getValue()));
                }
                return map;
            } else if (in.isJsonPrimitive()) {
                JsonPrimitive prim = in.getAsJsonPrimitive();
                if (prim.isBoolean()) {
                    return prim.getAsBoolean();
                } else if (prim.isString()) {
                    return prim.getAsString();
                } else if (prim.isNumber()) {

                    Number num = prim.getAsNumber();
                    // here you can handle double int/long values
                    // and return any type you want
                    // this solution will transform 3.0 float to long values
                    if (Math.ceil(num.doubleValue()) == num.longValue())
                        return num.longValue();
                    else {
                        return num.doubleValue();
                    }
                }
            }
            return null;
        }
    }
}
