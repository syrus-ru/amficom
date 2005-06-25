/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.20 2005/06/25 17:50:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/06/25 17:50:50 $
 * @author $Author: bass $
 * @module admin_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unusedPrivate")
	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_CODE) {
					final Domain domain = (Domain) StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain))
						condition = true;
				}
			}
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return condition;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MCM_CODE:
				final MCM mcm = (MCM) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.SERVER_CODE:
						condition = super.conditionTest(mcm.getServerId());
						break;
					case ObjectEntities.DOMAIN_CODE:
						condition = this.checkDomain(mcm);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.DOMAIN_CODE:
				final Domain domain = (Domain) storableObject;
				condition = this.checkDomain(domain);
				break;
			case ObjectEntities.SERVER_CODE:
				final Server server = (Server) storableObject;
				condition = this.checkDomain(server);
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MCM_CODE:
			case ObjectEntities.DOMAIN_CODE:
			case ObjectEntities.SERVER_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
