package com.syrus.impexp.unicablemap;

import java.util.Hashtable;

public class IntHashtable extends Hashtable {
	public synchronized boolean containsKey(int key) {
		return super.containsKey(new Integer(key));
	}

	public synchronized Object get(int key) {
		return super.get(new Integer(key));
	}

	public synchronized Object put(int key, Object value) {
		return super.put(new Integer(key), value);
	}

	public synchronized Object remove(int key) {
		return super.remove(new Integer(key));
	}
}
