/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.4 2005/02/10 08:09:01 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/10 08:09:01 $
 * @author $Author: bob $
 * @module admin_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		StringBuffer buffer = new StringBuffer();
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				buffer.append(super.getLinkedQuery(MCMWrapper.LINK_COLUMN_MCM_ID,
					StorableObjectWrapper.COLUMN_ID, ObjectEntities.KIS_ENTITY));
				/* break commenting is ok, append domain condition too */
//				break; 				
			case ObjectEntities.DOMAIN_ENTITY_CODE:
			case ObjectEntities.SERVER_ENTITY_CODE:
				buffer.append(super.getQuery(DomainMember.COLUMN_DOMAIN_ID));
				query = buffer.toString();
				break;
		}
		return query;
	}

}
