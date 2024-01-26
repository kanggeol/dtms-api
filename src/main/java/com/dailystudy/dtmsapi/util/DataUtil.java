package com.dailystudy.dtmsapi.util;

import com.dailystudy.dtmsapi.base.DbList;
import com.dailystudy.dtmsapi.base.DbMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataUtil {

	public static Map<String, List<DbMap>> convertListGroupToMap(String key, List<DbMap> list) {
		return list.stream().collect(Collectors.groupingBy((DbMap map) -> map.get(key, ""), LinkedHashMap::new, Collectors.toList()));
	}

	public static Map<String, Object> convertListToMap(String key, DbList list) {
		return convertListToMap(key, list, null);
	}

	public static Map<String, Object> convertListToMap(String key, DbList list, String valueKey) {
		if(valueKey != null) {
			return list.stream().collect(Collectors.toMap(item -> item.get(key).toString(), item -> item.get(valueKey), (p1, p2) -> p2));
		}

		return list.stream().collect(Collectors.toMap(item -> item.get(key).toString(), Function.identity(), (p1, p2) -> p2));
	}
}
