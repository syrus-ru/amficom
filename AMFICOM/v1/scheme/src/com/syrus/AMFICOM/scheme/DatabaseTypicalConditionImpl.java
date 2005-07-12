/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.6 2005/07/12 08:40:55 bass Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/07/12 08:40:55 $
 * @module scheme_v1
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {
	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	/**
	 * @see AbstractDatabaseTypicalCondition#getColumnName()
	 */
	@Override
	protected String getColumnName() {
		throw new UnsupportedOperationException();
	}
}
