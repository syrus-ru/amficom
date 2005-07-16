/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.12 2005/07/16 18:52:02 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/16 18:52:02 $
 * @author $Author: arseniy $
 * @module general
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case CHARACTERISTIC_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);
//					case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
//						return super.getQuery(StorableObjectWrapper.COLUMN_TYPE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
