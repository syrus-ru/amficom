package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.sql.Timestamp;
import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public abstract class TestProcessor extends Thread {
	Test test;
	/*	Number of measurements, passed to transceiver */
	int n_measurements;
	/*	Number of reports, received from transceiver */
	int n_reports;
	Transceiver transceiver;
	/*	Measurement results, stored before analysis and/or evaluation */
	private List measurementResultQueue;
	long tick_time;
	boolean running;

	public TestProcessor(Test test) {
		this.test = test;

		Identifier kis_id = test.getKIS().getId();
		this.transceiver = (Transceiver)MeasurementControlModule.transceivers.get(kis_id);
		this.measurementResultQueue = Collections.synchronizedList(new ArrayList());
		this.tick_time = ApplicationProperties.getInt("TickTime", MeasurementControlModule.TICK_TIME)*1000;
		this.running = true;

		switch (this.test.getStatus().value()) {
			case TestStatus._TEST_STATUS_SCHEDULED:
				this.n_measurements = 0;
				this.n_reports = 0;
				try {
					this.test.setStatus(TestStatus.TEST_STATUS_PROCESSING);
				}
				catch (Exception e) {
					Log.errorException(e);
				}
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				List measurments;
				try {
					measurments = this.test.retrieveMeasurementsOrderByStartTime(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
					this.n_measurements = measurments.size();
					this.n_reports = this.n_measurements;
				}
				catch (Exception e) {
					Log.errorException(e);
				}

				try {
					measurments = this.test.retrieveMeasurementsOrderByStartTime(MeasurementStatus.MEASUREMENT_STATUS_SCHEDULED);
					for (Iterator iterator = measurments.iterator(); iterator.hasNext();)
						this.transceiver.addMeasurement((Measurement)iterator.next(), this);
					this.n_measurements += measurments.size();
				}
				catch (Exception e) {
					Log.errorException(e);
				}

				try {
					measurments = this.test.retrieveMeasurementsOrderByStartTime(MeasurementStatus.MEASUREMENT_STATUS_PROCESSING);
					for (Iterator iterator = measurments.iterator(); iterator.hasNext();)
						this.transceiver.addProcessingMeasurement((Measurement)iterator.next(), this);
					this.n_measurements += measurments.size();
				}
				catch (Exception e) {
					Log.errorException(e);
				}

				try {
					measurments = this.test.retrieveMeasurementsOrderByStartTime(MeasurementStatus.MEASUREMENT_STATUS_MEASURED);
					for (Iterator iterator = measurments.iterator(); iterator.hasNext();)
						this.measurementResultQueue.add(((Measurement)iterator.next()).retrieveResult(ResultSort.RESULT_SORT_MEASUREMENT));
					this.n_measurements += measurments.size();
				}
				catch (Exception e) {
					Log.errorException(e);
				}

				try {
					measurments = this.test.retrieveMeasurementsOrderByStartTime(MeasurementStatus.MEASUREMENT_STATUS_ANALYZED);
					for (Iterator iterator = measurments.iterator(); iterator.hasNext();)
						this.measurementResultQueue.add(((Measurement)iterator.next()).retrieveResult(ResultSort.RESULT_SORT_MEASUREMENT));
					this.n_measurements += measurments.size();
				}
				catch (Exception e) {
					Log.errorException(e);
				}

				break;
			default:
				Log.errorMessage("TestProcessor.<init> | Inappropriate status " + this.test.getStatus() + " of test '" + this.test.getId().toString() + "'");
				this.running = false;
		}
	}

	public abstract void run();

	protected void addMeasurementResult(Result result) {
		this.measurementResultQueue.add(result);
	}

	protected void abort() {
		this.running = false;
		this.transceiver.abortMeasurements(this);		
		try {
			this.test.setStatus(TestStatus.TEST_STATUS_ABORTED);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		this.cleanup();
	}

	void checkMeasurementResults() {
		Result measurementResult;
		Measurement measurement;
		if (!this.measurementResultQueue.isEmpty()) {
			measurementResult = (Result)this.measurementResultQueue.remove(0);
			measurement = measurementResult.getMeasurement();
			Result analysisResult = null;
			Result evaluationResult = null;
			switch (measurement.getStatus().value()) {
				case MeasurementStatus._MEASUREMENT_STATUS_MEASURED:
					analysisResult = this.analyse(measurementResult);
					if (analysisResult != null) {
						try {
							measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ANALYZED);
						}
						catch (Exception e) {
							Log.errorException(e);
						}
						MeasurementControlModule.resultList.add(analysisResult);
					}
					evaluationResult = this.evaluate(analysisResult, measurementResult);
					if (evaluationResult != null)
						MeasurementControlModule.resultList.add(evaluationResult);
					MeasurementControlModule.resultList.add(measurementResult);
					try {
						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
					}
					catch (Exception e) {
						Log.errorException(e);
					}
					this.n_reports ++;
					break;
				case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED:
					try {
						analysisResult = measurement.retrieveResult(ResultSort.RESULT_SORT_ANALYSIS);
					}
					catch (Exception e) {
						Log.errorException(e);
						analysisResult = null;
					}
					evaluationResult = this.evaluate(analysisResult, measurementResult);
					if (evaluationResult != null)
						MeasurementControlModule.resultList.add(evaluationResult);
					MeasurementControlModule.resultList.add(measurementResult);
					try {
						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
					}
					catch (Exception e) {
						Log.errorException(e);
					}
					this.n_reports ++;
					break;
				default:
					Log.errorMessage("TestProcessor.checkMeasurementResults | Inappropriate status " + measurement.getStatus() + " of measurement '" + measurement.getId() + "'");
			}
		}//if (!this.measurementResultQueue.isEmpty())
	}

	private Result analyse(Result measurementResult) {
		Identifier analysis_type_id = this.test.getAnalysisTypeId();
		if (!analysis_type_id.equals("")) {
			Analysis analysis = null;
			Measurement measurement = measurementResult.getMeasurement();
			try {
				/*!!	During creation of a test we must consider:
				 * 		1. dependency among analysis_type_id, evaluation_type_id and measurement_type_id;
				 *    2. appropriate MeasurementSetup, i. e. criteria, thresholds and etalon are present
				 *       if necessary.
				 *    */
				analysis = Analysis.create(MeasurementControlModule.createIdentifier("analysis"),
																	 analysis_type_id,
																	 measurement.getSetup().getCriteriaSet(),
																	 this.test.getMonitoredElement().getId());
			}
			catch (Exception e) {
				Log.errorException(e);
			}
			if (analysis != null) {
				AnalysisManager analysisManager = AnalysisManager.getAnalysisManager(analysis_type_id);
				if (analysisManager != null)
					return analysisManager.analyse(analysis, measurementResult);
				else
					Log.errorMessage("Cannot find analysis manager for analysis type '" + analysis_type_id + "' of test '" + this.test.getId() + "'");
			}
		}
		return null;
	}

	private Result evaluate(Result analysisResult, Result measurementResult) {
		Identifier evaluation_type_id = this.test.getEvaluationTypeId();
		if (!evaluation_type_id.equals("")) {
			Evaluation evaluation = null;
			Measurement measurement = measurementResult.getMeasurement();
			try {
				/*!!	During creation of a test we must consider:
				 * 		1. dependency among analysis_type_id, evaluation_type_id and measurement_type_id;
				 *    2. appropriate MeasurementSetup, i. e. criteria, thresholds and etalon are present.
				 *    */
				 evaluation = Evaluation.create(MeasurementControlModule.createIdentifier("evaluation"),
																				evaluation_type_id,
																				measurement.getSetup().getThresholdSet(),
																				measurement.getSetup().getEtalon(),
																				this.test.getMonitoredElement().getId());
			}
			catch (Exception e) {
				Log.errorException(e);
			}
			if (evaluation != null) {
				EvaluationManager evaluationManager = EvaluationManager.getEvaluationManager(evaluation_type_id);
				if (evaluationManager != null)
					return evaluationManager.evaluate(evaluation, analysisResult, measurementResult);
				else
					Log.errorMessage("Cannot find evaluation manager for evaluation type '" + evaluation_type_id + "' of test '" + this.test.getId() + "'");
			}
		}
		return null;
	}

	void cleanup() {
		this.measurementResultQueue.clear();
		this.transceiver = null;
		this.test = null;
		MeasurementControlModule.testProcessors.remove(this);
	}
}