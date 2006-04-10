/*-
* $Id: IntersectionValidator.java,v 1.10.4.1 2006/04/10 11:46:00 saa Exp $
*
* Copyright © 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.validator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestView;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.Test.TestStatus;


/**
 * @version $Revision: 1.10.4.1 $, $Date: 2006/04/10 11:46:00 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class IntersectionValidator {
	
	private final boolean	useLoader;

	private static final TestStatus ACTIVE_TEST_STATES[] = new TestStatus[] {
			/*
			 * Измерения этих тестов запланированы
			 */
			TestStatus.TEST_STATUS_NEW,		
			TestStatus.TEST_STATUS_PROCESSING,
			TestStatus.TEST_STATUS_SCHEDULED,
			/*
			 * Измерения этих тестов все еще могут быть выполнены
			 */
			TestStatus.TEST_STATUS_STOPPING,
			/*
			 * Измерения этих тестов уже выполнены - и нечего задавать тесты
			 * на прошлое.
			 */
			TestStatus.TEST_STATUS_COMPLETED
	};

	public IntersectionValidator(final boolean useLoader) {
		this.useLoader = useLoader;
	}

	/**
	 * Определяет множество тестов на данном me и данном интервале времени,
	 *   которые нельзя пересекать вновь создаваемыми тестами.
	 * @param startInterval
	 * @param endInterval
	 * @param monitoredElementId
	 * @return список тестов,
	 *   которые нельзя пересекать вновь создаваемыми тестами.
	 * @throws ApplicationException
	 */
	private Set<Test> getNonIntersectiveTests(final Date startInterval,
			final Date endInterval,
			final Identifier monitoredElementId) throws ApplicationException{
		final TypicalCondition startTypicalCondition = 
			new TypicalCondition(endInterval,
				endInterval,
				OperationSort.OPERATION_LESS_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_START_TIME);
		final TypicalCondition endTypicalCondition = 
			new TypicalCondition(startInterval,
				startInterval,
				OperationSort.OPERATION_GREAT_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_END_TIME);
		
		final MonitoredElement monitoredElement1 = 
			StorableObjectPool.getStorableObject(monitoredElementId, true);
		
		final MeasurementPort measurementPort = 
			StorableObjectPool.getStorableObject(monitoredElement1.getMeasurementPortId(), true);
		
		final LinkedIdsCondition measurementPortCondition = 
			new LinkedIdsCondition(measurementPort.getKISId(), ObjectEntities.MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> ports = 
			StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, false);
		final LinkedIdsCondition measurementElementCondition = 
			new LinkedIdsCondition(ports, ObjectEntities.MONITOREDELEMENT_CODE);
		final Set<MonitoredElement> monitoredElements = 
			StorableObjectPool.getStorableObjectsByCondition(measurementElementCondition, false);

		final LinkedIdsCondition monitoredElementsCondition = 
			new LinkedIdsCondition(monitoredElements, ObjectEntities.TEST_CODE);

		final Set<StorableObjectCondition> stateConditions =
			new HashSet<StorableObjectCondition>(ACTIVE_TEST_STATES.length);
		for (TestStatus state: ACTIVE_TEST_STATES) {
			stateConditions.add(new TypicalCondition(state,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_STATUS));
		}

		final StorableObjectCondition stateCompound =
			new CompoundCondition(stateConditions, CompoundConditionSort.OR);

		final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>(4);
		conditions.add(startTypicalCondition);
		conditions.add(endTypicalCondition);
		conditions.add(monitoredElementsCondition);
		conditions.add(stateCompound);

		final CompoundCondition compoundCondition = 
			new CompoundCondition(conditions, CompoundConditionSort.AND);
		
		final Set<Test> tests = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, this.useLoader);
		
		return tests;
	}
	
	private String isValid(final Test test, 
	                       final MeasurementSetup measurementSetup,
	                       final AbstractTemporalPattern pattern,
	                       final Date startDate,
	                       final Date endDate,
	                       final long offset) throws ApplicationException {
		final Identifier temporalPatternId = test.getTemporalPatternId();
		final AbstractTemporalPattern testPattern;
		
		if (pattern != null) {
			testPattern = pattern;
		} else {
			if (temporalPatternId.isVoid()) {
				testPattern = null; 
			} else {
				testPattern =  StorableObjectPool.getStorableObject(temporalPatternId, true);
			}
		}
		
		final MeasurementSetup measurementSetup0;
		if (measurementSetup == null) {
			final Identifier mainMeasurementSetupId = test.getCurrentMeasurementSetupId();
			measurementSetup0 = 
				StorableObjectPool.getStorableObject(mainMeasurementSetupId, true);
		} else {
			measurementSetup0 = measurementSetup;
		}
		
		final long measurementDuration = measurementSetup0.calcTotalDuration();
		
		final Date startDate1 = new Date(startDate != null ? startDate.getTime() : test.getStartTime().getTime()  + offset);
		final Date endDate1 =  new Date(endDate != null ? endDate.getTime() : test.getEndTime().getTime() + offset);
		
		final TimeLabel timeLabel;
		try {
			timeLabel = 
				this.getTimeLabel(startDate1, endDate1, testPattern, measurementDuration);
		} catch (final IllegalDataException e) {
			return e.getMessage();
		}
		
		final Set<Test> tests = this.getNonIntersectiveTests(startDate1, endDate1, test.getMonitoredElementId());
		for (final Test aTest : tests) {
			if (aTest.equals(test)) {
				continue;
			}
			final String reason = this.isIntersects(timeLabel, aTest);
			if (reason != null) {
				return reason;
			}
		}
		
		return null;
	}
	
	public String isValid(final Test test, final MeasurementSetup measurementSetup) 
	throws ApplicationException {		
		return this.isValid(test, measurementSetup, null, null, null, 0L);
	}
	
	public String isValid(final Test test, final long offset) throws ApplicationException {
		return this.isValid(test, null, null, null, null, offset);
	}

	public String isValid(final Test test, final AbstractTemporalPattern temporalPattern) 
	throws ApplicationException {
		return this.isValid(test, null, temporalPattern, null, null, 0L);
	}
	
	public String isValid(final Test test, final Date startDate, final Date endDate) 
	throws ApplicationException {		
		return this.isValid(test, null, null, startDate, endDate, 0L);
	}
	
	public String isValid(final Identifier monitoredElementId,
       		final Date startDate,
       		final Date endDate,
       		final AbstractTemporalPattern temporalPattern,
       		final MeasurementSetup measurementSetup) 
       	throws ApplicationException {
		
		final long measurementDuration = measurementSetup.calcTotalDuration();
		
		final TimeLabel timeLabel;
		try {
			timeLabel = 
				this.getTimeLabel(startDate, endDate, temporalPattern, measurementDuration);
		} catch (final IllegalDataException e) {
			return e.getMessage();
		}
		
//		assert Log.debugMessage("startDate: " 
//				+ startDate 
//				+ ", endDate0:" 
//				+ endDate, 
//			Log.DEBUGLEVEL03);
		
		final Set<Test> tests = this.getNonIntersectiveTests(startDate, endDate, monitoredElementId);
		for (final Test test : tests) {
			final String reason = this.isIntersects(timeLabel, test);
			if (reason != null) {
				return reason;
			}
		}
		
		return null;
	}
	
	private String isIntersects(final TimeLabel timeLabel, final Test test) 
	throws ApplicationException {
		System.out.println("isIntersects(): " + timeLabel + " vs " + test);

		final Identifier temporalPatternId = test.getTemporalPatternId();
		final AbstractTemporalPattern testPattern;
		if (temporalPatternId.isVoid()) {
			testPattern = null; 
		} else {
			testPattern =  StorableObjectPool.getStorableObject(temporalPatternId, true);
		}
		final Identifier testMainMeasurementSetupId = test.getCurrentMeasurementSetupId();
		final MeasurementSetup testMeasurementSetup = 
			StorableObjectPool.getStorableObject(testMainMeasurementSetupId, true);
		final TimeLabel testTimeLabel = this.getTimeLabel(test.getStartTime(), 
			test.getEndTime(), 
			testPattern, 
			testMeasurementSetup.calcTotalDuration());
		if (TimeLabel.patternsIntersect(testTimeLabel, timeLabel)) {
			final TestView view = TestView.valueOf(test);
			return I18N.getString("Scheduler.Text.Scheduler.Model.TestIntersection")
				+ (view != null ? " "  + view.getExtendedDescription() : test.getId());
		}
		return null;
	}

	private final TimeLabel getTimeLabel(final Date startDate,
    		final Date endDate,
       		final AbstractTemporalPattern temporalPattern,
       		final long measurementDuration) throws IllegalDataException {
		
		final long startTime = startDate.getTime();
		final long endTime = endDate.getTime();
		
		final TimeLabel timeLabel;
		if (temporalPattern == null) {
			final Range range;
			// XXX bypass for old test time politic
			if (startTime == endTime) {
				range = new Range(startTime, endTime + measurementDuration - 1);
			} else {
				range = new Range(startTime, endTime - 1);
			}
			timeLabel = new OnceTimeLabel(range);
		} else {
			if (temporalPattern instanceof PeriodicalTemporalPattern) {
				final PeriodicalTemporalPattern periodicalTemporalPattern = 
					(PeriodicalTemporalPattern) temporalPattern;
				final long period = periodicalTemporalPattern.getPeriod();
				
				if (period < measurementDuration) {
					throw new IllegalDataException(I18N.getString("Scheduler.Text.Scheduler.Model.PediodLessThanMeasurementDuration"));
				}
				
				timeLabel = new PeriodicalTimeLabel(startTime, 
					measurementDuration,
					period,
					(int)(1 + (endTime - startTime) / period));
			} else {
				throw new UnsupportedOperationException("Unsupported temporal pattern : " 
					+ temporalPattern.getClass().getName());
			}
		}
		return timeLabel;
	}
	
}
