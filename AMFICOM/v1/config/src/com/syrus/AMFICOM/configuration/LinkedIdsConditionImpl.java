/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.18 2005/04/13 12:20:39 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

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
 * @version $Revision: 1.18 $, $Date: 2005/04/13 12:20:39 $
 * @author $Author: arseniy $
 * @module config_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
					final Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
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

	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				CableThreadType cableThreadType = (CableThreadType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
						condition = super.conditionTest(cableThreadType.getLinkType().getId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				Equipment equipment = (Equipment) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(equipment);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				TransmissionPath transmissionPath = (TransmissionPath) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PORT_ENTITY_CODE:
						condition = super.conditionTest(transmissionPath.getStartPortId())
								|| super.conditionTest(transmissionPath.getFinishPortId());
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(transmissionPath);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				KIS kis = (KIS) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MCM_ENTITY_CODE:
						condition = super.conditionTest(kis.getMCMId());
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(kis);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				MonitoredElement monitoredElement = (MonitoredElement) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(monitoredElement);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				Port port = (Port) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.EQUIPMENT_ENTITY_CODE:
						condition = super.conditionTest(port.getEquipmentId());
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						try {
							Equipment equipment1 = (Equipment) ConfigurationStorableObjectPool.getStorableObject(port.getEquipmentId(), true);
							condition = this.checkDomain(equipment1);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				MeasurementPort measurementPort = (MeasurementPort) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.KIS_ENTITY_CODE:
						condition = super.conditionTest(measurementPort.getKISId());
						break;
					case ObjectEntities.MCM_ENTITY_CODE:
						try {
							KIS kis1 = (KIS) ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = super.conditionTest(kis1.getMCMId());
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						break;
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						try {
							KIS kis1 = (KIS) ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = this.checkDomain(kis1);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return condition;
	}

	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
			case ObjectEntities.KIS_ENTITY_CODE:
			case ObjectEntities.ME_ENTITY_CODE:
			case ObjectEntities.PORT_ENTITY_CODE:
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
