/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.11 2005/07/06 13:16:35 max Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.11 $, $Date: 2005/07/06 13:16:35 $
 * @author $Author: max $
 * @module config_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalObjectEntityException {
		/*check key support */
		switch(super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.PORT_TYPE_CODE:
			if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_SORT)) {
				return PortTypeWrapper.COLUMN_SORT;
			}
		case ObjectEntities.EQUIPMENT_TYPE_CODE:
			if (this.condition.getKey().equals(EquipmentTypeWrapper.COLUMN_CODENAME)) {
				return EquipmentTypeWrapper.COLUMN_CODENAME;
			}
		default:
		throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
