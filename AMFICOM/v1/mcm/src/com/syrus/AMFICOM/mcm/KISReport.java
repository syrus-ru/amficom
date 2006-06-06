/*
 * $Id: KISReport.java,v 1.57 2006/06/06 11:29:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;

/**
 * @version $Revision: 1.57 $, $Date: 2006/06/06 11:29:12 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class KISReport {
	private Identifier measurementId;
	private String[] parameterCodenames;
	private byte[][] parameterValues;

	private Result result;

	public KISReport(final String measurementIdStr, final String[] parameterCodenames, final byte[][] parameterValues) {
		this.measurementId = Identifier.valueOf(measurementIdStr);
		this.parameterCodenames = parameterCodenames;
		this.parameterValues = parameterValues;
		
		this.result = null;
	}

	Result getResult() throws ApplicationException {
		if (this.result != null) {
			return this.result;
		}

		final Measurement measurement = StorableObjectPool.getStorableObject(this.measurementId, true);

		final Parameter[] parameters = new Parameter[this.parameterCodenames.length];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = Parameter.createInstance(ParameterType.REFLECTOGRAMMA, this.parameterValues[i]);
		}

		this.result = measurement.createResult(LoginManager.getUserId(), parameters);
		return this.result;
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
