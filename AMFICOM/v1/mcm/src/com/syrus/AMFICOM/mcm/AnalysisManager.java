package com.syrus.AMFICOM.mcm;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.LinkedList;
import com.syrus.AMFICOM.analysis.dadara.DadaraAnalysisManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

public abstract class AnalysisManager {
	private static final Identifier ANALYSIS_TYPE_DADARA = new Identifier("dadara");

  public static AnalysisManager getAnalysisManager(Identifier analysis_type_id) {
    if (analysis_type_id.equals(ANALYSIS_TYPE_DADARA))
      return new DadaraAnalysisManager();
    else
      return null;
  }

  public Result analyse(Analysis analysis, Result measurementResult) {
		SetParameter[] criteria = analysis.getCriteriaSet().getParameters();
		Hashtable hcriteria = new Hashtable(criteria.length);
		for (int i = 0; i < criteria.length; i++)
			hcriteria.put(criteria[i].getTypeId(), criteria[i].getValue());
		SetParameter[] measurementResultParameters = measurementResult.getParameters();
		Hashtable hmeasurementResultParameters = new Hashtable(measurementResultParameters.length);
		for (int i = 0; i < measurementResultParameters.length; i++)
			hmeasurementResultParameters.put(measurementResultParameters[i].getTypeId(), measurementResultParameters[i].getValue());

		Hashtable hanalysisResultParameters = this.analyse(hcriteria, hmeasurementResultParameters);

		LinkedList ids = new LinkedList();
		LinkedList type_ids = new LinkedList();
		LinkedList values = new LinkedList();
		Enumeration enames = hanalysisResultParameters.keys();
    Enumeration evalues = hanalysisResultParameters.elements();
		while (enames.hasMoreElements()) {
			ids.add(MeasurementControlModule.createIdentifier("parameter"));
			type_ids.add(enames.nextElement());
			values.add(evalues.nextElement());
		}
		Result result = null;
		try {
			result = analysis.createResult(MeasurementControlModule.createIdentifier("result"),
																		 measurementResult.getMeasurement(),
																		 AlarmLevel.ALARM_LEVEL_NONE,
																		 (Identifier[])ids.toArray(new String[ids.size()]),
																		 (Identifier[])type_ids.toArray(new String[type_ids.size()]),
																		 (byte[][])values.toArray(new byte[values.size()][]));
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		return result;
  }

  public abstract Hashtable analyse(Hashtable hcriteria, Hashtable hmeasurementResultParameters);
}