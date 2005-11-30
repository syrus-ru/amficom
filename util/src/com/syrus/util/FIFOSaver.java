/*-
 * $Id: FIFOSaver.java,v 1.1 2005/11/30 15:50:06 arseniy Exp $
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
import java.util.HashMap;
import java.util.Map;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/30 15:50:06 $
 * @author $Author: arseniy $
 * @module util
 */
public final class FIFOSaver {

	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	public static final String EXTENSION = "Fifo.serialized";

	private static String pathNameOfSaveDir;
	private static File saveDir;

	static {
		init();
	}

	private FIFOSaver() {
			// empty
	}

	private static void init() {
		if (pathNameOfSaveDir == null) {
			pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
			final String applicationName = Application.getApplicationName();
			if (!pathNameOfSaveDir.endsWith(applicationName) && !pathNameOfSaveDir.endsWith(applicationName + File.separator)) {
				pathNameOfSaveDir = pathNameOfSaveDir + File.separator + applicationName;
			}
		}
		if (saveDir == null || !saveDir.exists()) {
			saveDir = new File(pathNameOfSaveDir);
			saveDir.mkdirs();
		}		
	}

	public static Map<String, Fifo> load(final int fifoCapacity) {
		final Map<String, Fifo> fifoMap = new HashMap<String, Fifo>();
		final File[] fifoFiles = saveDir.listFiles(new FifoFileFilter());
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
		final int offset = fifoFileName.indexOf(EXTENSION);
		final String entityName = fifoFileName.substring(0, offset);

		Log.debugMessage("Loading Fifo '" + entityName + "'", Log.DEBUGLEVEL10);

		try {
			final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fifoFile));
			final String fileEntityName = (String) objectInputStream.readObject();
			if (fileEntityName == null || !fileEntityName.equals(entityName)) {
				Log.errorMessage("Wrong input file " + fifoFile.getAbsolutePath()
						+ " -- desired entity name: '" + entityName
						+ "' + actual entity name: '" + fileEntityName + "'");
				return;
			}
			final Object[] objects = (Object[]) objectInputStream.readObject();
			final Fifo fifo = new Fifo(fifoCapacity);
			fifo.setObjects(objects);
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
			fifoFile.delete();
		}
	}

	public static void save(final Fifo fifo, final String entityName) {
		final String path = saveDir.getPath() + File.separator + entityName + EXTENSION;
		final File fifoFile = new File(path);
		final File tmpFifoFile = new File(path + ".swp");
		try {
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(tmpFifoFile));
			Log.debugMessage("Saving Fifo '" + entityName + "' to file " + fifoFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			objectOutputStream.writeObject(entityName);
			objectOutputStream.writeObject(fifo.getObjects());
			objectOutputStream.flush();
			objectOutputStream.close();
			fifoFile.delete();
			tmpFifoFile.renameTo(fifoFile);
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		} finally {
			tmpFifoFile.delete();
		}
	}

}
