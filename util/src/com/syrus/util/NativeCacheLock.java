package com.syrus.util;

public class NativeCacheLock implements CacheLock {
	private static final String LOCK_POSTFIX = ".sm.USER";
	private static boolean fileLockLoaded = false;

	public native int lockWrite0(String filename);
	public native int lockRead0(String filename);
	public native int releaseWrite0(String filename);
	public native int releaseRead0(String filename);

	static {
		try {
			System.loadLibrary("sema");
			fileLockLoaded = true;
		}
		catch (UnsatisfiedLinkError ex) {
			ex.printStackTrace();
			// do nothing
		}
		catch (Exception ex) {
			ex.printStackTrace();
			// do nothing
		}
	}

	public CacheLockObject lockWrite(String filename) {
		if(!fileLockLoaded)
			return null;

		int ret = lockWrite0(filename + LOCK_POSTFIX);
		System.out.println(ret);
		if(ret != 0)
			return null;

		return new LeerCacheLockObject(ret);
	}

	public CacheLockObject lockRead(String filename) {
		if(!fileLockLoaded)
			return null;

		int ret = lockRead0(filename + LOCK_POSTFIX);
		System.out.println(ret);
		if(ret != 0)
			return null;

		return new LeerCacheLockObject(ret);
	}
	
	public boolean releaseWrite(CacheLockObject filelock) {
		String filename = "";//(String )filelock;
		if(!fileLockLoaded)
			return false;
		int ret = releaseWrite0(filename + LOCK_POSTFIX);
		System.out.println(ret);
		if(ret != 0)
			return false;
		return true;
	}

	public boolean releaseRead(CacheLockObject filelock) {
		String filename = "";//(String )filelock;
		if(!fileLockLoaded)
			return false;

		int ret = releaseRead0(filename + LOCK_POSTFIX);
		System.out.println(ret);
		if(ret != 0)
			return false;

		return true;
	}
}
