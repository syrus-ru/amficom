/*-
* $Id: AbstractClientObjectLoader.java,v 1.1 2005/04/04 15:21:37 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;


/**
 * @version $Revision: 1.1 $, $Date: 2005/04/04 15:21:37 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module generalclient_v1
 */
public abstract class AbstractClientObjectLoader {

	protected StorableObject fromTransferable(StorableObject storableObject, IDLEntity transferable) throws CreateObjectException {
		if (storableObject != null)
			storableObject.fromTransferable(transferable);
		return storableObject;
	}
}

