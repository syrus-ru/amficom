/*
 * $Id: ResultCondition.java,v 1.3 2004/10/18 14:25:19 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalCondition_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/10/18 14:25:19 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ResultCondition implements StorableObjectCondition {

	private Domain	domain;
	private Date	start;
	private Date	end;
	private int alarmLevel;
	private Short	entityCode	= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	
	
	public ResultCondition(ResultCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.start = new Date(transferable.start);
		this.end = new Date(transferable.end);
		this.alarmLevel = transferable.level.value();
		setEntityCode(new Short(transferable.entity_code));
	}

	public ResultCondition(Domain domain, Date start, Date end, AlarmLevel alarmLevel) {
		this.domain = domain;
		this.start = start;
		this.end = end;
		this.alarmLevel = alarmLevel.value();
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
			Result result = (Result) object;
			Test test = (Test)MeasurementStorableObjectPool.getStorableObject(result.getMeasurement().getTestId(), true);
			if ((test.getStartTime().getTime() >= this.start.getTime())
					&& (test.getEndTime().getTime() <= this.end.getTime())
					&& ((this.domain == null) || ((this.domain != null) && test
							.getMonitoredElement().getDomainId()
							.equals(this.domain.getId()))) && 
							(result.getAlarmLevel().value() == this.alarmLevel)) {
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

	public Object getTransferable() {
		return new ResultCondition_Transferable(this.entityCode.shortValue(),
												(Identifier_Transferable) this.domain
														.getId()
														.getTransferable(),												
												this.start.getTime(),
												this.end.getTime(),
												getAlarmLevel()
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
	
	public AlarmLevel getAlarmLevel() {
		return AlarmLevel.from_int(this.alarmLevel);
	}
	
	public void setAlarmLevel(AlarmLevel alarmLevel) {
		this.alarmLevel = alarmLevel.value();
	}
}
