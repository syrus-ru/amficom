/*
 * TemporalPatternInstanceTest.java
 * Created on 23.08.2004 13:26:31
 * 
 */

package test.com.syrus.AMFICOM.measurement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.AbstractMesurementTestCase;
import com.syrus.AMFICOM.measurement.TemporalPattern;

/**
 * @author Vladimir Dolzhenko
 */
public class TemporalPatternInstanceTest extends AbstractMesurementTestCase {

	public static final String		DATE_FORMAT		= "dd.MM.yy HH:mm:ss";
	public static final SimpleDateFormat	SIMPLE_DATE_FORMAT	= new SimpleDateFormat(DATE_FORMAT);

	public TemporalPatternInstanceTest(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = TemporalPatternTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static junit.framework.Test suite() {
		//return suiteWrapper(TemporalPatternInstanceTest.class);
		return new TestSuite(TemporalPatternInstanceTest.class);
	}

	public void testExporingPattern() throws RetrieveObjectException {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 41);
		
		long startTime = c.getTimeInMillis();

		//long startTime = System.currentTimeMillis();// - 47 * 60 * 1000 ;

		List cronString = new ArrayList();
		cronString.add("*/10 * * * *");
		TemporalPattern temporalPattern =
		 TemporalPattern.createInstance(null, null, null, cronString);

//		List list = TemporalPatternDatabase.retrieveAll();
//
//		if (list.isEmpty())
//			fail("must be at less one TemporalPattern at db");
//
//		TemporalPattern temporalPattern = (TemporalPattern) list.get(0);
//
//
//		String[] cronString = temporalPattern.getCronStrings();
//		for (int i = 0; i < cronString.length; i++) {
//			System.out.println(cronString[i]);
//		}
		
		long endTime = startTime + 2 * 60 * 1000;
		System.out.println("start:" + new Date(startTime));
		System.out.println("endTime:" + new Date(endTime));

		List dateList = temporalPattern.getTimes(startTime, endTime);
		System.out.println("size:" + dateList.size());
		for (Iterator it = dateList.iterator(); it.hasNext();) {
			Date time = (Date) it.next();
			System.out.println(SIMPLE_DATE_FORMAT.format(time));

		}

	}

}