package com.syrus.AMFICOM.measurement;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.PTTemporalTemplate_Transferable;
import com.syrus.AMFICOM.measurement.corba.temporal_template.TimeQuantum_Transferable;
import com.syrus.AMFICOM.measurement.corba.temporal_template.DayTime_Transferable;

public class PTTemporalTemplate extends StorableObject {
	private String description;
	private TimeQuantum period;
	private LinkedList dayTimes; /*of DayTime*/
	private LinkedList dates; /*of TimeQuantum*/

	private StorableObject_Database ptTemporalTemplateDatabase;

	public PTTemporalTemplate(Identifier id) throws RetrieveObjectException {
		super(id);

		this.ptTemporalTemplateDatabase = StorableObject_DatabaseContext.ptTemporalTemplateDatabase;
		try {
			this.ptTemporalTemplateDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PTTemporalTemplate(PTTemporalTemplate_Transferable pttt_t) throws CreateObjectException {
		super(new Identifier(pttt_t.id),
					new Date(pttt_t.created),
					new Date(pttt_t.modified),
					new Identifier(pttt_t.creator_id),
					new Identifier(pttt_t.modifier_id));
		this.description = new String(pttt_t.description);
		this.period = new TimeQuantum(pttt_t.period.scale, pttt_t.period.value);
		this.dayTimes = new LinkedList();
		for (int i = 0; i < pttt_t.day_times.length; i++)
			this.dayTimes.add(new DayTime(pttt_t.day_times[i].hour, pttt_t.day_times[i].minute, pttt_t.day_times[i].second));
		this.dates = new LinkedList();
		for (int i = 0; i < pttt_t.dates.length; i++)
			this.dates.add(new TimeQuantum(pttt_t.dates[i].scale, pttt_t.dates[i].value));

		this.ptTemporalTemplateDatabase = StorableObject_DatabaseContext.ptTemporalTemplateDatabase;
		try {
			this.ptTemporalTemplateDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public PTTemporalTemplate(Identifier id,
														Identifier creator_id,
														String description,
														int period_scale,
														int period_value) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creator_id,
					creator_id);
		this.description = description;
		this.period = new TimeQuantum(period_scale, period_value);
		this.dayTimes = new LinkedList();
		this.dates = new LinkedList();

	}

	public ArrayList getTimeStamps(long start_time,
																 long from_time,
																 long till_time) {
		ArrayList arraylist = new ArrayList();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start_time);

		switch (this.period.scale) {
			case Calendar.MONTH:
				calendar.set(Calendar.DAY_OF_MONTH, 1);
			case Calendar.DAY_OF_MONTH:
				calendar.set(Calendar.HOUR_OF_DAY, 0);
			case Calendar.HOUR_OF_DAY:
				calendar.set(Calendar.MINUTE, 0);
			case Calendar.MINUTE:
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				break;
			case Calendar.WEEK_OF_YEAR:
/*				calendar.set(Calendar.DAY_OF_WEEK, calendar.getMinimum(Calendar.DAY_OF_WEEK));
				calendar.add(Calendar.WEEK_OF_YEAR, -1);*/
				while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getMinimum(Calendar.DAY_OF_WEEK))
					calendar.add(Calendar.DAY_OF_WEEK, -1);

				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
		}

		long period_start = calendar.getTimeInMillis();
		boolean first = true;
		while (period_start <= till_time) {
			for (Iterator d_it = this.dates.iterator(); d_it.hasNext();) {
				TimeQuantum date = (TimeQuantum)d_it.next();
				calendar.add(date.scale, date.value - calendar.getMinimum(date.scale));

				long day_begin = calendar.getTimeInMillis();
				for (Iterator t_it = this.dayTimes.iterator(); t_it.hasNext();) {
					DayTime dt = (DayTime)t_it.next();
					calendar.add(Calendar.HOUR_OF_DAY, dt.hour);
					calendar.add(Calendar.MINUTE, dt.minute);
					calendar.add(Calendar.SECOND, dt.second);
					if (calendar.getTimeInMillis() >= from_time && calendar.getTimeInMillis() <= till_time) {
						arraylist.add(calendar.getTime());
						first = false;
					}
					calendar.setTimeInMillis(day_begin);
				}
				calendar.setTimeInMillis(period_start);
			}
			if (first)
				calendar.add(this.period.scale, 1);
			else
				calendar.add(this.period.scale, this.period.value);
			period_start = calendar.getTimeInMillis();
		}
		return arraylist;
/*
		long[] ti = new long[arraylist.size()];
		for (int i = 0; i < ti.length; i++)
			ti[i] = ((Date)arraylist.get(i)).getTime();
		return ti;*/
	}

	public void setPeriod(int scale, int value)	{
		this.period = new TimeQuantum(scale, value);
	}

	public void addDayTime(int hour, int minute, int second)	{
		if (this.findDayTime(hour, minute, second) == null)
			this.dayTimes.add(new DayTime(hour, minute, second));
	}
	
	public void removeDayTime(int hour, int minute, int second) {
		DayTime dt = this.findDayTime(hour, minute, second);
		if (dt != null)
			this.dayTimes.remove(dt);
	}

	public void addDate(int scale, int value)	{
		if (this.findDate(scale, value) == null)
			this.dates.add(new TimeQuantum(scale, value));
	}

	public void removeDate(int scale, int value) {
		TimeQuantum t = this.findDate(scale, value);
		if (t != null)
			this.dates.remove(t);
	}

	private DayTime findDayTime(int hour, int minute, int second) {
		DayTime dt;
		for (Iterator it = this.dayTimes.iterator(); it.hasNext();) {
			dt = (DayTime)it.next();
			if (dt.minute == minute && dt.hour == hour && dt.second == second)
				return dt;
		}
		return null;
	}

	private TimeQuantum findDate(int scale, int value)	{
		for (Iterator it = this.dates.iterator(); it.hasNext();) {
			TimeQuantum t = (TimeQuantum)it.next();
			if (t.scale == scale && t.value == value)
				return t;
		}
		return null;
	}

	public Object getTransferable() {
		DayTime_Transferable[] day_times_t = new DayTime_Transferable[this.dayTimes.size()];
		int i = 0;
		DayTime dayTime;
		for (Iterator iterator = this.dayTimes.iterator(); iterator.hasNext();) {
			dayTime = (DayTime)iterator.next();
			day_times_t[i++] = new DayTime_Transferable(dayTime.hour, dayTime.minute, dayTime.second);
		}
		TimeQuantum_Transferable[] dates_t = new TimeQuantum_Transferable[this.dates.size()];
		i = 0;
		TimeQuantum date;
		for (Iterator iterator = this.dates.iterator(); iterator.hasNext();) {
			date = (TimeQuantum)iterator.next();
			dates_t[i++] = new TimeQuantum_Transferable(date.scale, date.value);
		}
		return new PTTemporalTemplate_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																							 super.created.getTime(),
																							 super.modified.getTime(),
																							 (Identifier_Transferable)super.creator_id.getTransferable(),
																							 (Identifier_Transferable)super.modifier_id.getTransferable(),
																							 new String(this.description),
																							 new TimeQuantum_Transferable(this.period.scale, this.period.value),
																							 day_times_t,
																							 dates_t);
	}

	public String getDescription() {
		return this.description;
	}

	public TimeQuantum getPeriod() {
		return this.period;
	}

	public LinkedList getDayTimes() {
		return this.dayTimes;
	}

	public LinkedList getDates() {
		return this.dates;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String description,
																						TimeQuantum period,
																						LinkedList dayTimes,
																						LinkedList dates) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.description = description;
		this.period = period;
		this.dayTimes = dayTimes;
		this.dates = dates;
	}

	static class TimeQuantum {
		int scale;
		int value;

		TimeQuantum(int scale, int value) {
			this.scale = scale;
			this.value = value;
		}
	}

	static class DayTime	{
		int hour;
		int minute;
		int second;

		DayTime(int hour, int minute, int second)	{
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
	}
}