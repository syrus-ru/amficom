/*
 * $Id: KISReport.java,v 1.56.2.3 2006/03/06 14:15:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;

/**
 * @version $Revision: 1.56.2.3 $, $Date: 2006/03/06 14:15:26 $
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
		final Map<String, Identifier> parameterTypeCodenameIdMap = ParameterType.getCodenameIdentifierMap(this.parameterTypeCodenames);
		for (int i = 0; i < this.parameterTypeCodenames.length; i++) {
			final String parameterTypeCodename = this.parameterTypeCodenames[i];
			final byte[] parameterValue = this.parameterValues[i];

			final Identifier parameterTypeId = parameterTypeCodenameIdMap.get(parameterTypeCodename);
			if (parameterTypeId == null) {
				throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": for '" + parameterTypeCodename);
			}

			final MeasurementResultParameter measurementResultParameter = measurement.createActionResultParameter(LoginManager.getUserId(),
					parameterValue,
					parameterTypeId);
			this.measurementResultParameters.add(measurementResultParameter);
		}
		return this.measurementResultParameters;
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
