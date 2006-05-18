/*-
 * $Id: IdlAbstractSchemeLinkImpl.java,v 1.3 2005/07/24 17:40:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.scheme.AbstractSchemeLink;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/24 17:40:35 $
 * @module scheme
 */
final class IdlAbstractSchemeLinkImpl extends IdlAbstractSchemeLink {
	private static final long serialVersionUID = 2435227252708468644L;

	IdlAbstractSchemeLinkImpl() {
		// empty
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractSchemeLink getNative() {
		throw new UnsupportedOperationException();
	}
}
