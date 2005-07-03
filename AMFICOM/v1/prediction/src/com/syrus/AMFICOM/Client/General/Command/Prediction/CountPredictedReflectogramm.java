package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.*;
import com.syrus.AMFICOM.Client.Prediction.UI.Calendar.DateSelectionDialog;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.io.*;

public class CountPredictedReflectogramm
		extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	ReflectoEventStatistics reflectoEventStatistics;
	static SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

	public CountPredictedReflectogramm(ApplicationContext aContext,
																		 Dispatcher dispatcher)
	{
		this.aContext = aContext;
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new CountPredictedReflectogramm(aContext, dispatcher);
	}

	public void execute()
	{
		reflectoEventStatistics = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
		if (reflectoEventStatistics == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Статистические данные не найдены",
																		"Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}

		DateSelectionDialog dsd = new DateSelectionDialog(Environment.getActiveWindow(), "Дата прогнозирования");
		if (dsd.retCode == DateSelectionDialog.CANCEL)
			return;

		long date = dsd.getDate();
		ReflectogrammPredictor rep = new ReflectogrammPredictor(date, reflectoEventStatistics);

		if (rep == null || rep.getPredictedReflectogramm() == null)
			return;

		String title = "Ожидание на " + sdf.format(new Date(date));
		MonitoredElement me = rep.getStatistics().getMonitoredElement();
		title = title + ", трасса: " + me.getName();

		double[] predicted = rep.getPredictedReflectogramm();

		BellcoreStructure main = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		byte[] tmp = new BellcoreWriter().write(main);
		BellcoreStructure bs = new BellcoreReader().getData(tmp);
//		 bs.title = "(Идентификатор: "+String.valueOf(aContext.getDataSourceInterface().GetUId(Modeling.typ))+")  "+
//              "Ожидание на" + sdf.format(new Date(date));

		bs.title = title;

		BellcorePredictionWriter writer = new BellcorePredictionWriter(bs);
		writer.setDataPoints(predicted);
		writer.setTime(System.currentTimeMillis() / 1000);

		Pool.put("bellcorestructure", bs.title, bs);
		Pool.put("predictionTime", bs.title, new Long(date));

		dispatcher.notify(new RefChangeEvent(bs.title, RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}