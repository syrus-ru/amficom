/*
 * $Id: KISReport.java,v 1.26 2005/02/15 15:08:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/02/15 15:08:26 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class KISReport {
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
				parameterType = (ParameterType)GeneralStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(this.parameterCodenames[i]), true);
				parameters[i] = SetParameter.createInstance(parameterType, this.parameterValues[i]);
			}

			Result result = measurement.createResult(MeasurementControlModule.iAm.getUserId(), parameters);
			MeasurementDatabaseContext.getResultDatabase().insert(result);
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
		ParameterTypeDatabase parameterTypeDatabase = ((ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase());
		try {
			TypicalCondition tc = new TypicalCondition(codename,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			Collection collection = parameterTypeDatabase.retrieveByCondition(null, tc);
			if (collection != null || !collection.isEmpty()) {
				ParameterType parameterType = (ParameterType) collection.iterator().next();
				Identifier id = parameterType.getId();
				if (!outParameterTypeIds.containsKey(codename)) {
					outParameterTypeIds.put(codename, id);
					GeneralStorableObjectPool.putStorableObject(parameterType);
				}
				else
					Log.errorMessage("Out parameter type of codename '" + codename
							+ "' already added to map; id: '" + parameterType.getId() + "'");
			}
			else
				Log.errorMessage("Out parameter type of codename '" + codename + "' not found");
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}
}
