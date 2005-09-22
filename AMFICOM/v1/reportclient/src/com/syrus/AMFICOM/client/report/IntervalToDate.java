/*
 * $Id: IntervalToDate.java,v 1.1 2005/09/22 14:49:02 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

public class IntervalToDate {
	private static final String DAY = "report.Date.day";
	private static final String HOUR = "report.Date.hour";
	private static final String MINUTE = "report.Date.minute";
	private static final String SECOND = "report.Date.second";
	
	private static final String MULTIPLE = "s";
	private static final String SEPARATOR = ", ";	
	
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;
	/**
	 * Возвращает интервал времени преобразованный к виду
	 * (Число дней/часов/минут/секунд)
	 * @param intervalMs Интервал в миллисекундах
	 */
	public static String toDate(long intervalMs) {
		/**
		 * Интервал в секундах
		 */
		long intervalS = intervalMs / 1000;
		long intervalRemainder = intervalS;
		
		long daysCount = intervalRemainder /
			(SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY);
		intervalRemainder -= daysCount * SECONDS_IN_MINUTE 
			* MINUTES_IN_HOUR * HOURS_IN_DAY;
		
		long hoursCount = intervalRemainder /
			(SECONDS_IN_MINUTE * MINUTES_IN_HOUR);
		intervalRemainder -= hoursCount * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;

		long minutesCount = intervalRemainder / SECONDS_IN_MINUTE;
		intervalRemainder -= minutesCount * SECONDS_IN_MINUTE;

		long secondsCount = intervalRemainder;
		
		String result = getDatePiece(daysCount,DAY)
			+ getDatePiece(hoursCount,HOUR)
			+ getDatePiece(minutesCount,MINUTE)
			+ getDatePiece(secondsCount,SECOND);
		
		//Отсекаем последний символ
		return result.substring(0,result.length() - SEPARATOR.length());
	}
	
	private static String getDatePiece (
			long elementsCount,
			String elementLangKey) {
		String result = "";
		if (elementsCount > 0) {
			result =
				elementsCount
				+ " "
				+ LangModelReport.getString(
						((elementsCount % 10) == 1) 
						? elementLangKey : elementLangKey + MULTIPLE)
				+ SEPARATOR;
		}
		return result;
	}
}
