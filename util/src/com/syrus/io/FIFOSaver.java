/*-
 * $Id: FIFOSaver.java,v 1.1 2005/05/16 10:06:03 max Exp $
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

import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/16 10:06:03 $
 * @author $Author: max $
 * @module util
 */
public class FIFOSaver {
	
	private static final String KEY_CACHE_PATH = "CachePath"; //$NON-NLS-1$

	private static final String DEFAULT_HOME = System.getProperty("user.home"); //$NON-NLS-1$
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator 
			+ "cache" + File.separator + Application.getApplicationName(); //$NON-NLS-1$

	private static String pathNameOfSaveDir;
	private static File saveDir;   

	private FIFOSaver() {
			// empty
	}

	public static void save(Fifo fifo, String objectEntityName) {
		File tempFile = null;
		try {
			init();
			File saveFile = new File(saveDir.getPath() + File.separator + objectEntityName + "Fifo.serialized"); //$NON-NLS-1$
			tempFile = new File(saveFile.getPath() + ".swp"); //$NON-NLS-1$
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("FifoSaver.save | Trying to save Fifo with " + objectEntityName + " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10); //$NON-NLS-1$ //$NON-NLS-2$
			out.writeObject(objectEntityName);
			out.writeObject(fifo.getObjects());
			out.writeObject(new Integer(fifo.getNumber()));
			out.close();
			tempFile.renameTo(saveFile);
		}
		catch (FileNotFoundException fnfe) {
			Log.errorMessage("FifoSaver.save | Error: " + fnfe.getMessage());        	 //$NON-NLS-1$
		}
		catch (IOException ioe) {
			Log.errorMessage("FifoSaver.save | Error: " + ioe.getMessage()); //$NON-NLS-1$
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
	public static Fifo load(final String objectEntityName) {
		try {
			init();
			Log.debugMessage("FifoSaver.load | Trying to load Fifo with " + objectEntityName , Log.DEBUGLEVEL10); //$NON-NLS-1$
			File savedFile = new File(saveDir.getPath() + File.separator + objectEntityName + "Fifo.serialized"); //$NON-NLS-1$
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(savedFile));
			String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("FifoSaver.load | Wrong input file "+ savedFile.getAbsolutePath() + ". Loading failed"); //$NON-NLS-1$ //$NON-NLS-2$
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
			Log.debugMessage("FifoSaver.load | Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10); //$NON-NLS-1$
			return null;
		}
		catch (ClassNotFoundException cnfe) {
			Log.errorMessage("FifoSaver.load | Error: " + cnfe.getMessage()); //$NON-NLS-1$
			return null;
		}
		catch (IOException ioe) {
			Log.errorMessage("FifoSaver.load | Error: " + ioe.getMessage()); //$NON-NLS-1$
			return null;
		}
	}
	
	private static void init() {
		if (pathNameOfSaveDir == null)
			pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
		if (saveDir == null || !saveDir.exists()) {
			saveDir = new File(pathNameOfSaveDir);
			saveDir.mkdir(); 
		}		
	}
}
