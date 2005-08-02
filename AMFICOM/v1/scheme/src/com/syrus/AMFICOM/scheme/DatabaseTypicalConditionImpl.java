/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.10 2005/08/02 08:40:42 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.10 $, $Date: 2005/08/02 08:40:42 $
 * @module scheme
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
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE:
				if (this.condition.getKey().equals(SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE)) {
					return SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE;
				}
				break;
			case ObjectEntities.SCHEME_CODE:
				if (this.condition.getKey().equals(SchemeWrapper.COLUMN_KIND)) {
					return SchemeWrapper.COLUMN_KIND;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + this.condition.getKey() + "' are not supported.",
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
	}
}
