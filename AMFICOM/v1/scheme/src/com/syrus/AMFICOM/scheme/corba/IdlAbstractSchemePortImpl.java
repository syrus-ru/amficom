/*-
 * $Id: IdlAbstractSchemePortImpl.java,v 1.4 2005/07/24 17:40:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.scheme.AbstractSchemePort;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/24 17:40:35 $
 * @module scheme
 */
final class IdlAbstractSchemePortImpl extends IdlAbstractSchemePort {
	private static final long serialVersionUID = -54357695778007687L;

	IdlAbstractSchemePortImpl() {
		// empty
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractSchemePort getNative() {
		throw new UnsupportedOperationException();
	}
}
