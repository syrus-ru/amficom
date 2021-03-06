package com.syrus.AMFICOM.measurement;

import java.util.*;
import java.util.regex.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;

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

public class TemporalPattern implements TransferableObject {

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
			if (this.minutes == null)
					this.minutes = parseExpression(
							LangModelSchedule.getString("min"), "*", 0, 59); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.hours == null)
					this.hours = parseExpression(
							LangModelSchedule.getString("hour"), "*", 0, 23); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.dayOfMonth == null)
					this.dayOfMonth = parseExpression(LangModelSchedule
							.getString("day_of_month"), "*", 1, 31); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.month == null)
					this.month = parseExpression(
							LangModelSchedule.getString("month"), "*", 0, 11); //$NON-NLS-1$ //$NON-NLS-2$
			this.month.names = MONTH_NAMES;
			if (this.dayOfWeek == null)
					this.dayOfWeek = parseExpression(LangModelSchedule
							.getString("day_of_week"), //$NON-NLS-1$
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
				if (this.hours == null) parseTemplate();
				StringBuffer desc = new StringBuffer();
				//SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
				// //$NON-NLS-1$
				if (this.hours.isAll()) {
					desc.append(this.minutes.toString());
					desc.append(" "); //$NON-NLS-1$
					desc.append(LangModelSchedule.getString("of")); //$NON-NLS-1$
					desc.append(" "); //$NON-NLS-1$
					desc.append(this.hours.toString());

				} else {
					ArrayList list = new ArrayList();
					for (int h = 0; h < this.hours.host.length; h++) {
						for (int m = 0; m < this.minutes.host.length; m++) {
							list.add((this.hours.host[h] < 10 ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
									+ Integer.toString(this.hours.host[h])
									+ ":" //$NON-NLS-1$
									+ (this.minutes.host[m] < 10 ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
									+ Integer.toString(this.minutes.host[m]));
						}
					}
					if (list.size() < 10) {
						Collections.sort(list);
						for (int i = 0; i < list.size(); i++) {
							String str = (String) list.get(i);
							if (desc.length() > 0) desc.append(", "); //$NON-NLS-1$
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
			Pattern p = Pattern.compile("(.*)\\s+(.*)\\s+(.*)\\s+(.*)\\s+(.*)"); //$NON-NLS-1$
			//Pattern p = Pattern.compile("(.*)");
			if (this.dateList != null) this.dateList.clear();
			Matcher matcher = p.matcher(this.template);
			if (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					int begin = matcher.start(i);
					int end = matcher.end(i);
					if ((begin >= 0) && (end <= this.template.length())) {
						String subString = this.template.substring(matcher
								.start(i), matcher.end(i));
						if (DEBUG) System.out.println(i + "\t" + subString); //$NON-NLS-1$
						switch (i) {
							case 1:
								//minute
								this.minutes = parseExpression(LangModelSchedule
										.getString("min"), subString, 0, //$NON-NLS-1$
										59);
								break;
							case 2:
								//hour
								this.hours = parseExpression(LangModelSchedule
										.getString("hour"), //$NON-NLS-1$
										subString, 0, //$NON-NLS-1$
										23);
								break;
							case 3:
								//day of month
								this.dayOfMonth = parseExpression(LangModelSchedule
										.getString("day_of_month"), subString, //$NON-NLS-1$
										1, 31);
								break;
							case 4:
								//month
								this.month = parseExpression(LangModelSchedule
										.getString("month"), subString, 0, //$NON-NLS-1$
										11);
								break;
							case 5:
								//day of week
								this.dayOfWeek = parseExpression(LangModelSchedule
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
						+ new Date(this.startPeriod).toString());
				System.out.println("endPeriod:" //$NON-NLS-1$
						+ new Date(this.endPeriod).toString());
			}
			for (int m = 0; m < this.month.host.length; m++) {
				c.setTimeInMillis(backTime);
				c.set(Calendar.MONTH, this.month.host[m]);
				long mTime = c.getTimeInMillis();
				if ((this.startPeriod - MONTH_LONG / 2 <= mTime)
						&& (mTime <= this.endPeriod + MONTH_LONG / 2)) {
					if (DEBUG) System.out.println("month\t" //$NON-NLS-1$
							+ c.getTime().toString());
					long backMTime = c.getTimeInMillis();
					for (int dm = 0; dm < this.dayOfMonth.host.length; dm++) {
						c.setTimeInMillis(backMTime);
						c.set(Calendar.DAY_OF_MONTH, this.dayOfMonth.host[dm]);
						mTime = c.getTimeInMillis();
						if ((this.startPeriod - DAY_LONG / 2 <= mTime)
								&& (mTime <= this.endPeriod + DAY_LONG / 2)) {
							if (DEBUG) System.out.println("dayOfMonth\t" //$NON-NLS-1$
									+ c.getTime().toString());
							long backDMTime = c.getTimeInMillis();
							for (int dw = 0; dw < this.dayOfWeek.host.length; dw++) {
								c.setTimeInMillis(backDMTime);
								c.set(Calendar.DAY_OF_WEEK,
										weekNumber[this.dayOfWeek.host[dw]]);
								mTime = c.getTimeInMillis();
								if ((this.startPeriod - DAY_LONG / 2 <= mTime)
										&& (mTime <= this.endPeriod + DAY_LONG
												/ 2)) {
									if (DEBUG) System.out.println("dayOfWeek\t" //$NON-NLS-1$
											+ c.getTime().toString());
									long backDWTime = c.getTimeInMillis();
									for (int h = 0; h < this.hours.host.length; h++) {
										c.setTimeInMillis(backDWTime);
										//										if (DEBUG_LEVEL)
										//												System.out.println(" * hour:"
										// //$NON-NLS-1$
										//														+ hours.host[h]);
										c.set(Calendar.HOUR_OF_DAY,
												this.hours.host[h]);
										mTime = c.getTimeInMillis();
										if ((this.startPeriod - HOUR_LONG / 2 <= mTime)
												&& (mTime <= this.endPeriod
														+ HOUR_LONG / 2)) {
											if (DEBUG)
													System.out
															.println("hour\t" //$NON-NLS-1$
																	+ c
																			.getTime()
																			.toString());
											long backHTime = c
													.getTimeInMillis();
											for (int mm = 0; mm < this.minutes.host.length; mm++) {
												//System.out.println("backHTime"+new
												// Date(backHTime)+"\t"+new
												// Date(startPeriod)+", "+new
												// Date(endPeriod));
												c.setTimeInMillis(backHTime);
												c.set(Calendar.MINUTE,
														this.minutes.host[mm]);
												mTime = c.getTimeInMillis();
												if ((this.startPeriod
														- MINUTE_LONG / 2 <= mTime)
														&& (mTime <= this.endPeriod
																+ MINUTE_LONG
																/ 2)) {
													c.set(Calendar.SECOND, 0);
													if (this.dateList == null)
															this.dateList = new ArrayList();
													this.dateList.add(c
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
			if (DEBUG) System.out.println("Calc time:" //$NON-NLS-1$
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
			if (this.divisorList == null)
				this.divisorList = new ArrayList();
			else
				this.divisorList.clear();
			if (this.startsList == null)
				this.startsList = new ArrayList();
			else
				this.startsList.clear();
			if (this.endsList == null)
				this.endsList = new ArrayList();
			else
				this.endsList.clear();
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
				timeValue.divisor[i] = ((Integer) this.divisorList.get(i))
						.intValue();

			timeValue.starts = new int[this.startsList.size()];
			timeValue.ends = new int[this.endsList.size()];
			// count of start and end have to be equal !
			if (timeValue.starts.length == timeValue.ends.length) {
				for (int i = 0; i < timeValue.starts.length; i++) {
					timeValue.starts[i] = ((Integer) this.startsList.get(i))
							.intValue();
					timeValue.ends[i] = ((Integer) this.endsList.get(i))
							.intValue();
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
			if (this.divisor != null) {
				for (int i = 0; i < this.divisor.length; i++) {
					all = (this.divisor[i] == 1);
					if (!all) break;
				}
			}
			if ((this.starts != null) && (this.ends != null)) {
				if (this.starts.length > 0) all = false;
			}

			return all;
		}

		public String toString() {
			StringBuffer sbuf = new StringBuffer();
			if (this.divisor != null) {
				for (int i = 0; i < this.divisor.length; i++) {
					if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$

					sbuf.append(LangModelSchedule.getString("each")); //$NON-NLS-1$
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
						if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$
						sbuf.append(LangModelSchedule.getString("from")); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(Integer.toString(this.starts[i]));
						else
							sbuf.append(this.names[this.starts[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						sbuf.append(LangModelSchedule.getString("to")); //$NON-NLS-1$
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null)
							sbuf.append(Integer.toString(this.ends[i]));
						else
							sbuf.append(this.names[this.ends[i]]);
						sbuf.append(" "); //$NON-NLS-1$
						if (this.names == null) sbuf.append(this.name);
					} else {
						if (sbuf.length() > 0) sbuf.append(", "); //$NON-NLS-1$
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
	}
	public static final String[]	DAY_OF_WEEK_NAMES		= new String[] {
			LangModelSchedule.getString("Sunday"), LangModelSchedule.getString("Monday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("Tuesday"), LangModelSchedule.getString("Wednesday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("Thursday"), LangModelSchedule.getString("Friday"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("Saturday"),						};							//$NON-NLS-1$

	public static final String[]	MONTH_NAMES				= new String[] {
			LangModelSchedule.getString("January"), LangModelSchedule.getString("February"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("March"), LangModelSchedule.getString("April"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("May"), LangModelSchedule.getString("June"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("July"), LangModelSchedule.getString("Augest"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("September"), LangModelSchedule.getString("October"), //$NON-NLS-1$ //$NON-NLS-2$
			LangModelSchedule.getString("November"), LangModelSchedule.getString("December")};					//$NON-NLS-1$ //$NON-NLS-2$
	public static final int			TIMESTAMPTYPE_CONTINUOS	= 2;

	public static final int			TIMESTAMPTYPE_ONETIME	= 1;
	public static final int			TIMESTAMPTYPE_PERIODIC	= 3;

	public static final String		TYPE					= "TemporalPattern";		//$NON-NLS-1$

	public static final int[]		weekNumber				= new int[] {
			Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
			Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
			Calendar.SATURDAY								};

	final static boolean			DEBUG					= false;
	private Date					created;
	private Identifier				creator_id;
	//private ArrayList cron_strings;
	private String					description;
	private long					endPeriod;
	private Identifier				id;
	private Date					modified;
	private Identifier				modifier_id;

	private long					startPeriod;
	private HashMap					templates;
	private long[]					times;

	private int						type					= TIMESTAMPTYPE_ONETIME;

	public TemporalPattern(Identifier id) {
		/* retrieve */
	}

	public TemporalPattern(TemporalPattern_Transferable tpt) {
		this.id = new Identifier(tpt.id);
		this.created = new Date(tpt.created);
		this.modified = new Date(tpt.modified);
		this.creator_id = new Identifier(tpt.creator_id);
		this.modifier_id = new Identifier(tpt.modifier_id);
		this.description = new String(tpt.description);
		//this.cron_strings = new ArrayList(tpt.cron_strings.length);
		for (int i = 0; i < tpt.cron_strings.length; i++) {
			//this.cron_strings.add(new String(tpt.cron_strings[i]));
			addTemplate(new String(tpt.cron_strings[i]));
		}
	}

	private TemporalPattern(Identifier id, Date created, Date modified,
			Identifier creator_id, Identifier modifier_id, String description,
			ArrayList cron_strings) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creator_id = creator_id;
		this.modifier_id = modifier_id;
		this.description = description;
		//this.cron_strings = cron_strings;
		for (Iterator it = cron_strings.iterator(); it.hasNext();) {
			addTemplate((String) it.next());
		}
	}

	public static TemporalPattern create(Identifier id, Date created,
			Date modified, Identifier creator_id, Identifier modifier_id,
			String description, ArrayList cron_strings) {
		return new TemporalPattern(id, created, modified, creator_id,
				modifier_id, description, cron_strings);
	}

	public void addTemplate(String template) {
		this.times = null;
		if (this.templates == null) this.templates = new HashMap();
		TimeLine timeLine = new TimeLine();
		timeLine.setTemplate(template);
		timeLine.setStartPeriod(this.startPeriod);
		timeLine.setEndPeriod(this.endPeriod);
		this.templates.put(timeLine, timeLine);
		setType(TIMESTAMPTYPE_PERIODIC);
	}

	/**
	 * @return Returns the created.
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @return Returns the creatorId.
	 */
	public Identifier getCreatorId() {
		return this.creator_id;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Returns the endPeriod.
	 */
	public long getEndPeriod() {
		return this.endPeriod;
	}

	/**
	 * @return Returns the id.
	 */
	public Identifier getId() {
		return this.id;
	}

	/**
	 * @return Returns the modified.
	 */
	public Date getModified() {
		return this.modified;
	}

	/**
	 * @return Returns the modifierId.
	 */
	public Identifier getModifier_id() {
		return this.modifier_id;
	}

	/**
	 * @return Returns the startPeriod.
	 */
	public long getStartPeriod() {
		return this.startPeriod;
	}

	//	public void removeTemplate(String template) {
	//		if (templates != null) {
	//			//if (templates.containsKey(template))
	//			templates.remove(template);
	//		}
	//	}

	public int getTemplateCount() {
		int size = -1;
		if (this.templates != null) size = this.templates.size();
		return size;
	}

	public Collection getTimeLines() {
		Collection collection = null;
		if (this.templates != null) {
			collection = this.templates.values();
			//System.out.println("collection.size():" + collection.size());
		}
		return collection;
	}

	public long[] getTimes() {
		if (this.times == null) {
			//int count = 0;
			Collection list = this.templates.values();
			List timeList = new ArrayList();
			for (Iterator it = list.iterator(); it.hasNext();) {
				TimeLine timeLine = (TimeLine) it.next();
				timeLine.parseTemplate();
				//				if (timeLine.dateList != null) {
				//					//count += timeLine.dateList.size();
				//					//if (DEBUG_LEVEL)
				//					{
				//						if (timeLine.dateList.size() > 0) {
				//
				//							if (DEBUG_LEVEL) {
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

			this.times = new long[timeList.size()];
			int count = 0;
			for (Iterator it = timeList.iterator(); it.hasNext();) {
				this.times[count++] = ((Date) it.next()).getTime();
			}
		}

		return this.times;
	}

	public Object getTransferable() {
		//		String[] cr_strs = new String[this.cron_strings.size()];
		//		for (Iterator iterator = this.cron_strings.iterator(); iterator
		//				.hasNext();)
		//			cr_strs[i++] = new String((String) iterator.next());
		String[] cr_strs = new String[this.templates.size()];
		{
			int i = 0;
			for (Iterator it = this.templates.keySet().iterator(); it.hasNext();) {
				TimeLine line = (TimeLine) this.templates.get(it.next());
				cr_strs[i++] = new String(line.getTemplate());
			}
		}

		return new TemporalPattern_Transferable(
				(Identifier_Transferable) this.id.getTransferable(),
				this.created.getTime(), this.modified.getTime(),
				(Identifier_Transferable) this.creator_id.getTransferable(),
				(Identifier_Transferable) this.modifier_id.getTransferable(),
				new String(this.description), cr_strs);
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return this.type;
	}

	public void printTimes() {
		long[] t = getTimes();
		System.out.println("printTimes ::"); //$NON-NLS-1$
		System.out.println("startTime ::" + new Date(getStartPeriod())); //$NON-NLS-1$
		System.out.println("endTime ::" + new Date(getEndPeriod())); //$NON-NLS-1$
		for (int i = 0; i < this.times.length; i++) {
			System.out.println(new Date(t[i]).toString());
		}
		for (Iterator it = this.templates.values().iterator(); it.hasNext();) {
			TimeLine line = (TimeLine) it.next();
			System.out.println(line.getDescription());
		}
	}

	public void removeAll() {
		this.times = null;
		this.templates.clear();
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @param creatorId
	 *            The creatorId to set.
	 */
	public void setCreator_id(Identifier creatorId) {
		this.creator_id = creatorId;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param endPeriod
	 *            The endPeriod to set.
	 */
	public void setEndPeriod(long endPeriod) {
		this.times = null;
		this.endPeriod = endPeriod;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Identifier id) {
		this.id = id;
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @param modifierId
	 *            The modifierId to set.
	 */
	public void setModifier_id(Identifier modifierId) {
		this.modifier_id = modifierId;
	}

	/**
	 * @param startPeriod
	 *            The startPeriod to set.
	 */
	public void setStartPeriod(long startPeriod) {
		this.times = null;
		this.startPeriod = startPeriod;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}