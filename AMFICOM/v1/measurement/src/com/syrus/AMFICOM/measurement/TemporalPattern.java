/*
 * $Id: TemporalPattern.java,v 1.55 2004/12/09 12:47:20 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.55 $, $Date: 2004/12/09 12:47:20 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class TemporalPattern extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256437014894163509L;
	private static final String	I18N_KEY_MIN		= "min";
	private static final String	I18N_KEY_HOUR		= "hour";
	private static final String	I18N_KEY_DAY_OF_MONTH	= "dayOfMonth";
	private static final String	I18N_KEY_DAY_OF_WEEK	= "dayOfWeek";
	private static final String	I18N_KEY_MONTH		= "month";
	private static final String	I18N_KEY_OF		= "of";
	private static final String	I18N_KEY_TO		= "to";
	private static final String	I18N_KEY_FROM		= "from";
	private static final String	I18N_KEY_EACH		= "each";

	public class TimeLine {

		private static final long	SECOND_LONG	= 1000;
		private static final long	MINUTE_LONG	= 60 * SECOND_LONG;
		private static final long	HOUR_LONG	= 60 * MINUTE_LONG;
		private static final long	DAY_LONG	= 24 * HOUR_LONG;
		private static final long	MONTH_LONG	= 31 * DAY_LONG;

		protected List			dateList;
		private TimeValue		dayOfMonth;
		private TimeValue		dayOfWeek;

		private String			description;

		private List			divisorList	= new LinkedList();
		private long			endPeriod;
		private List			endsList	= new LinkedList();
		private TimeValue		hours;

		private TimeValue		minutes;
		private TimeValue		month;

		private long			startPeriod;
		private List			startsList	= new LinkedList();
		private String			template;

		public void fillAllData() {
			if (this.minutes == null)
				this.minutes = parseExpression(LangModelMeasurement.getString(I18N_KEY_MIN), "*", 0, 59); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.hours == null)
				this.hours = parseExpression(LangModelMeasurement.getString(I18N_KEY_HOUR), "*", 0, 23); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.dayOfMonth == null)
				this.dayOfMonth = parseExpression(
									LangModelMeasurement
											.getString(I18N_KEY_DAY_OF_MONTH),
									"*", 1, 31); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.month == null)
				this.month = parseExpression(LangModelMeasurement.getString(I18N_KEY_MONTH), "*", 0, 11); //$NON-NLS-1$ //$NON-NLS-2$
			this.month.names = MONTH_NAMES;
			if (this.dayOfWeek == null)
				this.dayOfWeek = parseExpression(LangModelMeasurement.getString(I18N_KEY_DAY_OF_WEEK), //$NON-NLS-1$
									"*", 0, 6); //$NON-NLS-1$ //$NON-NLS-2$
			this.dayOfWeek.names = DAY_OF_WEEK_NAMES;
		}

		/**
		 * @return Returns the dateList.
		 */
		public List getDateList() {
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
			if (this.description == null) {
				if (this.hours == null)
					parseTemplate();
				StringBuffer desc = new StringBuffer();
				// //$NON-NLS-1$
				if (this.hours.isAll()) {
					desc.append(this.minutes.toString());
					desc.append(" "); //$NON-NLS-1$
					desc.append(LangModelMeasurement.getString(I18N_KEY_OF)); //$NON-NLS-1$
					desc.append(" "); //$NON-NLS-1$
					desc.append(this.hours.toString());

				} else {
					LinkedList list = new LinkedList();
					for (int h = 0; h < this.hours.host.length; h++) {
						for (int m = 0; m < this.minutes.host.length; m++) {
							list.add((this.hours.host[h] < 10 ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
									+ Integer.toString(this.hours.host[h]) + ":" //$NON-NLS-1$
									+ (this.minutes.host[m] < 10 ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
									+ Integer.toString(this.minutes.host[m]));
						}
					}
					if (list.size() < 10) {
						Collections.sort(list);
						for (int i = 0; i < list.size(); i++) {
							String str = (String) list.get(i);
							if (desc.length() > 0)
								desc.append(", "); //$NON-NLS-1$
							desc.append(str);
						}
					} else {
						desc.append(this.hours.toString());
						desc.append("; "); //$NON-NLS-1$
						desc.append(this.minutes.toString());
					}
				}
				if (!this.dayOfWeek.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(this.dayOfWeek.toString());
				}
				if (!this.dayOfMonth.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(this.dayOfMonth.toString());
				}
				if (!this.month.isAll()) {
					desc.append("; "); //$NON-NLS-1$
					desc.append(this.month.toString());
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
			Pattern p = Pattern.compile("(.+)\\s+(.+)\\s+(.+)\\s+(.+)\\s+(.+)"); //$NON-NLS-1$
			//Pattern p = Pattern.compile("(.*)");
			if (this.dateList != null)
				this.dateList.clear();
			Matcher matcher = p.matcher(this.template);
			if (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					int begin = matcher.start(i);
					int end = matcher.end(i);
					if ((begin >= 0) && (end <= this.template.length())) {
						String subString = this.template.substring(matcher.start(i), matcher
								.end(i));
						if (DEBUG)
							System.out.println(i + "\t" + subString); //$NON-NLS-1$
						switch (i) {
							case 1:
								//minute
								this.minutes = parseExpression(LangModelMeasurement
										.getString(I18N_KEY_MIN), subString, 0, //$NON-NLS-1$
												59);
								break;
							case 2:
								//hour
								this.hours = parseExpression(LangModelMeasurement
										.getString(I18N_KEY_HOUR), //$NON-NLS-1$
												subString, 0, //$NON-NLS-1$
												23);
								break;
							case 3:
								//day of month
								this.dayOfMonth = parseExpression(LangModelMeasurement
										.getString(I18N_KEY_DAY_OF_MONTH),
													subString, //$NON-NLS-1$
													1, 31);
								break;
							case 4:
								//month
								this.month = parseExpression(LangModelMeasurement
										.getString(I18N_KEY_MONTH), subString,
												0, //$NON-NLS-1$
												11);
								break;
							case 5:
								//day of week
								this.dayOfWeek = parseExpression(LangModelMeasurement
										.getString(I18N_KEY_DAY_OF_WEEK), //$NON-NLS-1$
													subString, 0, 6);
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
			Calendar c = Calendar.getInstance();
			//c.set(Calendar.MINUTE, 41);
			long backTime = c.getTimeInMillis();
			if (DEBUG) {
				System.out.println("startPeriod:" //$NON-NLS-1$
						+ new Date(this.startPeriod).toString());
				System.out.println("endPeriod:" //$NON-NLS-1$
						+ new Date(this.endPeriod).toString());
			}
			for (int m = 0; m < this.month.host.length; m++) {
				c.setTimeInMillis(backTime);
				c.set(Calendar.MONTH, this.month.host[m]);
				long mTime = c.getTimeInMillis();
				if ((this.startPeriod - MONTH_LONG <= mTime) && (mTime <= this.endPeriod + MONTH_LONG)) {
					if (DEBUG)
						System.out.println("month\t" //$NON-NLS-1$
								+ c.getTime().toString());
					long backMTime = c.getTimeInMillis();
					for (int dm = 0; dm < this.dayOfMonth.host.length; dm++) {
						c.setTimeInMillis(backMTime);
						c.set(Calendar.DAY_OF_MONTH, this.dayOfMonth.host[dm]);
						mTime = c.getTimeInMillis();
						if ((this.startPeriod - DAY_LONG <= mTime)
								&& (mTime <= this.endPeriod + DAY_LONG)) {
							if (DEBUG)
								System.out.println("dayOfMonth\t" //$NON-NLS-1$
										+ c.getTime().toString());
							long backDMTime = c.getTimeInMillis();
							for (int dw = 0; dw < this.dayOfWeek.host.length; dw++) {
								c.setTimeInMillis(backDMTime);
								c.set(Calendar.DAY_OF_WEEK,
									WEEK_NUMBER[this.dayOfWeek.host[dw]]);
								mTime = c.getTimeInMillis();
								if ((this.startPeriod - DAY_LONG <= mTime)
										&& (mTime <= this.endPeriod + DAY_LONG)) {
									if (DEBUG)
										System.out
												.println("dayOfWeek\t" //$NON-NLS-1$
														+ c
																.getTime()
																.toString());
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
										c.set(Calendar.HOUR_OF_DAY,
											this.hours.host[h]);
										mTime = c.getTimeInMillis();
										if ((this.startPeriod - HOUR_LONG <= mTime)
												&& (mTime <= this.endPeriod
														+ HOUR_LONG)) {
											//											if
											// (DEBUG)
											//												System.out
											//														.println("hour\t"
											// //$NON-NLS-1$
											//																+ c
											//																		.getTime()
											//																		.toString());
											long backHTime = c
													.getTimeInMillis();
											for (int mm = 0; mm < this.minutes.host.length; mm++) {
												if (DEBUG)
													System.out
															.println(" \t* minute:"
																	+ this.minutes.host[mm]);
												//												System.out
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
												c
														.setTimeInMillis(backHTime);
												c
														.set(
															Calendar.MINUTE,
															this.minutes.host[mm]);
												c.set(Calendar.SECOND,
													0);

												mTime = c
														.getTimeInMillis();
												if (DEBUG)
													System.out
															.println(">"
																	+ new Date(
																			mTime)
																	+ "\t"
																	+ new Date(
																			this.startPeriod)
																	+ ", "
																	+ new Date(
																			this.endPeriod));
												if ((this.startPeriod <= mTime)
														&& (mTime <= this.endPeriod)) {
													if (this.dateList == null)
														this.dateList = new LinkedList();
													this.dateList
															.add(c
																	.getTime());
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
			if (DEBUG)
				System.out.println("Calc time:" //$NON-NLS-1$
						+ (System.currentTimeMillis() - startTimeCalc) + " ms."); //$NON-NLS-1$

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
		public void setEndPeriod(long endPeriod) {
			this.endPeriod = endPeriod;
		}

		/**
		 * @param startPeriod
		 *                The startPeriod to set.
		 */
		public void setStartPeriod(long startPeriod) {
			this.startPeriod = startPeriod;
		}

		/**
		 * @param template
		 *                The template to set.
		 */
		public void setTemplate(String template) {
			removeAll();
			this.description = null;
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
		private TimeValue parseExpression(String name, String exp, int min, int max) {
			TimeValue timeValue = new TimeValue(name);
			boolean[] indices = new boolean[max + 1];
			for (int i = 0; i < indices.length; i++)
				indices[i] = false;
			String[] params = exp.split(","); //$NON-NLS-1$
			int count = 0;
			if (this.divisorList == null)
				this.divisorList = new LinkedList();
			else
				this.divisorList.clear();
			if (this.startsList == null)
				this.startsList = new LinkedList();
			else
				this.startsList.clear();
			if (this.endsList == null)
				this.endsList = new LinkedList();
			else
				this.endsList.clear();
			for (int j = 0; j < params.length; j++) {
				int divisor = 0;
				int lvalue = -1;
				int rvalue = -1;
				String substr = params[j];
				//System.out.println("->" + substr);
				Pattern pattern = Pattern.compile("(\\*(\\/(\\d+))?)|((\\d+)(\\-(\\d+))?)"); //$NON-NLS-1$
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
							if (DEBUG)
								System.out.println(">" + i + "\t" + s //$NON-NLS-1$ //$NON-NLS-2$
										+ "\t" + lvalue + "," + rvalue); //$NON-NLS-1$ //$NON-NLS-2$
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
				timeValue.divisor[i] = ((Integer) this.divisorList.get(i)).intValue();

			timeValue.starts = new int[this.startsList.size()];
			timeValue.ends = new int[this.endsList.size()];
			// count of start and end have to be equal !
			if (timeValue.starts.length == timeValue.ends.length) {
				for (int i = 0; i < timeValue.starts.length; i++) {
					timeValue.starts[i] = ((Integer) this.startsList.get(i)).intValue();
					timeValue.ends[i] = ((Integer) this.endsList.get(i)).intValue();
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

		public String toString() {
			return this.getDescription();
		}

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
			hashCodeGenerator.addObject(this.description);
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

		public int[]	divisor;
		public int[]	ends;
		public int[]	host;
		public int	max	= 0;

		public int	min	= 0;

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

		public String toString() {
			StringBuffer sbuf = new StringBuffer();
			if (this.divisor != null) {
				for (int i = 0; i < this.divisor.length; i++) {
					if (sbuf.length() > 0)
						sbuf.append(", "); //$NON-NLS-1$

					sbuf.append(LangModelMeasurement.getString(I18N_KEY_EACH)); //$NON-NLS-1$
					sbuf.append(" "); //$NON-NLS-1$
					String str = Integer.toString(this.divisor[i]);
					if (this.divisor[i] != 1) {
						sbuf.append(str);
						sbuf.append(" "); //$NON-NLS-1$
					}
					sbuf.append(this.name);
				}
			}
			if ((this.starts != null) && (this.ends != null)) {
				for (int i = 0; i < this.starts.length; i++) {
					if (this.starts[i] != this.ends[i]) {
						if (sbuf.length() > 0)
							sbuf.append(", "); //$NON-NLS-1$
						sbuf.append(LangModelMeasurement.getString(I18N_KEY_FROM)); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(Integer.toString(this.starts[i]));
						else
							sbuf.append(this.names[this.starts[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						sbuf.append(LangModelMeasurement.getString(I18N_KEY_TO)); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(Integer.toString(this.ends[i]));
						else
							sbuf.append(this.names[this.ends[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(this.name);
					} else {
						if (sbuf.length() > 0)
							sbuf.append(", "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(Integer.toString(this.ends[i]));
						else
							sbuf.append(this.names[this.ends[i]]);
						if (this.names == null) {
							sbuf.append(" "); //$NON-NLS-1$
							sbuf.append(this.name);
						}
					}

				}
			}
			return sbuf.toString();
		}

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
			LangModelMeasurement.getString("Sunday"), LangModelMeasurement.getString("Monday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("Tuesday"), LangModelMeasurement.getString("Wednesday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("Thursday"), LangModelMeasurement.getString("Friday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("Saturday"),};							//$NON-NLS-1$

	static final String[]		MONTH_NAMES		= new String[] {
			LangModelMeasurement.getString("January"), LangModelMeasurement.getString("February"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("March"), LangModelMeasurement.getString("April"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("May"), LangModelMeasurement.getString("June"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("July"), LangModelMeasurement.getString("Augest"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("September"), LangModelMeasurement.getString("October"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelMeasurement.getString("November"), LangModelMeasurement.getString("December")};	//$NON-NLS-1$ //$NON-NLS-2$

	static final int[]		WEEK_NUMBER		= new int[] { Calendar.SUNDAY, Calendar.MONDAY,
			Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};

	static final boolean		DEBUG			= false;

	private String[]		cronStrings;
	private String			description;
	private StorableObjectDatabase	temporalPatternDatabase;

	/**
	 * Map of <{@link TimeLine},{@link TimeLine}>
	 */
	private List			templates;
	private List			times;

	private long			startTime		= 0;
	private long			endTime			= 0;

	public TemporalPattern(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			this.temporalPatternDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}

	}

	public TemporalPattern(TemporalPattern_Transferable tpt) throws CreateObjectException {
		super(tpt.header);

		this.description = new String(tpt.description);
		//this.cronStrings = new String[tpt.cronStrings.length];
		long ver = this.currentVersion;
		removeAll();
		for (int i = 0; i < tpt.cron_strings.length; i++) {
			//this.cronStrings[i] = new String(tpt.cronStrings[i]);
			addTemplate(new String(tpt.cron_strings[i]));
		}
		this.currentVersion = ver;

		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			if (this.temporalPatternDatabase != null)
				this.temporalPatternDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	protected TemporalPattern(Identifier id, Identifier creatorId, String description, String[] cronStrings) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.description = description;
		this.cronStrings = cronStrings;
		if (cronStrings != null) {
			removeAll();
			for (int i = 0; i < cronStrings.length; i++)
				addTemplate(cronStrings[i]);
		}

		this.currentVersion = super.getNextVersion();

		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
	}

	private TemporalPattern(Identifier id, Identifier creatorId, String description, List cronString) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.description = description;
		long ver = this.currentVersion;
		for (Iterator it = cronString.iterator(); it.hasNext();) {
			String str = (String) it.next();
			addTemplate(str);
		}

		this.currentVersion = ver;
		this.currentVersion = super.getNextVersion();

		this.temporalPatternDatabase = MeasurementDatabaseContext.temporalPatternDatabase;
	}

	/**
	 * create new instance for client
	 * 
	 * @param description
	 * @param cronString
	 * @throws CreateObjectException
	 */
	public static TemporalPattern createInstance(Identifier creatorId,
							String description,
							List cronString) throws CreateObjectException {
		if (creatorId == null || description == null || cronString == null )
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new TemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE),
				creatorId, description, cronString);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TemporalPattern.createInstance | cannot generate identifier ", e);
		}
	}

	public static TemporalPattern createInstance(Identifier creatorId,
							String description,
							String[] cronStrings) throws CreateObjectException {
		if (creatorId == null || description == null || cronStrings == null )
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new TemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), creatorId, description, cronStrings);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TemporalPattern.createInstance | cannot generate identifier ", e);
		}
	}

	public short getEntityCode() {
		return ObjectEntities.TEMPORALPATTERN_ENTITY_CODE;
	}

	public String[] getCronStrings() {
		if ((this.cronStrings == null) || (this.cronStrings.length != this.templates.size()))
			this.cronStrings = new String[this.templates.size()];
		{
			int i = 0;
			for (Iterator it = this.templates.iterator(); it.hasNext();) {
				TimeLine line = (TimeLine) it.next();
				this.cronStrings[i++] = new String(line.getTemplate());
			}
		}
		return this.cronStrings;
	}

	public String getDescription() {
		return this.description;
	}

	public Object getTransferable() {
		return new TemporalPattern_Transferable(super.getHeaderTransferable(),
							new String(this.description), getCronStrings());
	}

	protected synchronized void setAttributes(	Date created,
							Date modified,
							Identifier creatorId,
							Identifier modifierId,
							String description,
							String[] cronStrings) {
		super.setAttributes(created, modified, creatorId, modifierId);
		this.description = description;
		this.cronStrings = cronStrings;
		long ver = this.currentVersion;
		removeAll();
		for (int i = 0; i < cronStrings.length; i++)
			addTemplate(cronStrings[i]);
		this.currentVersion = ver;

	}

	public int getTemplateCount() {
		int size = -1;
		if (this.templates != null)
			size = this.templates.size();
		return size;
	}

	public Collection getTimeLines() {
		Collection collection = null;
		synchronized (this.templates) {
			if (this.templates != null) {
				collection = this.templates;
				//System.out.println("collection.size():" +
				// collection.size());
			}
		}
		return collection;
	}

	/**
	 * get times in ms that describes by temporal patterns and between start
	 * and end
	 * 
	 * @param start
	 *                Date
	 * @param end
	 *                Date
	 * @return List of java.util.Data
	 */
	public List getTimes(Date start, Date end) {
		return this.getTimes(start.getTime(), end.getTime());	
	}

	/**
	 * get times in ms that describes by temporal patterns and between start
	 * and end
	 * 
	 * @param start
	 *                long
	 * @param end
	 *                long
	 * @return List of java.util.Data
	 */
	public List getTimes(long start, long end) {
		if (this.times == null)
			this.times = new LinkedList();
		if (this.startTime != start)
			this.times.clear();
		this.startTime = start;
		if (this.endTime != end)
			this.times.clear();
		this.endTime = end;

		if (this.times.isEmpty()) {
			//int count = 0;
			Collection list = this.templates;
			for (Iterator it = list.iterator(); it.hasNext();) {
				TimeLine timeLine = (TimeLine) it.next();
				timeLine.setStartPeriod(start);
				timeLine.setEndPeriod(end);
				timeLine.parseTemplate();
				if (timeLine.dateList != null) {
					for (Iterator it2 = timeLine.dateList.iterator(); it2.hasNext();) {
						Object obj = it2.next();
						if (!this.times.contains(obj))
							this.times.add(obj);
					}

				}

			}
		}

		return this.times;
	}

	public void removeAll() {
		this.currentVersion = super.getNextVersion();
		if (this.times != null)
			this.times.clear();
		if (this.templates != null)
			this.templates.clear();
	}

	public void addTemplate(String template) {
		this.currentVersion = super.getNextVersion();		
		if (this.times == null)
			this.times = new LinkedList();
		else
			this.times.clear();
		if (this.templates == null)
			this.templates = new LinkedList();
		synchronized (this.templates) {
			TimeLine timeLine = new TimeLine();
			timeLine.setTemplate(template);
			this.templates.add(timeLine);
		}
		//setType(TIMESTAMPTYPE_PERIODIC);
	}

	public void removeTemplate(String template) {
		for (Iterator it = this.templates.iterator(); it.hasNext();) {
		TimeLine timeLine = (TimeLine) it.next();
		if (timeLine.getTemplate().equals(template)) {
			it.remove();
			break;
			}
		}

		this.times.clear();
		this.currentVersion = super.getNextVersion();		
	}

	/**
	 * @param description
	 *                The description to set.
	 */
	public void setDescription(String description) {
		this.currentVersion = super.getNextVersion();		
		this.description = description;
	}

	public boolean equals(Object obj) {
		boolean equals = (obj == this);
		if ((!equals) && (obj instanceof TemporalPattern)) {
			TemporalPattern pattern = (TemporalPattern) obj;
			if ((pattern.id.equals(super.id))
					&& HashCodeGenerator.equalsDate(this.created, pattern.created)
					&& (this.creatorId.equals(pattern.creatorId))
					&& HashCodeGenerator.equalsDate(this.modified, pattern.modified)
					&& (this.modifierId.equals(pattern.modifierId))
					&& (this.modifierId.equals(pattern.modifierId))
					&& (HashCodeGenerator.equalsArray(pattern.getTimeLines().toArray(),
										getTimeLines().toArray())))
				equals = true;
		}
		return equals;
	}

	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addObjectArray(this.cronStrings);
		hashCodeGenerator.addObject(this.getTimeLines());
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}
	
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Iterator it = this.templates.iterator(); it.hasNext();) {
			TimeLine timeLine = (TimeLine) it.next();
			buffer.append(timeLine.getDescription());
			if (it.hasNext()){
				buffer.append(";");
				buffer.append(LangModelMeasurement.getString("and"));
			}
		}
		return buffer.toString();
	}
	
	public List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
