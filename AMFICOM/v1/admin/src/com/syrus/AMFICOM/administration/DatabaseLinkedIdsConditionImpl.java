/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.11 2005/07/16 18:52:37 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @version $Revision: 1.11 $, $Date: 2005/07/16 18:52:37 $
 * @author $Author: arseniy $
 * @module admin
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case MCM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SERVER_CODE:
						return super.getQuery(MCMWrapper.COLUMN_SERVER_ID);
					case DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				} 				
			case DOMAIN_CODE:
			case SERVER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
