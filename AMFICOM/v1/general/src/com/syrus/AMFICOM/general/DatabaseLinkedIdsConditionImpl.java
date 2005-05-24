/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.7 2005/05/24 17:18:55 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/24 17:18:55 $
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
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
						return super.getQuery(StorableObjectWrapper.COLUMN_TYPE_ID);
					default:
						return super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);
				}
			default:
				throw new IllegalObjectEntityException("Unsupported entity type -- "
						+ super.condition.getEntityCode(), IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
