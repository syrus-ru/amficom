package com.syrus.impexp.unicablemap;

import java.util.TreeMap;

public class IntTreeMap<V> extends TreeMap<Integer, V> {
	public synchronized boolean containsKey(int key) {
		return super.containsKey(new Integer(key));
	}

	public synchronized V get(int key) {
		return super.get(new Integer(key));
	}

	public synchronized V put(int key, V value) {
		return super.put(new Integer(key), value);
	}

	public synchronized V remove(int key) {
		return super.remove(new Integer(key));
	}
	
}
