package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

public class TimeStamp {
	private Time period;
	//время теста
	private LinkedList testTime = new LinkedList(); //of DayTime
	//дата теста
	private LinkedList testDate = new LinkedList(); //of Time

	public TimeStamp() {
	}

	public long[] getTestTimes(long start, long periodStart, long periodEnd) {
		ArrayList list = new ArrayList();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(start);
		//устанавливаем начало отсчета
		switch (this.period.scale) {
			case Calendar.MONTH :
				cal.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case Calendar.DAY_OF_MONTH :
				cal.set(Calendar.HOUR_OF_DAY, 0);
				break;
			case Calendar.HOUR_OF_DAY :
				cal.set(Calendar.MINUTE, 0);
				break;
			case Calendar.MINUTE :
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				break;
			case Calendar.WEEK_OF_YEAR :
				while (cal.get(Calendar.DAY_OF_WEEK)
					!= cal.getMinimum(Calendar.DAY_OF_WEEK))
					cal.add(Calendar.DAY_OF_WEEK, -1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				break;
		}

		long period_t = cal.getTimeInMillis();
		boolean first = true;
		while (period_t <= periodEnd) {
			for (Iterator d_it = testDate.iterator(); d_it.hasNext();) {
				//устанавливаем день
				Time t = (Time) d_it.next();
				cal.add(t.scale, t.value - cal.getMinimum(t.scale));

				// запоминаем начало дня
				long day_t = cal.getTimeInMillis();
				for (Iterator t_it = testTime.iterator(); t_it.hasNext();) {
					//устанавливаем время
					DayTime dt = (DayTime) t_it.next();
					//int dst = cal.get(Calendar.DST_OFFSET);
					cal.add(Calendar.HOUR_OF_DAY, dt.hour);
					cal.add(Calendar.MINUTE, dt.minute);
					cal.add(Calendar.SECOND, dt.second);
					//int dst2 = cal.get(Calendar.DST_OFFSET);
					//if (dst != dst2)
					//cal.add(Calendar.MILLISECOND, dst - dst2);
					//добавляем время теста
					if (cal.getTimeInMillis() >= periodStart
						&& cal.getTimeInMillis() <= periodEnd) {
						list.add(cal.getTime());
						first = false;
					}
					//возврат на начало дня
					cal.setTimeInMillis(day_t);
				}
				//возврат на начало периода
				cal.setTimeInMillis(period_t);
			}
			//прибавляем период
			if (first)
				cal.add(period.scale, 1);
			else
				cal.add(period.scale, period.value);
			period_t = cal.getTimeInMillis();
		}

		long[] times = new long[list.size()];
		for (int i = 0; i < list.size(); i++)
			times[i] = ((Date) list.get(i)).getTime();
		return times;
	}

	public void setPeriod(int scale, int value) {
		this.period = new Time(scale, value);
	}

	public void addTestTime(int hour, int minute, int second) {
		if (findTestTime(hour, minute, second) == null)
			testTime.add(new DayTime(hour, minute, second));
	}

	public void removeTestTime(int hour, int minute, int second) {
		DayTime dt = findTestTime(hour, minute, second);
		if (dt != null)
			testTime.remove(dt);
	}

	public void addTestDate(int scale, int value) {
		if (findTestDate(scale, value) == null)
			testDate.add(new Time(scale, value));
	}

	public void removeTestDate(int scale, int value) {
		Time t = findTestDate(scale, value);
		if (t != null)
			testDate.remove(t);
	}

	private DayTime findTestTime(int hour, int minute, int second) {
		DayTime dayTime = null;
		for (Iterator it = testTime.iterator(); it.hasNext();) {
			DayTime dt = (DayTime) it.next();
			if ((dt.minute == minute)
				&& (dt.hour == hour)
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
			if ((t.scale == scale) && (t.value == value)) {
				time = t;
				break;
			}

		}
		return time;
	}

	private class Time {
		protected int scale;
		protected int value;

		Time(int scale, int value) {
			this.scale = scale;
			this.value = value;
		}
	}

	private class DayTime {
		int hour;
		int minute;
		int second;

		DayTime(int hour, int minute, int second) {
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
	}
}