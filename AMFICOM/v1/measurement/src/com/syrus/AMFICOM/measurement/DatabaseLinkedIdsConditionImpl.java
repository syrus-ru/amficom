/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/02/03 14:59:08 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/03 14:59:08 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	protected String getColumnName() {
		String columnName = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				columnName = MeasurementWrapper.COLUMN_TEST_ID;
				break;
		}
		return columnName;
	}

}
