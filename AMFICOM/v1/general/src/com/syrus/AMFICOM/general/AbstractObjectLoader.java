package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/*
 * $Id: AbstractObjectLoader.java,v 1.1 2005/04/05 09:00:59 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/05 09:00:59 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class AbstractObjectLoader {

	protected Identifier_Transferable[] createLoadIdsTransferable(java.util.Set ids, java.util.Set objects) {
		Identifier id;
		java.util.Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = Identifier.createTransferables(loadIds);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		return loadIdsT;
	}

}
