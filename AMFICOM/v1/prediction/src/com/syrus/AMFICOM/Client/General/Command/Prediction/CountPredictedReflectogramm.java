package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionModel;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.newFilter.DateSpinner;
import com.syrus.io.BellcorePredictionWriter;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.Log;

public class CountPredictedReflectogramm extends AbstractCommand {
	ApplicationContext aContext;
	
	static SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

	public CountPredictedReflectogramm(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		PredictionManager pm = PredictionModel.getPredictionManager();
		if (pm == null) {
			Log.debugMessage("PredictionModel not initialyzed yet", Level.WARNING);
			return;
		}

		DateSpinner dateSpinner = new DateSpinner();
		int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
				dateSpinner, "Дата прогнозирования", JOptionPane.OK_CANCEL_OPTION);
		
		if (res == JOptionPane.CANCEL_OPTION) {
			return;
		}

		Date date = (Date)dateSpinner.getValue();

		final double[] predictedReflectogramm = pm.getPredictedReflectogram(date.getTime());

		if (predictedReflectogramm == null) {
			
			return;
		}

		String title = "Ожидание на " + sdf.format(date);
		MonitoredElement me = pm.getMonitoredElement();
		title = title + ", трасса: " + me.getName();

		BellcoreStructure main = Heap.getPrimaryTrace().getPFTrace().getBS();
		
		byte[] tmp = new BellcoreWriter().write(main);
		BellcoreStructure bs = new BellcoreReader().getData(tmp);

		bs.title = title;
		bs.monitoredElementId = me.getId().getIdentifierString();

		BellcorePredictionWriter writer = new BellcorePredictionWriter(bs);
		writer.setDataPoints(predictedReflectogramm);
		writer.setTime(System.currentTimeMillis() / 1000);

		Trace tr = new Trace(bs, Heap.MODELED_TRACE_KEY, Heap.getMinuitAnalysisParams());

		Heap.putSecondaryTrace(tr);
		Heap.setCurrentTrace(Heap.MODELED_TRACE_KEY);
		Heap.setSecondaryTraceAsPrimary(Heap.MODELED_TRACE_KEY, true);
		Heap.getPFTracePrimary();
	}
}
