/*
 * $Id: MeasurementCondition.java,v 1.3 2004/10/05 11:41:18 bob Exp $
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
import com.syrus.AMFICOM.measurement.corba.MeasurementCondition_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/10/05 11:41:18 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementCondition implements StorableObjectCondition {
	private Domain	domain;
	private Short	entityCode	= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	

	private List	testIds;

	
	public MeasurementCondition(MeasurementCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.testIds = new ArrayList(transferable.test_ids.length);
		for (int i = 0; i < transferable.test_ids.length; i++) {
			this.testIds.add(new Identifier(transferable.test_ids[i]));
		}
	}
	
	public MeasurementCondition(List testIds) {
		this.testIds = testIds;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof Measurement) {
			Measurement measurement = (Measurement)object;
			for (Iterator it = this.testIds.iterator(); it.hasNext();) {
				Identifier testId = (Identifier) it.next();
				if (measurement.getTestId().equals(testId)){
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
		MeasurementCondition_Transferable transferable = new MeasurementCondition_Transferable();
		Identifier_Transferable[] testId_Transferable = new Identifier_Transferable[this.testIds.size()];
		int i = 0;
		
		for (Iterator it = this.testIds.iterator(); it.hasNext();i++) {
			Identifier id = (Identifier) it.next();
			testId_Transferable[i] = (Identifier_Transferable)id.getTransferable();
		}
		
		transferable.domain_id = (Identifier_Transferable) this.domain.getId().getTransferable();
		transferable.test_ids = testId_Transferable;
		
		return transferable;
	}
	
	public List getTestIds() {
		return this.testIds;
	}
}
