/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.4 2005/04/12 16:39:23 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.4 $, $Date: 2005/04/12 16:39:23 $
 * @author $Author: arseniy $
 * @module general_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				break;
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				break;
			default:
				throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode())
						+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
	}

}
