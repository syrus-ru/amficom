/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.9 2005/06/17 11:00:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/17 11:00:57 $
 * @author $Author: bass $
 * @module general_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CHARACTERISTIC_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
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
