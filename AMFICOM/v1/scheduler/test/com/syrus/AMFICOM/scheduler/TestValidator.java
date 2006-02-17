/*
 * $Id: TestValidator.java,v 1.3 2006/02/17 08:43:04 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.scheduler;

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
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.validator.IntersectionValidator;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2006/02/17 08:43:04 $
 * @author $Author: bob $
 * @module scheduler
 */
public final class TestValidator extends TestCase {
	
	private static MonitoredElement MONITORED_ELEMENT;
	private static MeasurementSetup LONG_MEASUREMENT_SETUP;
	private static MeasurementSetup SHORT_MEASUREMENT_SETUP;
	
	private static IntersectionValidator INTERSECTION_VALIDATOR;
	
	private static Identifier	CREATOR_ID;
	
	public TestValidator(final String name) {
		super(name);
	}
	
	static void createInitialValues() throws ApplicationException {
		CREATOR_ID = LoginManager.getUserId();
		INTERSECTION_VALIDATOR = new IntersectionValidator(false);
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
		commonTest.addTestSuite(TestValidator.class);
		return commonTest.createTestSetup();
	}

	public void testSelfIntersection() throws ApplicationException {
		// Periodical test has self intersection if period
		// less than measurement duration
		final MeasurementSetup measurementSetup = LONG_MEASUREMENT_SETUP;		
		final long measurementDuration = measurementSetup.getMeasurementDuration();		
		final long period = 5L * 60L * 1000L;
		
		assertTrue(measurementDuration > period);
		
		final Date now = new Date();
		final Date end = new Date(now.getTime() + 30L * 60L * 1000L);
		
		final PeriodicalTemporalPattern temporalPattern = 
			PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
		
		final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
			now, 
			end, 
			temporalPattern, 
			measurementSetup);
		assertNotNull("There must be self intersection", reason);
	}
	
	public void testAbsentSelfIntersection() throws ApplicationException {
		
		// Periodical test hasn't intersections if period
		// more than measurement duration
		
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		final long period = 5L * 60L * 1000L;
		
		assertTrue(measurementDuration < period);
		
		final Date now = new Date();
		final Date end = new Date(now.getTime() + 30L * 60L * 1000L + measurementDuration);
		
		final PeriodicalTemporalPattern temporalPattern = 
			PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
		
		final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
			now, 
			end, 
			temporalPattern, 
			measurementSetup);
		
		assertNull("There shouldn't be any intersection, " + reason, reason);
	}
	
	public void testAbsentSelfSimpleTestIntersection() throws ApplicationException {
		
		// Simple test hasn't any intersection
		
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		final long period = 5L * 60L * 1000L;
		
		assertTrue(measurementDuration < period);
		
		final Date start = new Date();
		final Date end = new Date(start.getTime() + measurementDuration);
		
		final AbstractTemporalPattern temporalPattern = null;
		
		final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
			start, 
			end, 
			temporalPattern, 
			measurementSetup);
		
		assertNull("There shouldn't be any intersection.", reason);
	}
	
	public void testSingleTestsIntersection() throws ApplicationException {
		// Two single tests has intersection for follow situation:
		// One test has shorter measurement duration than other one
		// that starts before first test and ends after first ends
		
		final Date now = new Date();
		final Test test;
		{
			final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
			final long measurementDuration = measurementSetup.getMeasurementDuration();
			
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);		
			final AbstractTemporalPattern temporalPattern = null;
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			
			test.normalize();
			
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
		}
		
		// The second test's parameters:
		{
			final MeasurementSetup measurementSetup = LONG_MEASUREMENT_SETUP;
			final long measurementDuration = measurementSetup.getMeasurementDuration();
			
			final Date start = new Date(now.getTime() - 60L * 1000L);
			final Date end = new Date(start.getTime() + measurementDuration);	
			final AbstractTemporalPattern temporalPattern = null;			
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			
			assertNotNull("There must be intersection", reason);
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleTestsIntersection2() throws ApplicationException {
		// Two single tests has intersection for follow situation:
		// One test has shorter measurement duration than other one
		// that starts before first test and ends after first starts 
		// but before it ends
		
		final Date now = new Date();
		
		final MeasurementSetup firstTestMeasurementSetup = SHORT_MEASUREMENT_SETUP;
		
		final Test test;
		{
			final long measurementDuration = firstTestMeasurementSetup.getMeasurementDuration();
			
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				firstTestMeasurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(firstTestMeasurementSetup.getId()));
			
			test.normalize();
			
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
		}
		
		// The second test's parameters:
		{
			final MeasurementSetup measurementSetup = LONG_MEASUREMENT_SETUP;
			final long measurementDuration = measurementSetup.getMeasurementDuration();
			final Date start = new Date(now.getTime() - measurementDuration 
				+ firstTestMeasurementSetup.getMeasurementDuration() / 2);
			final Date end = new Date(start.getTime() + measurementDuration);		
			final AbstractTemporalPattern temporalPattern = null;
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			
			assertNotNull("There must be intersection", reason);
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleTestsIntersection3() throws ApplicationException {
		// Two single tests has intersection for follow situation:
		// One test has shorter measurement duration than other one
		// that starts after first test but before its ends
		
		final Date now = new Date();		
		final MeasurementSetup measurementSetup = LONG_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		final Test test;
		{
			final Date start = now;			
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
			
			test.normalize();
		}
		
		// The second test's parameters:
		{
			final Date start = new Date(now.getTime() 
					+ measurementDuration / 2);
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			
			assertNotNull("There must be intersection", reason);
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleAndPeriodicalTestsIntersection() throws ApplicationException {
		final Date now = new Date();
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		// Two tests has intersection if the 1st is single test
		// and the 2nd is periodical tests and its not first measurement is on time of the 1st test
		
		final Test test;
		{
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;			
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			test.normalize();
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
		}
		
		// periodical second test's parameters
		{
			final long period = 5L * 60L * 1000L;
			final Date start = new Date(now.getTime() - 3L * period);
			final Date end = new Date(now.getTime() + 30L * 60L * 1000L + measurementDuration);
			
			final PeriodicalTemporalPattern temporalPattern = 
				PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNotNull("There must be intersection", reason);
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleAndTooLongPeriodicalTestsIntersection() 
	throws ApplicationException {
		final Calendar calendar = Calendar.getInstance();
		final Date now = calendar.getTime();
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		
		final Test test;
		{
			final long period = 5L * 60L * 1000L;
			final Date start = new Date(now.getTime() - 3L * period);
			calendar.add(Calendar.YEAR, 200);
			final Date end = calendar.getTime();
			assertTrue("end:" + end + ", now: " + now, end.compareTo(now) > 0);
			
			final PeriodicalTemporalPattern temporalPattern = 
				PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			assert Log.debugMessage("end:" + end, Log.DEBUGLEVEL03);
			test = Test.createInstance(CREATOR_ID, 
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
			test.normalize();
			assert Log.debugMessage("Add single test: " 
					+ test.getId(), 
				Log.DEBUGLEVEL03);
		}

		
		{	
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;			
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNotNull("There must be any intersection.", reason);
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleTestsMovementIntersection() throws ApplicationException {
		
		final Date now = new Date();
		final Test test;
		{
			final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
			final long measurementDuration = measurementSetup.getMeasurementDuration();
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;
			
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			test.normalize();
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
		}
		
		// The second test's parameters:
		{
			final MeasurementSetup measurementSetup = LONG_MEASUREMENT_SETUP;
			final long measurementDuration = measurementSetup.getMeasurementDuration();
			final Date start = new Date(test.getStartTime().getTime() + 60L * 1000L);
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;
			
			
			{
				final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
					start, 
					end, 
					temporalPattern, 
					measurementSetup);
				
				assertNull("There shouldn't be any intersection", reason);
			}
			
			final Test test2 = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			test2.normalize();
			final String reason = INTERSECTION_VALIDATOR.isValid(test2, -2L * 60L * 1000L);
			
			assertNotNull("There must be intersection", reason);
			StorableObjectPool.delete(test2.getId());
		}
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleTestSelfIntersectionChangeMeasurementSetup() throws ApplicationException {
		final Calendar calendar = Calendar.getInstance();
		final Date now = calendar.getTime();
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final MeasurementSetup measurementSetup2 = LONG_MEASUREMENT_SETUP;
		final long period = 5L * 60L * 1000L;
		assertTrue(measurementSetup2.getMeasurementDuration() > period);
		
		final Date start = new Date(now.getTime() - 3L * period);
		calendar.add(Calendar.YEAR, 200);
		final Date end = calendar.getTime();
		assertTrue("end:" + end + ", now: " + now, end.compareTo(now) > 0);
		
		final PeriodicalTemporalPattern temporalPattern = 
			PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
		{
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
		}
		
		assert Log.debugMessage("end:" + end, Log.DEBUGLEVEL03);
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
		
		test.normalize();
		
		assert Log.debugMessage("Add single test: " 
				+ test.getId(), 
			Log.DEBUGLEVEL03);
		
		final String reason = INTERSECTION_VALIDATOR.isValid(test, measurementSetup2);
		assertNotNull("There must be intersection", reason);
		
		StorableObjectPool.delete(test.getId());
	}
	
	public void testSingleAndPeriodicalTestsResumeIntersection() throws ApplicationException {
		final Date now = new Date();
		final MeasurementSetup measurementSetup = SHORT_MEASUREMENT_SETUP;
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		final Test test;
		{
			final Date start = now;
			final Date end = new Date(start.getTime() + measurementDuration);
			final AbstractTemporalPattern temporalPattern = null;			
			
			final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
				start, 
				end, 
				temporalPattern, 
				measurementSetup);
			assertNull("There shouldn't be any intersection.", reason);
			
			test = Test.createInstance(CREATOR_ID, 
				start, 
				end, 
				temporalPattern != null ? temporalPattern.getId() : Identifier.VOID_IDENTIFIER, 
				TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
				MeasurementType.REFLECTOMETRY, 
				AnalysisType.UNKNOWN, 
				Identifier.VOID_IDENTIFIER, 
				MONITORED_ELEMENT, 
				"single", 
				Collections.singleton(measurementSetup.getId()));
			test.normalize();
			assert Log.debugMessage("Add single test: " + test.getId(), Log.DEBUGLEVEL03);
		}
		
		// periodical second test's parameters
		{
			final long period = 5L * 60L * 1000L;
			final Date start = new Date(now.getTime() - 10L * period);
			final Date end = new Date(now.getTime() - 5L * period);
			
			final PeriodicalTemporalPattern temporalPattern = 
				PeriodicalTemporalPattern.getInstance(CREATOR_ID, period);
			
			{
				final String reason = INTERSECTION_VALIDATOR.isValid(MONITORED_ELEMENT.getId(), 
					start, 
					end, 
					temporalPattern, 
					measurementSetup);
				assertNull("There shouldn't be any intersection", reason);
			}
			
			final Test test2 = Test.createInstance(CREATOR_ID, 
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
			test2.normalize();
			final Date resumeEnd = new Date(now.getTime() + 5L * period);
			
			final String reason = INTERSECTION_VALIDATOR.isValid(test2, test2.getStartTime(), resumeEnd);
			assertNotNull("There must be intersection", reason);
			
			StorableObjectPool.delete(test2.getId());
		}
		
		StorableObjectPool.delete(test.getId());
	}
}
