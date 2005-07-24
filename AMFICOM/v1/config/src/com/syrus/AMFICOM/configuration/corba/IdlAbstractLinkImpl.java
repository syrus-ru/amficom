/*-
 * $Id: IdlAbstractLinkImpl.java,v 1.3 2005/07/24 18:08:38 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import com.syrus.AMFICOM.configuration.AbstractLink;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/24 18:08:38 $
 * @module config_v1
 */
final class IdlAbstractLinkImpl extends IdlAbstractLink {
	private static final long serialVersionUID = 6150235882869445595L;

	IdlAbstractLinkImpl() {
		// empty
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractLink getNative() {
		throw new UnsupportedOperationException();
	}
}
