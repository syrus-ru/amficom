package com.syrus.AMFICOM.Client.General.Command.Prediction;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
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

		Date from = dialog.getFromDate();
		Date to   = dialog.getToDate();
		MonitoredElement me = dialog.getMonitoredElement();
		MeasurementSetup ms = dialog.getMeasurementSetup();

		// Загружаем выбранные рефлектограммы
		try {
			TypicalCondition condition1 = new TypicalCondition(from, to, OperationSort.OPERATION_IN_RANGE, ObjectEntities.MEASUREMENT_CODE, MeasurementWrapper.COLUMN_START_TIME);
			LinkedIdsCondition mcond  = new LinkedIdsCondition(ms.getId(), ObjectEntities.MEASUREMENT_CODE);
			Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(
					new CompoundCondition(condition1, CompoundConditionSort.AND, mcond), true);
			if (measurements.isEmpty()) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						I18N.getString("Message.error.noMeasurementsFound"), 
						I18N.getString("Message.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// если в шаблоне есть параметры анализа, то используем их для Trace.getTraceWithARIfPossible
			AnalysisParameters analysisParameters = AnalysisUtil.getCriteriaSetByMeasurementSetup(ms);
			boolean hasCriteriaSet = true;
			if (analysisParameters == null) {
				// в противном случае берем дефолтные - должно быть не null
				hasCriteriaSet = false;
				analysisParameters = Heap.getMinuitDefaultParams();
			}

			Map<String, PredictionMtaeAndDate> pmads = new HashMap<String, PredictionMtaeAndDate>();
			Set<Trace> traces = new HashSet<Trace>(measurements.size());
			for (Measurement m : measurements) {
				final long date = m.getStartTime().getTime();
				for (Result result1 : m.getResults()) { // XXX: PERFORMANCE: 90% of load-from-cache time is m.getResults() (takes ~ 1 sec)
					if (result1.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
						try {
							final Trace trace = Trace.getTraceWithARIfPossible(result1, analysisParameters);
							pmads.put(trace.getKey(), new PredictionMtaeAndDate(trace.getMTAE(), date));							
							traces.add(trace);
						} catch (SimpleApplicationException e) {
							Log.errorMessage(e);
						}
					}
				}
				traces.remove(null);
			}
			if (traces.size() < 2) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						I18N.getString("Message.error.infufficientAnalysesFound"), 
						I18N.getString("Message.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Загружаем эталон в Heap как первичную р/г
			try {
				// если были критерии анализа в MS, значит есть эталон - грузим его как основную р/г
				if (hasCriteriaSet) { 
					if (!AnalysisUtil.loadEtalonAsEtalonAndAsPrimary(ms)) {
						return;
					}
					for (Trace tr: traces) {
						if (!Heap.hasSecondaryBSKey(tr.getKey()) && !Heap.getPrimaryTrace().getKey().equals(tr.getKey())) {
							Heap.putSecondaryTrace(tr);
							Heap.setCurrentTrace(tr.getKey());
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
		} catch (ApplicationException ex) {
			GUIUtil.processApplicationException(ex);
			return;
		} catch (DataFormatException e) {
			Log.errorMessage(e);
		}
	}
}
