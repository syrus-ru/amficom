package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class KISReport {
	private Identifier measurementId;
	private Identifier[] parameterTypeIds;
	private byte[][] parameterValues;

	public KISReport(String measurementIdStr,
									 String[] parameterTypeIdsStr,
									 byte[][] parameterValues) {
		this.measurementId = new Identifier(measurementIdStr);
		this.parameterTypeIds = new Identifier[parameterTypeIdsStr.length];
		for (int i = 0; i < this.parameterTypeIds.length; i++)
			this.parameterTypeIds[i] = new Identifier(parameterTypeIdsStr[i]);
		this.parameterValues = parameterValues;
	}

	public Result createResult(Measurement measurement) throws Exception {
		if (!measurement.getId().equals(this.measurementId))
			throw new Exception("KISReport | Alien measurement: identifier '" + measurement.getId().toString() + "' != my '" + this.measurementId.toString() + "'");

		Identifier[] parameterIds = new Identifier[this.parameterTypeIds.length];
		for (int i = 0; i < parameterIds.length; i++)
			parameterIds[i] = MeasurementControlModule.createIdentifier("parameter");

		Result result = measurement.createResult(MeasurementControlModule.createIdentifier("result"),
																						 MeasurementControlModule.iAm.getUserId(),
																						 null,
																						 AlarmLevel.ALARM_LEVEL_NONE,
																						 parameterIds,
																						 this.parameterTypeIds,
																						 this.parameterValues);
		return result;
	}

	public Result createResult() throws Exception {
		Measurement measurement = new Measurement(this.measurementId);

		Identifier[] parameterIds = new Identifier[this.parameterTypeIds.length];
		for (int i = 0; i < parameterIds.length; i++)
			parameterIds[i] = MeasurementControlModule.createIdentifier("parameter");

		Result result = measurement.createResult(MeasurementControlModule.createIdentifier("result"),
																						 MeasurementControlModule.iAm.getUserId(),
																						 null,
																						 AlarmLevel.ALARM_LEVEL_NONE,
																						 parameterIds,
																						 this.parameterTypeIds,
																						 this.parameterValues);
		return result;
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}
