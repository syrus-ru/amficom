/*
 * $Id: StringFieldCondition.java,v 1.1 2004/12/08 16:53:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/08 16:53:22 $
 * @module resource_v1
 */
final class StringFieldCondition extends com.syrus.AMFICOM.general.StringFieldCondition {
	private static final String STRING_FIELD_CONDITION_INIT = "StringFieldCondition.<init>() | "; //$NON-NLS-1$

	private StringFieldCondition(final String string, final Short entityCode, final StringFieldSort sort) {
		super();
		this.string = string;
		this.entityCode = entityCode;
		this.sort = sort.value();
		Log.debugMessage(STRING_FIELD_CONDITION_INIT
			+ "Created new instance of ResourceStringFieldCondition", //$NON-NLS-1$
			Log.FINEST);
	}

	/**
	 * @param object
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isConditionTrue(Object)
	 */
	public boolean isConditionTrue(final Object object) throws ApplicationException {
		return false;
	}
}
