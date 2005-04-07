package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Map;
import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.io.BellcoreStructure;

public class MinuitAnalyseCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public MinuitAnalyseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
		// strange code:
		//if(field.equals("id"))
		//	this.id = id;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MinuitAnalyseCommand(dispatcher, aContext);
	}

	public void execute()
	{
		long t0 = System.currentTimeMillis();
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs != null)
		{
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			//double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = Heap.getMinuitAnalysisParams();
			if (params == null)
			{
		System.out.println("MinuitAnalysis.execute(): create AnalysisManager at dt/ms " + (System.currentTimeMillis()-t0));
				new ClientAnalysisManager();
		System.out.println("MinuitAnalysis.execute(): AnalysisManager created at dt/ms " + (System.currentTimeMillis()-t0));
				params = Heap.getMinuitAnalysisParams();
			}

			Map tracesMap = Heap.getBsBellCoreMap();

	  ModelTraceManager mtm = CoreAnalysisManager.makeAnalysis(
		  bs, params, tracesMap);

	  //for (int i = 0; i < 2; i++) // FIXIT
	  //ep = AnalysisManager.fitTrace(
	  //   y, deltaX, ep, (int)params[6], meanAttenuation[0]);

	  //System.out.println("MinuitAnalysis.execute(): completed analyseTrace+fitTrace at dt/ms " + (System.currentTimeMillis()-t0));

			RefAnalysis a = new RefAnalysis();
	  //System.out.println("MinuitAnalysis.execute(): new RefAnalysis complete at dt/ms " + (System.currentTimeMillis()-t0));
			a.decode(y, mtm);
	  //System.out.println("MinuitAnalysis.execute(): decode complete at dt/ms " + (System.currentTimeMillis()-t0));

			Heap.setRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY, a);
			Heap.setMTMPrimary(mtm);
			Heap.setMTMEtalon(mtm);

			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	  //System.out.println("MinuitAnalysis.execute(): pool & Cursor complete at dt/ms " + (System.currentTimeMillis()-t0));
		}
	}
}
