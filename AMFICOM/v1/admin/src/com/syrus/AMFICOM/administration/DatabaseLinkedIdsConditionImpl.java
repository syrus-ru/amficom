/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.16 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.MCMWrapper.COLUMN_SERVER_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PARENT_ID;
import static com.syrus.AMFICOM.administration.RoleWrapper.LINK_COLUMN_ROLE_ID;
import static com.syrus.AMFICOM.administration.RoleWrapper.LINK_COLUMN_SYSTEM_USER_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERMATTR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.TableNames.SYSTEM_USER_ROLE_LINK;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @version $Revision: 1.16 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @module administration
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case SYSTEMUSER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ROLE_CODE:
						return super.getLinkedQuery(LINK_COLUMN_SYSTEM_USER_ID,
								LINK_COLUMN_ROLE_ID,
								SYSTEM_USER_ROLE_LINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ROLE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SYSTEMUSER_CODE:
						return super.getLinkedQuery(LINK_COLUMN_ROLE_ID,
								LINK_COLUMN_SYSTEM_USER_ID,
								SYSTEM_USER_ROLE_LINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case DOMAIN_CODE:
			case SERVER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MCM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SERVER_CODE:
						return super.getQuery(COLUMN_SERVER_ID);
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				} 				
			case PERMATTR_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					case ROLE_CODE:
					case SYSTEMUSER_CODE:
						return super.getQuery(COLUMN_PARENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
