package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Collection;
import java.util.HashSet;
import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.io.BellcoreStructure;

public class MinuitAnalyseCommand extends VoidCommand
{
	private Dispatcher dispatcher;

	private ApplicationContext aContext;

	public MinuitAnalyseCommand(Dispatcher dispatcher,
			ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if (field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
		// strange code:
		// if(field.equals("id"))
		// this.id = id;
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
			Environment.getActiveWindow().setCursor(
				Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// double deltaX = bs.getResolution();
			double[] y = bs.getTraceData();

			double[] params = Heap.getMinuitAnalysisParams();
			if (params == null)
			{
				System.out.println("MinuitAnalysis.execute(): create AnalysisManager at dt/ms "
						+ (System.currentTimeMillis() - t0));
				new ClientAnalysisManager();
				System.out.println("MinuitAnalysis.execute(): AnalysisManager created at dt/ms "
						+ (System.currentTimeMillis() - t0));
				params = Heap.getMinuitAnalysisParams();
			}

			// XXX: сначала проводим анализ для одной primary trace, а потом, независимо от этого, по всему набору р/г - для создания эталона
			
			// создаем анализ для primary trace
			// XXX: если InitialAnalysisCommand уже выполнен, то в принципе не нужно (если кнопки "IA" и "MA" будут отдельно)

			ModelTraceAndEventsImpl mtaePri = CoreAnalysisManager.makeAnalysis(bs, params);

			RefAnalysis a = new RefAnalysis();
			a.decode(y, mtaePri);

			Heap.setRefAnalysisByKey(Heap.PRIMARY_TRACE_KEY, a);
			Heap.setMTAEPrimary(mtaePri);

			// проводим анализ для всего набора р/г

			Collection bsColl = Heap.getBsBellCoreMap().values();
			if (bsColl == null) // случится только в странной ситуация - bsBellCoreMap пуст. Вероятно, это будет значить ошибку в коде
			{
				bsColl = new HashSet(1);
				bsColl.add(bs);
			}

			ModelTraceManager mtm;
			try {
				mtm = CoreAnalysisManager.makeEtalon(bsColl, params);
			} catch (IncompatibleTracesException e){
				GUIUtil.showErrorMessage("incompatibleTraces");
				return;
			}

			Heap.setMTMEtalon(mtm);

			Environment.getActiveWindow().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
