package com.syrus.AMFICOM.Client.Resource.Result;
import java.util.*;
import java.util.regex.*;

import com.syrus.AMFICOM.Client.Scheduler.General.I18N;

/*
 * 
 * Created on 18.05.2004
 *  
 */

//  
//  Format of template:
//  [*[/divisor]][,value[,value[...]]]
//  field allowed values.
//  ----- --------------.
//  minute 0-59.
//  hour 0-23.
//  day of month 1-31.
//  month 0-11 (0 is January)
//  day of week 0-6 (0 is Sunday).
/**
 * @author bob
 *  
 */

public class TemporalPattern {

	public class TimeLine {

		private static final long	SECOND_LONG	= 1000;
		private static final long	MINUTE_LONG	= 60 * SECOND_LONG;
		private static final long	HOUR_LONG	= 60 * MINUTE_LONG;
		private static final long	DAY_LONG	= 24 * HOUR_LONG;
		private static final long	MONTH_LONG	= 31 * DAY_LONG;

		protected ArrayList			dateList;
		private TimeValue			dayOfMonth;
		private TimeValue			dayOfWeek;

		private String				description;

		private ArrayList			divisorList	= new ArrayList();
		private long				endPeriod;
		private ArrayList			endsList	= new ArrayList();
		private TimeValue			hours;

		private TimeValue			minutes;
		private TimeValue			month;

		private long				startPeriod;
		private List				startsList	= new ArrayList();
		private String				template;

		public void fillAllData() {
			if (minutes == null)
					minutes = parseExpression(I18N.getString("min"), "*", 0, 59); //$NON-NLS-1$ //$NON-NLS-2$
			if (hours == null)
					hours = parseExpression(I18N.getString("hour"), "*", 0, 23); //$NON-NLS-1$ //$NON-NLS-2$
			if (dayOfMonth == null)
					dayOfMonth = parseExpression(
							I18N.getString("day_of_month"), "*", 1, 31); //$NON-NLS-1$ //$NON-NLS-2$
			if (month == null)
					month = parseExpression(I18N.getString("month"), "*", 0, 11); //$NON-NLS-1$ //$NON-NLS-2$
			month.names = MONTH_NAMES;
			if (dayOfWeek == null)
					dayOfWeek = parseExpression(I18N.getString("day_of_week"),
							"*", 0, 6); //$NON-NLS-1$ //$NON-NLS-2$
			dayOfWeek.names = DAY_OF_WEEK_NAMES;
		}

		/**
		 * @return Returns the dateList.
		 */
		public List getDateList() {
			return dateList;
		}

		/**
		 * @return Returns the dayOfMonth.
		 */
		public TimeValue getDayOfMonth() {
			return dayOfMonth;
		}

		/**
		 * @return Returns the dayOfWeek.
		 */
		public TimeValue getDayOfWeek() {
			return dayOfWeek;
		}

		public String getDescription() {
			if (description == null) {
				if (hours == null) parseTemplate();
				StringBuffer desc = new StringBuffer();
				//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				// //$NON-NLS-1$
				if (hours.isAll()) {
					desc.append(minutes.toString());					
					desc.append(" ");
					desc.append(I18N.getString("of"));
					desc.append(" ");
					desc.append(hours.toString());
					
				} else {
					ArrayList list = new ArrayList();
					for (int h = 0; h < hours.host.length; h++) {
						for (int m = 0; m < minutes.host.length; m++) {
							list.add((hours.host[h] < 10 ? "0" : "")
									+ Integer.toString(hours.host[h]) + ":"
									+ (minutes.host[m] < 10 ? "0" : "")
									+ Integer.toString(minutes.host[m]));
						}
					}
					if (list.size() < 10) {
						Collections.sort(list);
						for (int i = 0; i < list.size(); i++) {
							String str = (String) list.get(i);
							if (desc.length() > 0) desc.append(", ");
							desc.append(str);
						}
					} else {
						desc.append(hours.toString());
						desc.append("; ");
						desc.append(minutes.toString());
					}
				}
				if (!dayOfWeek.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(dayOfWeek.toString());
				}
				if (!dayOfMonth.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(dayOfMonth.toString());
				}
				if (!month.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(month.toString());
				}
				this.description = desc.toString();
			}
			//System.out.println("description:"+this.description);
			return this.description;
		}

		/**
		 * @return Returns the endPeriod.
		 */
		public long getEndPeriod() {
			return endPeriod;
		}

		/**
		 * @return Returns the hours.
		 */
		public TimeValue getHours() {
			return hours;
		}

		/**
		 * @return Returns the minutes.
		 */
		public TimeValue getMinutes() {
			return minutes;
		}

		/**
		 * @return Returns the month.
		 */
		public TimeValue getMonth() {
			return month;
		}

		/**
		 * @return Returns the startPeriod.
		 */
		public long getStartPeriod() {
			return startPeriod;
		}

		/**
		 * @return Returns the template.
		 */
		public String getTemplate() {
			return template;
		}

		public void parseTemplate() {
			//String template = timeLine.getTemplate();
			//String template = (String) templates.get(k);
			long startTimeCalc = System.currentTimeMillis();
			Pattern p = Pattern.compile("(.*)\\s+(.*)\\s+(.*)\\s+(.*)\\s+(.*)"); //$NON-NLS-1$
			//Pattern p = Pattern.compile("(.*)");
			if (dateList != null) dateList.clear();
			Matcher matcher = p.matcher(template);
			if (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					int begin = matcher.start(i);
					int end = matcher.end(i);
					if ((begin >= 0) && (end <= template.length())) {
						String subString = template.substring(matcher.start(i),
								matcher.end(i));
						if (DEBUG) System.out.println(i + "\t" + subString);
						switch (i) {
							case 1:
								//minute
								minutes = parseExpression(
										I18N.getString("min"), subString, 0, //$NON-NLS-1$
										59);
								break;
							case 2:
								//hour
								hours = parseExpression(I18N.getString("hour"),
										subString, 0, //$NON-NLS-1$
										23);
								break;
							case 3:
								//day of month
								dayOfMonth = parseExpression(I18N
										.getString("day_of_month"), subString, //$NON-NLS-1$
										1, 31);
								break;
							case 4:
								//month
								month = parseExpression(
										I18N.getString("month"), subString, 0, //$NON-NLS-1$
										11);
								break;
							case 5:
								//day of week
								dayOfWeek = parseExpression(I18N
										.getString("day_of_week"), //$NON-NLS-1$
										subString, 0, 6);
								break;
						}

					}

				}
			}
			fillAllData();

			//Calendar calendar = Calendar.getInstance();
			//calendar.setTimeInMillis(startPeriod);
			//calendar.set(Calendar.SECOND, 0);
			Calendar c = Calendar.getInstance();
			long backTime = c.getTimeInMillis();
			if (DEBUG) {
				System.out.println("startPeriod:" //$NON-NLS-1$
						+ new Date(startPeriod).toString());
				System.out.println("endPeriod:" //$NON-NLS-1$
						+ new Date(endPeriod).toString());
			}
			for (int m = 0; m < month.host.length; m++) {
				c.setTimeInMillis(backTime);
				c.set(Calendar.MONTH, month.host[m]);
				long mTime = c.getTimeInMillis();
				if ((startPeriod - MONTH_LONG / 2 <= mTime)
						&& (mTime <= endPeriod + MONTH_LONG / 2)) {
					if (DEBUG) System.out.println("month\t" //$NON-NLS-1$
							+ c.getTime().toString());
					long backMTime = c.getTimeInMillis();
					for (int dm = 0; dm < dayOfMonth.host.length; dm++) {
						c.setTimeInMillis(backMTime);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth.host[dm]);
						mTime = c.getTimeInMillis();
						if ((startPeriod - DAY_LONG / 2 <= mTime)
								&& (mTime <= endPeriod + DAY_LONG / 2)) {
							if (DEBUG) System.out.println("dayOfMonth\t" //$NON-NLS-1$
									+ c.getTime().toString());
							long backDMTime = c.getTimeInMillis();
							for (int dw = 0; dw < dayOfWeek.host.length; dw++) {
								c.setTimeInMillis(backDMTime);
								c.set(Calendar.DAY_OF_WEEK,
										weekNumber[dayOfWeek.host[dw]]);
								mTime = c.getTimeInMillis();
								if ((startPeriod - DAY_LONG / 2 <= mTime)
										&& (mTime <= endPeriod + DAY_LONG / 2)) {
									if (DEBUG) System.out.println("dayOfWeek\t" //$NON-NLS-1$
											+ c.getTime().toString());
									long backDWTime = c.getTimeInMillis();
									for (int h = 0; h < hours.host.length; h++) {
										c.setTimeInMillis(backDWTime);
										//										if (DEBUG)
										//												System.out.println(" * hour:"
										// //$NON-NLS-1$
										//														+ hours.host[h]);
										c.set(Calendar.HOUR_OF_DAY,
												hours.host[h]);
										mTime = c.getTimeInMillis();
										if ((startPeriod - HOUR_LONG / 2 <= mTime)
												&& (mTime <= endPeriod
														+ HOUR_LONG / 2)) {
											if (DEBUG)
													System.out
															.println("hour\t" //$NON-NLS-1$
																	+ c
																			.getTime()
																			.toString());
											long backHTime = c
													.getTimeInMillis();
											for (int mm = 0; mm < minutes.host.length; mm++) {
												//System.out.println("backHTime"+new
												// Date(backHTime)+"\t"+new
												// Date(startPeriod)+", "+new
												// Date(endPeriod));
												c.setTimeInMillis(backHTime);
												c.set(Calendar.MINUTE,
														minutes.host[mm]);
												mTime = c.getTimeInMillis();
												if ((startPeriod - MINUTE_LONG
														/ 2 <= mTime)
														&& (mTime <= endPeriod
																+ MINUTE_LONG
																/ 2)) {
													c.set(Calendar.SECOND, 0);
													if (dateList == null)
															dateList = new ArrayList();
													dateList.add(c.getTime());
													if (DEBUG)
															System.out
																	.println("minutes\t" //$NON-NLS-1$
																			+ c
																					.getTime()
																					.toString());
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
			if (DEBUG) System.out.println("Calc time:" //$NON-NLS-1$
					+ (System.currentTimeMillis() - startTimeCalc) + " ms."); //$NON-NLS-1$

		}

		public void removeAll() {
			minutes = null;
			hours = null;
			dayOfMonth = null;
			month = null;
			dayOfWeek = null;
		}

		/**
		 * @param endPeriod
		 *            The endPeriod to set.
		 */
		public void setEndPeriod(long endPeriod) {
			this.endPeriod = endPeriod;
		}

		/**
		 * @param startPeriod
		 *            The startPeriod to set.
		 */
		public void setStartPeriod(long startPeriod) {
			this.startPeriod = startPeriod;
		}

		/**
		 * @param template
		 *            The template to set.
		 */
		public void setTemplate(String template) {
			removeAll();
			this.description = null;
			this.template = template;
		}

		/**
		 * Parse one crontab-like expression [value[,value[...]]] . value can
		 * be: * (asterix) , integer number, integer interval n-m e.g 5-8
		 * 
		 * @param exp
		 *            expression to be parsed
		 * @param min
		 *            minimal value for expression type
		 * @param max
		 *            maximal value for expression type
		 * @return a host of values
		 */
		private TimeValue parseExpression(String name, String exp, int min,
				int max) {
			TimeValue timeValue = new TimeValue(name);
			boolean[] indices = new boolean[max + 1];
			for (int i = 0; i < indices.length; i++)
				indices[i] = false;
			String[] params = exp.split(","); //$NON-NLS-1$
			int count = 0;
			if (divisorList == null)
				divisorList = new ArrayList();
			else
				divisorList.clear();
			if (startsList == null)
				startsList = new ArrayList();
			else
				startsList.clear();
			if (endsList == null)
				endsList = new ArrayList();
			else
				endsList.clear();
			for (int j = 0; j < params.length; j++) {
				int divisor = 0;
				int lvalue = -1;
				int rvalue = -1;
				String substr = params[j];
				//System.out.println("->" + substr);
				Pattern pattern = Pattern
						.compile("(\\*(\\/(\\d+))?)|((\\d+)(\\-(\\d+))?)"); //$NON-NLS-1$
				Matcher matcher = pattern.matcher(params[j]);
				if (matcher.find()) {
					for (int i = 1; i <= matcher.groupCount(); i++) {
						int begin = matcher.start(i);
						int end = matcher.end(i);
						if ((begin >= 0) && (end <= exp.length())) {
							String s = substr.substring(begin, end);
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
							if (DEBUG) System.out.println(">" + i + "\t" + s //$NON-NLS-1$ //$NON-NLS-2$
									+ "\t" + lvalue + "," + rvalue); //$NON-NLS-1$ //$NON-NLS-2$
						}
						//					else {
						//						System.out.println(">" + i + " not found (" + begin
						//								+ "," + end + ")");
						//					}
					}
				}

				if ((divisor > 0) && (divisor >= min) && (divisor <= max)) {
					divisorList.add(new Integer(divisor));
					for (int i = min; i <= max; i++) {
						if (i % divisor == 0) {
							indices[i] = true;
							count++;
						}
					}
				}
				if (lvalue >= 0) {
					if ((lvalue >= min) && (lvalue <= max)) {
						startsList.add(new Integer(lvalue));
						endsList.add(new Integer(rvalue));
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
			timeValue.divisor = new int[divisorList.size()];
			for (int i = 0; i < timeValue.divisor.length; i++)
				timeValue.divisor[i] = ((Integer) divisorList.get(i))
						.intValue();

			timeValue.starts = new int[startsList.size()];
			timeValue.ends = new int[endsList.size()];
			// count of start and end have to be equal !
			if (timeValue.starts.length == timeValue.ends.length) {
				for (int i = 0; i < timeValue.starts.length; i++) {
					timeValue.starts[i] = ((Integer) startsList.get(i))
							.intValue();
					timeValue.ends[i] = ((Integer) endsList.get(i)).intValue();
				}
			}

			timeValue.host = new int[count];
			int k = 0;
			for (int i = 0; i < indices.length; i++) {
				if (indices[i]) timeValue.host[k++] = i;

			}

			if (DEBUG) System.out.println();
			return timeValue;
		}
	}

	public class TimeValue {

		public int[]	divisor;
		public int[]	ends;
		public int[]	host;
		public int		max	= 0;

		public int		min	= 0;

		public String	name;
		public String[]	names;
		public String	pluralName;
		public int[]	starts;

		public TimeValue(String name) {
			this.name = name;
		}

		public TimeValue(String name, String pluralName) {
			this.name = name;
			this.pluralName = pluralName;
		}

		public boolean isAll() {
			boolean all = false;
			if (divisor != null) {
				for (int i = 0; i < divisor.length; i++) {
					all = (divisor[i] == 1);
					if (!all) break;
				}
			}
			if ((starts != null) && (ends != null)) {
				if (starts.length > 0) all = false;
			}

			return all;
		}

		public String toString() {
			StringBuffer sbuf = new StringBuffer();
			if (divisor != null) {
				for (int i = 0; i < divisor.length; i++) {
					if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$

					sbuf.append(I18N.getString("each")); //$NON-NLS-1$
					sbuf.append(" "); //$NON-NLS-1$
					String str = Integer.toString(divisor[i]);
					if (divisor[i] != 1) {
						sbuf.append(str);
						sbuf.append(" "); //$NON-NLS-1$
					}
					sbuf.append(name);
				}
			}
			if ((starts != null) && (ends != null)) {
				for (int i = 0; i < starts.length; i++) {
					if (starts[i] != ends[i]) {
						if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$
						sbuf.append(I18N.getString("from")); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (names == null)
							sbuf.append(Integer.toString(starts[i]));
						else
							sbuf.append(names[starts[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						sbuf.append(I18N.getString("to")); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (names == null)
							sbuf.append(Integer.toString(ends[i]));
						else
							sbuf.append(names[ends[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						if (names == null) sbuf.append(name);
					} else {
						if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$
						if (names == null)
							sbuf.append(Integer.toString(ends[i]));
						else
							sbuf.append(names[ends[i]]);
						if (names == null) {
							sbuf.append(" ");
							sbuf.append(name);
						}
					}

				}
			}
			return sbuf.toString();
		}
	}
	public static final String[]	DAY_OF_WEEK_NAMES	= new String[] {
			I18N.getString("Sunday"), I18N.getString("Monday"),
			I18N.getString("Tuesday"), I18N.getString("Wednesday"),
			I18N.getString("Thursday"), I18N.getString("Friday"),
			I18N.getString("Saturday"),					};

	final static boolean			DEBUG				= false;

	public static final String[]	MONTH_NAMES			= new String[] {
			I18N.getString("January"), I18N.getString("February"), //$NON-NLS-1$ //$NON-NLS-2$
			I18N.getString("March"), I18N.getString("April"), //$NON-NLS-1$ //$NON-NLS-2$
			I18N.getString("May"), I18N.getString("June"), //$NON-NLS-1$ //$NON-NLS-2$
			I18N.getString("July"), I18N.getString("Augest"), //$NON-NLS-1$ //$NON-NLS-2$
			I18N.getString("September"), I18N.getString("October"), //$NON-NLS-1$ //$NON-NLS-2$
			I18N.getString("November"), I18N.getString("December")};	//$NON-NLS-1$ //$NON-NLS-2$

	public static final int[]		weekNumber			= new int[] {
			Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
			Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
			Calendar.SATURDAY							};
	private long					endPeriod;

	private long					startPeriod;
	private HashMap					templates;
	private long[]					times;

	public TemporalPattern() {
		// nothing
	}

	public static void main(String[] args) {
		//String template = ;
		//String template = "*/2,3,5";
		TemporalPattern ts = new TemporalPattern();
		Calendar c = Calendar.getInstance();
		ts.setStartPeriod(c.getTimeInMillis() - 1000 * 60 * 30);
		ts.setEndPeriod(c.getTimeInMillis() + 1000 * 60 * 60 * 1);

		//ts.addTemplate("*/10 */2,13 * * *"); //$NON-NLS-1$
		//ts.addTemplate("12,5-8 * * * *"); //$NON-NLS-1$
		//ts.addTemplate("*/20 * * 0,4,5 */2,0,6"); //$NON-NLS-1$
		//ts.addTemplate("*/20 0-9 */2 2,4,5 0,6");
		ts.addTemplate("20 * * * *");
		ts.addTemplate("*/3 * * * *");
		ts.printTimes();

	}

	public void addTemplate(String template) {
		times = null;
		if (templates == null) templates = new HashMap();
		TimeLine timeLine = new TimeLine();
		timeLine.setTemplate(template);
		timeLine.setStartPeriod(startPeriod);
		timeLine.setEndPeriod(endPeriod);
		templates.put(timeLine, timeLine);
	}

	/**
	 * @return Returns the endPeriod.
	 */
	public long getEndPeriod() {
		return endPeriod;
	}

	/**
	 * @return Returns the startPeriod.
	 */
	public long getStartPeriod() {
		return startPeriod;
	}

	//	public void removeTemplate(String template) {
	//		if (templates != null) {
	//			//if (templates.containsKey(template))
	//			templates.remove(template);
	//		}
	//	}

	public int getTemplateCount() {
		int size = -1;
		if (templates != null) size = templates.size();
		return size;
	}

	public Collection getTimeLines() {
		Collection collection = null;
		if (templates != null) {
			collection = templates.values();
			//System.out.println("collection.size():" + collection.size());
		}
		return collection;
	}

	public long[] getTimes() {
		if (times == null) {
			//int count = 0;
			Collection list = templates.values();
			List timeList = new ArrayList();
			for (Iterator it = list.iterator(); it.hasNext();) {
				TimeLine timeLine = (TimeLine) it.next();
				timeLine.parseTemplate();
				//				if (timeLine.dateList != null) {
				//					//count += timeLine.dateList.size();
				//					//if (DEBUG)
				//					{
				//						if (timeLine.dateList.size() > 0) {
				//
				//							if (DEBUG) {
				//								System.out.println(timeLine.getDescription());
				//								for (Iterator it2 = timeLine.dateList
				//										.iterator(); it2.hasNext();) {
				//									System.out.println(it2.next().toString());
				//								}
				//							}
				//						}
				//					}
				//				}
				if (timeLine.dateList != null) {
					for (Iterator it2 = timeLine.dateList.iterator(); it2
							.hasNext();) {
						Object obj = it2.next();
						if (!timeList.contains(obj)) timeList.add(obj);
					}

				}

			}

			times = new long[timeList.size()];
			int count = 0;
			for (Iterator it = timeList.iterator(); it.hasNext();) {
				times[count++] = ((Date) it.next()).getTime();
			}
		}

		return times;
	}

	public void printTimes() {
		long[] t = getTimes();
		System.out.println("printTimes ::"); //$NON-NLS-1$
		System.out.println("startTime ::" + new Date(getStartPeriod()));
		System.out.println("endTime ::" + new Date(getEndPeriod()));
		for (int i = 0; i < times.length; i++) {
			System.out.println(new Date(t[i]).toString());
		}
		for(Iterator it=templates.values().iterator();it.hasNext();){
			TimeLine line =(TimeLine)it.next();
			System.out.println(line.getDescription());
		}
	}

	/**
	 * @param endPeriod
	 *            The endPeriod to set.
	 */
	public void setEndPeriod(long endPeriod) {
		times = null;
		this.endPeriod = endPeriod;
	}

	/**
	 * @param startPeriod
	 *            The startPeriod to set.
	 */
	public void setStartPeriod(long startPeriod) {
		times = null;
		this.startPeriod = startPeriod;
	}
}