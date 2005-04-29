/*
 * $Id: ObjectLoader.java,v 1.1 2005/04/29 15:57:20 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/29 15:57:20 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class ObjectLoader {

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
