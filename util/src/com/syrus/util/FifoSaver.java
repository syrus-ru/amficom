/*-
 * $Id: FifoSaver.java,v 1.3.2.1 2006/02/21 12:27:08 arseniy Exp $
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
import java.util.HashMap;
import java.util.Map;


/**
 * @version $Revision: 1.3.2.1 $, $Date: 2006/02/21 12:27:08 $
 * @author $Author: arseniy $
 * @module util
 */
public final class FifoSaver {

	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	public static final String FILE_SUFFIX = ".fifo";

	private static File cacheDir;

	static {
		init();
	}

	private FifoSaver() {
		// empty
		assert false;
	}

	private static void init() {
		String cachePath = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
		final String applicationName = Application.getApplicationName();
		if (!cachePath.endsWith(applicationName) && !cachePath.endsWith(applicationName + File.separator)) {
			cachePath += File.separator + applicationName;
		}
		if (cacheDir == null || !cacheDir.exists()) {
			cacheDir = new File(cachePath);
			cacheDir.mkdirs();
		}		
	}

	public static Map<String, Fifo> load(final int fifoCapacity) {
		final Map<String, Fifo> fifoMap = new HashMap<String, Fifo>();
		final File[] fifoFiles = cacheDir.listFiles(new FifoFileFilter());
		if(fifoFiles == null) {
			return fifoMap;
		}

		for (int i = 0; i < fifoFiles.length; i++) {
			loadFifo(fifoMap, fifoFiles[i], fifoCapacity);
		}
		return fifoMap;
	}

	private static void loadFifo(final Map<String, Fifo> fifoMap, final File fifoFile, final int fifoCapacity) {
		final String fifoFileName = fifoFile.getName();
		final int offset = fifoFileName.indexOf(FILE_SUFFIX);
		final String entityName = fifoFileName.substring(0, offset);

		Log.debugMessage("Loading Fifo '" + entityName + "'", Log.DEBUGLEVEL10);

		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			fileInputStream = new FileInputStream(fifoFile);
			objectInputStream = new ObjectInputStream(fileInputStream);
			final String fileEntityName = (String) objectInputStream.readObject();
			if (fileEntityName == null || !fileEntityName.equals(entityName)) {
				Log.errorMessage("Wrong input file " + fifoFile.getAbsolutePath()
						+ " -- desired entity name: '" + entityName
						+ "' + actual entity name: '" + fileEntityName + "'");
				return;
			}
			final Object[] objects = (Object[]) objectInputStream.readObject();
			final Fifo fifo = new Fifo(fifoCapacity);
			fifo.populate(objects);
			fifoMap.put(entityName, fifo);
			return;
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
			return;
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
			return;
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage(cnfe);
			return;
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				} else if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException ioe) {
				// Hm... something strange. Should I handle Java problems?
			}
			if (fifoFile.exists() && !fifoFile.delete()) {
				throw new InternalError("Failed to delete fifo file " + fifoFileName);
			}
		}
	}

	public static void save(final Fifo fifo, final String entityName) {
		final String path = cacheDir.getPath() + File.separator + entityName + FILE_SUFFIX;
		final File fifoFile = new File(path);
		final File tmpFifoFile = new File(path + ".swp");

		if (fifoFile.exists()) {//При нормальном поведении файл не должен существовать. См. bug321, comment #9. 
			Log.errorMessage("FIFO file " + fifoFile.getAbsolutePath() + " exists");
			fifoFile.delete();//В случае ошибки при удалении файла, мы её увидим при переименовании.
		}

		ObjectOutputStream objectOutputStream = null;
		FileOutputStream fileOutputStream = null;
		boolean ok = false;
		try {
			fileOutputStream = new FileOutputStream(tmpFifoFile);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			Log.debugMessage("Saving Fifo '" + entityName + "' to file " + fifoFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			objectOutputStream.writeObject(entityName);
			objectOutputStream.writeObject(fifo.getObjects());
			objectOutputStream.flush();
			ok = true;
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				} else if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException ioe) {
				// Nicho
			}
		}

		if (ok) {
			if (!tmpFifoFile.renameTo(fifoFile)) {
				Log.errorMessage("Failed to rename swp FIFO file " + tmpFifoFile.getAbsolutePath() + " to " + fifoFile.getAbsolutePath());
				tmpFifoFile.delete();
			}
		}
	}

}
