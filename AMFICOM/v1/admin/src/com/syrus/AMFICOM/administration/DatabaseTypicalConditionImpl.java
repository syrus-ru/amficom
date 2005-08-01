/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.13 2005/08/01 11:11:12 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
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
 * @version $Revision: 1.13 $, $Date: 2005/08/01 11:11:12 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.SYSTEMUSER_CODE:
				if (this.condition.getKey().equals(SystemUserWrapper.COLUMN_LOGIN))
					return SystemUserWrapper.COLUMN_LOGIN;
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME))
					return StorableObjectWrapper.COLUMN_NAME;
				break;
			case ObjectEntities.DOMAIN_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME))
					return StorableObjectWrapper.COLUMN_NAME;
				break;
			case ObjectEntities.SERVERPROCESS_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + this.condition.getKey() + "' are not supported.",
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
	}

}
