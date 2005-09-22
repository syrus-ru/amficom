/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.40 2005/09/22 15:17:35 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.40 $, $Date: 2005/09/22 15:17:35 $
 * @module scheme
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private static final long serialVersionUID = -7694706898728720012L;

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@SuppressWarnings("unused")
	private boolean checkDomain(final DomainMember domainMember) {
		try {
			final Domain dmDomain = StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Identifier id : this.linkedIds) {
				if (id.getMajor() == DOMAIN_CODE
						&& dmDomain.isChild(StorableObjectPool.<Domain>getStorableObject(id, true))) {
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
			case SCHEMEPROTOELEMENT_CODE:
				final SchemeProtoElement protoElement = (SchemeProtoElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPROTOELEMENT_CODE:
					return super.conditionTest(protoElement.parentSchemeProtoElementId);
				case SCHEMEPROTOGROUP_CODE:
					return super.conditionTest(protoElement.parentSchemeProtoGroupId);
				case EQUIPMENT_TYPE_CODE:
					return super.conditionTest(protoElement.equipmentTypeId);
				default:
					throw newIllegalObjectEntityException();
				}
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
			case SCHEMEELEMENT_CODE:
				final SchemeElement schemeElement = (SchemeElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEELEMENT_CODE:
					return super.conditionTest(schemeElement.parentSchemeElementId);
				case SCHEME_CODE:
					return super.conditionTest(schemeElement.parentSchemeId);
				case SITENODE_CODE:
					return super.conditionTest(schemeElement.getSiteNodeId());
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEOPTIMIZEINFO_CODE:
				final SchemeOptimizeInfo schemeOptimizeInfo = (SchemeOptimizeInfo) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEME_CODE:
					return super.conditionTest(schemeOptimizeInfo.parentSchemeId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEOPTIMIZEINFOSWITCH_CODE:
				final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch = (SchemeOptimizeInfoSwitch) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEOPTIMIZEINFO_CODE:
					return super.conditionTest(schemeOptimizeInfoSwitch.parentSchemeOptimizeInfoId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEOPTIMIZEINFORTU_CODE:
				final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu = (SchemeOptimizeInfoRtu) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEOPTIMIZEINFO_CODE:
					return super.conditionTest(schemeOptimizeInfoRtu.parentSchemeOptimizeInfoId);
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEMONITORINGSOLUTION_CODE:
				final SchemeMonitoringSolution schemeMonitoringSolution = (SchemeMonitoringSolution) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEME_CODE:
					return super.conditionTest(schemeMonitoringSolution.parentSchemeId);
				case SCHEMEOPTIMIZEINFO_CODE:
					return super.conditionTest(schemeMonitoringSolution.parentSchemeOptimizeInfoId);
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
			case CABLECHANNELINGITEM_CODE:
				final CableChannelingItem cableChannelingItem = (CableChannelingItem) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMECABLELINK_CODE:
					return super.conditionTest(cableChannelingItem.parentSchemeCableLinkId);
				case SITENODE_CODE:
					return super.conditionTest(cableChannelingItem.getStartSiteNodeId()) 
						|| super.conditionTest(cableChannelingItem.getEndSiteNodeId());
				case PHYSICALLINK_CODE:
					return super.conditionTest(cableChannelingItem.getPhysicalLinkId());
				default:
					throw newIllegalObjectEntityException();
				}
			case SCHEMEPATH_CODE:
				final SchemePath schemePath = (SchemePath) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEMONITORINGSOLUTION_CODE:
					return super.conditionTest(schemePath.parentSchemeMonitoringSolutionId);
				default:
					throw newIllegalObjectEntityException();
				}
			case PATHELEMENT_CODE:
				final PathElement pathElement = (PathElement) storableObject;
				switch (super.linkedEntityCode) {
				case SCHEMEPATH_CODE:
					return super.conditionTest(pathElement.parentSchemePathId);
				case SCHEMELINK_CODE:
					if(pathElement.getKind() != IdlKind.SCHEME_LINK) {
						return false;
					}
					return super.conditionTest(pathElement.schemeLinkId);
				case SCHEMEELEMENT_CODE:
					if(pathElement.getKind() != IdlKind.SCHEME_ELEMENT) {
						return false;
					}
					AbstractSchemePort startSchemePort = pathElement.getStartAbstractSchemePort();
					if(startSchemePort != null) {
						SchemeDevice sd = startSchemePort.getParentSchemeDevice();
						if(sd != null) {
							if(super.conditionTest(sd.parentSchemeElementId)) {
								return true;
							}
						}
						
					}
					AbstractSchemePort endSchemePort = pathElement.getEndAbstractSchemePort();
					if(endSchemePort != null) {
						SchemeDevice sd = endSchemePort.getParentSchemeDevice();
						if(sd != null) {
							if(super.conditionTest(sd.parentSchemeElementId)) {
								return true;
							}
						}
					}	
					return false;
				case MEASUREMENTPORT_CODE:
					if(pathElement.getKind() != IdlKind.SCHEME_ELEMENT) {
						return false;
					}
					AbstractSchemePort endSchemePort2 = pathElement.getEndAbstractSchemePort();
					if(endSchemePort2 != null) {
						if(super.conditionTest(endSchemePort2.measurementPortId)) {
							return true;
						}
					}
					return false;
				case SCHEMECABLELINK_CODE:
					if(pathElement.getKind() != IdlKind.SCHEME_CABLE_LINK) {
						return false;
					}
					SchemeCableThread schemeCableThread2 = pathElement.getSchemeCableThread();
					boolean condition = false;
					if(schemeCableThread2 != null) {
						condition = super.conditionTest(schemeCableThread2.parentSchemeCableLinkId);
					}
					return condition;
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
			case SCHEMEPROTOGROUP_CODE:
			case SCHEMEPROTOELEMENT_CODE:
			case SCHEME_CODE:
			case SCHEMEELEMENT_CODE:
			case SCHEMEOPTIMIZEINFO_CODE:
			case SCHEMEOPTIMIZEINFOSWITCH_CODE:
			case SCHEMEOPTIMIZEINFORTU_CODE:
			case SCHEMEMONITORINGSOLUTION_CODE:
			case SCHEMEDEVICE_CODE:
			case SCHEMEPORT_CODE:
			case SCHEMECABLEPORT_CODE:
			case SCHEMELINK_CODE:
			case SCHEMECABLELINK_CODE:
			case SCHEMECABLETHREAD_CODE:
			case CABLECHANNELINGITEM_CODE:
			case SCHEMEPATH_CODE:
			case PATHELEMENT_CODE:
				super.entityCode = entityCode;
				break;
			default:
				throw newIllegalObjectEntityException();
		}
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isNeedMore(Set)
	 */
	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}

	private IllegalObjectEntityException newIllegalObjectEntityException() {
		return new IllegalObjectEntityException(
				ENTITY_CODE_NOT_REGISTERED + super.entityCode
				+ ", " + ObjectEntities.codeToString(super.entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
