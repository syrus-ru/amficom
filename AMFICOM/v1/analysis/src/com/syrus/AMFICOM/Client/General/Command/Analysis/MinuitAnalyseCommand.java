package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.General.Model.Environment;

//import com.syrus.AMFICOM.Client.Analysis.HistoAnalysis.WorkWithReflectoArray;
import com.syrus.AMFICOM.Client.Analysis.MathRef;
import com.syrus.AMFICOM.analysis.AnalysisManager;
//import com.syrus.AMFICOM.analysis.dadara.AnalysResult;
//import com.syrus.AMFICOM.analysis.dadara.EventReader;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;

public class MinuitAnalyseCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private String id;
	private ApplicationContext aContext;
	private Checker checker;


	public MinuitAnalyseCommand(Dispatcher dispatcher, String id,
															ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.id = id;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
		if(field.equals("id"))
			this.id = id;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MinuitAnalyseCommand(dispatcher, id, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.performMINUITanalysis))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs != null)
		{
			Environment.getActiveWindow().setCursor(new Cursor(Cursor.WAIT_CURSOR));

			double delta_x = bs.getDeltaX();
			double[] y = bs.getTraceData();

			double[] params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			if (params == null)
			{
				new AnalysisManager();
				params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			}

		//AnalysResult anaresult = new AnalysResult(y, delta_x);

//			InitialAnalysis ia = new InitialAnalysis(y, delta_x, params[0], params[1], params[2], params[3], params[4], (int)params[7], params[5]);
//			ReflectogramEvent[] ep = ia.performAnalysis();

			ReflectogramEvent[] ep = AnalysisManager.analyseTrace(y, delta_x, params);

			RefAnalysis a = new RefAnalysis();
			a.decode(y, ep);

//			EventReader reader = new EventReader();
			//reader.decode(result);


//			a.events = reader.readEvents();
//			a.noise = reader.readNoise();
//			a.filtered = reader.readFiltered();
//			a.normalyzed = reader.readNormalyzed();
//			a.overallStats = reader.readOverallStats();
//			a.concavities = reader.readConcavities();
			Pool.put("refanalysis", id, a);

			//ReflectogramEvent[] ep = anaresult.ep;
			Pool.put("eventparams", id, ep);

			Environment.getActiveWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
