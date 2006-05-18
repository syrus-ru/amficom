/*-
 * $Id: IdlAbstractNodeImpl.java,v 1.3 2005/08/08 11:35:11 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:35:11 $
 * @module map
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
