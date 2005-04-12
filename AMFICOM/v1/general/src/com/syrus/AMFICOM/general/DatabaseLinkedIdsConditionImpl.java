/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.6 2005/04/12 16:43:46 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/12 16:43:46 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
			return super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);
		default:
			throw new IllegalObjectEntityException("Unsupported entity type -- "
					+ super.condition.getEntityCode(), IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
