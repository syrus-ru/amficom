package com.syrus.util;

public interface CacheLock {
	public CacheLockObject lockWrite(String filename);
	public CacheLockObject lockRead(String filename);
	public boolean releaseWrite(CacheLockObject filelock);
	public boolean releaseRead(CacheLockObject filelock);
}
