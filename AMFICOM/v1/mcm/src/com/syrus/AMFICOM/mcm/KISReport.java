/*
 * $Id: KISReport.java,v 1.22 2004/12/15 14:09:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.HashMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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
 * @version $Revision: 1.22 $, $Date: 2004/12/15 14:09:13 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class KISReport implements ParameterTypeCodenames {
	private static Map outParameterTypeIds;	//Map <String parameterTypeCodename, Identifier parameterId>

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
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
				parameters[i] = SetParameter.createInstance(parameterType, this.parameterValues[i]);
			}

			Result result = measurement.createResult(MeasurementControlModule.iAm.getUserId(),
																			null,
																			AlarmLevel.ALARM_LEVEL_NONE,							
																			parameters);
			result.insert();
			return result;
		}
		catch (ApplicationException ae) {
			if (ae.getCause() instanceof IllegalObjectEntityException)
				throw new MeasurementException(ae.getMessage(), MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, ae);
			throw new MeasurementException(ae.getMessage(), MeasurementException.DATABASE_CALL_FAILED_CODE, ae);
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
