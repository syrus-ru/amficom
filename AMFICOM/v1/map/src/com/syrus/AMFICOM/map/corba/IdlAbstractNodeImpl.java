/*-
 * $Id: IdlAbstractNodeImpl.java,v 1.2 2005/07/24 18:09:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map.corba;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.map.AbstractNode;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/24 18:09:10 $
 * @module map_v1
 */
final class IdlAbstractNodeImpl extends IdlAbstractNode {
	private static final long serialVersionUID = -7881152030081598655L;

	IdlAbstractNodeImpl() {
		// empty
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractNode getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}
}
