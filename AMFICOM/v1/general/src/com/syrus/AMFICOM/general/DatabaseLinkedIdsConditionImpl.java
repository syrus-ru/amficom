/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.3 2005/02/09 11:07:05 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/09 11:07:05 $
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
	
	public String getSQLQuery() throws IllegalDataException {
		// TODO: implement this method 
		return null;
	}

}
