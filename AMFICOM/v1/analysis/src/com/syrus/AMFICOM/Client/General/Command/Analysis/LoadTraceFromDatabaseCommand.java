package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
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

		if(dialog.showDialog() == JOptionPane.CANCEL_OPTION)
			return;
		
		Result result = dialog.getResult();
		if (result == null)
			return;

		BellcoreStructure bs = null;
		

		SetParameter[] parameters = result.getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}
		if (bs == null)
			return; // FIXME: выдавать собщение об ошибке

		if (!Heap.hasEmptyAllBSMap())
		{
			if (Heap.getBSPrimaryTrace() != null)
				new FileCloseCommand(dispatcher, aContext).execute();
		}
		Heap.setBSPrimaryTrace(bs);

		if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)result.getAction();
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
			{
				Heap.setBSEtalonTrace(null);
				Heap.setMTMEtalon(null);
			}

			new InitialAnalysisCommand().execute();

			Heap.primaryTraceOpened(bs);
			Heap.setCurrentTracePrimary();

			dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE,
				RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

			dispatcher.notify(new RefUpdateEvent(this,
				RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));
		}
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
