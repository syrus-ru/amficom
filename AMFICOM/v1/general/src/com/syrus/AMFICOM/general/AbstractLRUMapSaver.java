/*-
* $Id: AbstractLRUMapSaver.java,v 1.6 2005/10/22 14:08:26 arseniy Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/10/22 14:08:26 $
 * @author $Author: arseniy $
 * @author Maxim Selivanov
 * @author Vladimir Dolzhenko
 * @module general
 */
public abstract class AbstractLRUMapSaver implements LRUSaver<Identifier, StorableObject> {

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
			final String applicationName = Application.getApplicationName();
			if (!this.pathNameOfSaveDir.endsWith(applicationName) && !this.pathNameOfSaveDir.endsWith(applicationName + File.separator)) {
				this.pathNameOfSaveDir = this.pathNameOfSaveDir + File.separator + applicationName;
			}
		}
		if (this.saveDir == null || !this.saveDir.exists()) {
			this.saveDir = new File(this.pathNameOfSaveDir);
			this.saveDir.mkdirs();
		}	

	}

	public final void save(final LRUMap<Identifier, StorableObject> lruMap, final String objectEntityName, final boolean cleanLRUMap) {
		File tempFile = null;
		try {
			final File saveFile = new File(this.saveDir.getPath() + File.separator + objectEntityName + this.extension);
			tempFile = new File(saveFile.getPath() + ".swp");
			final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			Log.debugMessage("Trying to save LRUMap with " + objectEntityName + " to file " + saveFile.getAbsolutePath(),
					Log.DEBUGLEVEL10);
			final Object keys = this.saving(lruMap);
			out.writeObject(objectEntityName);
			out.writeObject(keys);
			out.close();
			saveFile.delete();
			tempFile.renameTo(saveFile);
			if (cleanLRUMap) {
				lruMap.clear();
			}
		} catch (FileNotFoundException fnfe) {
			Log.errorMessage("Error: " + fnfe.getMessage());
		} catch (IOException ioe) {
			Log.errorMessage("Error: " + ioe.getMessage());
		} finally {
			if (tempFile != null) {
				tempFile.delete();
			}
		}
	}

	protected abstract Object saving(final LRUMap<Identifier, StorableObject> lruMap);

	/**
	 * @param objectEntityName
	 * @todo Consider returning an empty list instead of null. Check all
	 *       dependent code (within workspace).
	 */
	public final Set<StorableObject> load(final String objectEntityName) {
		try {
			Log.debugMessage("Trying to load LRUMap with " + objectEntityName, Log.DEBUGLEVEL10);
			final File saveFile = new File(this.saveDir.getPath() + File.separator + objectEntityName + this.extension);
			final ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile));
			final String keyObjectEntityName = (String) in.readObject();
			if (keyObjectEntityName == null || !keyObjectEntityName.equals(objectEntityName)) {
				Log.errorMessage("Wrong input file " + saveFile.getAbsolutePath() + ". Loading failed");
				return Collections.emptySet();
			}
			return this.loading(in);
		} catch (FileNotFoundException fnfe) {
			Log.debugMessage("Warning: " + fnfe.getMessage(), Log.DEBUGLEVEL10);
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage("Error: " + cnfe.getMessage());
		} catch (IOException ioe) {
			Log.errorMessage("Error: " + ioe.getMessage());
		}
		return Collections.emptySet();
	}

	protected abstract Set<StorableObject> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException;

}
