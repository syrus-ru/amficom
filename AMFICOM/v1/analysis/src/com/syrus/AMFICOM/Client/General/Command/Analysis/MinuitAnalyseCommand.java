package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Map;
import java.awt.Cursor;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.analysis.AnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.io.BellcoreStructure;

public class MinuitAnalyseCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private String id;
	private ApplicationContext aContext;


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
		return new MinuitAnalyseCommand(dispatcher, id, aContext);
	}

	public void execute()
	{
		long t0 = System.currentTimeMillis();
    bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs != null)
		{
			Environment.getActiveWindow().setCursor(new Cursor(Cursor.WAIT_CURSOR));

			double delta_x = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			if (params == null)
			{
        System.out.println("MinuitAnalysis.execute(): create AnalysisManager at dt/ms " + (System.currentTimeMillis()-t0));
				new AnalysisManager();
        System.out.println("MinuitAnalysis.execute(): AnalysisManager created at dt/ms " + (System.currentTimeMillis()-t0));
				params = (double[])Pool.get("analysisparameters", "minuitanalysis");
			}
			
			Map tracesMap = (Map )Pool.get("bellcoremap", "current");

			int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
			int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
					y,
					1000,
					bs.getIOR(),
					delta_x);
			if (nReflSize > 3 * reflSize / 5)
				nReflSize = 3 * reflSize / 5;

      //System.out.println("MinuitAnalysis.execute(): starting analyseTrace+fitTrace at dt/ms " + (System.currentTimeMillis()-t0));

      double[] meanAttenuation = { 0 };

      ReflectogramEvent[] ep = AnalysisManager.makeAnalysis(
          (int)params[6], bs, params, meanAttenuation, reflSize, nReflSize,
          tracesMap);

      //for (int i = 0; i < 2; i++) // FIXIT
      //ep = AnalysisManager.fitTrace(
      //   y, delta_x, ep, (int)params[6], meanAttenuation[0]);

      //System.out.println("MinuitAnalysis.execute(): completed analyseTrace+fitTrace at dt/ms " + (System.currentTimeMillis()-t0));

			RefAnalysis a = new RefAnalysis();
      //System.out.println("MinuitAnalysis.execute(): new RefAnalysis complete at dt/ms " + (System.currentTimeMillis()-t0));
			a.decode(y, ep);
      //System.out.println("MinuitAnalysis.execute(): decode complete at dt/ms " + (System.currentTimeMillis()-t0));

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
      //System.out.println("MinuitAnalysis.execute(): pool & Cursor complete at dt/ms " + (System.currentTimeMillis()-t0));
		}
	}
}
