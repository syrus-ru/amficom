/*-
 * $Id: IdlParameterImpl.java,v 1.1.2.1 2006/02/16 12:47:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/16 12:47:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlParameterImpl extends IdlParameter {

	IdlParameterImpl() {
		// empty
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException("This class can not have native pair");
	}

}
