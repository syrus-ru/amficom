/*
 * $Id: LinkedIdsConditionImpl.java,v 1.6 2005/02/24 09:16:16 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/24 09:16:16 $
 * @author $Author: bob $
 * @module config_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(Collection linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.linkedIds = Collections.singletonList(identifier);
		this.entityCode = entityCode;
	}

	private boolean checkDomain(DomainMember domainMember) throws ApplicationException {
		Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
		boolean condition = false;
		/* if linked ids is domain id */
		for (Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
				Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
				if (dmDomain.isChild(domain))
					condition = true;

			}

		}
		return condition;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		DomainMember domainMember;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				domainMember = (Equipment) object;
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				domainMember = (TransmissionPath) object;
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				domainMember = (KIS) object;
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				domainMember = (MonitoredElement) object;
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				Port port = (Port) object;
				domainMember = (Equipment) ConfigurationStorableObjectPool.getStorableObject(port.getEquipmentId(),
					true);
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				MeasurementPort measurementPort = (MeasurementPort) object;
				domainMember = (KIS) ConfigurationStorableObjectPool
						.getStorableObject(measurementPort.getKISId(), true);
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.isConditionTrue | entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}
		if (domainMember != null)
			condition = this.checkDomain(domainMember);
		return condition;
	}

	public void setEntityCode(Short entityCode) {
		switch (entityCode.shortValue()) {
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
			case ObjectEntities.KIS_ENTITY_CODE:
			case ObjectEntities.ME_ENTITY_CODE:
			case ObjectEntities.PORT_ENTITY_CODE:
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.setEntityCode | entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}
	}

	public boolean isNeedMore(Collection collection) {
		return true;
	}
}
