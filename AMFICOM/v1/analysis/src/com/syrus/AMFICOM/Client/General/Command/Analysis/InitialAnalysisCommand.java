package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Map;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class InitialAnalysisCommand extends VoidCommand {
	private static final String OT_analysisparameters = "analysisparameters";
	private static final String OID_minuitanalysis = "minuitanalysis";
	private static final String OID_minuitinitials = "minuitinitials";
	private static final String OID_minuitdefaults = "minuitdefaults";

	public InitialAnalysisCommand() {
	}

	public void execute()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", RefUpdateEvent.PRIMARY_TRACE);
		if (bs != null)
		{
			//double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = (double[]) Pool.get(OT_analysisparameters,
					OID_minuitanalysis);
			if (params == null) {
				new ClientAnalysisManager();
				params = (double[]) Pool.get(OT_analysisparameters,
						OID_minuitanalysis);
			}

			Map tracesMap = (Map )Pool.get("bellcoremap", "current");

			double[] pars = new double[params.length];
			for (int i = 0; i < params.length; i++)
				pars[i] = params[i];
			if (pars[6] > 1) //������ ���������
				pars[6] = 1;

			ModelTraceManager mtm = CoreAnalysisManager.makeAnalysis(
					0, bs, pars, tracesMap);

			// ��������� ����� ��� ����������� ��������������� ���������
//	        ep = AnalysisManager.fitTrace(
//	            y, deltaX, ep, (int)params[6], meanAttenuation[0]);

	        RefAnalysis a = new RefAnalysis();
			a.decode(y, mtm);

			Pool.put("refanalysis", RefUpdateEvent.PRIMARY_TRACE, a);
			//Pool.remove("eventparams", RefUpdateEvent.PRIMARY_TRACE);
			Pool.put(ModelTraceManager.CODENAME, RefUpdateEvent.PRIMARY_TRACE, mtm);
		}
	}
}
