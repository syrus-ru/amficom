/*
 * $Id: DomainCondition.java,v 1.5 2004/10/01 14:01:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.DomainCondition_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/10/01 14:01:15 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class DomainCondition implements StorableObjectCondition {

	private Domain	domain;

	private Short	entityCode;
	
	public DomainCondition(DomainCondition_Transferable transferable) throws DatabaseException, CommunicationException {
		this.domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(new Identifier(transferable.domain_id), true);
		this.entityCode = new Short(transferable.entity_code);
	}

	public DomainCondition(Domain domain) {
		this.domain = domain;
	}

	public DomainCondition(Domain domain, Short entityCode) {
		this(domain);
		this.entityCode = entityCode;
	}

	public DomainCondition(Domain domain, short entityCode) {
		this(domain);
		this.entityCode = new Short(entityCode);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if ((this.domain != null) && (object instanceof StorableObject)) {
			StorableObject storableObject = (StorableObject) object;
			/**
			 * TODO check for entites
			 */
			switch (this.entityCode.shortValue()) {
				case ObjectEntities.SET_ENTITY_CODE:
					Set set = (Set) storableObject;
					{
						List meList = set.getMonitoredElementIds();
						if (meList != null) {
							for (Iterator iter = meList.iterator(); iter.hasNext();) {
								Identifier id = (Identifier) iter.next();
								MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
										.getStorableObject(id, true);
								Domain meDomain = (Domain) ConfigurationStorableObjectPool
										.getStorableObject(me.getDomainId(),
													true);
								if (meDomain.isChild(this.domain)) {
									condition = true;
									break;
								}
							}
						} else
							condition = true;
					}
					break;
				case ObjectEntities.MODELING_ENTITY_CODE:
					Modeling modeling = (Modeling) storableObject;
					{
						MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
								.getStorableObject(modeling.getMonitoredElementId(),
											true);
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
					}
					break;
				case ObjectEntities.MS_ENTITY_CODE:
					MeasurementSetup measurementSetup = (MeasurementSetup) storableObject;
					{
						List meList = measurementSetup.getMonitoredElementIds();
						if (meList != null) {
							for (Iterator iter = meList.iterator(); iter.hasNext();) {
								Identifier id = (Identifier) iter.next();
								MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
										.getStorableObject(id, true);
								Domain meDomain = (Domain) ConfigurationStorableObjectPool
										.getStorableObject(me.getDomainId(),
													true);
								if (meDomain.isChild(this.domain)) {
									condition = true;
									break;
								}
							}
						} else
							condition = true;
					}
					break;
				case ObjectEntities.ANALYSIS_ENTITY_CODE:
					Analysis analysis = (Analysis) storableObject;
					{
						MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
								.getStorableObject(analysis.getMonitoredElementId(),
											true);
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
					}
					break;
				case ObjectEntities.EVALUATION_ENTITY_CODE:
					Evaluation evaluation = (Evaluation) storableObject;
					{
						MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
								.getStorableObject(evaluation.getMonitoredElementId(),
											true);
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
					}
					break;
				case ObjectEntities.MEASUREMENT_ENTITY_CODE:
					Measurement measurement = (Measurement) storableObject;
					{
						MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
								.getStorableObject(measurement.getMonitoredElementId(),
											true);
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
					}
					break;
				case ObjectEntities.TEST_ENTITY_CODE:
					Test test = (Test) storableObject;
					{
						MonitoredElement me = test.getMonitoredElement();
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
					}
					break;
				case ObjectEntities.RESULT_ENTITY_CODE:
					Result result = (Result) storableObject;
					Measurement measurement2 = result.getMeasurement();
					{
						MonitoredElement me = (MonitoredElement) ConfigurationStorableObjectPool
								.getStorableObject(
											measurement2
													.getMonitoredElementId(),
											true);
						Domain meDomain = (Domain) ConfigurationStorableObjectPool
								.getStorableObject(me.getDomainId(), true);
						if (meDomain.isChild(this.domain)) {
							condition = true;
							break;
						}
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

	public Object getTransferable() {
		return new DomainCondition_Transferable(this.entityCode
						.shortValue(), (Identifier_Transferable) this.domain.getId().getTransferable());
	}
}
