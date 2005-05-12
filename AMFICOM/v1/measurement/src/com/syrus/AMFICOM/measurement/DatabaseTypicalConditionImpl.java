/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.7 2005/05/12 16:33:39 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.7 $, $Date: 2005/05/12 16:33:39 $
 * @author $Author: bob $
 * @module measurement_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.TEST_ENTITY_CODE:
				if (this.condition.getKey().equals(TestWrapper.COLUMN_START_TIME))
					return TestWrapper.COLUMN_START_TIME;
				if (this.condition.getKey().equals(TestWrapper.COLUMN_END_TIME))
					return TestWrapper.COLUMN_END_TIME;
				if (this.condition.getKey().equals(TestWrapper.COLUMN_STATUS))
					return TestWrapper.COLUMN_STATUS;
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				break;
			case ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE:
				if (this.condition.getKey().equals(PeriodicalTemporalPatternWrapper.COLUMN_PERIOD))
					return PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
				break;
			default:
				throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode())
						+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
	}

}
