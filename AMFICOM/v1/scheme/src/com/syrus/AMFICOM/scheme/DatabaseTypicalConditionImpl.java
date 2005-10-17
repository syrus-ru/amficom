/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.12 2005/10/17 09:45:32 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;


import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.12 $, $Date: 2005/10/17 09:45:32 $
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
			case SCHEMEMONITORINGSOLUTION_CODE:
				if (this.condition.getKey().equals(SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE)) {
					return SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE;
				}
				break;
			case SCHEME_CODE:
				if (this.condition.getKey().equals(SchemeWrapper.COLUMN_KIND)) {
					return SchemeWrapper.COLUMN_KIND;
				}
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				break;
			case SCHEMEELEMENT_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				break;
			case SCHEMECABLELINK_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + this.condition.getKey() + "' are not supported.",
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
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

}
