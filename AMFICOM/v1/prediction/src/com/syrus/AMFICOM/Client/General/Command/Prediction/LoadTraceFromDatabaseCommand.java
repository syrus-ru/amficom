package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.*;
import com.syrus.AMFICOM.Client.Prediction.UI.Calendar.DateDiapazonAndPathAndTestSetupSelectionDialog;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.*;

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
		DateDiapazonAndPathAndTestSetupSelectionDialog dialog =
				new DateDiapazonAndPathAndTestSetupSelectionDialog(
						Environment.getActiveWindow(),
						"Выбор статистических данных",
						true, aContext);
		if (dialog.retCode == 0)
			return;

		long from = dialog.from;
		long to   = dialog.to;
		MonitoredElement me = dialog.me;
		MeasurementSetup ms = dialog.ms;

		AnalysisUtil.load_Etalon(ms);

		BellcoreStructure bs = null;
		ReflectogramEvent []re = null;
		ShortReflectogramEvent []sre = null;

		SetParameter[] parameters = ms.getEtalon().getParameters();
		for(int i = 0; i < parameters.length; i++)
		{
			ParameterType p = (ParameterType)parameters[i].getType();
			if(p.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
			{
				bs = new BellcoreReader().getData(parameters[i].getValue());
			}
			else if(p.getCodename().equals(ParameterTypeCodenames.DADARA_ETALON_EVENTS))
			{
				re = ReflectogramEvent.fromByteArray(parameters[i].getValue());
				sre = new ShortReflectogramEvent[re.length];
				for(int j = 0; j < re.length; j++)
					sre[j] = new ShortReflectogramEvent(re[j]);
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

		SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

		bs.title = "Состояние на " + sdf.format(ms.getCreated());
		ReflectoEventContainer statEtalon =
				new ReflectoEventContainer(sre, re, bs, me.getCreated().getTime()); //???

		LinkedIdsCondition condition = new LinkedIdsCondition(me.getId(), ObjectEntities.RESULT_ENTITY_CODE);
		List results = null;
		try {
			results = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
		catch (ApplicationException ex) {
			System.err.println("Exception retrieving result");
			ex.printStackTrace();
		}

		List v = new ArrayList();
		for (Iterator it = results.iterator(); it.hasNext(); )
		{
			Result r = (Result)it.next();
			if (r.getCreated().getTime() > from && r.getCreated().getTime() < to)
			{
				parameters = r.getParameters();
				for (int i = 0; i < parameters.length; i++)
				{
					ParameterType p = (ParameterType)parameters[i].getType();
					if (p.getCodename().equals(ParameterTypeCodenames.DADARA_EVENTS))
					{
						re = ReflectogramEvent.fromByteArray(parameters[i].getValue());
						if(re != null)
						{
							sre = new ShortReflectogramEvent[re.length];
							for (int k = 0; k < sre.length; k++)
								sre[k] = new ShortReflectogramEvent(re[k]);

							ReflectoEventContainer rec =
									new ReflectoEventContainer(sre, re, bs, r.getMeasurement().getStartTime().getTime());

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
			sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			String error = "Слишком мало рефлектограмм для составления статистики.\n";
			error += "С " + sdf.format(new Date(from)) + " по " +
							 sdf.format(new Date(to)) + " получено " + refEvCont.length + " рефлектограмм(ы).";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		ReflectoEventStatistics res = new ReflectoEventStatistics(refEvCont, statEtalon, from, to, me);
		Pool.put("statData", "theStatData", res);

		bs = statEtalon.bs;
		if(bs == null)
			return;

		if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
			new FileCloseCommand(dispatcher, aContext).execute();

		Pool.put("bellcorestructure", "primarytrace", bs);

//    new MinuitAnalyseCommand(dispatcher, "primarytrace", aContext).execute();
		{
				double delta_x = bs.getResolution();
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
