package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/*
 * $Id: AbstractObjectLoader.java,v 1.2 2005/04/05 10:29:54 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/05 10:29:54 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class AbstractObjectLoader {

	protected Set retrieveFromDatabase(final StorableObjectDatabase database, final Set ids) throws RetrieveObjectException {
		try {
			return database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException idse) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids);
		}
	}

	protected Set retrieveFromDatabaseButIdsByCondition(final StorableObjectDatabase database,
			final Set ids,
			final StorableObjectCondition condition)
			throws RetrieveObjectException {
		try {
			return database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids + ", condition: " + condition);
		}
	}

	protected Identifier_Transferable[] createLoadIdsTransferable(final Set ids, final Set butObjects) throws IllegalDataException {
		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = butObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		return Identifier.createTransferables(loadIds);
	}

	protected Identifier_Transferable[] createLoadButIdsTransferable(final Set ids, final Set alsoButObjects)
			throws IllegalDataException {
		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = alsoButObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}
		
		return Identifier.createTransferables(loadButIds);
	}
}
