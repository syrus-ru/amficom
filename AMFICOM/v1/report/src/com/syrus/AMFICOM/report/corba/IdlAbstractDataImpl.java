/*-
 * $Id: IdlAbstractDataImpl.java,v 1.1 2005/11/16 18:38:22 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/11/16 18:38:22 $
 * @module report
 */

public class IdlAbstractDataImpl extends IdlAbstractData {
	
	private static final long	serialVersionUID	= 5948641889101096254L;

	IdlAbstractDataImpl() {
		//empty
	}
	
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}
}
