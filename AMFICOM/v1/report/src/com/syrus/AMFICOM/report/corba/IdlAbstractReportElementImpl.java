/*-
 * $Id: IdlAbstractReportElementImpl.java,v 1.1 2005/10/04 11:02:35 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report.corba;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/10/04 11:02:35 $
 * @module report
 */

public class IdlAbstractReportElementImpl extends IdlAbstractReportElement {
	private static final long	serialVersionUID	= 2676184927651988187L;
	
	IdlAbstractReportElementImpl() {
		//empty
	}
	
	/** 
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}
}
