/*
 * $Id: CronTemporalPattern.java,v 1.37 2006/06/06 15:41:26 arseniy Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlCronTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlCronTemporalPatternHelper;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.37 $, $Date: 2006/06/06 15:41:26 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class CronTemporalPattern extends AbstractTemporalPattern
		implements IdlTransferableObjectExt<IdlCronTemporalPattern> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256437014894163509L;
	private static final String I18N_KEY_MIN = "min";
	private static final String I18N_KEY_HOUR = "hour";
	private static final String I18N_KEY_DAY_OF_MONTH = "dayOfMonth";
	private static final String I18N_KEY_DAY_OF_WEEK = "dayOfWeek";
	private static final String I18N_KEY_MONTH = "month";
	private static final String I18N_KEY_OF = "of";
	private static final String I18N_KEY_TO = "to";
	private static final String I18N_KEY_FROM = "from";
	private static final String I18N_KEY_EACH = "each";

	public class TimeLine {

		private static final long SECOND_LONG = 1000;
		private static final long MINUTE_LONG = 60 * SECOND_LONG;
		private static final long HOUR_LONG = 60 * MINUTE_LONG;
		private static final long DAY_LONG = 24 * HOUR_LONG;
		private static final long MONTH_LONG = 31 * DAY_LONG;

		protected SortedSet<Date> dateList;
		private TimeValue dayOfMonth;
		private TimeValue dayOfWeek;

		private String timeLineDescription;

		private List<Integer> divisorList = new LinkedList<Integer>();
		private long endPeriod;
		private List<Integer> endsList = new LinkedList<Integer>();
		private TimeValue hours;

		private TimeValue minutes;
		private TimeValue month;

		private long startPeriod;
		private List<Integer> startsList = new LinkedList<Integer>();
		private String template;

		public void fillAllData() {
			if (this.minutes == null)
				this.minutes = parseExpression(LangModelMeasurement.getString(I18N_KEY_MIN), "*", 0, 59);
			if (this.hours == null)
				this.hours = parseExpression(LangModelMeasurement.getString(I18N_KEY_HOUR), "*", 0, 23);
			if (this.dayOfMonth == null)
				this.dayOfMonth = parseExpression(LangModelMeasurement.getString(I18N_KEY_DAY_OF_MONTH), "*", 1, 31);
			if (this.month == null)
				this.month = parseExpression(LangModelMeasurement.getString(I18N_KEY_MONTH), "*", 0, 11);
			this.month.names = MONTH_NAMES;
			if (this.dayOfWeek == null)
				this.dayOfWeek = parseExpression(LangModelMeasurement.getString(I18N_KEY_DAY_OF_WEEK), "*", 0, 6);
			this.dayOfWeek.names = DAY_OF_WEEK_NAMES;
		}

		/**
		 * @return Returns the dateList.
		 */
		public SortedSet getDateList() {
			return this.dateList;
		}

		/**
		 * @return Returns the dayOfMonth.
		 */
		public TimeValue getDayOfMonth() {
			return this.dayOfMonth;
		}

		/**
		 * @return Returns the dayOfWeek.
		 */
		public TimeValue getDayOfWeek() {
			return this.dayOfWeek;
		}

		public String getDescription() {
			if (this.timeLineDescription == null) {
				if (this.hours == null)
					parseTemplate();
				final StringBuffer desc = new StringBuffer();
				//
				if (this.hours.isAll()) {
					desc.append(this.minutes.toString());
					desc.append(" ");
					desc.append(LangModelMeasurement.getString(I18N_KEY_OF));
					desc.append(" ");
					desc.append(this.hours.toString());

				} else {
					final LinkedList<String> list = new LinkedList<String>();
					for (int h = 0; h < this.hours.host.length; h++) {
						for (int m = 0; m < this.minutes.host.length; m++) {
							list.add((this.hours.host[h] < 10 ? "0" : "") + Integer.toString(this.hours.host[h]) + ":"
									+ (this.minutes.host[m] < 10 ? "0" : "") + Integer.toString(this.minutes.host[m]));
						}
					}
					if (list.size() < 10) {
						Collections.sort(list);
						for (int i = 0; i < list.size(); i++) {
							final String str = list.get(i);
							if (desc.length() > 0)
								desc.append(", ");
							desc.append(str);
						}
					} else {
						desc.append(this.hours.toString());
						desc.append("; ");
						desc.append(this.minutes.toString());
					}
				}
				if (!this.dayOfWeek.isAll()) {
					desc.append("; ");
					desc.append(this.dayOfWeek.toString());
				}
				if (!this.dayOfMonth.isAll()) {
					desc.append("; ");
					desc.append(this.dayOfMonth.toString());
				}
				if (!this.month.isAll()) {
					desc.append("; ");
					desc.append(this.month.toString());
				}
				this.timeLineDescription = desc.toString();
			}
			return this.timeLineDescription;
		}

		/**
		 * @return Returns the endPeriod.
		 */
		public long getEndPeriod() {
			return this.endPeriod;
		}

		/**
		 * @return Returns the hours.
		 */
		public TimeValue getHours() {
			return this.hours;
		}

		/**
		 * @return Returns the minutes.
		 */
		public TimeValue getMinutes() {
			return this.minutes;
		}

		/**
		 * @return Returns the month.
		 */
		public TimeValue getMonth() {
			return this.month;
		}

		/**
		 * @return Returns the startPeriod.
		 */
		public long getStartPeriod() {
			return this.startPeriod;
		}

		/**
		 * @return Returns the template.
		 */
		public String getTemplate() {
			return this.template;
		}

		public void parseTemplate() {
			//String template = timeLine.getTemplate();
			//String template = (String) templates.get(k);
			long startTimeCalc = System.currentTimeMillis();
			final Pattern p = Pattern.compile("(.+)\\s+(.+)\\s+(.+)\\s+(.+)\\s+(.+)");
			//Pattern p = Pattern.compile("(.*)");
			if (this.dateList != null)
				this.dateList.clear();
			final Matcher matcher = p.matcher(this.template);
			if (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					final int begin = matcher.start(i);
					final int end = matcher.end(i);
					if ((begin >= 0) && (end <= this.template.length())) {
						final String subString = this.template.substring(matcher.start(i), matcher.end(i));
						if (DEBUG)
							System.out.println(i + "\t" + subString);
						switch (i) {
							case 1:
								//minute
								this.minutes = parseExpression(LangModelMeasurement.getString(I18N_KEY_MIN), subString, 0, 59);
								break;
							case 2:
								//hour
								this.hours = parseExpression(LangModelMeasurement.getString(I18N_KEY_HOUR), subString, 0, 23);
								break;
							case 3:
								//day of month
								this.dayOfMonth = parseExpression(LangModelMeasurement.getString(I18N_KEY_DAY_OF_MONTH), subString, 1, 31);
								break;
							case 4:
								//month
								this.month = parseExpression(LangModelMeasurement.getString(I18N_KEY_MONTH), subString, 0, 11);
								break;
							case 5:
								//day of week
								this.dayOfWeek = parseExpression(LangModelMeasurement.getString(I18N_KEY_DAY_OF_WEEK), subString, 0, 6);
								break;
						}

					}

				}
			} else{
				throw new IllegalArgumentException("illegial cron string");
			}
			fillAllData();

			//Calendar calendar = Calendar.getInstance();
			//calendar.setTimeInMillis(startPeriod);
			//calendar.set(Calendar.SECOND, 0);
			final Calendar c = Calendar.getInstance();
			//c.set(Calendar.MINUTE, 41);
			final long backTime = c.getTimeInMillis();
			if (DEBUG) {
				System.out.println("startPeriod:" + new Date(this.startPeriod).toString());
				System.out.println("endPeriod:" + new Date(this.endPeriod).toString());
			}
			for (int m = 0; m < this.month.host.length; m++) {
				c.setTimeInMillis(backTime);
				c.set(Calendar.MONTH, this.month.host[m]);
				long mTime = c.getTimeInMillis();
				if ((this.startPeriod - MONTH_LONG <= mTime) && (mTime <= this.endPeriod + MONTH_LONG)) {
					if (DEBUG)
						System.out.println("month\t" + c.getTime().toString());
					final long backMTime = c.getTimeInMillis();
					for (int dm = 0; dm < this.dayOfMonth.host.length; dm++) {
						c.setTimeInMillis(backMTime);
						c.set(Calendar.DAY_OF_MONTH, this.dayOfMonth.host[dm]);
						mTime = c.getTimeInMillis();
						if ((this.startPeriod - DAY_LONG <= mTime)
								&& (mTime <= this.endPeriod + DAY_LONG)) {
							if (DEBUG)
								System.out.println("dayOfMonth\t" + c.getTime().toString());
							long backDMTime = c.getTimeInMillis();
							for (int dw = 0; dw < this.dayOfWeek.host.length; dw++) {
								c.setTimeInMillis(backDMTime);
								c.set(Calendar.DAY_OF_WEEK,
									WEEK_NUMBER[this.dayOfWeek.host[dw]]);
								mTime = c.getTimeInMillis();
								if ((this.startPeriod - DAY_LONG <= mTime) && (mTime <= this.endPeriod + DAY_LONG)) {
									if (DEBUG)
										System.out.println("dayOfWeek\t" + c.getTime().toString());
									long backDWTime = c.getTimeInMillis();
									for (int h = 0; h < this.hours.host.length; h++) {
										c.setTimeInMillis(backDWTime);
										//										if
										// (DEBUG)
										//											System.out.println("
										// *
										// hour:"
										// +
										// this.hours.host[h]);
										c.set(Calendar.HOUR_OF_DAY, this.hours.host[h]);
										mTime = c.getTimeInMillis();
										if ((this.startPeriod - HOUR_LONG <= mTime) && (mTime <= this.endPeriod + HOUR_LONG)) {
											// if
											// (DEBUG)
											//												System.out
											//														.println("hour\t"
											//
											//																+ c
											//																		.getTime()
											//																		.toString());
											long backHTime = c.getTimeInMillis();
											for (int mm = 0; mm < this.minutes.host.length; mm++) {
												if (DEBUG)
													System.out.println(" \t* minute:" + this.minutes.host[mm]);
												// System.out
												//														.println("backHTime:"
												//																+
												// new
												// Date(
												//																		backHTime)
												//																+
												// "\t"
												//																+
												// new
												// Date(
												//																		this.startPeriod)
												//																+ ",
												// "
												//																+
												// new
												// Date(
												//																		this.endPeriod));
												c.setTimeInMillis(backHTime);
												c.set(Calendar.MINUTE, this.minutes.host[mm]);
												c.set(Calendar.SECOND, 0);

												mTime = c.getTimeInMillis();
												if (DEBUG)
													System.out.println(">"
															+ new Date(mTime)
															+ "\t"
															+ new Date(this.startPeriod)
															+ ", "
															+ new Date(this.endPeriod));
												if ((this.startPeriod <= mTime) && (mTime <= this.endPeriod)) {
													if (this.dateList == null)
														this.dateList = new TreeSet<Date>();
													this.dateList.add(c.getTime());
													if (DEBUG)
														System.out.println("minutes\t" + c.getTime().toString());
												}

											}
										}

									}
								}

							}
						}

					}

				}
			}
			if (DEBUG)
				System.out.println("Calc time:" + (System.currentTimeMillis() - startTimeCalc) + " ms.");

		}

		public void removeAll() {
			this.minutes = null;
			this.hours = null;
			this.dayOfMonth = null;
			this.month = null;
			this.dayOfWeek = null;
		}

		/**
		 * @param endPeriod
		 *                The endPeriod to set.
		 */
		public void setEndPeriod(final long endPeriod) {
			this.endPeriod = endPeriod;
		}

		/**
		 * @param startPeriod
		 *                The startPeriod to set.
		 */
		public void setStartPeriod(final long startPeriod) {
			this.startPeriod = startPeriod;
		}

		/**
		 * @param template
		 *                The template to set.
		 */
		public void setTemplate(final String template) {
			removeAll();
			this.timeLineDescription = null;
			this.template = template;
		}

		/**
		 * Parse one crontab-like expression [value[,value[...]]] .
		 * value can be: * (asterix) , integer number, integer interval
		 * n-m e.g 5-8
		 *
		 * @param exp
		 *                expression to be parsed
		 * @param min
		 *                minimal value for expression type
		 * @param max
		 *                maximal value for expression type
		 * @return a host of values
		 */
		private TimeValue parseExpression(final String name, final String exp, final int min, final int max) {
			final TimeValue timeValue = new TimeValue(name);
			final boolean[] indices = new boolean[max + 1];
			for (int i = 0; i < indices.length; i++)
				indices[i] = false;
			String[] params = exp.split(",");
			int count = 0;
			if (this.divisorList == null)
				this.divisorList = new LinkedList<Integer>();
			else
				this.divisorList.clear();
			if (this.startsList == null)
				this.startsList = new LinkedList<Integer>();
			else
				this.startsList.clear();
			if (this.endsList == null)
				this.endsList = new LinkedList<Integer>();
			else
				this.endsList.clear();
			for (int j = 0; j < params.length; j++) {
				int divisor = 0;
				int lvalue = -1;
				int rvalue = -1;
				final String substr = params[j];
				//System.out.println("->" + substr);
				final Pattern pattern = Pattern.compile("(\\*(\\/(\\d+))?)|((\\d+)(\\-(\\d+))?)");
				final Matcher matcher = pattern.matcher(params[j]);
				if (matcher.find()) {
					for (int i = 1; i <= matcher.groupCount(); i++) {
						final int begin = matcher.start(i);
						final int end = matcher.end(i);
						if ((begin >= 0) && (end <= exp.length())) {
							final String s = substr.substring(begin, end);
							switch (i) {
								case 1:
									divisor = 1;
									break;
								case 3:
									divisor = Integer.parseInt(s);
									break;
								case 5:
									lvalue = Integer.parseInt(s);
									rvalue = lvalue;
									break;
								case 7:
									rvalue = Integer.parseInt(s);
									break;

							}
							if (DEBUG)
								System.out.println(">" + i + "\t" + s + "\t" + lvalue + "," + rvalue);
						}
						//					else {
						//						System.out.println(">" + i +
						// " not found (" + begin
						//								+ "," + end + ")");
						//					}
					}
				}

				if ((divisor > 0) && (divisor >= min) && (divisor <= max)) {
					this.divisorList.add(new Integer(divisor));
					for (int i = min; i <= max; i++) {
						if (i % divisor == 0) {
							indices[i] = true;
							count++;
						}
					}
				}
				if (lvalue >= 0) {
					if ((lvalue >= min) && (lvalue <= max)) {
						this.startsList.add(new Integer(lvalue));
						this.endsList.add(new Integer(rvalue));
						for (int i = lvalue; i <= rvalue; i++) {
							indices[i] = true;
							count++;
						}
					}

					//System.out.println(")\t" + value);
				}

			}
			//		for (int i = 0; i < array.length; i++) {
			//			if (array[i] != 0) System.out.print(i + ",");
			//		}

			// build TimeValue
			timeValue.min = min;
			timeValue.max = max;
			timeValue.divisor = new int[this.divisorList.size()];
			for (int i = 0; i < timeValue.divisor.length; i++)
				timeValue.divisor[i] = this.divisorList.get(i).intValue();

			timeValue.starts = new int[this.startsList.size()];
			timeValue.ends = new int[this.endsList.size()];
			// count of start and end have to be equal !
			if (timeValue.starts.length == timeValue.ends.length) {
				for (int i = 0; i < timeValue.starts.length; i++) {
					timeValue.starts[i] = this.startsList.get(i).intValue();
					timeValue.ends[i] = this.endsList.get(i).intValue();
				}
			}

			timeValue.host = new int[count];
			int k = 0;
			for (int i = 0; i < indices.length; i++) {
				if (indices[i])
					timeValue.host[k++] = i;

			}

			if (DEBUG)
				System.out.println();
			return timeValue;
		}

		@Override
		public String toString() {
			return this.getDescription();
		}

		@Override
		public boolean equals(Object obj) {
			boolean equals = (obj == this);
			if ((!equals) && (obj instanceof TimeLine)) {
				TimeLine line = (TimeLine) obj;
				if ((line.getTemplate().equals(getTemplate()))
						&& (line.getStartPeriod() == getStartPeriod())
						&& (line.getEndPeriod() == getEndPeriod()))
					equals = true;
			}
			return equals;
		}

		@Override
		public int hashCode() {
			HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
			hashCodeGenerator.addObject(this.template);
			hashCodeGenerator.addLong(this.startPeriod);
			hashCodeGenerator.addLong(this.endPeriod);
			hashCodeGenerator.addObject(this.minutes);
			hashCodeGenerator.addObject(this.hours);
			hashCodeGenerator.addObject(this.dayOfMonth);
			hashCodeGenerator.addObject(this.month);
			hashCodeGenerator.addObject(this.dayOfWeek);
			hashCodeGenerator.addObject(this.timeLineDescription);
			hashCodeGenerator.addObject(this.startsList);
			hashCodeGenerator.addObject(this.endsList);
			hashCodeGenerator.addObject(this.divisorList);
			hashCodeGenerator.addObject(this.dateList);
			int result = hashCodeGenerator.getResult();
			hashCodeGenerator = null;
			return result;
		}
	}

	public class TimeValue {

		public int[] divisor;
		public int[] ends;
		public int[] host;
		public int max = 0;

		public int min = 0;

		public String name;
		public String[] names;
		public String pluralName;
		public int[] starts;

		public TimeValue(final String name) {
			this.name = name;
		}

		public TimeValue(final String name, final String pluralName) {
			this.name = name;
			this.pluralName = pluralName;
		}

		public boolean isAll() {
			boolean all = false;
			if (this.divisor != null) {
				for (int i = 0; i < this.divisor.length; i++) {
					all = (this.divisor[i] == 1);
					if (!all)
						break;
				}
			}
			if ((this.starts != null) && (this.ends != null)) {
				if (this.starts.length > 0)
					all = false;
			}

			return all;
		}

		@Override
		public String toString() {
			StringBuffer sbuf = new StringBuffer();
			if (this.divisor != null) {
				for (int i = 0; i < this.divisor.length; i++) {
					if (sbuf.length() > 0)
						sbuf.append(", ");

					sbuf.append(LangModelMeasurement.getString(I18N_KEY_EACH));
					sbuf.append(" ");
					String str = Integer.toString(this.divisor[i]);
					if (this.divisor[i] != 1) {
						sbuf.append(str);
						sbuf.append(" ");
					}
					sbuf.append(this.name);
				}
			}
			if ((this.starts != null) && (this.ends != null)) {
				for (int i = 0; i < this.starts.length; i++) {
					if (this.starts[i] != this.ends[i]) {
						if (sbuf.length() > 0)
							sbuf.append(", ");
						sbuf.append(LangModelMeasurement.getString(I18N_KEY_FROM));
						sbuf.append(" ");
						if (this.names == null)
							sbuf.append(Integer.toString(this.starts[i]));
						else
							sbuf.append(this.names[this.starts[i]]);
						sbuf.append(" ");
						sbuf.append(LangModelMeasurement.getString(I18N_KEY_TO));
						sbuf.append(" ");
						if (this.names == null)
							sbuf.append(Integer.toString(this.ends[i]));
						else
							sbuf.append(this.names[this.ends[i]]);
						sbuf.append(" ");
						if (this.names == null)
							sbuf.append(this.name);
					} else {
						if (sbuf.length() > 0)
							sbuf.append(", ");
						if (this.names == null)
							sbuf.append(Integer.toString(this.ends[i]));
						else
							sbuf.append(this.names[this.ends[i]]);
						if (this.names == null) {
							sbuf.append(" ");
							sbuf.append(this.name);
						}
					}

				}
			}
			return sbuf.toString();
		}

		@Override
		public boolean equals(Object obj) {
			boolean equals = (obj == this);
			if ((!equals) && (obj instanceof TimeValue)) {
				TimeValue value = (TimeValue) obj;
				if (HashCodeGenerator.equalsArray(this.starts, value.starts)
						&& HashCodeGenerator.equalsArray(this.ends, value.ends)
						&& HashCodeGenerator.equalsArray(this.divisor, value.divisor)
						&& HashCodeGenerator.equalsArray(this.host, value.host)
						&& (this.max == value.max) && (this.min == value.min))
					equals = true;
			}
			return equals;
		}

		@Override
		public int hashCode() {
			HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
			hashCodeGenerator.addIntArray(this.starts);
			hashCodeGenerator.addIntArray(this.ends);
			hashCodeGenerator.addIntArray(this.divisor);
			hashCodeGenerator.addIntArray(this.host);
			hashCodeGenerator.addInt(this.max);
			hashCodeGenerator.addInt(this.min);
			hashCodeGenerator.addObject(this.pluralName);
			hashCodeGenerator.addObject(this.name);
			hashCodeGenerator.addObjectArray(this.names);
			int result = hashCodeGenerator.getResult();
			hashCodeGenerator = null;
			return result;
		}
	}

	static final String[]		DAY_OF_WEEK_NAMES	= new String[] {
			LangModelMeasurement.getString("Sunday"), LangModelMeasurement.getString("Monday"),
			LangModelMeasurement.getString("Tuesday"), LangModelMeasurement.getString("Wednesday"),
			LangModelMeasurement.getString("Thursday"), LangModelMeasurement.getString("Friday"),
			LangModelMeasurement.getString("Saturday"),};							

	static final String[]		MONTH_NAMES		= new String[] {
			LangModelMeasurement.getString("January"), LangModelMeasurement.getString("February"),
			LangModelMeasurement.getString("March"), LangModelMeasurement.getString("April"),
			LangModelMeasurement.getString("May"), LangModelMeasurement.getString("June"),
			LangModelMeasurement.getString("July"), LangModelMeasurement.getString("Augest"),
			LangModelMeasurement.getString("September"), LangModelMeasurement.getString("October"),
			LangModelMeasurement.getString("November"), LangModelMeasurement.getString("December")};	

	static final int[]		WEEK_NUMBER		= new int[] { Calendar.SUNDAY, Calendar.MONDAY,
			Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};

	static final boolean		DEBUG			= false;

	private String[]		cronStrings;
	private String			description;

	/**
	 * Map of <{@link TimeLine},{@link TimeLine}>
	 */
	private Set<TimeLine>			templates;

	public CronTemporalPattern(final IdlCronTemporalPattern ctpt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(ctpt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	CronTemporalPattern(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final String[] cronStrings) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);

		this.description = description;
		this.cronStrings = cronStrings;
		this.setTemplates0(cronStrings);
	}

	private CronTemporalPattern(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final Set<String> cronString) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.description = description;		
		this.setTemplates0(cronString.toArray(new String[cronString.size()]));
	}

	/**
	 * create new instance for client
	 *
	 * @param description
	 * @param cronString
	 * @throws CreateObjectException
	 */
	public static CronTemporalPattern createInstance(final Identifier creatorId,
			final String description,
			final Set<String> cronString) throws CreateObjectException {
		if (creatorId == null || description == null || cronString == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final CronTemporalPattern cronTemporalPattern = new CronTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CRONTEMPORALPATTERN_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					cronString);

			assert cronTemporalPattern.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cronTemporalPattern.markAsChanged();

			return cronTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public static CronTemporalPattern createInstance(final Identifier creatorId,
			final String description,
			final String[] cronStrings) throws CreateObjectException {
		if (creatorId == null || description == null || cronStrings == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final CronTemporalPattern cronTemporalPattern = new CronTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CRONTEMPORALPATTERN_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					cronStrings);

			assert cronTemporalPattern.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cronTemporalPattern.markAsChanged();

			return cronTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}
	
	public static String getCronStringsDescription(final String[] cronStrings) {
		final CronTemporalPattern pattern = new CronTemporalPattern(null,
				null,
				INITIAL_VERSION,
				null,
				cronStrings);
		final String desc = pattern.toString();
		return desc;
	}

	public short getEntityCode() {
		return ObjectEntities.CRONTEMPORALPATTERN_CODE;
	}

	public String[] getCronStrings() {
		if ((this.cronStrings == null) || (this.cronStrings.length != this.templates.size()))
			this.cronStrings = new String[this.templates.size()];
		int i = 0;
		for (final TimeLine timeLine : this.templates) {
			this.cronStrings[i++] = timeLine.getTemplate();
		}
		return this.cronStrings;
	}

	public String getDescription() {
		return this.description;
	}

	public synchronized void fromIdlTransferable(final IdlCronTemporalPattern ctpt)
	throws IdlConversionException {
		super.fromIdlTransferable(ctpt);

		this.description = ctpt.description;
		this.setTemplates0(ctpt.cronStrings);
	}
	
	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCronTemporalPattern getIdlTransferable(final ORB orb) {
		return IdlCronTemporalPatternHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.description,
				getCronStrings());
	}

	protected synchronized void setAttributes(	final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String description,
			final String[] cronStrings) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.description = description;
		this.cronStrings = cronStrings;
		this.setTemplates0(cronStrings);
	}

	public int getTemplateCount() {
		int size = -1;
		if (this.templates != null)
			size = this.templates.size();
		return size;
	}

	public Set getTimeLines() {
		Set collection = null;
		synchronized (this.templates) {
			if (this.templates != null) {
				collection = this.templates;
				//System.out.println("collection.size():" +
				// collection.size());
			}
		}
		return collection;
	}

	@Override
	public final SortedSet<Date> getTimes(final Date start,
		final Date end,
		final Date startInterval,
		final Date endInterval) {
		throw new UnsupportedOperationException("CronTemporalPattern.getTimes() is unsupported");
	}
	
	/**
	 * fill times in ms that describes by temporal patterns and between start
	 * and end
	 */
	@Override
	protected void fillTimes() {
		for (final TimeLine timeLine : this.templates) {
			timeLine.setStartPeriod(this.startTime);
			timeLine.setEndPeriod(this.endTime);
			timeLine.parseTemplate();
			if (timeLine.dateList != null) {
				for (final Date date : timeLine.dateList) {
					if (!this.times.contains(date))
						this.times.add(date);
				}
			}
		}
	}

	private void setTemplates0(final String[] cronStringArray) {
		if (this.times == null)
			this.times = new TreeSet<Date>();
		else
			this.times.clear();
		if (this.templates == null)
			this.templates = new HashSet<TimeLine>();
		else
			this.templates.clear();

		if (cronStringArray != null) {
			synchronized (this.templates) {
				for (int i = 0; i < cronStringArray.length; i++) {
					final TimeLine timeLine = new TimeLine();
					timeLine.setTemplate(cronStringArray[i]);
					this.templates.add(timeLine);
				}
			}
		}
	}

	public void removeAll() {
		boolean removed = false;
		if (this.times != null) {
			this.times.clear();
			removed = true;
		}
		if (this.templates != null) {
			this.templates.clear();
			removed = true;
		}

		if (removed)
			super.markAsChanged();
	}

	public void addTemplate(final String template) {
		if (this.times == null)
			this.times = new TreeSet<Date>();
		else
			this.times.clear();
		if (this.templates == null)
			this.templates = new HashSet<TimeLine>();
		synchronized (this.templates) {
			final TimeLine timeLine = new TimeLine();
			timeLine.setTemplate(template);
			this.templates.add(timeLine);
		}

		super.markAsChanged();
		//setType(TIMESTAMPTYPE_PERIODIC);
	}

	public void removeTemplate(final String template) {
		for (final Iterator<TimeLine> it = this.templates.iterator(); it.hasNext();) {
			final TimeLine timeLine = it.next();
			if (timeLine.getTemplate().equals(template)) {
				it.remove();
				break;
			}
		}

		this.times.clear();

		super.markAsChanged();
	}

	/**
	 * @param description
	 *                The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected StorableObjectWrapper<CronTemporalPattern> getWrapper() {
		throw new UnsupportedOperationException(
				"CronTemporalPattern#getWrapper() is unsupported");
	}
}
