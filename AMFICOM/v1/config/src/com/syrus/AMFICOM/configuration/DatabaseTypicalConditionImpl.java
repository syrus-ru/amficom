/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.6 2005/04/26 09:39:14 max Exp $
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
import com.syrus.AMFICOM.measurement.TestWrapper;


/**
 * @version $Revision: 1.6 $, $Date: 2005/04/26 09:39:14 $
 * @author $Author: max $
 * @module config_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalObjectEntityException {
		/*check key support */
		switch(super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.PORTTYPE_ENTITY_CODE:
			if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_SORT))
				return PortTypeWrapper.COLUMN_SORT;
		default:
		throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
