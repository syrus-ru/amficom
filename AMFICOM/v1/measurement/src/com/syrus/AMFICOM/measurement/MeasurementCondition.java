/*
 * $Id: MeasurementCondition.java,v 1.1 2004/09/30 14:34:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/30 14:34:21 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementCondition implements StorableObjectCondition {
	private Domain	domain;
	private Short	entityCode	= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	

	private List	testIds;

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
		if (object instanceof Test) {
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
}
