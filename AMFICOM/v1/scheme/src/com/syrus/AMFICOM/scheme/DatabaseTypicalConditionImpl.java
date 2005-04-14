/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.4 2005/04/14 09:27:09 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/04/14 09:27:09 $
 * @module scheme_v1
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	/**
	 * @see AbstractDatabaseTypicalCondition#getColumnName()
	 */
	protected String getColumnName() {
		throw new UnsupportedOperationException();
	}
}
