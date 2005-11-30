/*-
 * $Id: MultiServantCORBAObjectLoader.java,v 1.8 2005/11/30 15:42:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.8 $, $Date: 2005/11/30 15:42:34 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class MultiServantCORBAObjectLoader implements ObjectLoader {
	private Map<Short, CORBAObjectLoader> corbaObjectLoadersMap;

	public MultiServantCORBAObjectLoader() {
		this.corbaObjectLoadersMap = new HashMap<Short, CORBAObjectLoader>();
	}

	public void addCORBAObjectLoader(final short groupCode, final ServerConnectionManager serverConnectionManager) {
		this.addCORBAObjectLoader(new Short(groupCode), serverConnectionManager);
	}

	public void addCORBAObjectLoader(final short groupCode,
			final ServerConnectionManager serverConnectionManager,
			final CORBAActionProcessor corbaActionProcessor) {
		this.addCORBAObjectLoader(new Short(groupCode), serverConnectionManager, corbaActionProcessor);
	}

	public void addCORBAObjectLoader(final Short groupCode, final ServerConnectionManager serverConnectionManager) {
		final CORBAObjectLoader corbaObjectLoader = new CORBAObjectLoader(serverConnectionManager);
		this.corbaObjectLoadersMap.put(groupCode, corbaObjectLoader);
	}

	public void addCORBAObjectLoader(final Short groupCode,
			final ServerConnectionManager serverConnectionManager,
			final CORBAActionProcessor corbaActionProcessor) {
		final CORBAObjectLoader corbaObjectLoader = new CORBAObjectLoader(serverConnectionManager, corbaActionProcessor);
		this.corbaObjectLoadersMap.put(groupCode, corbaObjectLoader);
	}

	private CORBAObjectLoader getCORBAObjectLoader(final Set<? extends Identifiable> identifiables) throws IllegalDataException {
		final short groupCode = StorableObject.getGroupCodeOfIdentifiables(identifiables);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE;
		final CORBAObjectLoader corbaObjectLoader = this.corbaObjectLoadersMap.get(new Short(groupCode));
		if (corbaObjectLoader == null) {
			throw new IllegalDataException("CORBA loader for group '" + ObjectGroupEntities.codeToString(groupCode) + "'/" + groupCode
					+ " is not registered");
		}
		return corbaObjectLoader;
	}

	private CORBAObjectLoader getCORBAObjectLoader(final Short entityCode) throws IllegalDataException {
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE;
		final CORBAObjectLoader corbaObjectLoader = this.corbaObjectLoadersMap.get(new Short(groupCode));
		if (corbaObjectLoader == null) {
			throw new IllegalDataException("CORBA loader for group '" + ObjectGroupEntities.codeToString(groupCode) + "'/" + groupCode
					+ " is not registered");
		}
		return corbaObjectLoader;
	}


	public final <T extends StorableObject<T>> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}
		return this.getCORBAObjectLoader(ids).loadStorableObjects(ids);
	}

	public final <T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		return this.getCORBAObjectLoader(condition.getEntityCode()).loadStorableObjectsButIdsByCondition(ids, condition);
	}

	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}
		return this.getCORBAObjectLoader(ids).getRemoteVersions(ids);
	}

	public final void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}
		this.getCORBAObjectLoader(storableObjects).saveStorableObjects(storableObjects);
	}

	public final void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}
		this.getCORBAObjectLoader(identifiables).delete(identifiables);
	}
}
