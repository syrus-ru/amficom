/*
 * $Id: KISReport.java,v 1.54 2005/09/20 23:18:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;

/**
 * @version $Revision: 1.54 $, $Date: 2005/09/20 23:18:44 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class KISReport {
	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

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
				parameters[i] = Parameter.createInstance(ParameterType.REFLECTOGRAMMA, this.parameterValues[i]);
			}

			final Result result = measurement.createResult(LoginManager.getUserId(), parameters);
			StorableObjectPool.flush(result, LoginManager.getUserId(), false);
			return result;
		}
		catch (ApplicationException ae) {
			if (ae.getCause() instanceof IllegalObjectEntityException) {
				throw new MeasurementException(ae.getMessage(), MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE, ae);
			}
			if (ae instanceof CommunicationException) {
				throw new MeasurementException(ae.getMessage(), MeasurementException.COMMUNICATION_FAILED_CODE, ae);
			}
			throw new MeasurementException(ae.getMessage(), MeasurementException.DATABASE_CALL_FAILED_CODE, ae);
		}
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
