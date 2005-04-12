/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.6 2005/04/12 16:52:34 arseniy Exp $
*
* Copyright � 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.6 $, $Date: 2005/04/12 16:52:34 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.USER_ENTITY_CODE:
				if (this.condition.getKey().equals(UserWrapper.COLUMN_LOGIN))
					return UserWrapper.COLUMN_LOGIN;
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME))
					return StorableObjectWrapper.COLUMN_NAME;
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME))
					return StorableObjectWrapper.COLUMN_NAME;
				break;
			default:
				throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode())
						+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
	}

}
