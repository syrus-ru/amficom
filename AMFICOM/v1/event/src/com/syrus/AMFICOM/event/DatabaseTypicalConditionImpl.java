/*
 * $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/02/08 11:59:36 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/08 11:59:36 $
 * @author $Author: arseniy $
 * @module event_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	protected String getColumnName() throws IllegalDataException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				break;
			default:
				throw new IllegalDataException("DatabaseTypicalConditionImpl.getColumnName | entity "
					+ ObjectEntities.codeToString(this.condition.getEntityCode()) + " is not supported.");
		}
		return null;
	}

}
