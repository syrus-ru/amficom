/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/04/04 13:17:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/04 13:17:21 $
 * @module scheme_v1
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	/**
	 * @throws IllegalDataException
	 * @see AbstractDatabaseTypicalCondition#getColumnName()
	 */
	protected String getColumnName() throws IllegalDataException {
		throw new UnsupportedOperationException();
	}
}
