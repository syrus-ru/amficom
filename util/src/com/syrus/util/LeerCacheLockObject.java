package com.syrus.util;

public class LeerCacheLockObject implements CacheLockObject {
	int code;
	
	public LeerCacheLockObject(int code) {
		this.code = code;
	}
	
	public Object getResource() {
		return new Integer(code);
	}
}
