/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.2 2005/04/13 19:34:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/04/13 19:34:10 $
 * @module scheme_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) throws IllegalObjectEntityException {
		try {
			final Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator linkedIdIterator = this.linkedIds.iterator(); linkedIdIterator.hasNext();) {
				final Identifier id = (Identifier) linkedIdIterator.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE 
						&& dmDomain.isChild((Domain) AdministrationStorableObjectPool.getStorableObject(id, true)))
					return true;
			}
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return false;
	}

	/**
	 * @param storableObject
	 * @throws IllegalObjectEntityException
	 */
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param entityCode
	 * @throws IllegalObjectEntityException
	 */
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 */
	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
