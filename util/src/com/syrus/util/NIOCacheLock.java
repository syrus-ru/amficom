package com.syrus.util;

import java.io.*;
import java.nio.channels.*;
import java.util.*;

public class NIOCacheLock implements CacheLock {
	Hashtable locks = new Hashtable();
	Hashtable locks2 = new Hashtable();

	public CacheLockObject lockWrite(String filename) {
		synchronized(this) {
			FileLock fl = null;
			try {
				File file = new File(filename);
				file.mkdirs();
				if (file.exists())
					file.delete();
				file.createNewFile();

				FileOutputStream fos = new FileOutputStream(filename);
				FileChannel ch = fos.getChannel();

//				FileLock fl = ch.tryLock();
				fl = ch.lock();
				if(fl != null) {
					Entry e = new Entry(Entry.WRITE_LOCK, fl, filename, fos);
					put(e);
					return e;
				}
			} 
			catch (Exception ex) {
				try
				{
					fl.release();
				}
				catch (Exception ex2)
				{
					;
				}
//				ex.printStackTrace();
			} 
			return null;
		}
	}
	
	public CacheLockObject lockRead(String filename) {
		synchronized(this) {
			FileLock fl = null;
			try {
				FileInputStream fis = new FileInputStream(filename);
				FileChannel ch = fis.getChannel();
	
//				FileLock fl = ch.tryLock(0, Long.MAX_VALUE, true);
				fl = ch.lock(0L, Long.MAX_VALUE, true);
				if(fl != null) {
					Entry e = new Entry(Entry.READ_LOCK, fl, filename, fis);
					put(e);
					return e;
				}
			}
			catch (Exception ex) {
				try
				{
					fl.release();
				}
				catch (Exception ex2)
				{
					;
				}
//				ex.printStackTrace();
			} 
			return null;
		}
	}
	
	public boolean releaseWrite(CacheLockObject filelock)	{
		synchronized(this) {
			try	{
				Entry e = (Entry )filelock;
//				Entry e = get((FileLock )filelock, Entry.WRITE_LOCK);
//				Entry e = get(filename, Entry.WRITE_LOCK);
				if(e.lock != null) {
//					e.lock.channel().force(true);
					e.lock.release();
//					e.lock.channel().close();
				}
				OutputStream os = (OutputStream )e.stream;
				os.flush();
				os.close();
				remove(e);
			} 
			catch (Exception ex) {
//				ex.printStackTrace();
				;
			} 
			return true;
		}
	}
	
	public boolean releaseRead(CacheLockObject filelock) {
		synchronized(this) {
			try {
				Entry e = (Entry )filelock;
//				Entry e = get((FileLock )filelock, Entry.READ_LOCK);
//				Entry e = get(filename, Entry.READ_LOCK);
				if(e.lock != null) {
					e.lock.release();
//					e.lock.channel().close();
				}
				InputStream is = (InputStream )e.stream;
				is.close();
				remove(e);
			} 
			catch (Exception ex) {
//				ex.printStackTrace();
				;
			} 
			return true;
		}
	}

/////////////////////////////////////////////////////////////////////////////

	private void put(Entry e) {
		String key = e.filename;
		Vector vec = (Vector )this.locks.get(key);
		if(vec == null) {
			vec = new Vector();
			this.locks.put(key, vec);
		}
		vec.add(e);
		this.locks2.put(e.lock, e);
	}
	
//	private Entry get(FileLock lockkey, int type) {
//		return (Entry )this.locks2.get(lockkey);
//	}
//	
//	private Entry get(String key, int type) {
//		Vector vec = (Vector )this.locks.get(key);
//		if(vec == null)
//			return null;
//
//		for(int i = 0; i < vec.size(); i++) {
//			Entry e = (Entry )vec.get(i);
//			if(e.type == type)
//				return e;
//		}
//		return null;
//	}
	
	private void remove(Entry e) {
		String key = e.filename;
		Vector vec = (Vector )this.locks.get(key);
		if(vec == null)
			return;

		vec.remove(e);
		this.locks2.remove(e.lock);
	}
}
