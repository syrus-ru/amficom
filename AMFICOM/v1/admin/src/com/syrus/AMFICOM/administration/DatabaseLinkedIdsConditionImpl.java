/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.5 2005/03/10 19:34:22 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/10 19:34:22 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SERVER_ENTITY_CODE:
						query = super.getQuery(MCMWrapper.COLUMN_SERVER_ID);
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
						break;
				}
				break; 				
			case ObjectEntities.DOMAIN_ENTITY_CODE:
			case ObjectEntities.SERVER_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
						break;
				}
				break;
		}
		return query;
	}

}
