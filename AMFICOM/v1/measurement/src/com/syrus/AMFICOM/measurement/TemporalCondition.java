/*
 * $Id: TemporalCondition.java,v 1.5 2005/01/17 11:44:14 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalCondition_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2005/01/17 11:44:14 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class TemporalCondition implements StorableObjectCondition {

	private Domain	domain;
	private Date	start;
	private Date	end;
	private Short	entityCode	= new Short(ObjectEntities.TEST_ENTITY_CODE);
	
	public TemporalCondition(TemporalCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) AdministrationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.start = new Date(transferable.start);
		this.end = new Date(transferable.end);
		setEntityCode(new Short(transferable.entity_code));
	}

	public TemporalCondition(Domain domain, Date start, Date end) {
		this.domain = domain;
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
			Test test = (Test) object;
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

	public boolean isNeedMore(List list) throws ApplicationException {
		return true;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setEntityCode(Short entityCode) {
		if (entityCode.shortValue() != ObjectEntities.TEST_ENTITY_CODE)
			throw new UnsupportedOperationException();
	}

	public Object getTransferable() {
		return new TemporalCondition_Transferable(this.entityCode.shortValue(),
												(Identifier_Transferable) this.domain
														.getId()
														.getTransferable(),												
												this.start.getTime(),
												this.end.getTime()
												);

	}
	
	public Date getEnd() {
		return this.end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getStart() {
		return this.start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
}
