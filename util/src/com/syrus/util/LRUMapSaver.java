/*-
 * $Id: LRUMapSaver.java,v 1.1 2005/12/02 15:14:06 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/02 15:14:06 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public abstract class LRUMapSaver<K, V> {
	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	
	private final String fileSuffix;
	private final File cacheDir;
	
	protected LRUMapSaver(final String fileSuffix) {
		this.fileSuffix = fileSuffix;

		String cachePath = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
		final String applicationName = Application.getApplicationName();
		if (!cachePath.endsWith(applicationName) && !cachePath.endsWith(applicationName + File.separator)) {
			cachePath += File.separator + applicationName;
		}
		this.cacheDir = new File(cachePath);
		this.cacheDir.mkdirs();
	}

	public void load(final String entityName, final LRUMap<K, V> lruMap) {
		Log.debugMessage("Loading LRUMap '" + entityName + "'", Log.DEBUGLEVEL10);
		final File lruMapFile = new File(this.cacheDir.getPath() + File.separator + entityName + this.fileSuffix);
		try {
			final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(lruMapFile));
			final String fileEntityName = (String) objectInputStream.readObject();
			if (fileEntityName == null || !fileEntityName.equals(entityName)) {
				Log.errorMessage("Wrong input file " + lruMapFile.getAbsolutePath()
						+ " -- desired entity name: '" + entityName
						+ "' + actual entity name: '" + fileEntityName + "'");
				return;
			}
			final Object readObject = objectInputStream.readObject();
			objectInputStream.close();
			this.populateLRUMap(lruMap, readObject);
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage(cnfe);
		} finally {
			lruMapFile.delete();
		}
	}

	protected abstract void populateLRUMap(final LRUMap<K, V> lruMap, final Object readObject);

	public void save(final LRUMap<K, V> lruMap, final String entityName, final boolean clearLRUMap) {
		if (lruMap.isEmpty()) {
			return;
		}

		final String path = this.cacheDir.getPath() + File.separator + entityName + this.fileSuffix;
		final File lruMapFile = new File(path);
		final File tmpLruMapFile = new File(path + ".swp");
		try {
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(tmpLruMapFile));
			Log.debugMessage("Saving LRUMap '" + entityName + "' to file " + lruMapFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			objectOutputStream.writeObject(entityName);
			objectOutputStream.writeObject(this.getObjectToWrite(lruMap));
			objectOutputStream.flush();
			objectOutputStream.close();

			lruMapFile.delete();
			tmpLruMapFile.renameTo(lruMapFile);

			if (clearLRUMap) {
				lruMap.clear();
			}
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} finally {
			tmpLruMapFile.delete();
		}
	}

	protected abstract Object getObjectToWrite(final LRUMap<K, V> lruMap);
}
