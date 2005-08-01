/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.16 2005/08/01 11:11:19 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.16 $, $Date: 2005/08/01 11:11:19 $
 * @author $Author: arseniy $
 * @module config
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}
	
	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.PORT_TYPE_CODE:
				if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_SORT)) {
					return PortTypeWrapper.COLUMN_SORT;
				}
				break;
			case ObjectEntities.EQUIPMENT_TYPE_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			case ObjectEntities.MONITOREDELEMENT_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				break;
			default:
				break;
		}
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' and key '" + this.condition.getKey() + "' are not supported.",
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
