/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.4 2005/02/10 08:38:13 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/10 08:38:13 $
 * @author $Author: max $
 * @module general_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		StringBuffer query = new StringBuffer();
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
			query.append(super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID));
			break;
		default:
			throw new IllegalDataException(
					"general.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return query.toString();
	}

}
