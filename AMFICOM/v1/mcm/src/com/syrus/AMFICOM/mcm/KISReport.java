/*
 * $Id: KISReport.java,v 1.8 2004/07/28 16:02:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.MeasurementObjectTypePool;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.8 $, $Date: 2004/07/28 16:02:00 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class KISReport {
	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	public KISReport(String measurementIdStr,
									 String[] parameterCodenames,
									 byte[][] parameterValues) {
		this.measurementId = new Identifier(measurementIdStr);
		this.parameterCodenames = parameterCodenames;
		this.parameterValues = parameterValues;
	}

	public Result createResult(Measurement measurement) throws IllegalDataException, MeasurementException {
		if (!measurement.getId().equals(this.measurementId))
			throw new IllegalDataException("KISReport | Alien measurement: identifier '" + measurement.getId().toString() + "' != my '" + this.measurementId.toString() + "'");

		try {
			SetParameter[] parameters = new SetParameter[this.parameterCodenames.length];
			ParameterType parameterType;
			for (int i = 0; i < parameters.length; i++) {
				parameterType = (ParameterType)MeasurementObjectTypePool.getObjectType(this.parameterCodenames[i]);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY, 20),
																				 parameterType,
																				 this.parameterValues[i]);
			}

			Result result = measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY, 10),
																							 MeasurementControlModule.iAm.getUserId(),
																							 null,
																							 AlarmLevel.ALARM_LEVEL_NONE,
																							 parameters);
			return result;
		}
		catch (AMFICOMRemoteException are) {
			throw new MeasurementException("Cannot generate identifier: " + are.message, MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, are);
		}
		catch (Exception e) {
			throw new MeasurementException("Cannot create result: " + e.getMessage(), MeasurementException.DATABASE_CALL_FAILED_CODE, e);
		}
	}

	public Result createResult() throws MeasurementException {
		try {
			Measurement measurement = new Measurement(this.measurementId);

			SetParameter[] parameters = new SetParameter[this.parameterCodenames.length];
			ParameterType parameterType;
			for (int i = 0; i < parameters.length; i++) {
				parameterType = (ParameterType)MeasurementObjectTypePool.getObjectType(this.parameterCodenames[i]);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY, 20),
																				 parameterType,
																				 this.parameterValues[i]);
			}

			Result result = measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY, 10),
																							 MeasurementControlModule.iAm.getUserId(),
																							 null,
																							 AlarmLevel.ALARM_LEVEL_NONE,
																							 parameters);
			return result;
		}
		catch (AMFICOMRemoteException are) {
			throw new MeasurementException("Cannot generate identifier: " + are.message, MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, are);
		}
		catch (Exception e) {
			throw new MeasurementException("Cannot create result: " + e.getMessage(), MeasurementException.DATABASE_CALL_FAILED_CODE, e);
		}
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
