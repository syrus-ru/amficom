/*
 * $Id: AnalysisManager.java,v 1.2 2004/06/21 14:56:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.CORBA.KIS.*;
import com.syrus.AMFICOM.analysis.dadara.DadaraAnalysisManager;
import com.syrus.AMFICOM.server.measurement.Result;
import java.util.*;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/21 14:56:29 $
 * @author $Author: bass $
 * @module agent_v1
 */
public abstract class AnalysisManager {
	public static AnalysisManager getAnalysisManager(String analysis_type_id) {
		if (analysis_type_id.equals("dadara"))
			return new DadaraAnalysisManager();
		else
			return null;
	}

  public Result_Transferable analyse(Analysis_Transferable analysis, Result_Transferable result, Etalon_Transferable etalon) {
    Hashtable criterias = new Hashtable();
    for (int i = 0; i < analysis.criterias.length; i++)
      criterias.put(analysis.criterias[i].name, analysis.criterias[i].value);
    Hashtable resultparameters = new Hashtable();
    for (int i = 0; i < result.parameters.length; i++)
      resultparameters.put(result.parameters[i].name, result.parameters[i].value);

		Hashtable etalonparameters = new Hashtable();
    for (int i = 0; i < etalon.etalon_arguments.length; i++)
      etalonparameters.put(etalon.etalon_arguments[i].name, etalon.etalon_arguments[i].value);

    Hashtable analysisresultparameters = this.analyse(criterias, resultparameters, etalonparameters);

    Enumeration names = analysisresultparameters.keys();
    Enumeration values = analysisresultparameters.elements();
    LinkedList ll = new LinkedList();
    String par_name;
    while (names.hasMoreElements()) {
      par_name = (String)names.nextElement();
      ll.add(new Parameter_Transferable(par_name, ParametersDatabase.getAnalysisParameterTypeId(par_name, analysis.analysis_type_id), (byte[])values.nextElement()));
    }
    Parameter_Transferable[] arparameters = new Parameter_Transferable[ll.size()];
    arparameters = (Parameter_Transferable[])ll.toArray(arparameters);
    return new Result_Transferable(null,
                                   analysis.id,
																	 result.test_id,
                                   Result.RESULT_TYPE_ANALYSIS,
                                   result.elementary_start_time,
                                   arparameters,
																	 AlarmLevel.ALARM_LEVEL_NONE);
  }

	public abstract Hashtable analyse(Hashtable criterias, Hashtable resultparameters, Hashtable etalonparameters);
}
