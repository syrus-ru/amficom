/*-
 * $Id: EasyDateFormatter.java,v 1.2 2005/08/17 10:22:29 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Форматирует дату способом, удобным для именования рефлектограмм.
 * Обеспечивает отличие результирующих строк для разных исходных дат
 * при переходе с летнего времени на зимнее.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/08/17 10:22:29 $
 * @module util
 */
public class EasyDateFormatter {
	private static SimpleDateFormat sdf =
		new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
	private static SimpleDateFormat sdfFull =
		new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss/z");

	/**
	 * Formats date with default mode of detection
	 */
	public static final String formatDate(Date date) {
		return formatDateCareful(date);
	}

	/**
	 * Formats date in simple mode (without timezone)
	 */
	protected static final String formatDateSimple(Date date) {
		return sdf.format(date);
	}

	/**
	 * Formats date in full mode (with timezone)
	 */
	protected static final String formatDateFull(Date date) {
		return sdfFull.format(date);
	}

	/**
	 * Formats date carefully: use full mode if needed, otherwise simple mode
	 */
	protected static final String formatDateCareful(Date date) {
		return needFullFormat(date)
					? formatDateFull(date)
					: formatDateSimple(date); 
	}

	/**
	 * Check if the date needs a timezone info to avoid daylight saving
	 * 'overlap' problems. Assumes daylight saving addition is 1 hour sharp.
	 * @param date date to be checked
	 * @return true if timezone info is needed to get an unambiguous string
	 */
	protected static boolean needFullFormat(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String base = formatDateSimple(cal.getTime());
		cal.add(Calendar.HOUR, 1);
		String val1 = formatDateSimple(cal.getTime());
		cal.add(Calendar.HOUR, -2);
		String val2  = formatDateSimple(cal.getTime());
		return base.equals(val1) || base.equals(val2);
	}
}
