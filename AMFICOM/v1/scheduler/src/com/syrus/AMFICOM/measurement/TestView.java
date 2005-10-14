/*-
* $Id: TestView.java,v 1.2 2005/10/14 13:53:10 bob Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.text.NumberFormat;
import java.util.Collections;
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
 * @version $Revision: 1.2 $, $Date: 2005/10/14 13:53:10 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public final class TestView {
	
	private static final Map<Test, TestView> MAP = new HashMap<Test, TestView>();

	private final Test test;
	
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
		
		this.createKISName();
		this.createMeasurementPortName();		
		this.createTemporalNames();
		this.createMeasurements();
		this.createQuality();
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

	
	private boolean isTestNewer() {
		return this.test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION);
	}	
	
	public static final synchronized TestView valueOf(final Test test) 
	throws ApplicationException {
		TestView view = MAP.get(test);
		if (view == null) {
			view = new TestView(test);
			MAP.put(test, view);
		}
		return view;
	}
	
	public static final synchronized void clearCache(){
		MAP.clear();
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
}

