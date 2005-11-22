/*-
 * $Id: EasyDateFormatter.java,v 1.3 2005/11/22 18:47:19 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/11/22 18:47:19 $
 * @module util
 */
public final class EasyDateFormatter {
	private static final SimpleDateFormat SDF =
		new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
	private static final SimpleDateFormat SDF_FULL =
		new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss/z");

	private EasyDateFormatter() {
		assert false;
	}

	/**
	 * Formats date with default mode of detection
	 */
	public static String formatDate(final Date date) {
		return formatDateCareful(date);
	}

	/**
	 * Formats date in simple mode (without timezone)
	 */
	protected static String formatDateSimple(final Date date) {
		return SDF.format(date);
	}

	/**
	 * Formats date in full mode (with timezone)
	 */
	protected static String formatDateFull(final Date date) {
		return SDF_FULL.format(date);
	}

	/**
	 * Formats date carefully: use full mode if needed, otherwise simple mode
	 */
	protected static String formatDateCareful(final Date date) {
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
	protected static boolean needFullFormat(final Date date){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final String base = formatDateSimple(cal.getTime());
		cal.add(Calendar.HOUR, 1);
		final String val1 = formatDateSimple(cal.getTime());
		cal.add(Calendar.HOUR, -2);
		final String val2  = formatDateSimple(cal.getTime());
		return base.equals(val1) || base.equals(val2);
	}
}
