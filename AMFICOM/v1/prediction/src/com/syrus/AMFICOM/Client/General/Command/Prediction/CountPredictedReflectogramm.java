package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager;
import com.syrus.AMFICOM.Client.Prediction.UI.Calendar.DateSelectionDialog;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.io.BellcorePredictionWriter;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

public class CountPredictedReflectogramm
		extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	PredictionManager pm;
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
		pm = (PredictionManager)Pool.get("statData", "pmStatData");
		if (pm == null) {
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

		final double[] predictedReflectogramm =
			pm.getPredictedReflectogram(date);

		if (predictedReflectogramm == null)
			return;

		String title = "Ожидание на " + sdf.format(new Date(date));
		MonitoredElement me = pm.getMonitoredElement();
		title = title + ", трасса: " + me.getName();

		BellcoreStructure main = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		byte[] tmp = new BellcoreWriter().write(main);
		BellcoreStructure bs = new BellcoreReader().getData(tmp);
//		 bs.title = "(Идентификатор: "+String.valueOf(aContext.getDataSourceInterface().GetUId(Modeling.typ))+")  "+
//              "Ожидание на" + sdf.format(new Date(date));

		bs.title = title;

		BellcorePredictionWriter writer = new BellcorePredictionWriter(bs);
		writer.setDataPoints(predictedReflectogramm);
		writer.setTime(System.currentTimeMillis() / 1000);

		Pool.put("bellcorestructure", bs.title, bs);
		Pool.put("predictionTime", bs.title, new Long(date));

		dispatcher.notify(new RefChangeEvent(bs.title, RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	}
}
