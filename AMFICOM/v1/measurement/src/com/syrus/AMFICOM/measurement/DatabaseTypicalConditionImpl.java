/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/02/04 14:19:04 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/04 14:19:04 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() {
		String columnName = null;
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.TEST_ENTITY_CODE:
				if (this.condition.getKey().equals(TestWrapper.COLUMN_START_TIME))
					return TestWrapper.COLUMN_START_TIME;
				if (this.condition.getKey().equals(TestWrapper.COLUMN_END_TIME))
					return TestWrapper.COLUMN_END_TIME;
				break;
		}
		return columnName;
	}

}
