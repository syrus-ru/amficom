/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.5 2005/03/05 21:33:00 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/05 21:33:00 $
 * @author $Author: arseniy $
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
			query.append(super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID));
			break;
		default:
			throw new IllegalDataException(
					"general.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return query.toString();
	}

}
