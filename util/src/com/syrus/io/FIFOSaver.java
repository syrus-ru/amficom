/*-
 * $Id: FIFOSaver.java,v 1.7 2005/05/20 13:43:56 bob Exp $
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
import java.util.HashMap;
import java.util.Map;

import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/20 13:43:56 $
 * @author $Author: bob $
 * @module util
 */
public class FIFOSaver {
	
	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	public static final String EXTENSION = "Fifo.serialized";
	
	
	private static String pathNameOfSaveDir;
	private static File saveDir;

	private FIFOSaver() {
			// empty
	}

	public static void save(Fifo fifo, String objectEntityName) {
		File tempFile = null;
		try {
			init();
			File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + EXTENSION);
			tempFile = new File(saveFile.getPath() + ".swp");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("FifoSaver.save | Trying to save Fifo with " + objectEntityName + " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			out.writeObject(objectEntityName);
			out.writeObject(fifo.getObjects());
			out.writeObject(new Integer(fifo.getNumber()));
			out.close();
			tempFile.renameTo(saveFile);
		}
		catch (FileNotFoundException fnfe) {
			Log.errorMessage("FifoSaver.save | Error: " + fnfe.getMessage());        	
		}
		catch (IOException ioe) {
			Log.errorMessage("FifoSaver.save | Error: " + ioe.getMessage());
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
	private static Fifo load(final String objectEntityName) {
		try {
			Log.debugMessage("FifoSaver.load | Trying to load Fifo with " + objectEntityName , Log.DEBUGLEVEL10);
			File savedFile = new File(saveDir.getPath() + File.separator + objectEntityName + EXTENSION);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(savedFile));
			String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("FifoSaver.load | Wrong input file "+ savedFile.getAbsolutePath() + ". Loading failed");
				return null;
			}
			Object[] objects = (Object[]) in.readObject();
			Integer number = (Integer)in.readObject();
			Fifo fifo = new Fifo(objects.length);
			fifo.setObjects(objects);
			fifo.setNumber(number.intValue());
			return fifo;
		}
		catch (FileNotFoundException fnfe) {
			Log.debugMessage("FifoSaver.load | Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
			return null;
		}
		catch (ClassNotFoundException cnfe) {
			Log.errorMessage("FifoSaver.load | Error: " + cnfe.getMessage());
			return null;
		}
		catch (IOException ioe) {
			Log.errorMessage("FifoSaver.load | Error: " + ioe.getMessage());
			return null;
		}
	}
	
	public static Map load() {
		init();
		Map codeNameFifo = new HashMap();
		File[] fifoFiles = saveDir.listFiles(new FifoFileFilter());
		if(fifoFiles == null)
			return codeNameFifo;
		for (int i = 0; i < fifoFiles.length; i++) {
			File file = fifoFiles[i];
			String fileName = file.getName();
			int offset = fileName.indexOf(EXTENSION);
			String codeName = fileName.substring(0 , offset);
			Fifo fifo = load(codeName);
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
}
