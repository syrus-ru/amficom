/*-
 * $Id: AbstractObjectLoader.java,v 1.5 2005/04/22 14:43:43 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/22 14:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class AbstractObjectLoader {

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

	/**
	 * NOTE: this method removes updated objects from set, thus modifying the set.
	 * If you are planning to use the set somewhere after this method call - 
	 * create a copy of the set to supply to this method.  
	 * @param storableObjects
	 * @param headers
	 */
	protected final void updateHeaders(final Set storableObjects, final StorableObject_Transferable[] headers) {
		for (int i = 0; i < headers.length; i++) {
			final Identifier id = new Identifier(headers[i].id);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (storableObject.getId().equals(id)) {
					storableObject.updateFromHeaderTransferable(headers[i]);
					it.remove();
					break;
				}
			}
		}
	}

}
