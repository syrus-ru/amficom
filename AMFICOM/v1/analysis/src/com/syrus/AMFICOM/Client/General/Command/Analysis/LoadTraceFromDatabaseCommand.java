package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.ReflectogrammLoadDialog;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

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
			if(!checker.checkCommand(checker.loadReflectogrammFromDB))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;


		ReflectogrammLoadDialog dialog;
		if(Pool.get("dialog", "TraceLoadDialog") == null)
		{
			dialog = new ReflectogrammLoadDialog (this.aContext);
			Pool.put("dialog", "TraceLoadDialog", dialog);
		}
		else
		{
			dialog = (ReflectogrammLoadDialog)Pool.get("dialog", "TraceLoadDialog");
		}

		//Environment.getActiveWindow()
		dialog.show();

		if(dialog.ret_code == 0)
			return;
		if (dialog.getResult() == null)
			return;

		BellcoreStructure bs = null;
		Result res = dialog.getResult();

		java.util.Enumeration enum = res.parameters.elements();
		while (enum.hasMoreElements())
		{
			Parameter param = (Parameter)enum.nextElement();
			if (param.gpt.id.equals("reflectogramm"))
				bs = new BellcoreReader().getData(param.value);
		}
		if (bs == null)
			return;

		if (Pool.getHash("bellcorestructure") != null )
		{
			if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
				new FileCloseCommand(dispatcher, aContext).execute();
		}
		Pool.put("bellcorestructure", "primarytrace", bs);

		Test test = (Test)Pool.get(Test.typ, res.action_id);
		bs.title = res.getName();

		TestSetup ts;
		if(test != null)
		{
			bs.monitored_element_id = test.monitored_element_id;

			//Если нет тестсетапа создаем его

			if (test.test_setup_id.equals(""))
			{
				ts = new TestSetup();
				ts.test_type_id = test.test_type_id;
				ts.id = dataSource.GetUId(TestSetup.typ);
				ts.test_argument_set_id = test.test_argument_set_id;

				bs.test_setup_id = ts.getId();
				Pool.put(TestSetup.typ, ts.getId(), ts);
			}
			else
			{
				dataSource.loadTestSetup(test.test_setup_id);
				bs.test_setup_id = test.test_setup_id;
				ts = (TestSetup)Pool.get(TestSetup.typ, bs.test_setup_id);
			}

			AnalysisUtil.load_CriteriaSet(dataSource, ts);

			if (!ts.etalon_id.equals(""))
				AnalysisUtil.load_Etalon(dataSource, ts);
			else
				Pool.remove("bellcorestructure", "etalon");

			AnalysisUtil.load_Thresholds(dataSource, ts);

			new InitialAnalysisCommand().execute();
			//new MinuitAnalyseCommand(aContext.getDispatcher(), "primarytrace", aContext).execute();

			ReflectogramEvent[] etalon = (ReflectogramEvent[])Pool.get("eventparams", "etalon");
			ReflectogramEvent[] revents = (ReflectogramEvent[])Pool.get("eventparams", "primarytrace");

/*
			Threshold[] thresholds = new Threshold[etalon.length];
			for (int i = 0; i < thresholds.length; i++)
				thresholds[i] = etalon[i].getThreshold();

			ReflectogramComparer comp = new ReflectogramComparer(revents, etalon, thresholds, false);
			ReflectogramAlarm[] alarms = comp.getAlarms();
*/

			if (etalon != null && revents != null)
			{
				int delta = 5;
				//correct end of trace
				if (revents.length > etalon.length)
				{
					ReflectogramEvent endEvent = etalon[etalon.length-1];
					for (int i = revents.length - 1; i >= 0; i--)
					{
						if (revents[i].getType() == ReflectogramEvent.CONNECTOR &&
								Math.abs(revents[i].begin - endEvent.begin) < delta &&
								Math.abs(revents[i].end - endEvent.end) < delta)
							{
								ReflectogramEvent[] new_revents = new ReflectogramEvent[i+1];
								for (int j = 0; j < i+1; j++)
									new_revents[j] = revents[j];
								revents = new_revents;
								break;
							}
					}
				}

				//correct event types
				if (revents.length == etalon.length)
				{
					for (int i = 0; i < etalon.length; i++)
					{
						if (Math.abs(revents[i].begin - etalon[i].begin) < delta &&
								Math.abs(revents[i].end - etalon[i].end) < delta)
						{
							revents[i].setType(etalon[i].getType());
						}
					}
				}
			}

			dispatcher.notify(new RefChangeEvent("primarytrace",
					RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));

			dispatcher.notify(new RefUpdateEvent("primarytrace",
					RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

			dispatcher.notify(new RefUpdateEvent("etalon",
					RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));

			Environment.getActiveWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}