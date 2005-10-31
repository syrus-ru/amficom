/*-
 * $Id: FIFOSaver.java,v 1.16 2005/10/31 12:29:58 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
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
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/10/31 12:29:58 $
 * @author $Author: bass $
 * @module util
 */
public final class FIFOSaver {

	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	public static final String EXTENSION = "Fifo.serialized";

	private static final String FLAG_FILE_NAME = "serialized";

	private static String pathNameOfSaveDir;
	private static File saveDir;

	private FIFOSaver() {
			// empty
	}

	public static void save(Fifo fifo, String objectEntityName) {
		File tempFile = null;
		try {
			init();
			final File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + EXTENSION);
			tempFile = new File(saveFile.getPath() + ".swp");
			final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("Trying to save Fifo with " + objectEntityName + " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			out.writeObject(objectEntityName);
			out.writeObject(fifo.getObjects());
			out.writeObject(new Integer(fifo.getNumber()));
			out.close();
			saveFile.delete();
			tempFile.renameTo(saveFile);
		} catch (FileNotFoundException fnfe) {
			Log.errorMessage("Error: " + fnfe.getMessage());        	
		} catch (IOException ioe) {
			Log.errorMessage("Error: " + ioe.getMessage());
		} finally {
			if(tempFile != null) {
				tempFile.delete();
			}
		}
	}

	/**
	 * @param objectEntityName
	 * @todo Consider returning an empty list instead of null. Check all
	 *       dependent code (within workspace).
	 */
	private static Fifo load(final String objectEntityName) {
		try {
			Log.debugMessage("Trying to load Fifo with " + objectEntityName , Log.DEBUGLEVEL10);
			final File savedFile = new File(saveDir.getPath() + File.separator + objectEntityName + EXTENSION);
			final ObjectInputStream in = new ObjectInputStream(new FileInputStream(savedFile));
			final String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("Wrong input file "+ savedFile.getAbsolutePath() + ". Loading failed");
				return null;
			}
			final Object[] objects = (Object[]) in.readObject();
			final Integer number = (Integer)in.readObject();
			final Fifo fifo = new Fifo(objects.length);
			fifo.setObjects(objects);
			fifo.setNumber(number.intValue());
			return fifo;
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
			return null;
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage("Error: " + cnfe.getMessage());
			return null;
		} catch (IOException ioe) {
			Log.errorMessage("Error: " + ioe.getMessage());
			return null;
		}
	}

	public static Map<String, Fifo> load() {
		init();

		if (!FIFOSaver.ensureFlagFile()) {
			return Collections.emptyMap();
		}

		final Map<String, Fifo> codeNameFifo = new HashMap<String, Fifo>();
		final File[] fifoFiles = saveDir.listFiles(new FifoFileFilter());
		if(fifoFiles == null) {
			return codeNameFifo;
		}
		for (int i = 0; i < fifoFiles.length; i++) {
			final File file = fifoFiles[i];
			final String fileName = file.getName();
			final int offset = fileName.indexOf(EXTENSION);
			final String codeName = fileName.substring(0 , offset);
			final Fifo fifo = load(codeName);
			codeNameFifo.put(codeName, fifo);
		}
		return codeNameFifo;
	}
	
	private static void init() {
		if (pathNameOfSaveDir == null) {
			pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
			String applicationName = Application.getApplicationName();
			if (!pathNameOfSaveDir.endsWith(applicationName) && !pathNameOfSaveDir.endsWith(applicationName + File.separator)) {
				pathNameOfSaveDir = pathNameOfSaveDir + File.separator + applicationName;
			}
		}
		if (saveDir == null || !saveDir.exists()) {
			saveDir = new File(pathNameOfSaveDir);
			saveDir.mkdirs();
		}		
	}

	private static boolean ensureFlagFile() {
		final File flagFile = new File(saveDir.getPath() + File.separator + FLAG_FILE_NAME);
		return flagFile.exists();
	}

	public static void touchFlagFile() {
		try {
			final OutputStream os = new FileOutputStream(saveDir.getPath() + File.separator + FLAG_FILE_NAME);
			os.write(1);
			os.flush();
			os.close();
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
		}
	}
}
