/*
 * $Id: LinkedIdsCondition.java,v 1.4 2004/10/06 14:42:20 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.LinkedIdsCondition_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2004/10/06 14:42:20 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class LinkedIdsCondition implements StorableObjectCondition {
	private Domain	domain;
	
	private static final Short MEASUREMENT_SHORT = new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE); 
	private static final Short RESULT_SHORT = new Short(ObjectEntities.RESULT_ENTITY_CODE);
	private Short	entityCode;
	

	private List	linkedIds;

	
	public LinkedIdsCondition(LinkedIdsCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.linkedIds = new ArrayList(transferable.linked_ids.length);
		for (int i = 0; i < transferable.linked_ids.length; i++) {
			this.linkedIds.add(new Identifier(transferable.linked_ids[i]));
		}
		
		switch(transferable.entity_code){
			case ObjectEntities.MEASUREMENT_ENTITY_CODE : 
				this.entityCode = MEASUREMENT_SHORT;
				break;
			case ObjectEntities.RESULT_ENTITY_CODE : 
				this.entityCode = RESULT_SHORT;
				break;
			default: this.entityCode = null;				
		}
	}
	
	public LinkedIdsCondition(List linkedIds, short entityCode) {
		this(linkedIds, new Short(entityCode));
	}	
	
	public LinkedIdsCondition(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	
	public Domain getDomain() {
		return this.domain;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch(this.entityCode.shortValue()){
			case ObjectEntities.MEASUREMENT_ENTITY_CODE :
				if (object instanceof Measurement) {
					Measurement measurement = (Measurement)object;
					for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
						Identifier testId = (Identifier) it.next();
						if (measurement.getTestId().equals(testId)){
							condition = true;
							break;
						}				
					}
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE :
				if (object instanceof Result) {
					Result result = (Result)object;
					for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
						Identifier measurementId = (Identifier) it.next();
						if (result.getMeasurement().getId().equals(measurementId)){
							condition = true;
							break;
						}				
					}
				}

				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown for this condition");
		}

		return condition;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setEntityCode(Short entityCode) {
		switch(this.entityCode.shortValue()){
			case ObjectEntities.MEASUREMENT_ENTITY_CODE :
			case ObjectEntities.RESULT_ENTITY_CODE : 
				this.entityCode = entityCode;
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown for this condition");
		}
		
	}
	
	public void setLinkedIds(List linkedIds){
		this.linkedIds = linkedIds;
	}
	
	public Object getTransferable() {
		
		if (this.entityCode == null) {
			throw new UnsupportedOperationException("entityCode doesn't set");
		}
		
		short s = this.entityCode.shortValue();
		switch(s){
			case ObjectEntities.MEASUREMENT_ENTITY_CODE :
			case ObjectEntities.RESULT_ENTITY_CODE : 
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown");
		}

		LinkedIdsCondition_Transferable transferable = new LinkedIdsCondition_Transferable();
		Identifier_Transferable[] testId_Transferable = new Identifier_Transferable[this.linkedIds.size()];
		int i = 0;
		
		for (Iterator it = this.linkedIds.iterator(); it.hasNext();i++) {
			Identifier id = (Identifier) it.next();
			testId_Transferable[i] = (Identifier_Transferable)id.getTransferable();
		}
		
		transferable.domain_id = (Identifier_Transferable) this.domain.getId().getTransferable();
		transferable.linked_ids = testId_Transferable;
		transferable.entity_code = s;
		
		return transferable;
	}
	
	public List getTestIds() {
		if (this.entityCode.equals(MEASUREMENT_SHORT))
			return this.linkedIds;
		return null;
	}
	
	public List getMeasurementIds() {
		if (this.entityCode.equals(RESULT_SHORT))
			return this.linkedIds;
		return null;
	}

}
