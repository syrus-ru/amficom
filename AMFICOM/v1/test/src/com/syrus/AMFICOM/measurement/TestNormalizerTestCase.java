/*
 * $Id: TestNormalizerTestCase.java,v 1.1 2006/02/17 07:52:39 bob Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/17 07:52:39 $
 * @author $Author: bob $
 * @module test
 */
public final class TestNormalizerTestCase extends TestCase {
	
	private static MonitoredElement MONITORED_ELEMENT;
	private static MeasurementSetup LONG_MEASUREMENT_SETUP;
	private static MeasurementSetup SHORT_MEASUREMENT_SETUP;
	
	private static Identifier	CREATOR_ID;
	
	public TestNormalizerTestCase(final String name) {
		super(name);
	}
	
	static void createInitialValues() throws ApplicationException {
		CREATOR_ID = LoginManager.getUserId();
		getMonitoredElement();
		getMeasurementSetups();
	}
	
	private static void getMonitoredElement() throws ApplicationException {
		final Identifier meId = 
			new Identifier(ObjectEntities.MONITOREDELEMENT 
				+ Identifier.SEPARATOR 
				+ "21");
		MONITORED_ELEMENT = StorableObjectPool.getStorableObject(meId, true);
	}
	
	private static void getMeasurementSetups() throws ApplicationException {
		final Identifier longMsId = 
			new Identifier(ObjectEntities.MEASUREMENTSETUP 
				+ Identifier.SEPARATOR 
				+ "941");
		LONG_MEASUREMENT_SETUP = StorableObjectPool.getStorableObject(longMsId, true);
		
		final Identifier shortMsId = 
			new Identifier(ObjectEntities.MEASUREMENTSETUP 
				+ Identifier.SEPARATOR 
				+ "632");
		
		SHORT_MEASUREMENT_SETUP = StorableObjectPool.getStorableObject(shortMsId, true);
		
		final Set<Identifier> longMsMonitoredElementIds = 
			LONG_MEASUREMENT_SETUP.getMonitoredElementIds();
		final Set<Identifier> shortMsMonitoredElementIds = 
			SHORT_MEASUREMENT_SETUP.getMonitoredElementIds();
		
		assertEquals(longMsMonitoredElementIds.size(), 1);		
		assertEquals(shortMsMonitoredElementIds.size(), 1);		
		
		assertTrue(longMsMonitoredElementIds.contains(MONITORED_ELEMENT));
		assertTrue(shortMsMonitoredElementIds.contains(MONITORED_ELEMENT));
		
		final long longMsMeasurementDuration = LONG_MEASUREMENT_SETUP.getMeasurementDuration();
		final long shortMsMeasurementDuration = SHORT_MEASUREMENT_SETUP.getMeasurementDuration();
		
//		System.out.println("longMsMeasurementDuration is " 
//				+ longMsMeasurementDuration / 1000 
//				+ " sec");		
//		System.out.println("shortMsMeasurementDuration is " 
//				+ shortMsMeasurementDuration / 1000 
//				+ " sec");
		
		assertTrue(longMsMeasurementDuration / shortMsMeasurementDuration > 10);		
	}

	public static junit.framework.Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest() {
			@Override
			public void oneTimeSetUp() {
				super.oneTimeSetUp();
				try {
					createInitialValues();
				} catch (final ApplicationException ae) {
					Log.errorMessage(ae);
					System.exit(0);
				}
			}
		};
		commonTest.addTestSuite(TestNormalizerTestCase.class);
		return commonTest.createTestSetup();
	}
	
	public void testTestNormalization() throws ApplicationException {
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		
		final long period = 60L * 60L * 1000L;
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final Date start = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		final Date end = calendar.getTime();
		
		final PeriodicalTemporalPattern temporalPattern = 
			PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
		
		final Test test = Test.createInstance(CREATOR_ID, 
			start, 
			end, 
			temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
			TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL, 
			MeasurementType.REFLECTOMETRY, 
			AnalysisType.UNKNOWN, 
			Identifier.VOID_IDENTIFIER, 
			MONITORED_ELEMENT, 
			"single", 
			Collections.singleton(measurementSetup.getId()));
		
		assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);		

		test.normalize();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 0);
		final Date expectedLastMeasurementTime = calendar.getTime();
		final long expecetedTime = expectedLastMeasurementTime.getTime() 
			+ measurementSetup.getMeasurementDuration();
		
		final Date endTime = test.getEndTime();
		assertEquals("expectedTime: " 
				+ new Date(expecetedTime) 
				+ ", actual: "
				+ endTime, 
			expecetedTime, 
			endTime.getTime());
		
		StorableObjectPool.delete(test.getId());
	}

}
