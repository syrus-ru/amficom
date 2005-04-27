/*-
 * $Id: DatabaseObjectLoader.java,v 1.2 2005/04/27 15:30:21 arseniy Exp $
 * 
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 15:30:21 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class DatabaseObjectLoader {
	protected static Identifier userId;

	public static void init(Identifier userId1) {
		userId = userId1;
	}

	protected final Set retrieveFromDatabase(final StorableObjectDatabase database, final Set ids) throws RetrieveObjectException {
		try {
			return database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException idse) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids);
		}
	}

	protected final Set retrieveFromDatabaseButIdsByCondition(final StorableObjectDatabase database,
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

	protected final Identifier_Transferable[] createLoadIdsTransferable(final Set ids, final Set butObjects) {
		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = butObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		return Identifier.createTransferables(loadIds);
	}

	protected final Identifier_Transferable[] createLoadButIdsTransferable(final Set ids, final Set alsoButObjects) {
		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = alsoButObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}
		
		return Identifier.createTransferables(loadButIds);
	}

}
