/*-
 * $Id: StorableObjectContainerDelegate.java,v 1.1 2005/09/20 07:49:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static java.util.logging.Level.INFO;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/09/20 07:49:51 $
 * @module scheme
 */
final class StorableObjectContainerDelegate<T extends StorableObject> {
	private static final String KEY = "amficom.sof.cache.on.modification";

	private static final String DEFAULT_VALUE = "false";

	private boolean cacheBuilt = false;

	private StorableObjectCondition condition;

	private Set<T> storableObjects;

	static {
		Log.debugMessage(KEY + '=' + System.getProperty(KEY, DEFAULT_VALUE), INFO);
	}

	/**
	 * @param storableObject
	 * @param entityCode
	 */
	StorableObjectContainerDelegate(final StorableObject storableObject, final short entityCode) {
		this.condition = new LinkedIdsCondition(storableObject.getId(), entityCode);
	}

	/**
	 * @param storableObject
	 * @throws ApplicationException
	 */
	void addToCache(final T storableObject, final boolean usePool) throws ApplicationException {
		if (this.cacheBuilt) {
			this.storableObjects.add(storableObject);
		} else if (buildCacheOnModification()) {
			this.ensureCacheBuilt(usePool);
			this.storableObjects.add(storableObject);
		}
	}

	/**
	 * @param storableObject
	 * @param usePool
	 * @throws ApplicationException
	 */
	void removeFromCache(final T storableObject, final boolean usePool) throws ApplicationException {
		if (this.cacheBuilt) {
			this.storableObjects.remove(storableObject);
		} else if (buildCacheOnModification()) {
			this.ensureCacheBuilt(usePool);
			this.storableObjects.remove(storableObject);
		}
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<T> getStorableObjects(final boolean usePool) throws ApplicationException {
		this.ensureCacheBuilt(usePool);
		return this.storableObjects;
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private void ensureCacheBuilt(final boolean usePool) throws ApplicationException {
		if (!this.cacheBuilt || usePool) {
			if (this.storableObjects == null) {
				this.storableObjects = new HashSet<T>();
			} else {
				this.storableObjects.clear();
			}
			this.storableObjects.addAll(StorableObjectPool.<T>getStorableObjectsByCondition(this.condition, true));
			this.cacheBuilt = true;
		}
	}

	private static boolean buildCacheOnModification() {
		return Boolean.parseBoolean(System.getProperty(KEY, DEFAULT_VALUE));
	}
}
