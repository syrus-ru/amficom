/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.7 2005/05/23 10:01:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/05/23 10:01:25 $
 * @module scheme_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator linkedIdIterator = this.linkedIds.iterator(); linkedIdIterator.hasNext();) {
				final Identifier id = (Identifier) linkedIdIterator.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE
						&& dmDomain.isChild((Domain) StorableObjectPool.getStorableObject(id, true))) {
					return true;
				}
			}
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return false;
	}

	/**
	 * @param storableObject
	 * @throws IllegalObjectEntityException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (super.entityCode.shortValue()) {
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				final SchemeLink schemeLink = (SchemeLink) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
						final boolean precondition1 = super.conditionTest(schemeLink.sourceAbstractSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeLink.targetAbstractSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					default:
						throw new IllegalObjectEntityException(
								LINKED_ENTITY_CODE_NOT_REGISTERED
										+ super.linkedEntityCode
										+ ", "
										+ ObjectEntities
												.codeToString(super.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				final SchemeCableLink schemeCableLink = (SchemeCableLink) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
						final boolean precondition1 = super.conditionTest(schemeCableLink.sourceAbstractSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeCableLink.targetAbstractSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					default:
						throw new IllegalObjectEntityException(
								LINKED_ENTITY_CODE_NOT_REGISTERED
										+ super.linkedEntityCode
										+ ", "
										+ ObjectEntities
												.codeToString(super.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				final SchemeCableThread schemeCableThread = (SchemeCableThread) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
						final boolean precondition1 = super.conditionTest(schemeCableThread.sourceSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeCableThread.targetSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					default:
						throw new IllegalObjectEntityException(
								LINKED_ENTITY_CODE_NOT_REGISTERED
										+ super.linkedEntityCode
										+ ", "
										+ ObjectEntities
												.codeToString(super.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			default:
				throw new IllegalObjectEntityException(
						ENTITY_CODE_NOT_REGISTERED
								+ super.entityCode
								+ ", "
								+ ObjectEntities
										.codeToString(super.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	/**
	 * @param entityCode
	 * @throws IllegalObjectEntityException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#setEntityCode(Short)
	 */
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				super.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(
						ENTITY_CODE_NOT_REGISTERED
								+ super.entityCode
								+ ", "
								+ ObjectEntities
										.codeToString(super.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	/**
	 * @param storableObjects
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isNeedMore(Set)
	 */
	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
