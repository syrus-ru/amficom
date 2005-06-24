/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.13 2005/06/24 14:09:05 max Exp $
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
 * @author $Author: max $
 * @version $Revision: 1.13 $, $Date: 2005/06/24 14:09:05 $
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
				if (id.getMajor() == ObjectEntities.DOMAIN_CODE
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
			case ObjectEntities.SCHEME_CODE:
				final Scheme scheme = (Scheme) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.DOMAIN_CODE:
						return super.conditionTest(scheme.getDomainId());
					default:
						throw newIllegalObjectEntityException();	
				}
			case ObjectEntities.SCHEMEPORT_CODE:
				final SchemePort schemePort = (SchemePort) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEDEVICE_CODE:
						return super.conditionTest(schemePort.parentSchemeDeviceId);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMECABLEPORT_CODE:
				final SchemeCablePort schemeCablePort = (SchemeCablePort) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEDEVICE_CODE:
						return super.conditionTest(schemeCablePort.parentSchemeDeviceId);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMELINK_CODE:
				final SchemeLink schemeLink = (SchemeLink) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEPORT_CODE:
						final boolean precondition1 = super.conditionTest(schemeLink.sourceAbstractSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeLink.targetAbstractSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMECABLELINK_CODE:
				final SchemeCableLink schemeCableLink = (SchemeCableLink) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMECABLEPORT_CODE:
						final boolean precondition1 = super.conditionTest(schemeCableLink.sourceAbstractSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeCableLink.targetAbstractSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				final SchemeCableThread schemeCableThread = (SchemeCableThread) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEPORT_CODE:
						final boolean precondition1 = super.conditionTest(schemeCableThread.sourceSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeCableThread.targetSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					case ObjectEntities.SCHEMECABLELINK_CODE:
						return super.conditionTest(schemeCableThread.getParentSchemeCableLink().getId());
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMEELEMENT_CODE:
				final SchemeElement schemeElement = (SchemeElement) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEELEMENT_CODE:
						return super.conditionTest(schemeElement.parentSchemeId);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMEDEVICE_CODE:
				final SchemeDevice schemeDevice = (SchemeDevice) storableObject;
				switch (super.linkedEntityCode) {
				case ObjectEntities.SCHEMEELEMENT_CODE:
					return super.conditionTest(schemeDevice.getParentSchemeElement().getId());
				case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(schemeDevice.getParentSchemeProtoElement().getId());
				default:
					throw newIllegalObjectEntityException();
				}
			case ObjectEntities.PATHELEMENT_CODE:
				final PathElement pathElement = (PathElement) storableObject;
				switch (super.linkedEntityCode) {
					case ObjectEntities.SCHEMEPATH_CODE:
						return super.conditionTest(pathElement.getParentSchemePath().getId());
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
				final SchemeProtoElement protoElement = (SchemeProtoElement) storableObject;
				switch (super.linkedEntityCode) {
				case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(protoElement.getParentSchemeProtoElement().getId());
				default:
					throw newIllegalObjectEntityException();
				}
			default:
				throw newIllegalObjectEntityException();
		}
	}

	/**
	 * @param entityCode
	 * @throws IllegalObjectEntityException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#setEntityCode(Short)
	 */
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.SCHEMEPORT_CODE:
			case ObjectEntities.SCHEMECABLEPORT_CODE:
			case ObjectEntities.SCHEMELINK_CODE:
			case ObjectEntities.SCHEMECABLELINK_CODE:
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				super.entityCode = entityCode;
				break;
			default:
				throw newIllegalObjectEntityException();
		}
	}

	/**
	 * @param storableObjects
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isNeedMore(Set)
	 */
	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}

	private IllegalObjectEntityException newIllegalObjectEntityException() {
		return new IllegalObjectEntityException(
				ENTITY_CODE_NOT_REGISTERED + super.entityCode
				+ ", " + ObjectEntities.codeToString(super.entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
