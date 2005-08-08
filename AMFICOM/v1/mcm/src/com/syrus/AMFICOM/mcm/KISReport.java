/*
 * $Id: KISReport.java,v 1.48 2005/08/08 11:46:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.48 $, $Date: 2005/08/08 11:46:55 $
 * @author $Author: arseniy $
 * @module mcm
 */

public class KISReport {
	private static final Map<String, Identifier> OUT_PARAMETER_TYPE_IDS_MAP;	//Map <String parameterTypeCodename, Identifier parameterTypeId>

	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	static {
		OUT_PARAMETER_TYPE_IDS_MAP = new HashMap<String, Identifier>(1);
		addOutParameterTypeIds(new ParameterTypeCodename[] {ParameterTypeCodename.REFLECTOGRAMMA});
	}

	private static void addOutParameterTypeIds(final ParameterTypeCodename[] codenames) {
		assert codenames != null : ErrorMessages.NON_NULL_EXPECTED;
		assert codenames.length > 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final Set<StorableObjectCondition> typicalConditions = new HashSet<StorableObjectCondition>(codenames.length);
		for (int i = 0; i < codenames.length; i++) {
			typicalConditions.add(new TypicalCondition(codenames[i].stringValue(),
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETER_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}

		try {
			final StorableObjectCondition condition;
			if (typicalConditions.size() == 1) {
				condition = typicalConditions.iterator().next();
			}
			else {
				condition = new CompoundCondition(typicalConditions, CompoundConditionSort.OR);
			}
			final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true, true);
			for (final ParameterType parameterType : parameterTypes) {
				OUT_PARAMETER_TYPE_IDS_MAP.put(parameterType.getCodename(), parameterType.getId());
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	public KISReport(final String measurementIdStr, final String[] parameterCodenames, final byte[][] parameterValues) {
		this.measurementId = new Identifier(measurementIdStr);
		this.parameterCodenames = parameterCodenames;
		this.parameterValues = parameterValues;
	}

	public Result createResult() throws MeasurementException {
		try {
			final Measurement measurement = (Measurement) StorableObjectPool.getStorableObject(this.measurementId, true);

			final Parameter[] parameters = new Parameter[this.parameterCodenames.length];
			for (int i = 0; i < parameters.length; i++) {
				final Identifier parameterTypeId = OUT_PARAMETER_TYPE_IDS_MAP.get(this.parameterCodenames[i]);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				parameters[i] = Parameter.createInstance(parameterType, this.parameterValues[i]);
			}

			final Result result = measurement.createResult(LoginManager.getUserId(), parameters);
			StorableObjectPool.flush(result, LoginManager.getUserId(), false);
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
