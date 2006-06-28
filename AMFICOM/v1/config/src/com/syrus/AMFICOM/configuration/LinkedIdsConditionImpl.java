/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.38 2006/06/28 10:34:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.38 $, $Date: 2006/06/28 10:34:12 $
 * @author $Author: arseniy $
 * @module config
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Identifiable identifiable : this.linkedIdentifiables) {
				final Identifier id = identifiable.getId();
				if (id.getMajor() == DOMAIN_CODE) {
					final Domain domain = (Domain) StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain)) {
						condition = true;
					}
				}
			}
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
		}
		return condition;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case CABLETHREAD_TYPE_CODE:
				final CableThreadType cableThreadType = (CableThreadType) storableObject;
				switch (this.linkedEntityCode) {
					case CABLELINK_TYPE_CODE:
						condition = super.conditionTest(cableThreadType.getCableLinkType().getId());
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			case PROTOEQUIPMENT_CODE:
				final ProtoEquipment protoEquipment = (ProtoEquipment) storableObject;
				switch (this.linkedEntityCode) {
					case EQUIPMENT_TYPE_CODE:
						condition = super.conditionTest(protoEquipment.getTypeId());
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			case EQUIPMENT_CODE:
				final Equipment equipment = (Equipment) storableObject;
				switch (this.linkedEntityCode) {
					case DOMAIN_CODE:
						condition = this.checkDomain(equipment);
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			case TRANSMISSIONPATH_CODE:
				final TransmissionPath transmissionPath = (TransmissionPath) storableObject;
				switch (this.linkedEntityCode) {
					case PORT_CODE:
						final boolean precondition1 = super.conditionTest(transmissionPath.getStartPortId());
						final boolean precondition2 = super.conditionTest(transmissionPath.getFinishPortId());
						assert !(precondition1 && precondition2);
						condition = precondition1 ^ precondition2;
						break;
					case DOMAIN_CODE:
						condition = this.checkDomain(transmissionPath);
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			case PORT_CODE:
				final Port port = (Port) storableObject;
				switch (this.linkedEntityCode) {
					case EQUIPMENT_CODE:
						condition = super.conditionTest(port.getEquipmentId());
						break;
					case DOMAIN_CODE:
						try {
							final Equipment equipment1 = (Equipment) StorableObjectPool.getStorableObject(port.getEquipmentId(), true);
							condition = this.checkDomain(equipment1);
						} catch (ApplicationException ae) {
							Log.errorMessage(ae);
						}
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			default:
				throw super.newExceptionEntityIllegal();
		}
		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case CABLETHREAD_TYPE_CODE:
			case EQUIPMENT_CODE:
			case TRANSMISSIONPATH_CODE:
			case PORT_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw super.newExceptionEntityCodeToSetIsIllegal(entityCode);
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
