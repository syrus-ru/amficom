package com.syrus.util;

public interface CacheLock {
	CacheLockObject lockWrite(String filename);
	CacheLockObject lockRead(String filename);
	boolean releaseWrite(CacheLockObject filelock);
	boolean releaseRead(CacheLockObject filelock);
}
