package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.AnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class InitialAnalysisCommand extends VoidCommand
{
	public InitialAnalysisCommand()
	{
	}

	public void execute()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		if (bs != null)
		{
			double delta_x = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			if (params == null)
			{
				new AnalysisManager();
				params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			}

			double[] pars = new double[params.length];
			for (int i = 0; i < params.length; i++)
				pars[i] = params[i];
			if (pars[6] > 1)  //убрать фитировку
				pars[6] = 1;


			int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
			int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
					y,
					1000,
					bs.getIOR(),
					delta_x);
			if (nReflSize > 3 * reflSize / 5)
				nReflSize = 3 * reflSize / 5;

	/*		InitialAnalysis ia = new InitialAnalysis(
					y,
					delta_x,
					pars[0],
					pars[1],
					pars[2],
					pars[3],
					pars[4],
					(int)pars[7],
					pars[5],
					reflSize,
					nReflSize);
			ReflectogramEvent[] ep = ia.performAnalysis();
			ep = AnalysisManager.fitTrace(
					y,
					delta_x,
					ep,
					(int)pars[6],
					ia.getMeanAttenuation());*/
//
		 ReflectogramEvent[] ep = AnalysisManager.analyseTrace(y, delta_x, pars, reflSize, nReflSize);

			RefAnalysis a = new RefAnalysis();
			a.decode(y, ep);

			Pool.put("refanalysis", "primarytrace", a);
			//Pool.remove("eventparams", "primarytrace");
			Pool.put("eventparams", "primarytrace", ep);
		}
	}

}

