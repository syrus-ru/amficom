/*
 * $Id: LinkedIdsConditionImpl.java,v 1.13 2005/03/24 12:40:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/03/24 12:40:22 $
 * @author $Author: arseniy $
 * @module config_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(Collection linkedIds, Short linkedEntityCode, Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	public boolean isConditionTrue(Object object) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				CableThreadType cableThreadType = (CableThreadType) object;
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
				Equipment equipment = (Equipment) object;
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
				TransmissionPath transmissionPath = (TransmissionPath) object;
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
				KIS kis = (KIS) object;
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
				MonitoredElement monitoredElement = (MonitoredElement) object;
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
				Port port = (Port) object;
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
				MeasurementPort measurementPort = (MeasurementPort) object;
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

	public void setEntityCode(Short entityCode) throws IllegalObjectEntityException {
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

	public boolean isNeedMore(Collection collection) {
		return true;
	}
}
