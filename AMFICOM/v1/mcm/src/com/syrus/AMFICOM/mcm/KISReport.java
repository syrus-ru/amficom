/*
 * $Id: KISReport.java,v 1.56.2.1 2006/03/01 15:51:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;

/**
 * @version $Revision: 1.56.2.1 $, $Date: 2006/03/01 15:51:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class KISReport {
	private Identifier measurementId;
	private String[] parameterTypeCodenames;
	private byte[][] parameterValues;

	private Set<MeasurementResultParameter> measurementResultParameters;

	public KISReport(final String measurementIdStr, final String[] parameterTypeCodenames, final byte[][] parameterValues) {
		this.measurementId = new Identifier(measurementIdStr);
		this.parameterTypeCodenames = parameterTypeCodenames;
		this.parameterValues = parameterValues;
		
		this.measurementResultParameters = null;
	}

	Set<MeasurementResultParameter> getResult() throws ApplicationException {
		if (this.measurementResultParameters != null) {
			return this.measurementResultParameters;
		}

		this.measurementResultParameters = new HashSet<MeasurementResultParameter>();
		final Measurement measurement = StorableObjectPool.getStorableObject(this.measurementId, true);

		if (this.parameterTypeCodenames.length > 1) {
			final Set<TypicalCondition> parameterTypeCodenameConditions = new HashSet<TypicalCondition>();
			for (final String parameterTypeCodename : this.parameterTypeCodenames) {
				parameterTypeCodenameConditions.add(new TypicalCondition(parameterTypeCodename,
						OPERATION_EQUALS,
						PARAMETER_TYPE_CODE,
						COLUMN_CODENAME));
			}
			final CompoundCondition parameterTypeCondition = new CompoundCondition(parameterTypeCodenameConditions, OR);
			final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(parameterTypeCondition, true);
			final Map<String, Identifier> codenameParameterTypeMap = new HashMap<String, Identifier>();
			for (final ParameterType parameterType : parameterTypes) {
				codenameParameterTypeMap.put(parameterType.getCodename(), parameterType.getId());
			}

			for (int i = 0; i < this.parameterTypeCodenames.length; i++) {
				final String parameterTypeCodename = this.parameterTypeCodenames[i];
				final Identifier parameterTypeId = codenameParameterTypeMap.get(parameterTypeCodename);
				if (parameterTypeId == null) {
					throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": for '" + parameterTypeCodename);
				}

				final MeasurementResultParameter measurementResultParameter = measurement.createActionResultParameter(LoginManager.getUserId(),
						this.parameterValues[i],
						parameterTypeId);
				this.measurementResultParameters.add(measurementResultParameter);
			}
		} else if (this.parameterTypeCodenames.length == 1) {
			final String parameterTypeCodename = this.parameterTypeCodenames[0];
			final TypicalCondition parameterTypeCondition = new TypicalCondition(parameterTypeCodename,
					OPERATION_EQUALS,
					PARAMETER_TYPE_CODE,
					COLUMN_CODENAME);
			final Set<Identifier> parameterTypeIds = StorableObjectPool.getIdentifiersByCondition(parameterTypeCondition, true);
			if (parameterTypeIds.isEmpty()) {
				throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": for '" + parameterTypeCodename);
			}
			assert parameterTypeIds.size() == 1 : ONLY_ONE_EXPECTED;

			final Identifier parameterTypeId = parameterTypeIds.iterator().next();
			final MeasurementResultParameter measurementResultParameter = measurement.createActionResultParameter(LoginManager.getUserId(),
					this.parameterValues[0],
					parameterTypeId);
			this.measurementResultParameters.add(measurementResultParameter);
		} else {
			throw new ObjectNotFoundException("No result parameters for measurement '" + this.measurementId + "'");
		}
		return this.measurementResultParameters;
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
