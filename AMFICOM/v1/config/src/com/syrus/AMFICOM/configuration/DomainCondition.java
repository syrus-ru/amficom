/*
 * $Id: DomainCondition.java,v 1.1 2004/10/01 10:25:09 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/01 10:25:09 $
 * @author $Author: bob $
 * @module config_v1
 */
public class DomainCondition implements StorableObjectCondition {

	private Domain	domain;

	private Short	entityCode;

	public DomainCondition(Domain domain) {
		this.domain = domain;
	}
    
    public DomainCondition(Domain domain, Short entityCode) {
        this(domain);
        this.entityCode = entityCode; 
    }

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if ((this.domain != null) && (object instanceof StorableObject)) {
			StorableObject storableObject = (StorableObject)object;
			switch (this.entityCode.shortValue()) {
				case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
					{
						CharacteristicType characteristicType = (CharacteristicType)storableObject;
						condition = true;
					}
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					{
						EquipmentType equipmentType = (EquipmentType)storableObject;
						condition = true;
					}
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					{
						PortType portType = (PortType)storableObject;
						condition = true;
					}
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					{
						MeasurementPortType measurementPortType = (MeasurementPortType)storableObject;
						condition = true;
					}
					break;
				case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
					{
						Characteristic characteristic = (Characteristic)storableObject;
						condition = true;
					}
					break;
//				case ObjectEntities.PERMATTR_ENTITY_CODE:
//					condition = true;
//					break;
				case ObjectEntities.USER_ENTITY_CODE:
					User user = (User)storableObject;
				condition = true;
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					Domain domain2 = (Domain)storableObject;
					if (domain2.isChild(this.domain))
						condition = true;
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					{
						Server server = (Server) storableObject;
						Domain serverDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(server.getDomainId(), true);
						if (serverDomain.isChild(this.domain))
							condition = true;
					}
					break;
				case ObjectEntities.MCM_ENTITY_CODE:	
					{
						MCM mcm = (MCM)storableObject;
						Domain mcmDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(mcm.getDomainId(), true);
						if (mcmDomain.isChild(this.domain))
							condition = true;
					}
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					{
						Equipment equipment = (Equipment)storableObject;
						Domain eqDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(equipment.getDomainId(), true);
						if (eqDomain.isChild(this.domain))
							condition = true;								
					}
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					{
						Port port = (Port)storableObject;
						Equipment equipment = (Equipment)ConfigurationStorableObjectPool.getStorableObject(port.getEquipmentId(), true);
						Domain eqDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(equipment.getDomainId(), true);
						if (eqDomain.isChild(this.domain))
							condition = true;								
					}
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					{
						TransmissionPath transmissionPath = (TransmissionPath) storableObject;
						Domain pathDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(transmissionPath.getDomainId(), true);
						if (pathDomain.isChild(this.domain))
							condition = true;
						
					}
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					{
						KIS kis = (KIS)storableObject;
						Domain kisDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(kis.getDomainId(), true);
						if (kisDomain.isChild(this.domain))
							condition = true;
					}
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					{
						MeasurementPort measurementPort = (MeasurementPort)storableObject;
						KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
						Domain kisDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(kis.getDomainId(), true);
						if (kisDomain.isChild(this.domain))
							condition = true;
					}
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					{
						MonitoredElement me = (MonitoredElement)storableObject;
						Domain meDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) 
							condition = true;
					}
					break;

			default:                        
				condition = true;
				break;

			}
		}

		return condition;
	}

	public Short getEntityCode() {
		return null;
	}

	public Domain getDomain() {
		return this.domain;
	}
	
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;
	}
}
