/*-
 * $Id: IdlAbstractLinkTypeImpl.java,v 1.4 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import com.syrus.AMFICOM.configuration.AbstractLinkType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/27 15:59:22 $
 * @module config
 */
final class IdlAbstractLinkTypeImpl extends IdlAbstractLinkType {
	private static final long serialVersionUID = -2003590318900850319L;

	IdlAbstractLinkTypeImpl() {
		// empty
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractLinkType getNative() {
		throw new UnsupportedOperationException();
	}
}
