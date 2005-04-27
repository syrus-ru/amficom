/*-
 * $Id: IntervalsTemporalPatternTestCase.java,v 1.2 2005/04/27 11:36:45 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 11:36:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class IntervalsTemporalPatternTestCase extends TestCase {

	private static Identifier userId;
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(IntervalsTemporalPatternTestCase.class);
	}
	
	protected static void oneTimeSetUp() {
		MeasurementStorableObjectPool.init(null);
		userId = new Identifier("User_1");
	}
	
	protected static void oneTimeTearDown() {
		// nothing
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() throws ApplicationException {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}
	
	public static Test suite() {
		return suiteWrapper(IntervalsTemporalPatternTestCase.class);
	}
	
	public void testLongPeriodItem() throws IllegalObjectEntityException {
		userId = new Identifier("User_1");

		long startTime = System.currentTimeMillis();		
		long intervalLength = 60L * 60L * 1000L; 
		long endTime = startTime + 7L * intervalLength;
		
		IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(
																							new Identifier(
																											"IntervalsTemporalPattern_1"),
																							userId, 0L, null, null);
		MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
		
		{
		PeriodicalTemporalPattern periodicTemporalPattern = new PeriodicalTemporalPattern(new Identifier("PeriodicalTemporalPattern_1"),
			userId, 0L, intervalLength);
		
		MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern);
		
		intervalsTemporalPattern.addIntervalItem(0, periodicTemporalPattern.getId());
		}
		
		this.printTimes(intervalsTemporalPattern, startTime, endTime);

		{

			long timeToDelete = 0;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = endTime - startTime;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = 3 * intervalLength;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}

		{

			long timeToDelete = 2 * intervalLength;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}

		{

			long timeToDelete = intervalLength;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = endTime - startTime - intervalLength; 

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = endTime - startTime - 2*intervalLength; 

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}


		{

			long timeToDelete = endTime - startTime - 3*intervalLength; 

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		
		
//		{
//
//			long timeToInsert = endTime - startTime - 2*intervalLength; 
//
//			Date insertDate = new Date(startTime + timeToInsert);
//
//			System.out.println("expect added time " + insertDate);
//
//			intervalsTemporalPattern.addIntervalItem(timeToInsert, null);
//
//			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);
//
//			this.printTimes(intervalsTemporalPattern, startTime, endTime);
//
//			assertTrue(times.contains(insertDate));
//		}
		
		{
			System.out.println("add periodical ");
			PeriodicalTemporalPattern periodicTemporalPattern2 = new PeriodicalTemporalPattern(new Identifier("PeriodicalTemporalPattern_2"),
				userId, 0L, intervalLength/2);
			
			MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern2);
			
			intervalsTemporalPattern.addIntervalItem(intervalLength, periodicTemporalPattern2.getId());
			
			this.printTimes(intervalsTemporalPattern, startTime, endTime);
		}
	}

	public void _testShortForwardPeriodItem() throws IllegalObjectEntityException {
		userId = new Identifier("User_1");

		long startTime = System.currentTimeMillis();		
		long intervalLength = 60L * 60L * 1000L; 
		long endTime = startTime + 2L * intervalLength;
		
		IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(
																							new Identifier(
																											"IntervalsTemporalPattern_2"),
																							userId, 0L, null, null);
		MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
		
		{
		PeriodicalTemporalPattern periodicTemporalPattern = new PeriodicalTemporalPattern(new Identifier("PeriodicTemporalPattern_2"),
			userId, 0L, intervalLength);
		
		MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern);
		
		intervalsTemporalPattern.addIntervalItem(0, periodicTemporalPattern.getId());
		}
		
		this.printTimes(intervalsTemporalPattern, startTime, endTime);

		{

			long timeToDelete = 0;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = intervalLength;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
	}
	
	public void _testShortBackwardPeriodItem() throws IllegalObjectEntityException {
		

		long startTime = System.currentTimeMillis();		
		long intervalLength = 60L * 60L * 1000L; 
		long endTime = startTime + 2L * intervalLength;
		
		IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(
																							new Identifier(
																											"IntervalsTemporalPattern_2"),
																							userId, 0L, null, null);
		MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
		
		{
		PeriodicalTemporalPattern periodicTemporalPattern = new PeriodicalTemporalPattern(new Identifier("PeriodicTemporalPattern_2"),
			userId, 0L, intervalLength);
		
		MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern);
		
		intervalsTemporalPattern.addIntervalItem(0, periodicTemporalPattern.getId());
		}
		
		this.printTimes(intervalsTemporalPattern, startTime, endTime);

		{

			long timeToDelete = intervalLength;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
		
		{

			long timeToDelete = 0;

			Date deletedDate = new Date(startTime + timeToDelete);

			System.out.println("expect delete " + deletedDate);

			intervalsTemporalPattern.removeIntervalItem(timeToDelete);

			SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);

			this.printTimes(intervalsTemporalPattern, startTime, endTime);

			assertFalse(times.contains(deletedDate));
		}
	}
	
	private void printTimes(IntervalsTemporalPattern intervalsTemporalPattern, long startTime, long endTime) {
		System.out.println("----");
		intervalsTemporalPattern.printStructure();
		SortedSet times = intervalsTemporalPattern.getTimes(startTime, endTime);
		for (Iterator it = times.iterator(); it.hasNext();) {
			Date date = (Date) it.next();
			System.out.println(date);
		}
		System.out.println("----");
	}
	
}
