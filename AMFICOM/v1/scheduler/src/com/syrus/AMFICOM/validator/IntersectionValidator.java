/*-
* $Id: IntersectionValidator.java,v 1.1 2006/02/16 12:22:45 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
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


/**
 * @version $Revision: 1.1 $, $Date: 2006/02/16 12:22:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class IntersectionValidator {
	
	private final boolean	useLoader;

	public IntersectionValidator(final boolean useLoader) {
		this.useLoader = useLoader;
	}

	private Set<Test> getTests(final Date startInterval,
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

		final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>(3);
		conditions.add(startTypicalCondition);
		conditions.add(endTypicalCondition);
		conditions.add(monitoredElementsCondition);
		
		final CompoundCondition compoundCondition = 
			new CompoundCondition(conditions,
					CompoundConditionSort.AND);
		
		final Set<Test> tests = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, this.useLoader);
		
		return tests;
	}
	
//	private boolean selfIntersection(final Date startDate,
// 		final Date endDate,
// 		final AbstractTemporalPattern<?> temporalPattern,
// 		final long measurementDuration) {
// 		
// 		if (temporalPattern == null) {
// 			return false;
// 		}
// 		
// 		final long interval = 30L * 24L * 60L * 60L * 1000L;
// 		
// 		Date start = startDate;
// 		Date previousDate = null;
// 		while(start.compareTo(endDate) <= 0) {
// 			final Date end = start.getTime() + interval < endDate.getTime() ? 
// 					new Date(start.getTime() + interval) : 
// 					endDate;
// 			final SortedSet<Date> times = temporalPattern.getTimes(startDate, endDate, start, end);
// 			for (final Date date : times) {
// 				if (previousDate == null) {
// 					previousDate = date;
// 				} else {
// 					final long diff = date.getTime() - previousDate.getTime();
//					if (diff < measurementDuration) {
// 						assert Log.debugMessage("previousDate:" 
// 								+ previousDate 
// 								+ ", date:" 
// 								+ date
// 								+ ",\n\t interval:" + measurementDuration
// 								+ ", diff:" + diff, 
// 							Log.DEBUGLEVEL03);
// 						return  true;
// 					}
// 				}
// 			}
// 			
// 			if (start.equals(end)) {
// 				break;
// 			}
// 			start = end;
// 		}
// 		
// 		return false;
// 	}
	
//	private SortedSet<Date> getTestTimes(final AbstractTemporalPattern<?> temporalPattern,
//		final Date startDate,
//		final Date endDate,
//		final Date startInterval,
//		final Date endInterval,
//		final long offset) {
//		
//		final Date startTime0 = offset == 0 ? startDate : new Date(startDate.getTime() + offset);
//		final Date endTime0 = offset == 0 ? endDate : new Date(endDate.getTime() + offset);
//		
////		assert Log.debugMessage("startDate:" + startDate 
////				+ ", endDate:" + endDate
////				+ ", startTime0:" + startTime0
////				+ ", endTime0:" + endTime0
////				+ ", startInterval: " + startInterval
////				+ ", endInterval:" + endInterval, 
////				Log.DEBUGLEVEL03);
//		final SortedSet<Date> times;
//		if (temporalPattern != null) {
//			times = temporalPattern.getTimes(startTime0,
//						endTime0,
//						startInterval,
//						endInterval);
//		} else {
//			times = new TreeSet<Date>();
//			times.add(startTime0);
//		}
//		return times;
//	}
	
//	private SortedSet<Date> getTestTimes(final Identifier temporalPatternId,
//		final Date startDate,
//		final Date endDate,
//		final Date startInterval,
//		final Date endInterval,
//		final long offset) throws ApplicationException{
//		
//		if (temporalPatternId != null && !temporalPatternId.isVoid()) {
//			final AbstractTemporalPattern temporalPattern = 
//				StorableObjectPool.getStorableObject(temporalPatternId, true);
//			return this.getTestTimes(temporalPattern, 
//				startDate, 
//				endDate, 
//				startInterval, 
//				endInterval, 
//				offset);
//		}
//		return this.getTestTimes((AbstractTemporalPattern)null, 
//			startDate, 
//			endDate, 
//			startInterval, 
//			endInterval, 
//			offset);
//	}
	
//	private SortedSet<Date> getTestTimes(final Test test,
//		final Date startInterval,
//		final Date endInterval,
//		final long offset) throws ApplicationException{
//		return this.getTestTimes(test, test.getStartTime(), test.getEndTime(), startInterval, endInterval, offset);
//	}
	
//	private SortedSet<Date> getTestTimes(final Test test,
//		final Date startTime,
//		final Date endTime,
//		final Date startInterval,
//		final Date endInterval,
//		final long offset) throws ApplicationException{		
//		
//		final SortedSet<Date> testTimes = this.getTestTimes(test.getTemporalPatternId(), startTime, endTime, startInterval, endInterval, 0L);
//		
//		final TestStatus status = test.getStatus();
//		if (status == TestStatus.TEST_STATUS_STOPPED ||  
//				status == TestStatus.TEST_STATUS_STOPPING) {			
//			final SortedMap<Date, String> stoppingMap = test.getStoppingMap();
//			final Date stopDate = stoppingMap.lastKey();
//			Log.debugMessage("stopDate:" + stopDate,
//				Log.DEBUGLEVEL10);
//			final SortedSet<Date> tailSet2 = testTimes.tailSet(stopDate);
//			Log.debugMessage("clear tailSet from " + stopDate + " : " + tailSet2,
//				Log.DEBUGLEVEL10);
//			tailSet2.clear();			
//		}
//		
//		if (offset != 0 && !testTimes.isEmpty()) {
//			final SortedSet<Date> testTimesOffsetted = new TreeSet<Date>();
//			for (final Date date : testTimes) {
//				testTimesOffsetted.add(new Date(date.getTime() + offset));
//			}
//			return testTimesOffsetted;
//		}
//		
//		return testTimes;
//	}
	
	private String isValid(final Test test, 
	                       final MeasurementSetup measurementSetup,
	                       final Date startDate,
	                       final Date endDate,
	                       final long offset) throws ApplicationException {
		final Identifier temporalPatternId = test.getTemporalPatternId();
		final AbstractTemporalPattern<?> testPattern;
		if (temporalPatternId.isVoid()) {
			testPattern = null; 
		} else {
			testPattern =  StorableObjectPool.getStorableObject(temporalPatternId, true);
		}
		final MeasurementSetup measurementSetup0;
		if (measurementSetup == null) {
			final Identifier mainMeasurementSetupId = test.getMainMeasurementSetupId();
			measurementSetup0 = 
				StorableObjectPool.getStorableObject(mainMeasurementSetupId, true);
		} else {
			measurementSetup0 = measurementSetup;
		}
		
		final long measurementDuration = measurementSetup0.getMeasurementDuration();
		
		final Date startDate1 = new Date(startDate != null ? startDate.getTime() : test.getStartTime().getTime()  + offset);
		final Date endDate1 =  new Date(endDate != null ? endDate.getTime() : test.getEndTime().getTime() + offset);
		
		final TimeLabel timeLabel;
		try {
			timeLabel = 
				this.getTimeLabel(startDate1, endDate1, testPattern, measurementDuration);
		} catch (final IllegalDataException e) {
			return e.getMessage();
		}
		
		final Date endDate0 = 
			new Date(endDate1.getTime() + measurementDuration);
		
		final Set<Test> tests = this.getTests(startDate1, endDate0, test.getMonitoredElementId());
		for (final Test aTest : tests) {
			if (aTest.equals(test)) {
				continue;
			}
			final String reason = this.isIntersects(timeLabel, test);
			if (reason != null) {
				return reason;
			}
		}
		
		return null;
	}
	
	public String isValid(final Test test, final MeasurementSetup measurementSetup) 
	throws ApplicationException {		
		return this.isValid(test, measurementSetup, null, null, 0L);
	}
	
	public String isValid(final Test test, final long offset) throws ApplicationException {
		return this.isValid(test, null, null, null, offset);
	}
	
	public String isValid(final Test test, final Date startDate, final Date endDate) 
	throws ApplicationException {		
		return this.isValid(test, null, startDate, endDate, 0L);
	}
	
	public String isValid(final Identifier monitoredElementId,
       		final Date startDate,
       		final Date endDate,
       		final AbstractTemporalPattern temporalPattern,
       		final MeasurementSetup measurementSetup) 
       	throws ApplicationException {
		
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		
		final TimeLabel timeLabel;
		try {
			timeLabel = 
				this.getTimeLabel(startDate, endDate, temporalPattern, measurementDuration);
		} catch (final IllegalDataException e) {
			return e.getMessage();
		}
		
		final Date endDate0 = 
			new Date(endDate.getTime() + measurementDuration);
		
		final Set<Test> tests = this.getTests(startDate, endDate0, monitoredElementId);
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
		final Identifier temporalPatternId = test.getTemporalPatternId();
		final AbstractTemporalPattern<?> testPattern;
		if (temporalPatternId.isVoid()) {
			testPattern = null; 
		} else {
			testPattern =  StorableObjectPool.getStorableObject(temporalPatternId, true);
		}
		final Identifier testMainMeasurementSetupId = test.getMainMeasurementSetupId();
		final MeasurementSetup testMeasurementSetup = 
			StorableObjectPool.getStorableObject(testMainMeasurementSetupId, true);
		final TimeLabel testTimeLabel = this.getTimeLabel(test.getStartTime(), 
			test.getEndTime(), 
			testPattern, 
			testMeasurementSetup.getMeasurementDuration());
		if (testTimeLabel.intersects(timeLabel)) {
			final TestView view = TestView.valueOf(test);			
			return I18N.getString("Scheduler.Text.Scheduler.Model.TestIntersection")
				+ (view != null ? " "  + view.getExtendedDescription() : "");
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
			assert startTime == endTime : "Single test must has equal start and end dates " + startDate + ", " + endDate;
			final Range range = new Range(startTime, startTime + measurementDuration - 1);
			timeLabel = new OnceTimeLabel(range);
		} else {
			if (temporalPattern instanceof PeriodicalTemporalPattern) {
				final PeriodicalTemporalPattern periodicalTemporalPattern = 
					(PeriodicalTemporalPattern) temporalPattern;
				final long period = periodicalTemporalPattern.getPeriod();
				
				if (period < measurementDuration) {
					throw new IllegalDataException(I18N.getString("Scheduler.Text.Scheduler.Model.SelfIntersection"));
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
	
//	@Deprecated
//	private String isValid0(final Identifier monitoredElementId,
//		final Date startDate,
//		final Date endDate,
//		final AbstractTemporalPattern temporalPattern,
//		final MeasurementSetup newTestMeasurementSetup) 
//	throws ApplicationException {
//
//		final long measurementDuration = newTestMeasurementSetup.getMeasurementDuration();
//
//		if (this.selfIntersection(startDate, endDate, temporalPattern, measurementDuration)) {
//			return I18N.getString("Scheduler.Text.Scheduler.Model.SelfIntersection");
//		}
//		
//		final Date endDate0 = 
//			new Date(endDate.getTime() + measurementDuration);
//		
//		// XXX can be performance problem with too long interval		
//		final Set<Test> tests = this.getTests(startDate, endDate0, monitoredElementId);
//		// XXX can be performance problem with too long interval
//		
//		final SortedSet<Date> times = 
//			this.getTestTimes(temporalPattern, startDate, endDate, startDate, endDate, 0L);
//		assert Log.debugMessage("times.size():" + times.size(), Log.DEBUGLEVEL03);
//		
//		for (final Test test : tests) {			
//			final Date testStartTime = test.getStartTime();
//			
//			if (testStartTime.after(endDate0)) {
//				// skip test lies after end of interval 
//				continue;
//			}
//			
//			final Date testEndTime0 = test.getEndTime();
//			final Identifier testMainMeasurementSetupId = test.getMainMeasurementSetupId();
//			final MeasurementSetup testMeasurementSetup = 
//				StorableObjectPool.getStorableObject(testMainMeasurementSetupId, true);
//			
//			final long testMeasurementDuration = testMeasurementSetup.getMeasurementDuration();
//			final Date testEndTime = 
//				new Date(testEndTime0.getTime() + testMeasurementDuration);
//			
//			if (testEndTime.before(startDate)) {
//				// skip test lies before start of interval
//				continue;
//			}
//			
//			// XXX can be performance problem with too long interval
//			final SortedSet<Date> testTimes = 
//				this.getTestTimes(test, testStartTime, testEndTime0, 0L);
//			
//			for (final Date date : testTimes) {
//				final long st0 = date.getTime();
//				final long en0 = st0 + testMeasurementDuration;
//				
//				final SortedSet<Date> headSet = times.headSet(new Date(en0));
//				if (!headSet.isEmpty()) {
//					final Date date2 = headSet.last();
//					if (date2.getTime() + measurementDuration > st0) {
//						return I18N.getString("Scheduler.Text.Scheduler.Model.TestIntersection");
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	
}

