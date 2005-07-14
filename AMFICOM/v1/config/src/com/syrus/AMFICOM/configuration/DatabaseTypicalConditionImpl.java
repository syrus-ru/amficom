/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.12 2005/07/14 19:07:37 arseniy Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/07/14 19:07:37 $
 * @author $Author: arseniy $
 * @module config_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}
	
	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		/*check key support */
		switch(super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.PORT_TYPE_CODE:
			if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_SORT)) {
				return PortTypeWrapper.COLUMN_SORT;
			}
		case ObjectEntities.EQUIPMENT_TYPE_CODE:
			if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
				return StorableObjectWrapper.COLUMN_CODENAME;
			}
		default:
		throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
