/*-
 * $Id: TemporalPatternTest.java,v 1.2 2005/06/02 14:31:03 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:03 $
 * @author $Author: arseniy $
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
