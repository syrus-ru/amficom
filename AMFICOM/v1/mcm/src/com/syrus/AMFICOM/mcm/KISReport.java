/*
 * $Id: KISReport.java,v 1.17 2004/10/08 05:58:51 bob Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2004/10/08 05:58:51 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public class KISReport implements ParameterTypeCodenames {
	private static Map outParameterTypeIds;	//Map <String parameterTypeCodename, Identifier parameterId>

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
    	MeasurementDatabaseContext.init(new ParameterTypeDatabase(), null, null, null, null, null, null,
										null, null, null, null, null, null);
		outParameterTypeIds = new HashMap(1);
		addOutParameterTypeId(ParameterTypeCodenames.REFLECTOGRAMMA);
	}

	public KISReport(String measurementIdStr,
									 String[] parameterCodenames,
									 byte[][] parameterValues) {
		this.measurementId = new Identifier(measurementIdStr);
		this.parameterCodenames = parameterCodenames;
		this.parameterValues = parameterValues;
	}

	public Result createResult() throws MeasurementException {
		try {
			Measurement measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(this.measurementId, true);

			SetParameter[] parameters = new SetParameter[this.parameterCodenames.length];
			ParameterType parameterType;
			for (int i = 0; i < parameters.length; i++) {
				parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(this.parameterCodenames[i]), true);
				parameters[i] = new SetParameter(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY_CODE, 20),
																				 parameterType,
																				 this.parameterValues[i]);
			}

			return measurement.createResult(NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10),
																			MeasurementControlModule.iAm.getUserId(),
																			null,
																			AlarmLevel.ALARM_LEVEL_NONE,
																			parameters);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new MeasurementException(ioee.getMessage(), MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, ioee);
		}
		catch (ApplicationException ae) {
			throw new MeasurementException(ae.getMessage(), MeasurementException.DATABASE_CALL_FAILED_CODE, ae);
		}
		catch (AMFICOMRemoteException are) {
			throw new MeasurementException("Cannot generate identifier: " + are.message, MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, are);
		}
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}

	private static void addOutParameterTypeId(String codename) {
		ParameterTypeDatabase parameterTypeDatabase = ((ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase());
		try {
			ParameterType parameterType = parameterTypeDatabase.retrieveForCodename(codename);
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
