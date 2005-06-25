/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.8 2005/06/25 17:50:50 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.8 $, $Date: 2005/06/25 17:50:50 $
 * @author $Author: bass $
 * @module admin_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unusedPrivate")
	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MCM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SERVER_CODE:
						return super.getQuery(MCMWrapper.COLUMN_SERVER_ID);
					case ObjectEntities.DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				} 				
			case ObjectEntities.DOMAIN_CODE:
			case ObjectEntities.SERVER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			default:
				throw new IllegalObjectEntityException("Unsupported entity type -- "
						+ super.condition.getEntityCode(), IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
