/*
 * $Id: MeasurementSetupCondition.java,v 1.5 2004/10/13 10:35:52 max Exp $
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
import com.syrus.AMFICOM.measurement.corba.MeasurementSetupCondition_Transferable;

/**
 * @deprecated use {@link com.syrus.AMFICOM.measurement.LinkedIdsCondition}
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/13 10:35:52 $
 * @author $Author: max $
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
		this.entityCode = new Short(transferable.entity_code);

		if (transferable.entity_code == ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE)
			this.measurementType = (MeasurementType) MeasurementStorableObjectPool
					.getStorableObject(new Identifier(transferable.object_id), true);
		else if (transferable.entity_code == ObjectEntities.ME_ENTITY_CODE)
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
			transferable.entity_code = ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
		} else if (this.monitoredElement != null) {
			transferable.object_id = (Identifier_Transferable) this.monitoredElement.getId()
					.getTransferable();
			transferable.entity_code = ObjectEntities.ME_ENTITY_CODE;
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
