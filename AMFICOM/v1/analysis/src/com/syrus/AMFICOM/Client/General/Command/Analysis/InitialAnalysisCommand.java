package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Map;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
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
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		if (bs != null)
		{
			//double delta_x = bs.getResolution();
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
			if (pars[6] > 1) //убрать фитировку
				pars[6] = 1;

			ReflectogramEvent[] ep = ClientAnalysisManager.makeAnalysis(
					0, bs, pars, tracesMap);

			// фитировка нужна для определения вспомогательных парметров
//	        ep = AnalysisManager.fitTrace(
//	            y, delta_x, ep, (int)params[6], meanAttenuation[0]);

	        RefAnalysis a = new RefAnalysis();
			a.decode(y, ep);

			Pool.put("refanalysis", "primarytrace", a);
			//Pool.remove("eventparams", "primarytrace");
			Pool.put("eventparams", "primarytrace", ep);
		}
	}

}

