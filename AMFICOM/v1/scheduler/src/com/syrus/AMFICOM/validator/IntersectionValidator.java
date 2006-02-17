/*-
* $Id: IntersectionValidator.java,v 1.4 2006/02/17 09:51:58 bob Exp $
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
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2006/02/17 09:51:58 $
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
		
		final Set<Test> tests = this.getTests(startDate1, endDate1, test.getMonitoredElementId());
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
		
//		assert Log.debugMessage("startDate: " 
//				+ startDate 
//				+ ", endDate0:" 
//				+ endDate, 
//			Log.DEBUGLEVEL03);
		
		final Set<Test> tests = this.getTests(startDate, endDate, monitoredElementId);
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
	
}
