package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import javax.swing.JFrame;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.*;

public class LoadTraceFromDatabaseCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	private Checker checker;

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
			this.checker = new Checker(this.aContext.getSessionInterface());
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
		if(Pool.get("dialog", parent.getName()) != null)
		{
			dialog = (ReflectogrammLoadDialog)Pool.get("dialog", parent.getName());
		}
		else
		{
			dialog = new ReflectogrammLoadDialog (aContext);
			Pool.put("dialog", parent.getName(), dialog);
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

		if (Pool.getMap("bellcorestructure") != null )
		{
			if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
				new FileCloseCommand(dispatcher, aContext).execute();
		}
		Pool.put("bellcorestructure", "primarytrace", bs);

		Measurement m = res.getMeasurement();
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		bs.title = m.getName();
		bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();

		bs.measurementId = m.getId().getIdentifierString();
		MeasurementSetup ms = res.getMeasurement().getSetup();
		Pool.put(AnalysisUtil.CONTEXT, "MeasurementSetup", ms);

		AnalysisUtil.load_CriteriaSet(userId, ms);

		if (ms.getEtalon() != null)
			AnalysisUtil.load_Etalon(ms);
		else
			Pool.remove("bellcorestructure", AnalysisUtil.ETALON);

		AnalysisUtil.load_Thresholds(userId, ms);

		new InitialAnalysisCommand().execute();

		ModelTraceManager mtmEtalon = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, AnalysisUtil.ETALON);
		ModelTraceManager mtmEvents = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, "primarytrace");

		if (mtmEtalon != null && mtmEvents != null)
		{
			int delta = 5;
			// correct end of trace
			// XXX: removed by saa because had no effect due to mistake

			// correct event types
			// FIXME: (is necessary??)
			//mtmEvents.fixEventTypes(mtmEtalon, delta);
		}

		dispatcher.notify(new RefChangeEvent("primarytrace",
			RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		
		dispatcher.notify(new RefUpdateEvent("primarytrace",
			RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
		
		dispatcher.notify(new RefUpdateEvent(AnalysisUtil.ETALON,
			RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		
		Environment.getActiveWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
