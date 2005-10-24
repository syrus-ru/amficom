/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.13 2005/10/24 13:01:03 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/10/24 13:01:03 $
 * @module scheme
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {
	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected boolean isKeySupported(final String key) {
		switch (this.condition.getEntityCode().shortValue()) {
		case SCHEMEMONITORINGSOLUTION_CODE:
			return key == SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE;
		case SCHEME_CODE:
			return key == SchemeWrapper.COLUMN_KIND
					|| key == COLUMN_NAME;
		case SCHEMEELEMENT_CODE:
			return key == COLUMN_NAME;
		case SCHEMECABLELINK_CODE:
			return key == COLUMN_NAME;
		default:
			return false;
		}
	}
}
