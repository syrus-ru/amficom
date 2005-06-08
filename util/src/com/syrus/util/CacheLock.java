package com.syrus.util;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public interface CacheLock {
	CacheLockObject lockWrite(String filename);
	CacheLockObject lockRead(String filename);
	boolean releaseWrite(CacheLockObject filelock);
	boolean releaseRead(CacheLockObject filelock);
}
