/*
 * $Id: LinkedIdsConditionImpl.java,v 1.6 2005/02/10 07:26:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/10 07:26:37 $
 * @author $Author: bob $
 * @module admin_v1
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.linkedIds = Collections.singletonList(identifier);
		this.entityCode = entityCode;
	}

	private boolean checkDomain(DomainMember domainMember) throws ApplicationException {
		Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
		boolean condition = false;
		/* if linked ids is domain id */
		for (Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
				Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
				if (dmDomain.isChild(domain))
					condition = true;

			}

		}
		return condition;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				MCM mcm = (MCM) object;
				/* if linked ids is kiss id */
				condition = super.conditionTest(mcm.getKISIds());
				if (!condition)
					condition = this.checkDomain(mcm);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				Domain domain = (Domain) object;
				condition = this.checkDomain(domain);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				Server server = (Server) object;
				condition = this.checkDomain(server);
				break;
			default:
				throw new UnsupportedOperationException("entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}

		return condition;
	}

	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;
	}

	public boolean isNeedMore(List list) {
		return true;
	}
}
