package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import javax.swing.JFrame;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.*;

public class LoadTraceFromDatabaseCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public LoadTraceFromDatabaseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
			if(field.equals("aContext"))
				setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new LoadTraceFromDatabaseCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			Checker checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(Checker.loadReflectogrammFromDB))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		ReflectogrammLoadDialog dialog;
		JFrame parent = Environment.getActiveWindow();
		if(Heap.getRLDialogByKey(parent.getName()) != null)
		{
			dialog = Heap.getRLDialogByKey(parent.getName());
		}
		else
		{
			dialog = new ReflectogrammLoadDialog (aContext);
			Heap.setRLDialogByKey(parent.getName(), dialog);
		}

		//Environment.getActiveWindow()
		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (dialog.getResult() == null)
			return;

		BellcoreStructure bs = null;
		Result res = dialog.getResult();

		SetParameter[] parameters = res.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return;

		if (!Heap.hasEmptyAllBSMap())
		{
			if (Heap.getBSPrimaryTrace() != null)
				new FileCloseCommand(dispatcher, aContext).execute();
		}
		Heap.setBSPrimaryTrace(bs);

		if (res.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)res.getAction();
			Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			bs.title = m.getName();
			bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();
	
			bs.measurementId = m.getId().getIdentifierString();
			MeasurementSetup ms = m.getSetup();
			Heap.setContextMeasurementSetup(ms);
	
			AnalysisUtil.load_CriteriaSet(userId, ms);
	
			if (ms.getEtalon() != null)
				AnalysisUtil.load_Etalon(ms);
			else
				Heap.removeAnyBSByName(AnalysisUtil.ETALON);
	
			AnalysisUtil.load_Thresholds(userId, ms);
	
			new InitialAnalysisCommand().execute();
	
			ModelTraceManager mtmEtalon = Heap.getMTMByKey(AnalysisUtil.ETALON);
			ModelTraceManager mtmEvents = Heap.getMTMByKey(RefUpdateEvent.PRIMARY_TRACE);
	
			if (mtmEtalon != null && mtmEvents != null)
			{
				int delta = 5;
				// correct end of trace
				// XXX: removed by saa because had no effect due to mistake
	
				// correct event types
				// FIXME: (is necessary??)
				//mtmEvents.fixEventTypes(mtmEtalon, delta);
			}

			Heap.primaryTraceOpened(bs);
			Heap.setCurrentTracePrimary();

			dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

			dispatcher.notify(new RefUpdateEvent(AnalysisUtil.ETALON,
				RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
