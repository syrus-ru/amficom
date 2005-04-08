/*
 * $Id: SchemeStorableObjectPool.java,v 1.12 2005/04/08 09:26:11 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;

import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/08 09:26:11 $
 * @todo Move to corba subpackage.
 * @module scheme_v1
 */
public final class SchemeStorableObjectPool extends StorableObjectPool {
	private static SchemeStorableObjectPool instance;

	private SchemeStorableObjectPool() {
		super(ObjectGroupEntities.SCHEME_GROUP_CODE);
	}

	private SchemeStorableObjectPool(final Class cacheMapClass) {
		super(ObjectGroupEntities.SCHEME_GROUP_CODE, cacheMapClass);
	}

	public static void cleanChangedStorableObject(final Short entityCode) {
		throw new UnsupportedOperationException();
	}

	public static void cleanChangedStorableObjects() {
		throw new UnsupportedOperationException();
	}

	public static void delete(final Identifier id) {
		instance.deleteImpl(id);
	}

	/**
	 * @param objects a <code>Set</code> of {@link Identifier}s and/or
	 *        {@link Identifiable}s.
	 * @throws IllegalDataException
	 */
	public static void delete(final Set objects) throws IllegalDataException {
		instance.deleteImpl(objects);
	}

	public static void flush(final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		throw new UnsupportedOperationException();
	}

	public static StorableObject getStorableObject(final Identifier objectId, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static Set getStorableObjects(final Set objectIds, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static Set getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public static Set getStorableObjectsByConditionButIds(final Set ids, final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public static void init(final SchemeObjectLoader schemeObjectLoader) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeObjectLoader
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader, final Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new SchemeStorableObjectPool(clazz);
		} catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Cache class '" + cacheClass.getName() //$NON-NLS-1$
					+ "' cannot be found, using default"); //$NON-NLS-1$
			instance = new SchemeStorableObjectPool();
		}
		init(schemeObjectLoader, size);
	}

	public static void init(final SchemeObjectLoader schemeObjectLoader, final int size) {
		if (instance == null)
			instance = new SchemeStorableObjectPool();
		throw new UnsupportedOperationException();
	}

	public static StorableObject putStorableObject(final StorableObject iStorableObject)
			throws IllegalObjectEntityException {
		throw new UnsupportedOperationException();
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void serializePool() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 */
	protected void deleteStorableObject(Identifier id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @throws IllegalDataException
	 * @see StorableObjectPool#deleteStorableObjects(Set)
	 */
	protected void deleteStorableObjects(final Set objects) throws IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objectId
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#loadStorableObjects(Short, Set)
	 */
	protected Set loadStorableObjects(Short entityCode, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @throws DatabaseException
	 */
	protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#saveStorableObjects(short, Set, boolean)
	 */
	protected void saveStorableObjects(final short entityCode, final Set storableObjects, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
