/*
 * $Id: XMLObjectLoader.java,v 1.16 2006/04/19 11:21:37 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */
package com.syrus.AMFICOM.general;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.16 $, $Date: 2006/04/19 11:21:37 $
 * @author $Author: bass $
 * @author Voffka
 * @module csbridge
 */
public final class XMLObjectLoader implements ObjectLoader {
	private static final String PACKAGE_NAME = "amficom";

	private StorableObjectXML soXML;

	public XMLObjectLoader(final File path) {
		final StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, PACKAGE_NAME);
		this.soXML = new StorableObjectXML(driver);
	}


	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<T> storableObjects = new HashSet<T>(ids.size());
		for (final Identifier id : ids) {
			final T storableObject = this.soXML.<T> retrieve(id);
			storableObjects.add(storableObject);
		}
		return storableObjects;
	}

	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		return this.soXML.retrieveButIdsByCondition(ids, condition);
	}

	public Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		//@todo Maybe not efficient.
		return Identifier.createIdentifiers(this.soXML.retrieveButIdsByCondition(ids, condition));
	}

	public Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>(ids.size());
		for (final Identifier id : ids) {
			versionsMap.put(id, StorableObjectVersion.ILLEGAL_VERSION);
		}
		return versionsMap;
	}

	public void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		for (final StorableObject storableObject : storableObjects) {
			this.soXML.updateObject(storableObject);
		}
		this.soXML.flush();
	}

	public void delete(final Set<? extends Identifiable> identifiables) {
		for (final Identifiable identifiable : identifiables) {
			this.soXML.delete(identifiable.getId());
		}
		this.soXML.flush();
	}

	final StorableObjectXML getStorableObjectXML() {
		return this.soXML;
	}
}
