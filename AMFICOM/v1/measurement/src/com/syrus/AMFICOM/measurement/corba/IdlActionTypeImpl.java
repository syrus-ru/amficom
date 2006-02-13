/*-
 * $Id: IdlActionTypeImpl.java,v 1.1.2.2 2006/02/13 12:11:57 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/13 12:11:57 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlActionTypeImpl extends IdlActionType {

	IdlActionTypeImpl() {
		// empty
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException("This class can not have native pair");
	}

}
