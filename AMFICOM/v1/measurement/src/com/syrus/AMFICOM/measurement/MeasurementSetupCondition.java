/*
 * $Id: MeasurementSetupCondition.java,v 1.3 2004/10/04 13:17:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetupConditionSort;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetupCondition_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/10/04 13:17:12 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementSetupCondition implements StorableObjectCondition {

	private Short			entityCode	= new Short(ObjectEntities.MS_ENTITY_CODE);

	private MeasurementType		measurementType;
	private MonitoredElement	monitoredElement;

	public MeasurementSetupCondition(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}

	public MeasurementSetupCondition(MonitoredElement monitoredElement) {
		this.monitoredElement = monitoredElement;
	}

	public MeasurementSetupCondition(MeasurementSetupCondition_Transferable transferable) throws DatabaseException,
			CommunicationException {
		int sort = transferable.sort.value();

		if (sort == MeasurementSetupConditionSort._MSC_SORT_MEASUREMENT_TYPE)
			this.measurementType = (MeasurementType) MeasurementStorableObjectPool
					.getStorableObject(new Identifier(transferable.object_id), true);
		else if (sort == MeasurementSetupConditionSort._MSC_SORT_MONITORED_ELEMENT)
			this.monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool
					.getStorableObject(new Identifier(transferable.object_id), true);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof MeasurementSetup) {
			MeasurementSetup measurementSetup = (MeasurementSetup) object;

			if (this.measurementType != null) {
				SetParameter[] setParameters = measurementSetup.getParameterSet().getParameters();
				for (int i = 0; (i < setParameters.length) && (!condition); i++) {
					ParameterType parameterType = (ParameterType) setParameters[i].getType();
					List inParameterTypes = this.measurementType.getInParameterTypes();
					for (Iterator it = inParameterTypes.iterator(); it.hasNext();) {
						Object element = it.next();
						if (element instanceof ParameterType) {
							ParameterType parameterType2 = (ParameterType) element;
							if (parameterType.getId().equals(parameterType2.getId())) {
								condition = true;
								break;
							}
						}

					}

					if (!condition) {
						List outParameterTypes = this.measurementType.getOutParameterTypes();
						for (Iterator it = outParameterTypes.iterator(); it.hasNext();) {
							Object element = it.next();
							if (element instanceof ParameterType) {
								ParameterType parameterType2 = (ParameterType) element;
								if (parameterType.getId()
										.equals(parameterType2.getId())) {
									condition = true;
									break;
								}
							}

						}
					}

				}
			} else if (this.monitoredElement != null){
				Identifier meId = this.monitoredElement.getId();
				for (Iterator it=measurementSetup.getMonitoredElementIds().iterator();it.hasNext();){
					MonitoredElement monitoredElement = (MonitoredElement)it.next();
					if (monitoredElement.getId().equals(meId)){
						condition = true;
						break;
					}
				}
			}
		}
		return condition;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		throw new UnsupportedOperationException("coundn't change entity for this condition");
	}

	public Object getTransferable() {
		MeasurementSetupCondition_Transferable transferable = new MeasurementSetupCondition_Transferable();
		if (this.measurementType != null) {
			transferable.object_id = (Identifier_Transferable) this.measurementType.getId()
					.getTransferable();
			transferable.sort = MeasurementSetupConditionSort.MSC_SORT_MEASUREMENT_TYPE;
		} else if (this.monitoredElement != null) {
			transferable.object_id = (Identifier_Transferable) this.monitoredElement.getId()
					.getTransferable();
			transferable.sort = MeasurementSetupConditionSort.MSC_SORT_MONITORED_ELEMENT;
		}
		return transferable;
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
		this.monitoredElement = null;
	}

	public MonitoredElement getMonitoredElement() {
		return this.monitoredElement;
	}

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		this.monitoredElement = monitoredElement;
		this.measurementType = null;
	}
}
