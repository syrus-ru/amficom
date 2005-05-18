/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.5 2005/05/18 12:37:39 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $
 * @author $Author: bass $
 * @module mapview_v1
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {
	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			/* if linked ids is domain id */
			for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
					final Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain))
						condition = true;

				}

			}
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return condition;
	}

	public boolean isConditionTrue(final StorableObject storableObject) {
		boolean condition = false;
		DomainMember domainMember;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				domainMember = (MapView) storableObject;
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.isConditionTrue | entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}
		if (domainMember != null)
			condition = this.checkDomain(domainMember);
		return condition;
	}

	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException("LinkedIdsConditionImpl.setEntityCode | entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
