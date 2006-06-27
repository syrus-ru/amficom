package com.syrus.AMFICOM.mcm;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.LinkedList;
import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.analysis.dadara.DadaraEvaluationManager;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.mcm.MeasurementControlModule;
import com.syrus.util.Log;


public abstract class EvaluationManager {
	private static final Identifier EVALUATION_TYPE_DADARA = new Identifier("dadara");

	protected Hashtable hevaluationResultParameters;
	protected AlarmLevel alarmLevel;

  public static EvaluationManager getEvaluationManager(Identifier evaluation_type_id) {
    if (evaluation_type_id.equals(EVALUATION_TYPE_DADARA))
      return new DadaraEvaluationManager();
    else
      return null;
  }

  public Result evaluate(Evaluation evaluation, Result analysisResult, Result measurementResult) {
		SetParameter[] thresholds = evaluation.getThresholdSet().getParameters();
		Hashtable hthresholds = new Hashtable(thresholds.length);
		for (int i = 0; i < thresholds.length; i++)
			hthresholds.put(thresholds[i].getTypeId(), thresholds[i].getValue());
		
		SetParameter[] etalon = evaluation.getEtalon().getParameters();
		Hashtable hetalon = new Hashtable(etalon.length);
		for (int i = 0; i < etalon.length; i++)
			hetalon.put(etalon[i].getTypeId(), etalon[i].getValue());

		Hashtable hanalysisResultParameters = null;
		if (analysisResult != null) {
			SetParameter[] analysisResultParameters = analysisResult.getParameters();
			hanalysisResultParameters = new Hashtable(analysisResultParameters.length);
			for (int i = 0; i < analysisResultParameters.length; i++)
				hanalysisResultParameters.put(analysisResultParameters[i].getTypeId(), analysisResultParameters[i].getValue());
		}
		
		SetParameter[] measurementResultParameters = measurementResult.getParameters();
		Hashtable hmeasurementResultParameters = new Hashtable(measurementResultParameters.length);
		for (int i = 0; i < measurementResultParameters.length; i++)
			hmeasurementResultParameters.put(measurementResultParameters[i].getTypeId(), measurementResultParameters[i].getValue());

		this.evaluate(hthresholds, hetalon, hanalysisResultParameters, hmeasurementResultParameters);

		LinkedList ids = new LinkedList();
		LinkedList type_ids = new LinkedList();
		LinkedList values = new LinkedList();
		Enumeration enames = this.hevaluationResultParameters.keys();
    Enumeration evalues = this.hevaluationResultParameters.elements();
		while (enames.hasMoreElements()) {
			ids.add(MeasurementControlModule.createIdentifier("parameter"));
			type_ids.add(enames.nextElement());
			values.add(evalues.nextElement());
		}
		Result result = null;
		try {
			result = evaluation.createResult(MeasurementControlModule.createIdentifier("result"),
																			 measurementResult.getMeasurement(),
																			 this.alarmLevel,
																			 (Identifier[])ids.toArray(new String[ids.size()]),
																			 (Identifier[])type_ids.toArray(new String[type_ids.size()]),
																			 (byte[][])values.toArray(new byte[values.size()][]));
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		 return result;
  }

  public abstract void evaluate(Hashtable thresholds, Hashtable etalonparameters, Hashtable analysisresultparameters, Hashtable resultparameters);
}