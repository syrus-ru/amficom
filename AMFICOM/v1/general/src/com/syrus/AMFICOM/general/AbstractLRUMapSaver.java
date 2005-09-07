/*-
* $Id: AbstractLRUMapSaver.java,v 1.1 2005/09/07 13:02:05 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Set;

import com.syrus.io.LRUSaver;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/09/07 13:02:05 $
 * @author $Author: bob $
 * @author Maxim Selivanov
 * @author Vladimir Dolzhenko
 * @module general
 */
public abstract class AbstractLRUMapSaver<V extends StorableObject> implements LRUSaver<Identifier, V> {

	private static final String KEY_CACHE_PATH = "CachePath";

	private static final String DEFAULT_HOME = System.getProperty("user.dir");
	private static final String DEFAULT_CACHE_PATH = DEFAULT_HOME + File.separator
			+ "cache" + File.separator + Application.getApplicationName();
	
	private final String extension;

	private String pathNameOfSaveDir;
	private File saveDir;

	protected AbstractLRUMapSaver(final String extension) {
		this.extension = extension;

		if (this.pathNameOfSaveDir == null) {
			this.pathNameOfSaveDir = ApplicationProperties.getString(KEY_CACHE_PATH, DEFAULT_CACHE_PATH);
			String applicationName = Application.getApplicationName();
			if (!this.pathNameOfSaveDir.endsWith(applicationName)
					&& !this.pathNameOfSaveDir.endsWith(applicationName + File.separator)) {
				this.pathNameOfSaveDir = this.pathNameOfSaveDir + File.separator + applicationName;
			}
		}
		if (this.saveDir == null || !this.saveDir.exists()) {
			this.saveDir = new File(this.pathNameOfSaveDir);
			this.saveDir.mkdirs();
		}	
	
	}

	public final void save(final LRUMap<Identifier, V> lruMap, final String objectEntityName, final boolean cleanLRUMap) {
		File tempFile = null;
		try {
			final File saveFile = new File(this.saveDir.getPath() + File.separator + objectEntityName + this.extension);
			tempFile = new File(saveFile.getPath() + ".swp");
			final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("AbstractLRUMapSaver.save | Trying to save LRUMap with " + objectEntityName + " to file " + saveFile.getAbsolutePath(), Log.DEBUGLEVEL10);
			final Object keys = this.saving(lruMap);
			if(keys == null) {
				Log.debugMessage("AbstractLRUMapSaver.save | LruMap has no elements. Nothing to save.", Log.DEBUGLEVEL10);
				return;
			}
			out.writeObject(objectEntityName);
			out.writeObject(keys);
			out.close();
			saveFile.delete();
			tempFile.renameTo(saveFile);
			if(cleanLRUMap) {
				lruMap.clear();
			}
		} catch (FileNotFoundException fnfe) {
			Log.errorMessage("AbstractLRUMapSaver.save | Error: " + fnfe.getMessage());        	
		} catch (IOException ioe) {
			Log.errorMessage("AbstractLRUMapSaver.save | Error: " + ioe.getMessage());
		} finally {
			if(tempFile != null)
				tempFile.delete();
		}
	}
	
	protected abstract Object saving(final LRUMap<Identifier, V> lruMap);
	
	/**
	 * @param objectEntityName
	 * @todo Consider returning an empty list instead of null. Check all
	 *       dependent code (within workspace).
	 */
	public final Set<V> load(final String objectEntityName) {
		try {
			Log.debugMessage("AbstractLRUMapSaver.load | Trying to load LRUMap with " + objectEntityName , Log.DEBUGLEVEL10);
			File saveFile = new File(this.saveDir.getPath() + File.separator + objectEntityName + this.extension);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile));
			String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("AbstractLRUMapSaver.load | Wrong input file "+ saveFile.getAbsolutePath() + ". Loading failed");
				return Collections.emptySet();
			}
			return loading(in);
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("AbstractLRUMapSaver.load | Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage("AbstractLRUMapSaver.load | Error: " + cnfe.getMessage());
		} catch (IOException ioe) {
			Log.errorMessage("AbstractLRUMapSaver.load | Error: " + ioe.getMessage());
		}
		return Collections.emptySet();
	}
	
	protected abstract Set<V> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException;
}
