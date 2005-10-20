/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.16 2005/10/20 10:49:42 bob Exp $
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
 * @version $Revision: 1.16 $, $Date: 2005/10/20 10:49:42 $
 * @author $Author: bob $
 * @module administration
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		final String key = this.condition.getKey().intern();
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.SYSTEMUSER_CODE:
				if (key == SystemUserWrapper.COLUMN_LOGIN) {
					return key;
				}
				if (key == StorableObjectWrapper.COLUMN_NAME) {
					return key;
				}
				break;
			case ObjectEntities.DOMAIN_CODE:
				if (key == StorableObjectWrapper.COLUMN_NAME) {
					return key;
				}
				break;
			case ObjectEntities.SERVERPROCESS_CODE:
				if (key == StorableObjectWrapper.COLUMN_CODENAME) {
					return key;
				}
				break;
			case ObjectEntities.PERMATTR_CODE:
				if (key == PermissionAttributesWrapper.COLUMN_MODULE) {
					return key;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + key + "' are not supported.",
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
