/*
 * $Id: KISReport.java,v 1.36 2005/06/04 16:56:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.36 $, $Date: 2005/06/04 16:56:18 $
 * @author $Author: bass $
 * @module mcm_v1
 */

public class KISReport {
	private static final Map OUT_PARAMETER_TYPE_IDS_MAP;	//Map <String parameterTypeCodename, Identifier parameterTypeId>

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
		OUT_PARAMETER_TYPE_IDS_MAP = new HashMap(1);
		addOutParameterTypeIds(new String[] {ParameterTypeCodenames.REFLECTOGRAMMA});
	}

	private static void addOutParameterTypeIds(String[] codenames) {
		assert codenames != null : ErrorMessages.NON_NULL_EXPECTED;
		assert codenames.length > 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final java.util.Set typicalConditions = new HashSet(codenames.length);
		for (int i = 0; i < codenames.length; i++) {
			typicalConditions.add(new TypicalCondition(codenames[i],
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}

		try {
			final StorableObjectCondition condition;
			if (typicalConditions.size() == 1)
				condition = (StorableObjectCondition) typicalConditions.iterator().next();
			else
				condition = new CompoundCondition(typicalConditions, CompoundConditionSort.OR);
			final java.util.Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (final Iterator it = parameterTypes.iterator(); it.hasNext();) {
				final ParameterType parameterType = (ParameterType) it.next();
				OUT_PARAMETER_TYPE_IDS_MAP.put(parameterType.getCodename(), parameterType.getId());
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
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
			final Measurement measurement = (Measurement) StorableObjectPool.getStorableObject(this.measurementId, true);

			final SetParameter[] parameters = new SetParameter[this.parameterCodenames.length];
			for (int i = 0; i < parameters.length; i++) {
				final Identifier parameterTypeId = (Identifier) OUT_PARAMETER_TYPE_IDS_MAP.get(this.parameterCodenames[i]);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				parameters[i] = SetParameter.createInstance(parameterType, this.parameterValues[i]);
			}

			Result result = measurement.createResult(LoginManager.getUserId(), parameters);
			StorableObjectPool.flush(result, true);
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
}
