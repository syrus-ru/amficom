/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/02/08 13:56:53 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 13:56:53 $
 * @author $Author: max $
 * @module general_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	protected String getColumnName(short entityCode) {
		String columnName = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				columnName = CharacteristicWrapper.COLUMN_CHARACTERIZED_ID;
				break;
		}
		return columnName;
	}

}
