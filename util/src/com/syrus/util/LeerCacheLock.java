package com.syrus.util;

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
