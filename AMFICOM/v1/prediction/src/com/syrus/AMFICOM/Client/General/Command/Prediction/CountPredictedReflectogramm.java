package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.Modeling;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectogrammPredictor;
import com.syrus.AMFICOM.Client.Prediction.UI.Calendar.DateSelectionDialog;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

public class CountPredictedReflectogramm extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	ReflectoEventStatistics reflectoEventStatistics;
	static SimpleDateFormat sdf =	new java.text.SimpleDateFormat("dd.MM.yyyy");

//----------------------------------------------------------------
	public CountPredictedReflectogramm(ApplicationContext aContext,
																		 Dispatcher dispatcher)
	{
		this.aContext = aContext;
		this.dispatcher = dispatcher;
	}

//----------------------------------------------------------------
	public Object clone()
	 {
		 return new CountPredictedReflectogramm(aContext, dispatcher);
	 }

//----------------------------------------------------------------
	 public void execute()
	 {
		 reflectoEventStatistics = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
		 if (reflectoEventStatistics == null)
		 {
			 JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Статистические данные не найдены", "Ошибка", JOptionPane.OK_OPTION);
			 return;
		 }

		 DateSelectionDialog dsd = new DateSelectionDialog(Environment.getActiveWindow(), "Дата прогнозирования");
		 if (dsd.retCode == DateSelectionDialog.CANCEL)
			 return;

		 long date = dsd.getDate();
		 ReflectogrammPredictor rep = new ReflectogrammPredictor(date, reflectoEventStatistics);

		 if(rep == null || rep.getPredictedReflectogramm() == null)
			 return;

		 String title = "Ожидание на " + sdf.format(new Date(date));
		 String pathId = rep.getStatistics().getPathID();
		 if(pathId != null)
		 {
			 pathId = ((ObjectResource)Pool.get(MonitoredElement.typ, pathId)).getName();
		 }
		 if(pathId != null)
		 {
			 title = title + ", трасса: "+pathId + ", id: "+aContext.getDataSourceInterface().GetUId(Modeling.TYPE);
		 }

		 double[] predicted = rep.getPredictedReflectogramm();

		 BellcoreStructure main = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		 byte[] tmp = new BellcoreWriter().write(main);
		 BellcoreStructure bs = new BellcoreReader().getData(tmp);
//		 bs.title = "(Идентификатор: "+String.valueOf(aContext.getDataSourceInterface().GetUId(Modeling.typ))+")  "+
//              "Ожидание на" + sdf.format(new Date(date));

		 bs.title = title;

		 for (int i = 0; i < bs.dataPts.TPS[0] && i<predicted.length; i++)
			 bs.dataPts.DSF[0][i] = (int)(65535 - 1000d * predicted[i]);

		 bs.fxdParams.DTS = System.currentTimeMillis();
		 bs.dataPts.TPS[0] = predicted.length;
		 bs.dataPts.TNDP = predicted.length;

		 Pool.put("bellcorestructure", bs.title, bs);
		 Pool.put("predictionTime", bs.title, new Long(date));

		 dispatcher.notify(new RefChangeEvent(bs.title, RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
	 }
}