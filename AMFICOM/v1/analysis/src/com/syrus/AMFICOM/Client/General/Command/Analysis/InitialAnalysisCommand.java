package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;

public class InitialAnalysisCommand extends VoidCommand {

	public void execute()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs != null)
		{
			//double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = Heap.getMinuitAnalysisParams();
			if (params == null) {
				new ClientAnalysisManager();
				params = Heap.getMinuitAnalysisParams();
			}

			double[] pars = new double[params.length];
			for (int i = 0; i < params.length; i++)
				pars[i] = params[i];

			ModelTraceAndEventsImpl mtae = CoreAnalysisManager.makeAnalysis(bs, pars);

			// ��������� ����� ��� ����������� ��������������� ���������
//	        ep = AnalysisManager.fitTrace(
//	            y, deltaX, ep, (int)params[6], meanAttenuation[0]);

	        RefAnalysis a = new RefAnalysis();
			a.decode(y, mtae);

			Heap.setRefAnalysisByKey(RefUpdateEvent.PRIMARY_TRACE, a);
			Heap.setMTAEPrimary(mtae);
		}
	}
}
