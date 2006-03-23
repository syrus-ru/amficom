package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.syrus.AMFICOM.client.resource.I18N;
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

	@Override
	public void execute() {
		PredictionManager pm = PredictionModel.getPredictionManager();
		if (pm == null) {
			Log.debugMessage("PredictionModel not initialyzed yet", Level.WARNING);
			return;
		}
		
		if (pm.getMaxTime() <= pm.getMinTime()) {
			Log.errorMessage("Incorrect state of PredictionManager: pm.getMaxTime() <= pm.getMinTime()");
			return;
		}
		
		DateSpinner dateSpinner = new DateSpinner();
		Calendar defaultTime = Calendar.getInstance();
		defaultTime.setTime(new Date(2 * pm.getMaxTime() - pm.getMinTime()));
		defaultTime.set(Calendar.HOUR_OF_DAY, 0);
		defaultTime.set(Calendar.MINUTE, 0);
		defaultTime.set(Calendar.SECOND, 0);
		defaultTime.set(Calendar.MILLISECOND, 0);
		defaultTime.add(Calendar.DAY_OF_MONTH, 1);
		dateSpinner.setValue(defaultTime.getTime());
		
		final long maxTime = pm.getMaxTime() + (pm.getMaxTime() - pm.getMinTime()) * 2;
		
		while (true) {
			int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), 
					dateSpinner, I18N.getString("Label.predictionDate"), JOptionPane.OK_CANCEL_OPTION);
			
			if (res != JOptionPane.OK_OPTION) {
				return;
			}

			final long time = ((Date)dateSpinner.getValue()).getTime();
			if (time < pm.getMaxTime()) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						I18N.getString("Message.error.priorDate"), 
						I18N.getString("Message.error"),
						JOptionPane.ERROR_MESSAGE);
				dateSpinner.setValue(new Date(pm.getMaxTime()));
			} else if (time > maxTime) {
				final Date maxDate = new Date(maxTime);
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						I18N.getString("Message.error.tooFarPrediction")+ "\n" + 
						I18N.getString("Message.error.maximalPrediction") +
						DateFormat.getDateInstance().format(maxDate), 
						I18N.getString("Message.error"),
						JOptionPane.ERROR_MESSAGE);
				dateSpinner.setValue(maxDate);
			} else {
				break;	
			}
		}

		Date date = (Date)dateSpinner.getValue();

		final double[] predictedReflectogramm = pm.getPredictedReflectogram(date.getTime());

		if (predictedReflectogramm == null) {
			return;
		}

		// XXX предполагаетс€, что предсказанна€ р/г не может превышать нулевой уровень.
		// поэтому здесь отсекаем такие значени€.
		// ≈сли этого не сделать, то BS при нормировке уедет вниз 
		for (int i = 0; i < predictedReflectogramm.length; i++) {
			if (predictedReflectogramm[i] > 0) {
				predictedReflectogramm[i] = 0;
			}
		}
		
		String title = "ќжидание на " + sdf.format(date);
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
