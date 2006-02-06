/*-
* $Id: TestView.java,v 1.20 2006/02/06 10:49:24 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.reflectometry.MeasurementReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;


/**
 * @version $Revision: 1.20 $, $Date: 2006/02/06 10:49:24 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public final class TestView {
	private static final Map<Identifiable, TestView> MAP = new HashMap<Identifiable, TestView>();
	
	private final Test test;
	
	private Date firstDate;
	private Date lastDate;
	
	private MeasurementSetup measurementSetup;
	
	private String kisName;
	private String kisDescription;
	private String portName;	
	
	private String temporalType;
	private String temporalName;
	private String monitoredElementName;
	private String testQ;
	private String testD;
	
	private SortedSet<Measurement> measurements;

	private Date	start;
	private Date	end;

	private String	extendedDescription;

	private TestStatus	status;

	private Measurement	qualityMeasurement;	
	
	private TestView(final Test test,
	                 final Date start,
	                 final Date end) throws ApplicationException {
		this.test = test;
		this.start = start;
		this.end = end;
		
		this.createStartEndTimes();
		
		final long t0 = System.currentTimeMillis();
		this.createKISName();
		final long t1 = System.currentTimeMillis();
		this.createMeasurementPortName();		
		final long t2 = System.currentTimeMillis();
		this.createTemporalNames();
		final long t3 = System.currentTimeMillis();
		this.createMeasurements();
		final long t4 = System.currentTimeMillis();
		this.createQuality();
		final long t5 = System.currentTimeMillis();
		this.createStatus();
		
		Log.debugMessage(test + " createKISName " + (t1-t0), Log.DEBUGLEVEL03);
		Log.debugMessage(test + " createMeasurementPortName " + (t2-t1), Log.DEBUGLEVEL03);
		Log.debugMessage(test + " createTemporalNames " + (t3-t2), Log.DEBUGLEVEL03);
		Log.debugMessage(test + " createMeasurements " + (t4-t3), Log.DEBUGLEVEL03);
		Log.debugMessage(test + " createQuality " + (t5-t4), Log.DEBUGLEVEL03);
	}

	@Override
	public int hashCode() {
		return this.test.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestView) {
			final TestView that = (TestView) obj;
			return this.test.equals(that.test);
		}
		return false;
	}
	
	private final void createStartEndTimes() throws ApplicationException {
		this.firstDate = this.test.getStartTime();
		
		final Identifier mainMeasurementSetupId = this.test.getMainMeasurementSetupId();
		
		this.measurementSetup = 
			StorableObjectPool.getStorableObject(mainMeasurementSetupId, true);
		
		final Identifier groupTestId = this.test.getGroupTestId();
		if (groupTestId.isVoid()) {
			this.lastDate = this.test.getEndTime();
		} else {
			final LinkedIdsCondition groupTestCondition = 
				new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE);
			final Set<Test> tests = 
				StorableObjectPool.getStorableObjectsByCondition(groupTestCondition, true);
			for (final Test groupTest : tests) {
				final Date endTime = groupTest.getEndTime();
				if (this.lastDate != null || endTime.compareTo(this.lastDate) > 0){
					this.lastDate = endTime;
				}
			} 
			// TODO calculate status for group test
		}
		this.lastDate = new Date(this.lastDate.getTime()  
				+ this.measurementSetup.getMeasurementDuration());
	}
	
	private final void createStatus() throws ApplicationException {
		final Identifier groupTestId = this.test.getGroupTestId();
		if (groupTestId.isVoid()) {
			this.status = this.test.getStatus();
		} else {
			this.status = this.test.getStatus();
			final LinkedIdsCondition groupTestCondition = 
				new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE);
			final Set<Test> tests = 
				StorableObjectPool.getStorableObjectsByCondition(groupTestCondition, true);
			// TODO calculate status for group test
		}
	}
	
	private final void createKISName() throws ApplicationException {
		final KIS kis = 
			StorableObjectPool.getStorableObject(this.test.getKISId(), true);
		this.kisName = kis.getName();
		this.kisDescription = kis.getDescription();
	}

	private final void createMeasurementPortName() throws ApplicationException {
		final MonitoredElement monitoredElement = this.test.getMonitoredElement();
		
		this.monitoredElementName = monitoredElement.getName();
		final MeasurementPort mp = 
			StorableObjectPool.getStorableObject(monitoredElement.getMeasurementPortId(),
			true);
		this.portName = mp.getName();
	}

	private final void createTemporalNames() throws ApplicationException {
		if (this.test.getGroupTestId().isVoid()) {
			final TestTemporalType testTemporalType = this.test.getTemporalType();
			this.temporalType = this.getTestTemporalTypeName(testTemporalType);
			final StringBuffer buffer = new StringBuffer(this.temporalType);

			if (testTemporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
				final Identifier temporalPatternId = this.test.getTemporalPatternId();
				if (!temporalPatternId.isVoid() && 
						temporalPatternId.getMajor() == ObjectEntities.PERIODICALTEMPORALPATTERN_CODE) {
					final PeriodicalTemporalPattern periodicalTemporalPattern = 
						StorableObjectPool.getStorableObject(temporalPatternId, true);
					buffer.append(", ");
					buffer.append(I18N.getString("Scheduler.Text.TimePanel.Period"));
					buffer.append(": ");
					buffer.append(periodicalTemporalPattern.getPeriodDescription());
				}
			}
			this.temporalName = buffer.toString();
		} else {
			this.temporalName = I18N.getString("Scheduler.Text.Test.TemporalType.Sectional");
			this.temporalType = this.temporalName;
		}
	}

	private final void refreshStatus() throws ApplicationException {
		this.createStatus();
		this.refreshMeasurements();
		this.createQuality();
	}
	
	private void refreshMeasurements() throws ApplicationException {		
		final long time0 = System.currentTimeMillis();
		final Set<Identifier> measurementIds = new HashSet<Identifier>();
		for (final Measurement measurement : this.measurements) {
			if (measurement.getStatus() != 
				MeasurementStatus.MEASUREMENT_STATUS_COMPLETED) {
				measurementIds.add(measurement.getId());
			}
		}
		if (!measurementIds.isEmpty()) {
			assert Log.debugMessage("need to be updated:" + measurementIds, Log.DEBUGLEVEL03);
			StorableObjectPool.refresh(measurementIds);			
		}
		this.createMeasurements();
		final long time1 = System.currentTimeMillis();
		assert Log.debugMessage("takes " + (time1 - time0), Log.DEBUGLEVEL03);
	}
	
	private final void createMeasurements() throws ApplicationException {
		final boolean newerTest = this.isTestNewer();

		final SortedSet<Measurement> set = new TreeSet<Measurement>(
				new WrapperComparator<Measurement>(
						MeasurementWrapper.getInstance(),
						MeasurementWrapper.COLUMN_START_TIME)); 
		if (!newerTest) {			
			final TypicalCondition startTypicalCondition = 
				new TypicalCondition(this.end,
					this.end,
					OperationSort.OPERATION_LESS_EQUALS,
					ObjectEntities.MEASUREMENT_CODE,
					MeasurementWrapper.COLUMN_START_TIME);
			final TypicalCondition endTypicalCondition = 
				new TypicalCondition(this.start,
					this.start,
					OperationSort.OPERATION_GREAT_EQUALS,
					ObjectEntities.MEASUREMENT_CODE,
					MeasurementWrapper.COLUMN_START_TIME);
			
			final CompoundCondition compoundCondition = 
				new CompoundCondition(startTypicalCondition,
					CompoundConditionSort.AND,
					endTypicalCondition);
			
			compoundCondition.addCondition(
				new LinkedIdsCondition(this.test.getId(), 
					ObjectEntities.MEASUREMENT_CODE));
			final Set<Measurement> measurements1;
			
			final long t0 = System.currentTimeMillis();
			
			if (this.measurements == null) {
				measurements1 = StorableObjectPool.getStorableObjectsByCondition(compoundCondition, 
					true);				
				assert Log.debugMessage("getStorableObjectsByCondition", 
					Log.DEBUGLEVEL03);
			} else {
				measurements1 = StorableObjectPool.getStorableObjectsButIdsByCondition(
					Identifier.createIdentifiers(this.measurements), 
					compoundCondition, 
					true);
				set.addAll(this.measurements);
				assert Log.debugMessage("getStorableObjectsButIdsByCondition", 
					Log.DEBUGLEVEL03);
			}

			set.addAll(measurements1);
			final long t1 = System.currentTimeMillis();
			assert Log.debugMessage("Update " 
					+ measurements1 
					+ " takes " 
					+ (t1 - t0) 
					+ " ms", 
				Log.DEBUGLEVEL03);
		}		
		this.measurements  = Collections.unmodifiableSortedSet(set);
//		for (final Measurement measurement : set) {
//			assert Log.debugMessage(measurement 
//					+ " > " + this.test.getId() 
//					+ " > " + measurement.getStartTime()
//					+ " > " + measurement.getStatus().value(), 
//				Log.DEBUGLEVEL03);
//		}
	}
	
	private final void createQuality() throws ApplicationException {
		SortedSet<Measurement> set = this.measurements;
		
		final long t0 = System.currentTimeMillis();
		while (!set.isEmpty()) {
			final Measurement measurement = set.last();			
			if (measurement.getStatus() == 
				MeasurementStatus.MEASUREMENT_STATUS_COMPLETED) {
				if (this.qualityMeasurement == measurement) {
					return;
				}
				this.qualityMeasurement = measurement;
				final long t01 = System.currentTimeMillis();
				try {
					final MeasurementReflectometryAnalysisResult result =
						new MeasurementReflectometryAnalysisResult(measurement);
					final long t012 = System.currentTimeMillis();
					final ReflectometryEvaluationOverallResult reflectometryEvaluationOverallResult = 
						result.getReflectometryEvaluationOverallResult();
					final long t013 = System.currentTimeMillis();
					Log.debugMessage(this.test 
							+ ", new MeasurementReflectometryAnalysisResult(" 
							+ measurement 
							+ ") takes " 
							+ (t012-t01), 
						Log.DEBUGLEVEL03);
					Log.debugMessage(this.test 
							+ ", " 
							+ measurement 
							+ ", result.getReflectometryEvaluationOverallResult() takes " 
							+ (t013-t012), 
						Log.DEBUGLEVEL03);
					if (reflectometryEvaluationOverallResult != null && 
							reflectometryEvaluationOverallResult.hasDQ()) {
						final NumberFormat numberFormat = NumberFormat.getInstance();
						numberFormat.setMaximumFractionDigits(3);
						final double d = reflectometryEvaluationOverallResult.getD();
						Log.debugMessage("d: " + d,
							Log.DEBUGLEVEL10);
						this.testD = numberFormat.format(d);
						numberFormat.setMaximumFractionDigits(2);
						final double q = reflectometryEvaluationOverallResult.getQ();
						Log.debugMessage("q: " + q,
							Log.DEBUGLEVEL10);
						this.testQ = numberFormat.format(q);
					}
				} catch (final DataFormatException e) {
					throw new CreateObjectException(I18N.getString("Error.CannotAcquireObject"));
				}
				final long t02 = System.currentTimeMillis();
				Log.debugMessage(this.test + ", " + measurement + ", it takes " + (t02-t01), Log.DEBUGLEVEL03);
				break;
			} 
			set = set.headSet(measurement);
		}
		final long t1 = System.currentTimeMillis();
		Log.debugMessage(this.test + ", it takes " + (t1-t0), Log.DEBUGLEVEL03);
		if (this.testD == null) {
			this.testD = "--";
		}
		
		if (this.testQ == null) {
			this.testQ = "--";
		}
	}

	
	public boolean isTestNewer() {
		return this.test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION);
	}	
	
	public static final synchronized TestView valueOf(final Test test) {
		return MAP.get(test);
	}
	
	public static final synchronized TestView valueOf(final Identifier testId) {
		return MAP.get(testId);
	}
	
	public static final synchronized void addTest(final Test test,
		final Date start,
	    final Date end) 
	throws ApplicationException {
		final TestView testView = new TestView(test, start, end);
		MAP.put(test, testView);
	}
	
	public static final synchronized void clearCache(){
		MAP.clear();
	}
	
	/**
	 * update quaility for cached test views but tests
	 * @param butTests
	 * @throws ApplicationException
	 */
	private static final synchronized void updateQualityCache(final Set<Test> butTests,
      		final Date start,
    		final Date end) 
	throws ApplicationException {
		for (final Identifiable testId : MAP.keySet()) {
			if (butTests.contains(testId)) {
				continue;
			}
			final TestView testView = MAP.get(testId);
			if (testView == null || testView.start.after(end) || testView.end.before(start)) {
				continue;
			}
			if (testView.status == TestStatus.TEST_STATUS_PROCESSING) {
				testView.refreshStatus();
			}
		}
	}
	
	public static final synchronized void refreshCache(final Set<Test> tests,
		final Date start,
		final Date end) 
	throws ApplicationException{
		for (final Test test : tests) {
			final TestView testView = MAP.get(test.getId());
			if (testView == null) {
				addTest(test, start, end);
			} else {
				testView.refreshStatus();
			}
		}
		updateQualityCache(tests, start, end);
	}
	
	private String getTestTemporalTypeName(final TestTemporalType testTemporalType) {
		switch(testTemporalType.value()) {
		case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
			return I18N.getString("Scheduler.Text.Test.TemporalType.Onetime");	
		case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
			return I18N.getString("Scheduler.Text.Test.TemporalType.Periodical");
		}
		return null;
	}
	
	public final Test getTest() {
		return this.test;
	}
	
	public final String getKISName() {
		return this.kisName;
	}
	
	public final String getPortName() {
		return this.portName;
	}
	
	public final String getTemporalName() {
		return this.temporalName;
	}
	
	public final String getTemporalType() {
		return this.temporalType;
	}
	
	public final String getMonitoredElementName() {
		return this.monitoredElementName;
	}	
	
	public final Set<Measurement> getMeasurements() {
		return this.measurements;
	}
	
	public final String getTestD() {
		return this.testD;
	}
	
	public final String getTestQ() {
		return this.testQ;
	}
	
	public final Date getFirstDate() {
		return this.firstDate;
	}
	
	public final Date getLastDate() {
		return this.lastDate;
	}
	
	public final Date getEnd() {
		return this.end;
	}
	
	public final Date getStart() {
		return this.start;
	}	

	public final void setEnd(final Date end) {
		this.end = end;
	}
	
	public final void setStart(final Date start) {
		this.start = start;
	}
	
	public final MeasurementSetup getMeasurementSetup() {
		return this.measurementSetup;
	}
	
//	public final TestStatus getStatus() {
//		return this.status;
//	}
	
	public final String getExtendedDescription() {
		if (this.extendedDescription == null) {
			final MonitoredElement testMonitoredElement = 
				this.test.getMonitoredElement();		
			final TestViewAdapter adapter = TestViewAdapter.getInstance();
			final SimpleDateFormat sdf = 
				(SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			this.extendedDescription = 
				adapter.getValue(this, TestViewAdapter.KEY_TEMPORAL_TYPE_NAME).toString()
					+ "\n" 
					+ adapter.getName(TestViewAdapter.KEY_START_TIME) 
					+ " : " 
					+ sdf.format(adapter.getValue(this, TestViewAdapter.KEY_START_TIME))
					+ "\n"
					+ testMonitoredElement.getName() 
					+ "\n"
					+ this.kisDescription;
		}
		return this.extendedDescription;
	}
	
	@Override
	public String toString() {
		return "TestView of <" + this.test + '>';
	}	
	
}

