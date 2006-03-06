/*-
 * $Id: TestProcessor.java,v 1.90.2.2 2006/03/06 14:15:26 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_NOT_EQUALS;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_ABORTED;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_COMPLETED;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_STATUS;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_PROCESSING;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_SCHEDULED;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.AnalysisResultParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.90.2.2 $, $Date: 2006/03/06 14:15:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
abstract class TestProcessor extends SleepButWorkThread {
	static final long PAST_MEASUREMENT_TIMEOUT = 30 * 1000; //msec
	static final long PAST_TEST_TIMEOUT = PAST_MEASUREMENT_TIMEOUT * 2;
	private static final String ABORT_REASON_DATABASE_ERROR = "Database error";

	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_CREATE_IDENTIFIER = 1;
	public static final int FALL_CODE_CREATE_MEASUREMENT = 2;

	private Test test;
	long measurementDuration;

	private Date lastMeasurementStartTime;
	private Date nextMeasurementStartTime;
	private int numberOfMResults;
	private boolean lastMeasurementAcquisition;

	private List<MeasurementResultParameter> measurementResults;

	private Transceiver transceiver;

	private boolean running;

	public TestProcessor(final Test test) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));
		super.setName("TestProcessor " + test.getId());

		this.test = test;

		MeasurementControlModule.putTestProcessor(this);

		this.measurementResults = new LinkedList<MeasurementResultParameter>();

		this.running = true;

		try {
			final Identifier kisId = test.getKISId();
			this.transceiver = MeasurementControlModule.transceivers.get(kisId);
			if (this.transceiver == null) {
				Log.errorMessage("Cannot find transceiver for kis '" + kisId + "'");
				this.shutdown();
				return;
			}			
		} catch (ApplicationException ae) {
			Log.errorMessage("Cannot load data for test '" + this.test.getId() + "'");
			Log.errorMessage(ae);
			this.shutdown();
			return;
		}

		this.setupMeasurements();

		if (this.test.getStatus() != TestStatus.TEST_STATUS_PROCESSING) {
			test.setStatus(TestStatus.TEST_STATUS_PROCESSING);
			try {
				StorableObjectPool.flush(test, LoginManager.getUserId(), false);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		}
	}

	private void setupMeasurements() {
		final Identifier testId = this.test.getId();

		final TestStatus testStatus = this.test.getStatus();
		if (testStatus != TEST_STATUS_SCHEDULED && testStatus != TEST_STATUS_PROCESSING) {
			Log.errorMessage("ERROR: Test '" + testId + "' has status " + testStatus
					+ " -- not SCHEDULED or PROCESSING; shutting down");
			this.shutdown();
			return;
		}

		final int numberOfMeasurements = this.test.getNumberOfMeasurements();
		Log.debugMessage("Test '" + testId + "' -- number of measurements: " + numberOfMeasurements, Log.DEBUGLEVEL06);
		if (numberOfMeasurements == 0) {
			this.numberOfMResults = 0;
			this.lastMeasurementStartTime = null;
			return;
		}

		final StorableObjectDatabase<Measurement> mDatabase = DatabaseContext.getDatabase(MEASUREMENT_CODE);
		final MeasurementDatabase measurementDatabase = (MeasurementDatabase) mDatabase;
		Measurement lastMeasurement = null;
		try {
			lastMeasurement = measurementDatabase.retrieveLast(this.test.getId());
		} catch (RetrieveObjectException roe) {
			Log.errorMessage(roe);
			this.abort(ABORT_REASON_DATABASE_ERROR);
		} catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Last measurement for test '" + testId + "' not found; assume test has none measurements",
					Log.DEBUGLEVEL06);
			this.numberOfMResults = 0;
			this.lastMeasurementStartTime = null;
			return;
		}
		final ActionStatus lastMeasurementStatus = lastMeasurement.getStatus();
		this.lastMeasurementStartTime = lastMeasurement.getStartTime();

		try {
			final LinkedIdsCondition measurementResultParameterCondition = new LinkedIdsCondition(testId, MEASUREMENTRESULTPARAMETER_CODE);
			final Set<Identifier> measurementResultParameterIds = StorableObjectPool.getIdentifiersByCondition(measurementResultParameterCondition, true);
			this.numberOfMResults = measurementResultParameterIds.size();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			this.abort(ABORT_REASON_DATABASE_ERROR);
		}
		Log.debugMessage("Test '" + testId + "' -- last measurement: " + lastMeasurement.getId()
				+ " (status: " + lastMeasurementStatus
				+ "), number of measurement results: " + this.numberOfMResults,
				Log.DEBUGLEVEL06);

		switch (lastMeasurementStatus) {
			case ACTION_STATUS_NEW:
				this.transceiver.addMeasurement(lastMeasurement, this);
				break;
			case ACTION_STATUS_RUNNING:
				this.transceiver.addAcquiringMeasurement(lastMeasurement, this);
				break;
			case ACTION_STATUS_COMPLETED:
				try {
					final Set<MeasurementResultParameter> lastMeasurementResultParameters = lastMeasurement.getActionResultParameters();
					if (!lastMeasurementResultParameters.isEmpty()) {
						//-Число результатов измерений уже вычислено, не нужно позволять методу addMeasurementResult его изменять
						final int numberOfMResultsSave = this.numberOfMResults;
						for (final MeasurementResultParameter measurementResultParameter : lastMeasurementResultParameters) {
							this.addMeasurementResult(measurementResultParameter);
						}
						this.numberOfMResults = numberOfMResultsSave;
					} else {
						Log.errorMessage("ERROR: Cannot find result for acquired measurement '" + lastMeasurement.getId()
								+ "'; setting measurement as ABORTED");
						lastMeasurement.setStatus(ACTION_STATUS_ABORTED);
					}
				} catch (ApplicationException ae) {
					Log.errorMessage("ERROR: Cannot load results for acquired measurement '" + lastMeasurement.getId()
							+ "'; setting measurement as ABORTED");
					lastMeasurement.setStatus(ACTION_STATUS_ABORTED);
				}
				break;
			default:
				Log.debugMessage("Test '" + testId + "' -- status of last measurement: " + lastMeasurementStatus, Log.DEBUGLEVEL06);
		}

		try {
			StorableObjectPool.flush(lastMeasurement, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	final void addMeasurementResultParameters(final Set<MeasurementResultParameter> measurementResultParameters) {
		assert measurementResultParameters != null : NON_NULL_EXPECTED;

		synchronized (this.measurementResults) {
			for (final MeasurementResultParameter measurementResultParameter : measurementResultParameters) {
				if (!this.measurementResults.contains(measurementResultParameter)) {
					this.measurementResults.add(measurementResultParameter);
					this.numberOfMResults++;
				}
			}
		}
	}

	final void addMeasurementResult(final MeasurementResultParameter measurementResult) {
		synchronized (this.measurementResults) {
			if (!this.measurementResults.contains(measurementResult)) {
				this.measurementResults.add(measurementResult);
				this.numberOfMResults++;
			}
		}
	}

	// -Return null if no more measurements to run
	abstract Date getNextMeasurementStartTime(final Date fromDate, final boolean includeFromDate);

	@Override
	public final void run() {
		while (this.running) {

			if (!this.lastMeasurementAcquisition) {
				if (this.nextMeasurementStartTime == null) {
					if (this.lastMeasurementStartTime == null) {
						this.nextMeasurementStartTime = this.getNextMeasurementStartTime(this.test.getStartTime(), true);
					} else {
						this.nextMeasurementStartTime = this.getNextMeasurementStartTime(this.lastMeasurementStartTime, false);
					}
					if (this.nextMeasurementStartTime == null) {
						this.lastMeasurementAcquisition = true;
						Log.debugMessage("Test '" + this.test.getId() + "' | Last measurement acquisition", Log.DEBUGLEVEL06);
					} else {
						Log.debugMessage("Test '" + this.test.getId() + "' | Next measurement at: " + this.nextMeasurementStartTime,
								Log.DEBUGLEVEL06);
					}
				} else {
					if (this.nextMeasurementStartTime.getTime() <= System.currentTimeMillis()) {
						try {
							this.newMeasurementCreation(this.nextMeasurementStartTime);
							this.nextMeasurementStartTime = null;
							super.clearFalls();
						} catch (CreateObjectException coe) {
							Log.errorMessage(coe);
							if (coe.getCause() instanceof IllegalObjectEntityException) {
								super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
							} else {
								super.fallCode = FALL_CODE_CREATE_MEASUREMENT;
							}
							super.sleepCauseOfFall();
						}
					}
				}
			}

			synchronized (this.measurementResults) {
				this.processMeasurementResults();
				this.checkIfOver();
			}

			try {
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorMessage(ie);
			}
		}
	}

	private final void newMeasurementCreation(final Date startTime) throws CreateObjectException {
		final Measurement measurement = this.test.createMeasurement(LoginManager.getUserId(), startTime);
		this.transceiver.addMeasurement(measurement, this);
		this.lastMeasurementStartTime = startTime;
		this.measurementDuration = measurement.getDuration();
		try {
			StorableObjectPool.flush(measurement, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	private final void processMeasurementResults() {
		final Set<Identifiable> objectsToFlush = new HashSet<Identifiable>();

		for (final Iterator<MeasurementResultParameter> it = this.measurementResults.iterator(); it.hasNext();) {
			final MeasurementResultParameter measurementResult = it.next();
			try {
				final Measurement measurement = measurementResult.getAction();
				final Set<AnalysisResultParameter> analysisResultParameters = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
				objectsToFlush.addAll(analysisResultParameters);
				measurement.setStatus(ACTION_STATUS_COMPLETED);
				objectsToFlush.add(measurement);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			} catch (AnalysisException ae) {
				Log.errorMessage(ae);
			}

			it.remove();
		}

		try {
			StorableObjectPool.flush(objectsToFlush, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	private void checkIfOver() {
		final int numberOfMeasurements = this.test.getNumberOfMeasurements();

		final StringBuffer mesg = new StringBuffer("\n");
		mesg.append("'");
		mesg.append(this.test.getId());
		mesg.append("' on '");
		String kisIdString;
		try {
			kisIdString = this.test.getKISId().toString();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			kisIdString = "UNKNOWN";
		}
		mesg.append(kisIdString);
		mesg.append("', '");
		mesg.append(this.test.getMonitoredElementId());
		mesg.append("':\n");
		mesg.append("\t numberOfMeasurements: ");
		mesg.append(numberOfMeasurements);
		mesg.append("\n");
		mesg.append("\t numberOfMResults: ");
		mesg.append(this.numberOfMResults);
		mesg.append("\n");
		mesg.append("\t startTime: ");
		mesg.append(this.test.getStartTime());
		mesg.append("\n");
		if (this.lastMeasurementAcquisition) {
			mesg.append("\t last measurement acquisition");
		} else {
			mesg.append("\t nextMeasurementStartTime: ");
			mesg.append(this.nextMeasurementStartTime);
		}
		Log.debugMessage(mesg.toString(), Log.DEBUGLEVEL07);

		if (this.lastMeasurementAcquisition
				&& (this.numberOfMResults >= numberOfMeasurements
						|| System.currentTimeMillis() >= this.test.getEndTime().getTime() + this.measurementDuration + PAST_TEST_TIMEOUT)) {
			this.complete();
		}
	}

	@Override
	protected final void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_CREATE_IDENTIFIER:
				this.continueWithNextMeasurement();
				break;
			case FALL_CODE_CREATE_MEASUREMENT:
				this.continueWithNextMeasurement();
				break;
			default:
				Log.errorMessage("Unknown error code: " + super.fallCode);
		}
	}

	private void continueWithNextMeasurement() {
		this.nextMeasurementStartTime = null;
	}

	final Identifier getTestId() {
		return this.test.getId();
	}

	private void complete() {
		Log.debugMessage("Test '" + this.test.getId() + "' is completed", Log.DEBUGLEVEL07);
		this.transceiver.removeMeasurementsOfTestProcessor(this);

		this.test.setStatus(TestStatus.TEST_STATUS_COMPLETED);
		try {
			StorableObjectPool.flush(this.test, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		this.shutdown();
	}

	void abort(final String abortReason) {
		this.test.addStop(abortReason);
		this.test.setStatus(TestStatus.TEST_STATUS_ABORTED);
		try {
			StorableObjectPool.flush(this.test, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		this.stopTest();
	}

	void stopTest() {
		if (this.transceiver != null) {
			this.transceiver.removeMeasurementsOfTestProcessor(this);
		}

		try {
			final LinkedIdsCondition lic = new LinkedIdsCondition(this.test.getId(), MEASUREMENT_CODE);
			final TypicalCondition tc1 = new TypicalCondition(ACTION_STATUS_COMPLETED,
					OPERATION_NOT_EQUALS,
					MEASUREMENT_CODE,
					COLUMN_STATUS);
			final TypicalCondition tc2 = new TypicalCondition(ACTION_STATUS_ABORTED,
					OPERATION_NOT_EQUALS,
					MEASUREMENT_CODE,
					COLUMN_STATUS);
			final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>();
			conditions.add(lic);
			conditions.add(tc1);
			conditions.add(tc2);
			final CompoundCondition condition = new CompoundCondition(conditions, CompoundConditionSort.AND);
			final Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (final Measurement measurement : measurements) {
				measurement.setStatus(ACTION_STATUS_ABORTED);
			}
			StorableObjectPool.flush(measurements, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}

		this.shutdown();
	}

	void shutdown() {
		this.running = false;
		this.cleanup();
	}

	private void cleanup() {
		this.measurementResults.clear();
		MeasurementControlModule.removeTestProcessor(this.test.getId());
		this.test = null;
	}

}
