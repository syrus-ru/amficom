/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.19 2005/08/30 16:35:08 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.19 $, $Date: 2005/08/30 16:35:08 $
 * @author $Author: bass $
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
			case PORT_TYPE_CODE:
				if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_SORT)) {
					return PortTypeWrapper.COLUMN_SORT;
				} else if (this.condition.getKey().equals(PortTypeWrapper.COLUMN_KIND)) {
					return PortTypeWrapper.COLUMN_KIND;
				}
				break;
			case EQUIPMENT_TYPE_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			case MONITOREDELEMENT_CODE:
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
