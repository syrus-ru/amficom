package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

public class TimeStamp_dep {

	public static final String	typ						= "newtimestamp";
	public static final int		TIMESTAMPTYPE_ONETIME	= 1;
	public static final int		TIMESTAMPTYPE_CONTINUOS	= 2;
	public static final int		TIMESTAMPTYPE_PERIODIC	= 3;

	public static final int		WEEKDAY_TYPE_ANY		= 0;
	public static final int		WEEKDAY_TYPE_WORK		= 1;
	public static final int		WEEKDAY_TYPE_REST		= 2;

	private int					type					= 0;
	private int					weekDayTime				= WEEKDAY_TYPE_ANY;
	//private long start;
	private long				periodStart;
	private long				periodEnd;
	private Time				period;
	//время теста
	private LinkedList			testTime				= new LinkedList(); //of
	// DayTime
	//дата теста
	private LinkedList			testDate				= new LinkedList(); //of

	// Time
	private long[]				testTimes;

	public TimeStamp_dep() {
		// nothing
	}

	/** 
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {
		TimeStamp_dep timeStamp = new TimeStamp_dep();
		Calendar c = Calendar.getInstance();
		long start = c.getTimeInMillis();
		timeStamp.setPeriodStart(start);
		timeStamp.setPeriodEnd(start + 1000 * 60 * 60 * 24 * 8);
		int testType = Calendar.DAY_OF_MONTH;
		switch (testType) {
			case Calendar.MINUTE:
				timeStamp.setPeriod(Calendar.MINUTE, 1);
				timeStamp.addTestDate(Calendar.MINUTE, 0);
				timeStamp.addTestTime(0, 0, 0);
				break;
			case Calendar.HOUR_OF_DAY:
				timeStamp.setPeriod(Calendar.HOUR_OF_DAY, 1);
				timeStamp.addTestDate(Calendar.HOUR_OF_DAY, 0);
				//timeStamp.addTestTime(0, 17, 35);
				timeStamp.addTestTime(0, 0, 0);

				break;
			case Calendar.DAY_OF_MONTH:
				timeStamp.setPeriod(Calendar.DAY_OF_MONTH, 1);

				//				timeStamp.addTestDate(Calendar.DAY_OF_MONTH, 0);
				//				for (Iterator it = time.iterator(); it.hasNext();) {
				//					String str = (String) it.next();
				//					int h = Integer.parseInt(str.substring(0, 2));
				//					int m = Integer.parseInt(str.substring(3, 5));
				//					timeStamp.addTestTime(h, m, 0);
				//				}
				//				if (time.size() == 0)
				timeStamp.addTestDate(Calendar.DAY_OF_MONTH, 0);
				timeStamp.addTestTime(17, 35, 0);

				//switch (dayType.getimeStampelectedIndex()) {
				//	case DAYTYPE_WORK:
				//				timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				//				timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.TUESDAY);
				//				timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.WEDNESDAY);
				//				timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.THURSDAY);
				//				timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				//timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.SATURDAY);
				//timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				timeStamp.setWeekDayTime(TimeStamp_dep.WEEKDAY_TYPE_REST);
				//	break;
				//					case DAYTYPE_REST:
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.SATURDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				//						break;
				//					case DAYTYPE_ANY:
				//					default:
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.TUESDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.WEDNESDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.THURSDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK,
				// Calendar.SATURDAY);
				//						timeStamp.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				//						break;
				//				}
				break;
		}

		timeStamp.printTimes();
		//		System.out.println("times.length:" + times.length);
		//		for (int i = 0; i < times.length; i++) {
		//			System.out.println(i + "\t" + new Date(times[i]).toString());
		//		}
	}

	public void printTimes() {
		System.out.println("printTimes()::");
		long[] times = this.getTestTimes();
		System.out.println("times.length:" + times.length);
		for (int i = 0; i < times.length; i++) {
			System.out.println(i + "\t" + new Date(times[i]).toString());
		}
	}

	public long[] getTestTimes() {
		if (testTimes == null) {
			ArrayList list = new ArrayList();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(periodStart);
			//устанавливаем начало отсчета
			switch (this.period.getScale()) {
				case Calendar.MONTH:
					cal.set(Calendar.DAY_OF_MONTH, 1);
					break;
				case Calendar.DAY_OF_MONTH:
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					break;
				case Calendar.HOUR_OF_DAY:
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					break;
				case Calendar.MINUTE:
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					break;
				case Calendar.WEEK_OF_YEAR:
					while (cal.get(Calendar.DAY_OF_WEEK) != cal
							.getMinimum(Calendar.DAY_OF_WEEK))
						cal.add(Calendar.DAY_OF_WEEK, -1);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					break;
			}

			long period_t = cal.getTimeInMillis();
			boolean first = true;

			//System.out.println("testDate.size():" + testDate.size());
			//System.out.println("testTime.size():" + testTime.size());
			while (period_t <= periodEnd) {
				//boolean added = false;
				for (Iterator d_it = testDate.iterator(); d_it.hasNext();) {
					//устанавливаем день
					Time t = (Time) d_it.next();
					cal.add(t.getScale(), t.getValue()
							- cal.getMinimum(t.getScale()));

					// запоминаем начало дня
					long day_t = cal.getTimeInMillis();
					//for (Iterator t_it = testTime.iterator();
					// t_it.hasNext();) {
					for (int i = 0; i < testTime.size(); i++) {
						//устанавливаем время
						//DayTime dt = (DayTime) t_it.next();
						//if (period.getScale() != Calendar.HOUR) {
						DayTime dt = (DayTime) testTime.get(i);
						//System.out.println(dt.hour+":"+dt.minute+":"+dt.second);
						//int dst = cal.get(Calendar.DST_OFFSET);

						//						cal.add(Calendar.HOUR_OF_DAY, dt.hour);
						//						cal.add(Calendar.MINUTE, dt.minute);
						//						cal.add(Calendar.SECOND, dt.second);
						cal.add(Calendar.HOUR_OF_DAY, dt.getHour());
						cal.add(Calendar.MINUTE, dt.getMinute());
						cal.add(Calendar.SECOND, dt.getSecond());

						//int dst2 = cal.get(Calendar.DST_OFFSET);
						//if (dst != dst2)
						//cal.add(Calendar.MILLISECOND, dst - dst2);
						//добавляем время теста
						if ((cal.getTimeInMillis() >= periodStart)
								&& (cal.getTimeInMillis() <= periodEnd)
								&& weekConditions(cal)) {
							list.add(cal.getTime());
							//System.out.println(">" +
							// cal.getTime().toString());
							first = false;
							//added = true;
						}
						//}
						//возврат на начало дня
						cal.setTimeInMillis(day_t);
					}
					//возврат на начало периода
					cal.setTimeInMillis(period_t);
				}
				//прибавляем период
				if (first)
					cal.add(period.getScale(), 1);
				else
					cal.add(period.getScale(), period.getValue());
				//if (!added) list.add(cal.getTime());
				period_t = cal.getTimeInMillis();
			}

			// add only periodic events
			//		cal.setTimeInMillis(periodStart);
			//		period_t = cal.getTimeInMillis();
			//		System.out.println("// add only periodic events");
			//		System.out.println(cal.getTime().toString());
			//		while (period_t <= periodEnd) {
			//			list.add(cal.getTime());
			//			System.out.println(">"+cal.getTime().toString());
			//			cal.add(period.getScale(), period.getValue());
			//			period_t = cal.getTimeInMillis();
			//		}

			testTimes = new long[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Date date = (Date) list.get(i);
				testTimes[i] = date.getTime();
			}
		}
		return testTimes;
	}

	private boolean weekConditions(Calendar cal) {

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		boolean result = false;
		switch (weekDayTime) {
			case WEEKDAY_TYPE_WORK:
				if ((dayOfWeek == Calendar.SATURDAY)
						|| (dayOfWeek == Calendar.SUNDAY))
					result = false;
				else
					result = true;
				break;
			case WEEKDAY_TYPE_REST:
				if ((dayOfWeek == Calendar.SATURDAY)
						|| (dayOfWeek == Calendar.SUNDAY))
					result = true;
				else
					result = false;

				break;
			default:
				result = true;
				break;
		}
		//System.out.println(cal.getTime().toString() + "\t" + result);
		return result;
	}

	public void setPeriod(int scale, int value) {
		testTimes = null;
		this.period = new Time(scale, value);
	}

	public void setPeriod(Time period) {
		testTimes = null;
		this.period = period;
	}

	public void addTestTime(int hour, int minute, int second) {
		testTimes = null;
		if (findTestTime(hour, minute, second) == null)
				testTime.add(new DayTime(hour, minute, second));
	}

	public void removeTestTime(int hour, int minute, int second) {
		testTimes = null;
		DayTime dt = findTestTime(hour, minute, second);
		if (dt != null) testTime.remove(dt);
	}

	public void addTestDate(int scale, int value) {
		testTimes = null;
		if (findTestDate(scale, value) == null)
				testDate.add(new Time(scale, value));
	}

	public void removeTestDate(int scale, int value) {
		testTimes = null;
		Time t = findTestDate(scale, value);
		if (t != null) testDate.remove(t);
	}

	private DayTime findTestTime(int hour, int minute, int second) {
		DayTime dayTime = null;
		for (Iterator it = testTime.iterator(); it.hasNext();) {
			DayTime dt = (DayTime) it.next();
			if ((dt.minute == minute) && (dt.hour == hour)
					&& (dt.second == second)) {
				dayTime = dt;
				break;
			}
		}
		return dayTime;
	}

	private Time findTestDate(int scale, int value) {
		Time time = null;
		for (Iterator it = testDate.iterator(); it.hasNext();) {
			Time t = (Time) it.next();
			if ((t.getScale() == scale) && (t.getValue() == value)) {
				time = t;
				break;
			}

		}
		return time;
	}

	/**
	 * @return
	 */
	public long getPeriodEnd() {
		return periodEnd;
	}

	/**
	 * @return
	 */
	public long getPeriodStart() {
		return periodStart;
	}

	//	/**
	//	 * @return
	//	 */
	//	public long getStart() {
	//		return start;
	//	}

	/**
	 * @param periodEnd
	 */
	public void setPeriodEnd(long periodEnd) {
		testTimes = null;
		this.periodEnd = periodEnd;
	}

	/**
	 * @param periodStart
	 */
	public void setPeriodStart(long periodStart) {
		testTimes = null;
		this.periodStart = periodStart;
	}

	//	/**
	//	 * @param start
	//	 */
	//	public void setStart(long start) {
	//		this.start = start;
	//	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param timeStampType
	 */
	public void setType(int type) {
		testTimes = null;
		this.type = type;
	}

	public Time getPeriod() {
		return period;
	}

	//время теста
	//private LinkedList testTime = new LinkedList(); //of DayTime
	public LinkedList getTimeList() {
		return this.testTime;
	}

	//дата теста
	//private LinkedList testDate = new LinkedList(); //of Time
	public LinkedList getDateList() {
		return this.testDate;
	}

	/**
	 * @return Returns the weekDayTime.
	 */
	public int getWeekDayTime() {
		return weekDayTime;
	}

	/**
	 * @param weekDayTime
	 *            The weekDayTime to set.
	 */
	public void setWeekDayTime(int weekDayTime) {
		this.weekDayTime = weekDayTime;
	}
}