package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Etalon;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventContainer;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.AMFICOM.Client.Prediction.UI.Calendar.DateDiapazonAndPathAndTestSetupSelectionDialog;
//import com.syrus.AMFICOM.analysis.dadara.AnalysResult;
//import com.syrus.AMFICOM.analysis.dadara.EventReader;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ShortReflectogramEvent;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class LoadTraceFromDatabaseCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;

// ---------------------------------------------------------------
	public LoadTraceFromDatabaseCommand(Dispatcher dispatcher,
																			ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}


// ---------------------------------------------------------------
	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else if(field.equals("aContext"))
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

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		if(dataSource == null)
		{
			String error = "Ошибка базы данных.\n";
			error += "Неизвестная ошибка базы данных. В случае повторения, обратитесь \n";
			error += "к специалистам SYRUS systems.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		DateDiapazonAndPathAndTestSetupSelectionDialog dialog =
				new DateDiapazonAndPathAndTestSetupSelectionDialog((JFrame)Environment.getActiveWindow(), "Выбор статистических данных", true, dataSource);
		if (dialog.retCode == 0)
			return;

		long from = dialog.from;
		long to   = dialog.to;
		String path_id = dialog.me_id;
		String testSetupId = dialog.testSetup_id;

//    MonitoredElement path = (MonitoredElement)Pool.get(MonitoredElement.typ, path_id);

		TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, testSetupId);
		if(ts == null)
		{
			String error = "Ошибка загрузки тестовых установок.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		dataSource.LoadEtalons(new String[] {ts.getEthalonId()});
		Etalon et = (Etalon)Pool.get(Etalon.typ, ts.getEthalonId());
		if(et == null)
		{
			String error = "Ошибка загрузки эталона.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs = null;
		ReflectogramEvent []re = null;
		ShortReflectogramEvent []sre = null;

		for(Iterator it = et.getEthalonParameterList().iterator(); it.hasNext(); )
		{
			Parameter p = (Parameter)it.next();
			if(p.getCodename().equals("reflectogramm"))
			{
				bs = new BellcoreReader().getData(p.getValue());
			}
			else if(p.getCodename().equals("dadara_etalon_event_array"))
			{
				re = ReflectogramEvent.fromByteArray(p.getValue());
				sre = new ShortReflectogramEvent[re.length];
				for(int i=0; i<re.length; i++)
				{
					sre[i] = new ShortReflectogramEvent(re[i]);
				}
			}
		}
		if(bs == null || re == null)
		{
			String error = "Ошибка загрузки эталона.\n";
			error += "Эталон не содержтт в себе всех необходимых данных.\n";
			error += "(Сырой рефлектограммы и/или данных анализа)";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		SimpleDateFormat sdf_ = new java.text.SimpleDateFormat("dd.MM.yyyy");

		bs.title = "Состояние на " + sdf_.format(new Date(et.getCreated()));
		ReflectoEventContainer statEtalon =
				new ReflectoEventContainer(path_id, sre, re, bs, et.getCreated()); //???



		ArrayList v = new ArrayList();
		String []analysisResultIds = dataSource.GetAnalysisResultsForStatistics(path_id, from, to, testSetupId);
		Result analysisResult;
		for(int i=0; i<analysisResultIds.length; i++)
		{
			analysisResult = (Result)Pool.get(Result.typ, analysisResultIds[i]);
			if(analysisResult != null)
			{
				for (Iterator it = analysisResult.getParameterList().iterator(); it.hasNext();)
				{
					Parameter param = (Parameter)it.next();
					if(param.getGpt() != null && param.getGpt().getId() != null && param.getGpt().getId().equals("dadara_event_array"))
					{
						re = ReflectogramEvent.fromByteArray(param.getValue());
						if(re != null)
						{
							sre = new ShortReflectogramEvent[re.length];

							for (int k = 0; k < sre.length; k++)
							{
								sre[k] = new ShortReflectogramEvent(re[k]);
							}

							ReflectoEventContainer rec =
									new ReflectoEventContainer(analysisResultIds[i], sre, re, bs, analysisResult.elementary_start_time);
							v.add(rec);
						}
					}
				}
			}
		}

//    v.add(statEtalon);

		// Creating of the statistics container;
		ReflectoEventContainer []refEvCont =
				(ReflectoEventContainer[])v.toArray(new ReflectoEventContainer[v.size()]);

		if(refEvCont.length <= 3)
		{
			SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

			String error = "Слишком мало рефлектограмм для составления статистики.\n";
			error += "С " + sdf.format(new Date(from)) + " по " +
							 sdf.format(new Date(to)) + " получено " + refEvCont.length + " рефлектограмм(ы).";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		ReflectoEventStatistics res = new ReflectoEventStatistics(refEvCont, statEtalon, from, to, path_id);
		Pool.put("statData", "theStatData", res);

		bs = statEtalon.bs;
		if(bs == null)
			return;

		if (Pool.getHash("bellcorestructure") != null )
		{
			if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
				new FileCloseCommand(dispatcher, aContext).execute();
		}
		Pool.put("bellcorestructure", "primarytrace", bs);

//    new MinuitAnalyseCommand(dispatcher, "primarytrace", aContext).execute();
		{
				double delta_x = bs.getDeltaX();
				double[] y = bs.getTraceData();

				//ReflectogramEvent[] ep = AnalysisManager.analyseTrace(y, delta_x, params);
				RefAnalysis a = new RefAnalysis();
				a.decode(y, statEtalon.re);

/*
				AnalysResult anaresult = new AnalysResult(y, delta_x);
				double[] result = anaresult.megaHalyava(y, delta_x, statEtalon.re);

				EventReader reader = new EventReader();
				reader.decode(result);

				RefAnalysis a = new RefAnalysis();
				a.events = reader.readEvents();
				a.noise = reader.readNoise();
				a.filtered = reader.readFiltered();
				a.normalyzed = reader.readNormalyzed();
				a.overallStats = reader.readOverallStats();
				a.concavities = reader.readConcavities();*/
				Pool.put("refanalysis", "primarytrace", a);

				//ReflectogramEvent[] ep = anaresult.ep;
				Pool.put("eventparams", "primarytrace", statEtalon.re);
		}

					double day = 1000.*60.*60.*24; //length of a day in millis;
					long t1=Long.MAX_VALUE, t2=0;
					for(int i=0; i<res.statData.length; i++)
					{
						if(t1>res.statData[i].date)
							t1 = res.statData[i].date;
						if(t2<res.statData[i].date)
							t2=res.statData[i].date;
					}
					int nDays = (int)((t2-t1)/day);
					if(nDays<=0)
						nDays = 1;
					if(nDays<10)
					{
						System.out.println("The data is taken for "+nDays+" days");

						String warning = "По запросу найдены данные за ";
						warning += String.valueOf(nDays);
						warning += " суток. \n";
						warning += "Систематическая ошибка составляет приблизительно ";
						warning += String.valueOf((int)(1./Math.sqrt((double)nDays)*100));
						warning += "%";
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), warning, "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
					}

		dispatcher.notify(new RefChangeEvent("primarytrace",
				RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
		dispatcher.notify(new RefUpdateEvent("primarytrace",
				RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
	}

}
