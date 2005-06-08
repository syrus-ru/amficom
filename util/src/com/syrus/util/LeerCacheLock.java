package com.syrus.util;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public class LeerCacheLock implements CacheLock {

	public CacheLockObject lockWrite(String filename) {
		return new LeerCacheLockObject(0);
	}
	
	public CacheLockObject lockRead(String filename) {
		return new LeerCacheLockObject(0);
	}
	
	public boolean releaseWrite(CacheLockObject filelock) {
		return true;
	}
	
	public boolean releaseRead(CacheLockObject filelock) {
		return true;
	}
}
