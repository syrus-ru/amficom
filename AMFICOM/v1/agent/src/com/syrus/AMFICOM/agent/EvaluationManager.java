package com.syrus.AMFICOM.agent;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;
import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.CORBA.KIS.Result_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Evaluation_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;
import com.syrus.AMFICOM.server.measurement.Result;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/20 12:54:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */
public abstract class EvaluationManager {
	protected Hashtable evaluationresultparameters;
	protected AlarmLevel alarmLevel;

	EvaluationManager() {}

  public static EvaluationManager getEvaluationManager(String evaluation_type_id) {
    if (evaluation_type_id.equals("dadara"))
      return new DadaraEvaluationManager();
    else
      return null;
  }

  public Result_Transferable evaluate(Evaluation_Transferable evaluation, Result_Transferable analysisResult, Result_Transferable result) throws Exception {
    Hashtable thresholds = new Hashtable();
    for (int i = 0; i < evaluation.thresholds.length; i++)
      thresholds.put(evaluation.thresholds[i].name, evaluation.thresholds[i].value);
    Hashtable etalonparameters = new Hashtable();
    for (int i = 0; i < evaluation.etalon.etalon_arguments.length; i++)
      etalonparameters.put(evaluation.etalon.etalon_arguments[i].name, evaluation.etalon.etalon_arguments[i].value);

		Hashtable analysisresultparameters = null;
		if (analysisResult != null) {
			analysisresultparameters = new Hashtable();
			for (int i = 0; i < analysisResult.parameters.length; i++)
				analysisresultparameters.put(analysisResult.parameters[i].name, analysisResult.parameters[i].value);
		}

    Hashtable resultparameters = new Hashtable();
    for (int i = 0; i < result.parameters.length; i++)
      resultparameters.put(result.parameters[i].name, result.parameters[i].value);

    this.evaluate(thresholds, etalonparameters, analysisresultparameters, resultparameters);

    Enumeration names = this.evaluationresultparameters.keys();
    Enumeration values = this.evaluationresultparameters.elements();
    LinkedList ll = new LinkedList();
    String parName;
    while (names.hasMoreElements()) {
      parName = (String)names.nextElement();
      ll.add(new Parameter_Transferable(parName, ParametersDatabase.getEvaluationParameterTypeId(parName, evaluation.evaluation_type_id), (byte[])values.nextElement()));
    }
    Parameter_Transferable[] erparameters = (Parameter_Transferable[])ll.toArray(new Parameter_Transferable[ll.size()]);

    return new Result_Transferable(null,
																	 evaluation.id,
																	 result.test_id,
																	 Result.RESULT_TYPE_EVALUATION,
																	 result.elementary_start_time,
																	 erparameters,
																	 this.alarmLevel);
  }

	public abstract void evaluate(Hashtable thresholds, Hashtable etalonparameters, Hashtable analysisresultparameters, Hashtable resultparameters) throws Exception;
}
