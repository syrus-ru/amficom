/*
 * $Id: SchemeStorableObjectPool.java,v 1.6 2005/03/04 19:25:01 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StorableObject;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/03/04 19:25:01 $
 * @todo Move to corba subpackage.
 * @module scheme_v1
 */
public final class SchemeStorableObjectPool extends StorableObjectPool {
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

	public static void delete(final Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void delete(final List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void flush(final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		throw new UnsupportedOperationException();
	}

	public static IStorableObject getStorableObject(final Identifier objectId, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjects(final List objectIds, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjectsByConditionButIds(final List ids, final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

//	public static void init(final SchemeObjectLoader schemeObjectLoader) {
//		throw new UnsupportedOperationException();
//	}

//	public static void init(final SchemeObjectLoader schemeObjectLoader, final Class cacheClass, final int size) {
//		throw new UnsupportedOperationException();
//	}

//	public static void init(final SchemeObjectLoader schemeObjectLoader, final int size) {
//		throw new UnsupportedOperationException();
//	}

	public static StorableObject putStorableObject(final IStorableObject iStorableObject)
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
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#deleteStorableObject(com.syrus.AMFICOM.general.Identifier)
	 */
	protected void deleteStorableObject(Identifier id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#deleteStorableObjects(java.util.Collection)
	 */
	protected void deleteStorableObjects(Collection objects) throws IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#deleteStorableObjects(java.util.List)
	 */
	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objectId
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#loadStorableObject(com.syrus.AMFICOM.general.Identifier)
	 */
	protected com.syrus.AMFICOM.general.StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @param ids
	 * @return
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#loadStorableObjects(java.lang.Short, java.util.Collection)
	 */
	protected Collection loadStorableObjects(Short entityCode, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @param ids
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#loadStorableObjects(java.lang.Short, java.util.List)
	 */
	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @return
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#loadStorableObjectsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Collection)
	 */
	protected Collection loadStorableObjectsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#loadStorableObjectsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
	 */
	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @throws DatabaseException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#refreshStorableObjects(java.util.Set)
	 */
	protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.VtStorableObjectPool#saveStorableObjects(java.util.List, boolean)
	 */
	protected void saveStorableObjects(List storableObjects, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectPool#saveStorableObjects(short, java.util.Collection, boolean)
	 */
	protected void saveStorableObjects(short entityCode, Collection storableObjects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
