/*
 * $Id: KISReport.java,v 1.9 2004/08/12 13:35:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.HashMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/12 13:35:08 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class KISReport {
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
	private static Map outParameterTypeIds;

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
		outParameterTypeIds = new HashMap(1);
		addOutParameterTypeId(CODENAME_REFLECTOGRAMMA);
	}

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
				parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(this.parameterCodenames[i]), true);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY_CODE, 20),
																				 parameterType,
																				 this.parameterValues[i]);
			}

			Result result = measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10),
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
				parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(this.parameterCodenames[i]), true);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY_CODE, 20),
																				 parameterType,
																				 this.parameterValues[i]);
			}

			Result result = measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10),
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

	private static void addOutParameterTypeId(String codename) {
		try {
			ParameterType parameterType = ParameterTypeDatabase.retrieveForCodename(codename);
			Identifier id = parameterType.getId();
			if (! outParameterTypeIds.containsKey(codename)) {
				outParameterTypeIds.put(codename, id);
				MeasurementStorableObjectPool.putStorableObject(parameterType);
			}
			else
				Log.errorMessage("Out parameter type of codename '" + codename + "' already added to map; id: '" + parameterType.getId() + "'");
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}
}
