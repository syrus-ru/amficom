/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.3 2005/02/07 09:58:45 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.3 $, $Date: 2005/02/07 09:58:45 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalDataException {
		String columnName = null;
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.TEST_ENTITY_CODE:
				if (this.condition.getKey().equals(TestWrapper.COLUMN_START_TIME))
					return TestWrapper.COLUMN_START_TIME;
				if (this.condition.getKey().equals(TestWrapper.COLUMN_END_TIME))
					return TestWrapper.COLUMN_END_TIME;
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
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
