/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.4 2005/02/08 12:05:47 arseniy Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/02/08 12:05:47 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalDataException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.TEST_ENTITY_CODE:
				if (this.condition.getKey().equals(TestWrapper.COLUMN_START_TIME))
					return TestWrapper.COLUMN_START_TIME;
				if (this.condition.getKey().equals(TestWrapper.COLUMN_END_TIME))
					return TestWrapper.COLUMN_END_TIME;
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
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
