/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/02/07 08:47:33 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/02/07 08:47:33 $
 * @author $Author: bob $
 * @module general_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() {
		String columnName = null;
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
		}
		return columnName;
	}

}
