/*
 * $Id: MeasurementSetupCondition.java,v 1.2 2004/10/04 10:22:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetupCondition_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/10/04 10:22:17 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementSetupCondition implements StorableObjectCondition {

	private Short		entityCode	= new Short(ObjectEntities.MS_ENTITY_CODE);

	private MeasurementType	measurementType;

	public MeasurementSetupCondition(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}

	public MeasurementSetupCondition(MeasurementSetupCondition_Transferable transferable) throws DatabaseException,
			CommunicationException {
		this.measurementType = (MeasurementType) MeasurementStorableObjectPool
				.getStorableObject(new Identifier(transferable.measurement_type_id), true);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof MeasurementSetup) {
			MeasurementSetup measurementSetup = (MeasurementSetup) object;
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
							if (parameterType.getId().equals(parameterType2.getId())) {
								condition = true;
								break;
							}
						}

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
		return new MeasurementSetupCondition_Transferable((Identifier_Transferable) this.measurementType
				.getId().getTransferable());
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}
	
	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}
}
