/*-
 * $Id: IdlActionImpl.java,v 1.1.2.3 2006/02/14 01:26:42 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/14 01:26:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlActionImpl extends IdlAction {
	private static final long serialVersionUID = 5397980779297497694L;

	IdlActionImpl() {
		// empty
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException("This class can not have native pair");
	}

}