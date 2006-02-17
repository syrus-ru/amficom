/*-
 * $Id: LRUMapSaver.java,v 1.6 2006/02/17 10:17:27 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
import java.util.Map;

/**
 * @version $Revision: 1.6 $, $Date: 2006/02/17 10:17:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public abstract class LRUMapSaver<K, V extends LRUMap.Retainable> {
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

		ObjectInputStream objectInputStream = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(lruMapFile);
			objectInputStream = new ObjectInputStream(fileInputStream);
			final String fileEntityName = (String) objectInputStream.readObject();
			if (fileEntityName == null || !fileEntityName.equals(entityName)) {
				Log.errorMessage("Wrong input file " + lruMapFile.getAbsolutePath()
						+ " -- desired entity name: '" + entityName
						+ "' + actual entity name: '" + fileEntityName + "'");
				return;
			}
			final Object readObject = objectInputStream.readObject();
			final Map<K, V> map = this.getMap(readObject);
			lruMap.putAll(map);
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage(cnfe);
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				} else if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException ioe) {
				//Nicho
			}
			if (lruMapFile.exists() && !lruMapFile.delete()) {
				throw new InternalError("Failed to delete LRUMap file " + lruMapFile.getName());
			}
		}
	}

	protected abstract Map<K, V> getMap(final Object readObject);

	public void save(final LRUMap<K, V> lruMap, final String entityName, final boolean clearLRUMap) {
		if (lruMap.isEmpty()) {
			return;
		}

		final String path = this.cacheDir.getPath() + File.separator + entityName + this.fileSuffix;
		final File lruMapFile = new File(path);
		final File tmpLruMapFile = new File(path + ".swp");


		if (lruMapFile.exists()) {//При нормальном поведении файл не должен существовать. См. bug321, comment #9. 
			Log.errorMessage("LRUMap file " + lruMapFile.getAbsolutePath() + " exists");
			lruMapFile.delete();//В случае ошибки при удалении файла, мы её увидим при переименовании.
		}

		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		boolean ok = false;
		try {
			fileOutputStream = new FileOutputStream(tmpLruMapFile);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			Log.debugMessage("Saving LRUMap '" + entityName + "' to file " + lruMapFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			objectOutputStream.writeObject(entityName);
			objectOutputStream.writeObject(this.getObjectToWrite(lruMap));
			objectOutputStream.flush();
			ok = true;
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				} else {
					fileOutputStream.close();
				}
			} catch (IOException ioe) {
				// Nicho
			}
			tmpLruMapFile.delete();
		}

		if (ok) {
			if (!tmpLruMapFile.renameTo(lruMapFile)) {
				Log.errorMessage("Failed to rename swp LRUMap file " + tmpLruMapFile.getAbsolutePath() + " to " + lruMapFile.getAbsolutePath());
				tmpLruMapFile.delete();
			}
		}

		if (clearLRUMap) {
			lruMap.clear();
		}
	}

	protected abstract Object getObjectToWrite(final LRUMap<K, V> lruMap);
}
