/*
 * $Id: ResultCondition.java,v 1.1 2004/10/05 11:40:58 bob Exp $
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
import com.syrus.AMFICOM.measurement.corba.ResultCondition_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/05 11:40:58 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ResultCondition implements StorableObjectCondition {
	private Domain	domain;
	private Short	entityCode	= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	

	private List	measurementIds;

	
	public ResultCondition(ResultCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.measurementIds = new ArrayList(transferable.measurement_ids.length);
		for (int i = 0; i < transferable.measurement_ids.length; i++) {
			this.measurementIds.add(new Identifier(transferable.measurement_ids[i]));
		}
	}
	
	public ResultCondition(List measurementIds) {
		this.measurementIds = measurementIds;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof Result) {
			Result result = (Result)object;
			for (Iterator it = this.measurementIds.iterator(); it.hasNext();) {
				Identifier measurementId = (Identifier) it.next();
				if (result.getMeasurement().getId().equals(measurementId)){
					condition = true;
					break;
				}				
			}
		}
		return condition;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setEntityCode(Short entityCode) {
		throw new UnsupportedOperationException();
	}
	
	
	public Object getTransferable() {
		ResultCondition_Transferable transferable = new ResultCondition_Transferable();
		Identifier_Transferable[] measurementId_transferable = new Identifier_Transferable[this.measurementIds.size()];
		int i = 0;
		
		for (Iterator it = this.measurementIds.iterator(); it.hasNext();i++) {
			Identifier id = (Identifier) it.next();
			measurementId_transferable[i] = (Identifier_Transferable)id.getTransferable();
		}
		
		transferable.domain_id = (Identifier_Transferable) this.domain.getId().getTransferable();
		transferable.measurement_ids = measurementId_transferable;
		
		return transferable;
	}
	
	public List getMeasurementIds() {
		return this.measurementIds;
	}
}
