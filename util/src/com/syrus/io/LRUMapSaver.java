/*
 * $Id: LRUMapSaver.java,v 1.10 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */
public class LRUMapSaver {
	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.home");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + "/cache";

	private static String pathNameOfSaveDir;
	private static File saveDir;

	private LRUMapSaver() {
			// empty
	}

	public static void save(LRUMap lruMap, String objectEntityName) {
		File tempFile = null;
		try {
			if (pathNameOfSaveDir == null)
				pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
			if (saveDir == null || !saveDir.exists()) {
				saveDir = new File(pathNameOfSaveDir);
				saveDir.mkdir();
			}

			File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
			tempFile = new File(saveFile.getPath() + ".swp");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("LRUMapSaver.save | Trying to save LRUMap with " + objectEntityName + " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			Set keys = new HashSet();
			for (Iterator it = lruMap.keyIterator(); it.hasNext();) {
				Object key = it.next();
				keys.add(key);
			}
			if(keys == null || keys.isEmpty()) {
				Log.debugMessage("LRUMapSaver.save | LruMap has no elements. Nothing to save.", Log.DEBUGLEVEL10);
				return;
			}
			out.writeObject(objectEntityName);
			out.writeObject(keys);
			out.close();
			tempFile.renameTo(saveFile);
		}
		catch (FileNotFoundException fnfe) {
			Log.errorMessage("LRUMapSaver.save | Error: " + fnfe.getMessage());        	
		}
		catch (IOException ioe) {
			Log.errorMessage("LRUMapSaver.save | Error: " + ioe.getMessage());
		}
		finally {
			if(tempFile != null)
				tempFile.delete();
		}
	}

	
	/**
	 * @param objectEntityName
	 * @todo Consider returning an empty list instead of null. Check all
	 *       dependent code (within workspace).
	 */
	public static Set load(final String objectEntityName) {
		try {
			if (pathNameOfSaveDir == null)
				pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
			if (saveDir == null || !saveDir.exists()) {
				saveDir = new File(pathNameOfSaveDir);
				saveDir.mkdir();
			}
			Log.debugMessage("LRUMapSaver.load | Trying to load LRUMap with " + objectEntityName , Log.DEBUGLEVEL10);
			File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "LRUMap.serialized");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile));
			String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("LRUMapSaver.load | Wrong input file "+ saveFile.getAbsolutePath() + ". Loading failed");
				return null;
			}
			Set keys = (HashSet) in.readObject();
			return keys;
		}
		catch (FileNotFoundException fnfe) {
			Log.debugMessage("LRUMapSaver.load | Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
			return null;
		}
		catch (ClassNotFoundException cnfe) {
			Log.errorMessage("LRUMapSaver.load | Error: " + cnfe.getMessage());
			return null;
		}
		catch (IOException ioe) {
			Log.errorMessage("LRUMapSaver.load | Error: " + ioe.getMessage());
			return null;
		}
	}
}
