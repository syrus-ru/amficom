/*
 * $Id: TestCondition.java,v 1.1 2004/09/30 14:34:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/30 14:34:21 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class TestCondition implements StorableObjectCondition {
	private Domain	domain;
	private Date	start;
	private Date	end;
	private Short	entityCode	= new Short(ObjectEntities.TEST_ENTITY_CODE);
	

	public TestCondition(Date	start, Date	end) {
		this.start = start;
		this.end = end;
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
			Test test = (Test)object;
			if ((test.getStartTime().getTime() >= this.start.getTime())
					&& (test.getEndTime().getTime() <= this.end.getTime())
					&& ((this.domain == null) || ((this.domain != null) && test
							.getMonitoredElement().getDomainId()
							.equals(this.domain.getId())))) {
				condition = true;
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
