/*
 * $Id: LinkedIdsConditionImpl.java,v 1.12 2005/03/24 12:40:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collection;

import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.12 $, $Date: 2005/03/24 12:40:00 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(Collection linkedIds, Short linkedEntityCode, Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	public boolean isConditionTrue(Object object) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				MCM mcm = (MCM) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.SERVER_ENTITY_CODE:
						condition = super.conditionTest(mcm.getServerId());
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(mcm);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
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
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		return condition;
	}

	public void setEntityCode(Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
			case ObjectEntities.DOMAIN_ENTITY_CODE:
			case ObjectEntities.SERVER_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	public boolean isNeedMore(Collection collection ) {
		return true;
	}
}
