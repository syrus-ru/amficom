/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.28 2005/07/19 13:34:34 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

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
 * @version $Revision: 1.28 $, $Date: 2005/07/19 13:34:34 $
 * @module scheme_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@SuppressWarnings("unused")
	private boolean checkDomain(final DomainMember domainMember) {
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Identifier id : this.linkedIds) {
				if (id.getMajor() == DOMAIN_CODE
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
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (super.entityCode.shortValue()) {
			case SCHEME_CODE:
				final Scheme scheme = (Scheme) storableObject;
				switch (super.linkedEntityCode) {
				case DOMAIN_CODE:
					return super.conditionTest(scheme.getDomainId());
				case SCHEMEELEMENT_CODE:
					return super.conditionTest(scheme.parentSchemeElementId);
				default:
					throw newIllegalObjectEntityException();	
				}
			case SCHEMEPORT_CODE:
				final SchemePort schemePort = (SchemePort) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEDEVICE_CODE:
					return super.conditionTest(schemePort.parentSchemeDeviceId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMECABLEPORT_CODE:
				final SchemeCablePort schemeCablePort = (SchemeCablePort) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEDEVICE_CODE:
					return super.conditionTest(schemeCablePort.parentSchemeDeviceId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMELINK_CODE:
				final SchemeLink schemeLink = (SchemeLink) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPORT_CODE:
					final boolean precondition1 = super.conditionTest(schemeLink.sourceAbstractSchemePortId);
					final boolean precondition2 = super.conditionTest(schemeLink.targetAbstractSchemePortId);
					assert !(precondition1 && precondition2);
					return precondition1 ^ precondition2;
				case SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(schemeLink.parentSchemeProtoElementId);
				case SCHEME_CODE:
					return super.conditionTest(schemeLink.parentSchemeId);
				case SCHEMEELEMENT_CODE:
					return super.conditionTest(schemeLink.parentSchemeElementId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMECABLELINK_CODE:
				final SchemeCableLink schemeCableLink = (SchemeCableLink) storableObject;
				switch (super.linkedEntityCode) {
					case SCHEMECABLEPORT_CODE:
						final boolean precondition1 = super.conditionTest(schemeCableLink.sourceAbstractSchemePortId);
						final boolean precondition2 = super.conditionTest(schemeCableLink.targetAbstractSchemePortId);
						assert !(precondition1 && precondition2);
						return precondition1 ^ precondition2;
					case SCHEME_CODE:
						return super.conditionTest(schemeCableLink.parentSchemeId);
					default:
						throw newIllegalObjectEntityException();
				}
			case SCHEMECABLETHREAD_CODE:
				final SchemeCableThread schemeCableThread = (SchemeCableThread) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPORT_CODE:
					final boolean precondition1 = super.conditionTest(schemeCableThread.sourceSchemePortId);
					final boolean precondition2 = super.conditionTest(schemeCableThread.targetSchemePortId);
					assert !(precondition1 && precondition2);
					return precondition1 ^ precondition2;
				case SCHEMECABLELINK_CODE:
					return super.conditionTest(schemeCableThread.parentSchemeCableLinkId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEELEMENT_CODE:
				final SchemeElement schemeElement = (SchemeElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEELEMENT_CODE:
					return super.conditionTest(schemeElement.parentSchemeElementId);
				case SCHEME_CODE:
					return super.conditionTest(schemeElement.parentSchemeId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEDEVICE_CODE:
				final SchemeDevice schemeDevice = (SchemeDevice) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEELEMENT_CODE:
					return super.conditionTest(schemeDevice.parentSchemeElementId);
				case SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(schemeDevice.parentSchemeProtoElementId);
				default:
					throw newIllegalObjectEntityException();
				}
			case PATHELEMENT_CODE:
				final PathElement pathElement = (PathElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPATH_CODE:
					return super.conditionTest(pathElement.parentSchemePathId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEPATH_CODE:
				final SchemePath schemePath = (SchemePath) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEME_CODE:
					return super.conditionTest(schemePath.parentSchemeId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEPROTOELEMENT_CODE:
				final SchemeProtoElement protoElement = (SchemeProtoElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(protoElement.parentSchemeProtoElementId);
				case SCHEMEPROTOGROUP_CODE:
					return super.conditionTest(protoElement.parentSchemeProtoGroupId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEPROTOGROUP_CODE:
				final SchemeProtoGroup protoGroup = (SchemeProtoGroup) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPROTOGROUP_CODE:
					return super.conditionTest(protoGroup.parentSchemeProtoGroupId);
				case UPDIKE_CODE:
					return super.conditionTest(protoGroup.parentSchemeProtoGroupId);
				default:
					throw newIllegalObjectEntityException();
				}
			case CABLECHANNELINGITEM_CODE:
				final CableChannelingItem cableChannelingItem = (CableChannelingItem) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMECABLELINK_CODE:
					return super.conditionTest(cableChannelingItem.parentSchemeCableLinkId);
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
	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case SCHEMEPORT_CODE:
			case SCHEMECABLEPORT_CODE:
			case SCHEMELINK_CODE:
			case SCHEMECABLELINK_CODE:
			case SCHEMECABLETHREAD_CODE:
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
	@Override
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		return true;
	}

	private IllegalObjectEntityException newIllegalObjectEntityException() {
		return new IllegalObjectEntityException(
				ENTITY_CODE_NOT_REGISTERED + super.entityCode
				+ ", " + ObjectEntities.codeToString(super.entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
