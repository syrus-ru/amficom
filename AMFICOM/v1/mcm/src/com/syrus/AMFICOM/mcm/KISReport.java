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

//	public Result createResult(Measurement measurement) throws Exception {
//		if (!measurement.getId().equals(this.measurement_id))
//			throw new Exception("KISReport | Alien measurement: identifier '" + measurement.getId().toString() + "' != my '" + this.measurement_id.toString() + "'");
//
//		Identifier[] parameter_ids = new Identifier[this.parameter_type_ids.length];
//		for (int i = 0; i < parameter_ids.length; i++)
//			parameter_ids[i] = MeasurementControlModule.createIdentifier("parameter");
//
//		Result result = measurement.createResult(MeasurementControlModule.createIdentifier("result"),
//																						 null,
//																						 AlarmLevel.ALARM_LEVEL_NONE,
//																						 parameter_ids,
//																						 this.parameter_type_ids,
//																						 this.parameter_values);
//		return result;
//	}
//
//	public Result createResult() throws Exception {
//		Measurement measurement = new Measurement(this.measurement_id);
//
//		Identifier[] parameter_ids = new Identifier[this.parameter_type_ids.length];
//		for (int i = 0; i < parameter_ids.length; i++)
//			parameter_ids[i] = MeasurementControlModule.createIdentifier("parameter");
//
//		Result result = measurement.createResult(MeasurementControlModule.createIdentifier("result"),
//																						 null,
//																						 AlarmLevel.ALARM_LEVEL_NONE,
//																						 parameter_ids,
//																						 this.parameter_type_ids,
//																						 this.parameter_values);
//		return result;
//	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
}