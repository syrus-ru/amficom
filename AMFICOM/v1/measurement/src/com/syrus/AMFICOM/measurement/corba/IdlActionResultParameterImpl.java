/*-
 * $Id: IdlActionResultParameterImpl.java,v 1.1.2.2 2006/02/14 01:26:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/14 01:26:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlActionResultParameterImpl extends IdlActionResultParameter {
	private static final long serialVersionUID = -8983781446597640074L;

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException("This class can not have native pair");
	}

}
