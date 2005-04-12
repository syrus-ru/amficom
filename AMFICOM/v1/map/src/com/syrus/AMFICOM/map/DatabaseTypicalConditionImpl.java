/*
 * $Id: DatabaseTypicalConditionImpl.java,v 1.2 2005/04/12 16:41:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/12 16:41:57 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	protected String getColumnName() throws IllegalObjectEntityException {
		String columnName = null;
		/* check key support */
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME; 
					}
				break;
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) { 
					return StorableObjectWrapper.COLUMN_CODENAME; 
					}
				break;
			default:
				throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode())
						+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return columnName;
	}

}
