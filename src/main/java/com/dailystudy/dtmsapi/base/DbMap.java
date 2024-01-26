package com.dailystudy.dtmsapi.base;

import com.dailystudy.dtmsapi.util.UnitUtil;

import java.util.HashMap;
import java.util.Map;

public class DbMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 599162507926542478L;
	
	public DbMap() {
		
	}
	
	public DbMap(Map<? extends String, ? extends Object> map){
		super(map);
	}


	public String get(String name, String def) {
		if(!super.containsKey(name) || super.get(name) == null)
			return def;
		
		return super.get(name).toString();
	}
	
	public int get(String name, int def) {
		if(!super.containsKey(name) || super.get(name) == null || super.get(name).toString().isEmpty())
			return def;
		
		return UnitUtil.toInt(super.get(name));
	}

    public double get(String name, double def) {
        if(!super.containsKey(name) || super.get(name) == null || super.get(name).toString().isEmpty())
            return def;

        return UnitUtil.toDouble(super.get(name));
    }

    public DbMap clone() {
		return new DbMap(this);
	}
}
