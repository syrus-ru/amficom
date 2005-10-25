/*-
* $Id: TestView.java,v 1.5 2005/10/25 09:22:23 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.reflectometry.MeasurementReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;


/**
 * @version $Revision: 1.5 $, $Date: 2005/10/25 09:22:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public final class TestView {
	
	private static final Map<Test, TestView> MAP = new HashMap<Test, TestView>();

	private final Test test;
	
	private Date firstDate;
	private Date lastDate;
	
	private MeasurementSetup measurementSetup;
	
	private String kisName;
	private String portName;	
	
	private String temporalType;
	private String temporalName;
	private String monitoredElementName;
	private String testQ;
	private String testD;
	
	private SortedSet<Measurement> measurements;
	
	private TestView(final Test test) throws ApplicationException {
		this.test = test;
		
		this.createStartEndTimes();
		
//		final long t0 = System.currentTimeMillis();
		this.createKISName();
//		final long t1 = System.currentTimeMillis();
		this.createMeasurementPortName();		
//		final long t2 = System.currentTimeMillis();
		this.createTemporalNames();
//		final long t3 = System.currentTimeMillis();
		this.createMeasurements();
//		final long t4 = System.currentTimeMillis();
		this.createQuality();
//		final long t5 = System.currentTimeMillis();
		
//		assert Log.debugMessage("TestView.TestView | " + test + " createKISName " + (t1-t0), Log.DEBUGLEVEL03);
//		assert Log.debugMessage("TestView.TestView | " + test + " createMeasurementPortName " + (t2-t1), Log.DEBUGLEVEL03);
//		assert Log.debugMessage("TestView.TestView | " + test + " createTemporalNames " + (t3-t2), Log.DEBUGLEVEL03);
//		assert Log.debugMessage("TestView.TestView | " + test + " createMeasurements " + (t4-t3), Log.DEBUGLEVEL03);
//		assert Log.debugMessage("TestView.TestView | " + test + " createQuality " + (t5-t4), Log.DEBUGLEVEL03);
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
		
		this.lastDate = new Date(this.test.getEndTime().getTime() 
			+ this.measurementSetup.getMeasurementDuration());
	}
	
	private final void createKISName() throws ApplicationException {
		final KIS kis = 
			StorableObjectPool.getStorableObject(this.test.getKISId(), true);
		this.kisName = kis.getName();
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

	private final void createMeasurements() throws ApplicationException {
		final boolean newerTest = this.isTestNewer();

		final SortedSet<Measurement> set = new TreeSet<Measurement>(
				new WrapperComparator<Measurement>(
						MeasurementWrapper.getInstance(),
						MeasurementWrapper.COLUMN_START_TIME)); 
		if (!newerTest) {
			final LinkedIdsCondition linkedIdsCondition = 
				new LinkedIdsCondition(this.test.getId(), ObjectEntities.MEASUREMENT_CODE);	
			final Set<Measurement> measurements1 = 
				StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true);
			set.addAll(measurements1);			
		}		
		this.measurements  = Collections.unmodifiableSortedSet(set);
	}
	
	private final void createQuality() throws ApplicationException {
		SortedSet<Measurement> set = this.measurements;
		while (!set.isEmpty()) {
			final Measurement measurement = set.last();
			if (measurement.getStatus() == 
				MeasurementStatus.MEASUREMENT_STATUS_COMPLETED) {
				try {
					final MeasurementReflectometryAnalysisResult result =
						new MeasurementReflectometryAnalysisResult(measurement);
					final ReflectometryEvaluationOverallResult reflectometryEvaluationOverallResult = 
						result.getReflectometryEvaluationOverallResult();
					if (reflectometryEvaluationOverallResult != null && 
							reflectometryEvaluationOverallResult.hasDQ()) {
						final NumberFormat numberFormat = NumberFormat.getInstance();
						numberFormat.setMaximumFractionDigits(3);
						final double d = reflectometryEvaluationOverallResult.getD();
						assert Log.debugMessage("TestView.createQuality | d:" + d,
							Log.DEBUGLEVEL10);
						this.testD = numberFormat.format(d);
						numberFormat.setMaximumFractionDigits(2);
						final double q = reflectometryEvaluationOverallResult.getQ();
						assert Log.debugMessage("TestView.createQuality | q:" + q,
							Log.DEBUGLEVEL10);
						this.testQ = numberFormat.format(q);
					}
				} catch (final DataFormatException e) {
					throw new CreateObjectException(I18N.getString("Error.CannotAcquireObject"));
				}
				break;
			}
			set = set.headSet(measurement);
		}
		
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
	
	public static final synchronized void addTest(final Test test) 
	throws ApplicationException {
		MAP.put(test, new TestView(test));
	}
	
	public static final synchronized void clearCache(){
		MAP.clear();
	}
	
	public static final synchronized void refreshCache(final Set<Test> tests) 
	throws ApplicationException{
		for (final Test test : tests) {
			MAP.put(test, new TestView(test));
		}
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
	
	public final MeasurementSetup getMeasurementSetup() {
		return this.measurementSetup;
	}
	
	@Override
	public String toString() {
		return "TestView of <" + this.test + '>';
	}

}

