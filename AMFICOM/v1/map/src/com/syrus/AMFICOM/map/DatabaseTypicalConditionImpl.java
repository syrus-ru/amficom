/*
 * $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/02/07 10:33:29 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/07 10:33:29 $
 * @author $Author: bob $
 * @module map_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	protected String getColumnName() throws IllegalDataException {
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
				throw new IllegalDataException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode()) + " is not supported.");
		}
		return columnName;
	}

}
