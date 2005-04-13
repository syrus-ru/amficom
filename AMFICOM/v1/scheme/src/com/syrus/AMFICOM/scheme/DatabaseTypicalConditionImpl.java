/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.3 2005/04/13 19:34:10 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/04/13 19:34:10 $
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
		throw new UnsupportedOperationException("Method not implemented");
	}
}
