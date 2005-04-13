/*-
 * $Id: TemporalPatternTest.java,v 1.1.1.1 2005/04/13 09:35:39 cvsadmin Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/13 09:35:39 $
 * @author $Author: cvsadmin $
 * @author Vladimir Dolzhenko
 * @module tools
 */
public class TemporalPatternTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TemporalPatternTest.class);
	}

	public void testTemporalPattern() throws CreateObjectException {
		TemporalPattern_Transferable pattern_Transferable = new TemporalPattern_Transferable(
																								new StorableObject_Transferable(
																																new Identifier_Transferable(
																																							"TemporalPattern_1"),
																																0,
																																0,
																																new Identifier_Transferable(
																																							"User_1"),
																																new Identifier_Transferable(
																																							"User_1"),
																																0L),
																								"1st TemporalPattern",
																								new String[] { "*/5 * * * *"});
		
		TemporalPattern temporalPattern = new TemporalPattern(pattern_Transferable); 
		long currentTime = System.currentTimeMillis();
		long hour = 60L * 60L * 1000L;
		SortedSet times = temporalPattern.getTimes(new Date(currentTime), new Date(currentTime +  hour));
		for (Iterator iter = times.iterator(); iter.hasNext();) {
			Date date = (Date) iter.next();
			System.out.println(date);
		}
	}
}
