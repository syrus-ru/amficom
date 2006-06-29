package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.FilteredMTAEPredictionManager;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionModel;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.MTAEPredictionManager.PredictionMtaeAndDate;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.util.SynchronousWorker;
import com.syrus.AMFICOM.client_.prediction.ui.MSChooserUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

public class LoadTraceFromDatabaseCommand extends AbstractCommand {
	ApplicationContext aContext;

	public LoadTraceFromDatabaseCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MSChooserUI dialog = new MSChooserUI(AbstractMainFrame.getActiveMainFrame());
		
		if (dialog.getRetCode() != Command.RESULT_OK) {
			return;
		}

		final Date from = dialog.getFromDate();
		final Date to   = dialog.getToDate();
		final MonitoredElement me = dialog.getMonitoredElement();
		final MeasurementSetup ms = dialog.getMeasurementSetup();

		final Map<String, PredictionMtaeAndDate> pmads = new HashMap<String, PredictionMtaeAndDate>();
		final Set<Trace> traces = new HashSet<Trace>();
		
		final SynchronousWorker<Boolean> worker = new SynchronousWorker<Boolean>(null,
				I18N.getString("Message.Information.please_wait"), 
				I18N.getString("Message.Information.loading_data"), true) {
			@Override
			public Boolean construct() throws Exception {
				
				TypicalCondition condition1 = new TypicalCondition(from, to, OperationSort.OPERATION_IN_RANGE, ObjectEntities.MEASUREMENT_CODE, MeasurementWrapper.COLUMN_START_TIME);
				LinkedIdsCondition mcond  = new LinkedIdsCondition(ms.getId(), ObjectEntities.MEASUREMENT_CODE);
				Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(
						new CompoundCondition(condition1, CompoundConditionSort.AND, mcond), true);
				if (measurements.isEmpty()) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
									I18N.getString("Message.error.noMeasurementsFound"), 
									I18N.getString("Message.error"),
									JOptionPane.ERROR_MESSAGE);
						}
					});
					return Boolean.valueOf(false);
				}
				
				// если в шаблоне есть параметры анализа, то используем их для Trace.getTraceWithARIfPossible
				AnalysisParameters analysisParameters = AnalysisUtil.getCriteriaSetByMeasurementSetup(ms);
				if (analysisParameters == null) {
					// в противном случае берем дефолтные - должно быть не null
					analysisParameters = Heap.getMinuitDefaultParams();
				}

				final LinkedIdsCondition condition2 = new LinkedIdsCondition(measurements, ObjectEntities.RESULT_CODE);
//				final TypicalCondition condition3 = new TypicalCondition(ResultSort.RESULT_SORT_MEASUREMENT.value(),
//						OperationSort.OPERATION_EQUALS, ObjectEntities.RESULT_CODE, ResultWrapper.COLUMN_SORT);
				
				Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(
//						new CompoundCondition(condition2, CompoundConditionSort.AND, condition3)
						condition2,
						true);
				
				for (Result result1 : results) {
					if (result1.getSort().value() == ResultSort.RESULT_SORT_MEASUREMENT.value()) {
						final long date = ((Measurement)result1.getAction()).getStartTime().getTime();
						final Trace trace = Trace.getTraceWithARIfPossible(result1, analysisParameters);
						pmads.put(trace.getKey(), new PredictionMtaeAndDate(trace.getMTAE(), date));							
						traces.add(trace);
					}
				}
				/*
				for (Measurement m : measurements) {
					final long date = m.getStartTime().getTime();
					for (Result result1 : m.getResults()) { // XXX: PERFORMANCE: 90% of load-from-cache time is m.getResults() (takes ~ 1 sec)
						if (result1.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
							try {
								
							} catch (SimpleApplicationException e) {
								Log.errorMessage(e);
							}
						}
					}
					traces.remove(null);
				}*/
				if (traces.size() < 2) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
									I18N.getString("Message.error.infufficientAnalysesFound"), 
									I18N.getString("Message.error"),
									JOptionPane.ERROR_MESSAGE);
						}
					});
					return Boolean.valueOf(false);
				}
				return Boolean.valueOf(true);
			}
		};
		
		Boolean result1 = Boolean.valueOf(false);
		try {
			result1 = worker.execute();
		} catch (ExecutionException e) {
			Log.errorMessage(e);
		}
		
		if (result1.booleanValue() != false) {
			// Загружаем эталон в Heap как первичную р/г
			try {
				// если были критерии анализа в MS, значит есть эталон - грузим его как основную р/г
				if (AnalysisUtil.getCriteriaSetByMeasurementSetup(ms) != null) { 
					if (!AnalysisUtil.loadEtalonAsEtalonAndAsPrimary(ms)) {
						return;
					}
					
					for (Trace tr: traces) {
						final String key = tr.getKey();
						if (!Heap.hasSecondaryBSKey(key)) {
							Heap.putSecondaryTrace(tr);
							Heap.setCurrentTrace(key);
						}
					}
				} else { // если не было критериев анализа, грузим всю кучу (там выбирается наиболее типичная как основная) 
					Heap.openManyTraces(traces);
					Heap.updatePrimaryAnalysis();
				}
			} catch (DataFormatException e) {
				Log.errorMessage(e);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			
			PredictionManager pm = new FilteredMTAEPredictionManager(pmads,
					Heap.getMTAEPrimary(),
					from.getTime(),
					to.getTime(),
					me);
			
			PredictionModel.init(this.aContext);
			PredictionModel.initPredictionManager(pm);
			
			Heap.setCurrentEvent(0);
		}
	}
}
