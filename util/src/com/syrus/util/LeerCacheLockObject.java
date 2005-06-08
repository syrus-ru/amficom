package com.syrus.util;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public class LeerCacheLockObject implements CacheLockObject {
	int code;
	
	public LeerCacheLockObject(int code) {
		this.code = code;
	}
	
	public Object getResource() {
		return new Integer(this.code);
	}
}
