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
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.general.Identifier;
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

//		res.getMeasurement().getT
//		Test test = (Test)Pool.get(Test.TYPE, res.getActionId());

		Measurement m = res.getMeasurement();
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		bs.title = m.getName();
		bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();

			//Если нет тестсетапа создаем его
			/*if (test.getTestSetupId().equals(""))
			{
				ts = new TestSetup();
				ts.settestTypeId(test.getTestTypeId());
				ts.setId(dataSource.GetUId(TestSetup.TYPE));
				ts.setTestArgumentSetId(test.getTestArgumentSetId());

				bs.test_setup_id = ts.getId();
				Pool.put(TestSetup.TYPE, ts.getId(), ts);
			}
			else*/

		bs.measurementId = m.getId().getIdentifierString();
		Pool.put("testsetup", m.getId().getIdentifierString(), m);
		MeasurementSetup ms = res.getMeasurement().getSetup();

		AnalysisUtil.load_CriteriaSet(userId, ms);

		if (ms.getEtalon() != null)
			AnalysisUtil.load_Etalon(ms);
		else
			Pool.remove("bellcorestructure", AnalysisUtil.ETALON);

		AnalysisUtil.load_Thresholds(userId, ms);

		new InitialAnalysisCommand().execute();
		//new MinuitAnalyseCommand(aContext.getDispatcher(), "primarytrace", aContext).execute();

		ReflectogramEvent[] etalon = (ReflectogramEvent[])Pool.get("eventparams", AnalysisUtil.ETALON);
		ReflectogramEvent[] revents = (ReflectogramEvent[])Pool.get("eventparams", "primarytrace");

/*
			Threshold[] thresholds = new Threshold[etalon.length];
			for (int i = 0; i < thresholds.length; i++)
				thresholds[i] = etalon[i].getThreshold();

			ReflectogramComparer comp = new ReflectogramComparer(revents, etalon, thresholds, false);
			ReflectogramAlarm[] alarms = comp.getAlarms();
*/

	 if(etalon != null && revents != null)
	 {
		 int delta = 5;
		 //correct end of trace
		 if(revents.length > etalon.length)
		 {
			 ReflectogramEvent endEvent = etalon[etalon.length - 1];
			 for(int i = revents.length - 1; i >= 0; i--)
			 {
				 if(revents[i].getEventType() == ReflectogramEvent.CONNECTOR &&
						 Math.abs(revents[i].getBegin() - endEvent.getBegin()) < delta &&
						 Math.abs(revents[i].getEnd() - endEvent.getEnd()) < delta)
				 {
					 ReflectogramEvent[] new_revents = new ReflectogramEvent[i + 1];
					 for(int j = 0; j < i + 1; j++)
						 new_revents[j] = revents[j];
					 revents = new_revents;
					 break;
				 }
			 }
		 }

		 //correct event types
		 // XXX: rude alg.: probable errors when end - begin < delta
		 if(revents.length == etalon.length)
		 {
			 for(int i = 0; i < etalon.length; i++)
			 {
				 if(Math.abs(revents[i].getBegin() - etalon[i].getBegin()) < delta &&
						 Math.abs(revents[i].getEnd() - etalon[i].getEnd()) < delta)
				 {
					 revents[i].setEventType(etalon[i].getEventType());
				 }
			 }
		 }
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
