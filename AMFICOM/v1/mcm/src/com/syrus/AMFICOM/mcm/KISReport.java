/*
 * $Id: KISReport.java,v 1.11 2004/08/16 10:48:22 arseniy Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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
 * @version $Revision: 1.11 $, $Date: 2004/08/16 10:48:22 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class KISReport {
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
	private static Map outParameterTypeIds;	//Map <String parameterTypeCodename, Identifier parameterId>

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
	System.out.println("---------------- KISReport -----------------");
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

	public Result createResult() throws MeasurementException {
		Measurement measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(this.measurementId, true);

		SetParameter[] parameters = new SetParameter[this.parameterCodenames.length];
		try {
			ParameterType parameterType;
			for (int i = 0; i < parameters.length; i++) {
				parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(this.parameterCodenames[i]), true);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY_CODE, 20),
																					 parameterType,
																					 this.parameterValues[i]);
			}
		}
		catch (IllegalObjectEntityException ioee) {
			throw new MeasurementException(ioee.getMessage(), MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, ioee);
		}
		catch (AMFICOMRemoteException are) {
			throw new MeasurementException(are.message, MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, are);
		}

		try {
			return measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10),
																			MeasurementControlModule.iAm.getUserId(),
																			null,
																			AlarmLevel.ALARM_LEVEL_NONE,
																			parameters);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new MeasurementException(ioee.getMessage(), MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, ioee);
		}
		catch (AMFICOMRemoteException are) {
			throw new MeasurementException("Cannot generate identifier: " + are.message, MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, are);
		}
		catch (CreateObjectException e) {
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
