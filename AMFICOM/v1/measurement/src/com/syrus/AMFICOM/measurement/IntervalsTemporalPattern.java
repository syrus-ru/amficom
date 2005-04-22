/*-
* $Id: IntervalsTemporalPattern.java,v 1.6 2005/04/22 14:32:59 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ITP_Interval_Duration;
import com.syrus.AMFICOM.measurement.corba.ITP_Interval_Temporal_Pattern_Id;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2005/04/22 14:32:59 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class IntervalsTemporalPattern extends AbstractTemporalPattern {

	private static final long	serialVersionUID	= 3257567312898175032L;
	
	/** SortedMap<Long milliseconds, Identifier<AbstractTemporalPattern>> */
	private SortedMap intervalsAbstractTemporalPatternMap;
	
	/** SortedMap<Long milliseconds, Long duration> */
	private SortedMap intervalsDuration;

	
	protected IntervalsTemporalPattern(final Identifier id, 
	                                final Identifier creatorId, 
	                                final long version, 
	                                final SortedMap intervalsAbstractTemporalPatternMap,
	                                final SortedMap intervalsDuration) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.setIntervalsDuration0(intervalsDuration);
		this.changed = false;
	}	
	
	/**
	 * create new instance for client
	 * @param creatorId creator id 
	 * @param intervalsAbstractTemporalPatternMap
	 * @param intervalsDuration 
	 */
	public static IntervalsTemporalPattern createInstance(	final Identifier creatorId,
															final SortedMap intervalsAbstractTemporalPatternMap,
							                                final SortedMap intervalsDuration) throws CreateObjectException {

		try {
			IntervalsTemporalPattern intervalsTemporalPattern = 
				new IntervalsTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE), 
					creatorId, 
					0L, 
					intervalsAbstractTemporalPatternMap,
					intervalsDuration);
			intervalsTemporalPattern.changed = true;
			
			assert intervalsTemporalPattern.isValid() : ErrorMessages.OBJECT_NOT_INITIALIZED;			
			
			return intervalsTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	
	public IntervalsTemporalPattern(IntervalsTemporalPattern_Transferable itpt) throws CreateObjectException {
		
		try {
			this.fromTransferable(itpt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		IntervalsTemporalPattern_Transferable itpt = (IntervalsTemporalPattern_Transferable)transferable;
		super.fromTransferable(itpt.header);

		{
			SortedMap map = new TreeMap();
			for(int i=0;i<itpt.intervals_temporal_pattern_id.length;i++) {
				map.put(new Long(itpt.intervals_temporal_pattern_id[i].ms), new Identifier(itpt.intervals_temporal_pattern_id[i].temporal_pattern_id));
			}
			this.setIntervalsAbstractTemporalPatternMap0(map);
		}
		
		{
			SortedMap map = new TreeMap();
			for(int i=0;i<itpt.intervals_duration.length;i++) {
				map.put(new Long(itpt.intervals_duration[i].ms), new Long(itpt.intervals_duration[i].duration));
			}
			this.setIntervalsDuration0(map);
		}		

		this.changed = false;	
	}


	protected boolean isValid() {
		return super.isValid() && this.intervalsAbstractTemporalPatternMap != null;
	}
	
	protected void fillTimes() {
		AbstractTemporalPattern previousTemporalPattern = null;

		Long previousDuration = null;
		Date previousDate = null;
		Date startDate = new Date(this.startTime);

		java.util.Set keys = this.intervalsAbstractTemporalPatternMap.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			Long milliseconds = (Long) it.next();
			Identifier abstractTemporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(milliseconds);
			
			AbstractTemporalPattern temporalPattern = null;			
			if (abstractTemporalPatternId != null && !abstractTemporalPatternId.isVoid()) {
				try {
					temporalPattern = (AbstractTemporalPattern) MeasurementStorableObjectPool.getStorableObject(
						abstractTemporalPatternId, true);
				} catch (ApplicationException e) {
					// nothing do, just ignore temporal pattern
					Log.errorException(e);
				}
			}

//			Log.debugMessage("IntervalsTemporalPattern.fillTimes | milliseconds " + milliseconds, Log.FINEST);
			long localStartTime = startDate.getTime() + milliseconds.longValue();

			Date localStartDate = new Date(localStartTime);

//			Log.debugMessage("IntervalsTemporalPattern.fillTimes | previousDate:" + previousDate + " ,\n\t localStartDate:" + localStartDate + ", \n\tpreviousTemporalPattern:"
//				+ (previousTemporalPattern == null ? "'null'" : previousTemporalPattern.getId().toString()), Log.FINEST);
			/* add not added last (previous item) */
			this.addTimeItem(previousDate, localStartDate, previousDuration, previousTemporalPattern);
			
			previousDate = localStartDate;
			previousTemporalPattern = temporalPattern;
			previousDuration = this.intervalsDuration != null ? (Long) this.intervalsDuration.get(milliseconds) : null;
		}

//		Log.debugMessage("IntervalsTemporalPattern.fillTimes | previousDate:" + previousDate + " ,\n\t localStartDate:" + new Date(this.endTime) + ", \n\tpreviousTemporalPattern:"
//			+ (previousTemporalPattern == null ? "'null'" : previousTemporalPattern.getId().toString()), Log.FINEST);
		/* add not added last (previous item) */
		this.addTimeItem(previousDate, new Date(this.endTime), previousDuration, previousTemporalPattern);
	}
	
	private void addTimeItem(final Date startDate, final Date endDate, final Long duration, final AbstractTemporalPattern temporalPattern) {
		if (startDate != null) {
			if (temporalPattern == null) {
				super.times.add(startDate);
			} else {
				Date endDate2 = null;
				if (duration == null) {
					endDate2 = endDate;
				} else {
					long time = startDate.getTime() + duration.longValue();
					Log.debugMessage("IntervalsTemporalPattern.addTimeItem | duration occur " + duration +" , "+ new Date(time) +" \n\ttime < endDate.getTime():" + (time < endDate.getTime()), Log.FINEST);
					endDate2 = time < endDate.getTime() ? new Date(time) : endDate;
					Log.debugMessage("IntervalsTemporalPattern.addTimeItem | duration occur, start date is " + startDate +", end date is " + endDate2  , Log.FINEST);
				}
				super.times.addAll(temporalPattern.getTimes(startDate, endDate2));
			}
		}
	}

	/**
	 * @param intervalsAbstractTemporalPatternMap
	 */
	protected void setIntervalsAbstractTemporalPatternMap0(SortedMap intervalsAbstractTemporalPatternMap) {
		if (this.intervalsAbstractTemporalPatternMap != null) {
			this.intervalsAbstractTemporalPatternMap.clear();
		}
		
		if (intervalsAbstractTemporalPatternMap != null) {
			for (Iterator it = intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long ms = (Long) it.next();
				Identifier abstractTemporalPatternId = (Identifier) intervalsAbstractTemporalPatternMap.get(ms); 
				this.addIntervalItem0(ms, abstractTemporalPatternId);
				
			}
		}
		
	}	
	
	protected void setIntervalsDuration0(SortedMap intervalsDuration) {
		if (this.intervalsDuration == null) {
			this.intervalsDuration = new TreeMap();
		} else {
			this.intervalsDuration.clear();
		}
		
		if (intervalsDuration != null) {
			this.intervalsDuration.putAll(intervalsDuration);
		}
	}	
	
	public void addIntervalItem(long milliseconds, Identifier temporalPatternId) {
		this.addIntervalItem0(new Long(milliseconds), temporalPatternId);
		super.changed = true;
	}
	
	public final void printStructure() {
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Long ms = (Long) it.next();
			Identifier abstractTemporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
			Log.debugMessage("IntervalsTemporalPattern.printStructure | ms:" + ms.longValue()
					+ (abstractTemporalPatternId.isVoid() ? "" : (", id:" + abstractTemporalPatternId.toString())),
				Log.FINEST);
		}
	}
	
	public void setIntervalDuration(long millesonds, long duration) {
		Long startedTime = this.getStartedTime(millesonds);
		if (startedTime != null) {
			this.intervalsDuration.put(startedTime, new Long(duration));
		}
	}
	
	public void removeIntervalItem(long milliseconds) {
//		Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | > milliseconds:" + milliseconds, Log.FINEST);
		/* clear times cache */
		this.times.clear();

		Long long1 = new Long(milliseconds);

		Long ms = this.getStartedTime(milliseconds);
		Identifier abstractTemporalid = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
//		Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | abstractTemporalid is "
//				+ (abstractTemporalid.isVoid() ? "VOID" : abstractTemporalid.toString()), Log.FINEST);

		if (abstractTemporalid.isVoid()) {
			this.intervalsAbstractTemporalPatternMap.remove(long1);
		} else {
			try {
				AbstractTemporalPattern abstractTemporalPattern = (AbstractTemporalPattern) MeasurementStorableObjectPool
						.getStorableObject(abstractTemporalid, true);
				if (abstractTemporalPattern instanceof IntervalsTemporalPattern) {
					IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) abstractTemporalPattern;
					intervalsTemporalPattern.removeIntervalItem(milliseconds);
				} else if (abstractTemporalPattern instanceof PeriodicalTemporalPattern) {
					PeriodicalTemporalPattern periodicTemporalPattern = (PeriodicalTemporalPattern) abstractTemporalPattern;

					long period = periodicTemporalPattern.getPeriod();

					Long nextMilliseconds = this.getNextStartedTime(ms);
					long nextms = nextMilliseconds == null ? this.endTime - this.startTime : nextMilliseconds
							.longValue();

					Long duration = (Long) this.intervalsDuration.get(ms);

					if (duration != null) {
						long nextms1 = ms.longValue() + duration.longValue();
						if (nextms1 < nextms) {
							nextms = nextms1;
						}
					}

					SortedSet times2 = periodicTemporalPattern.getTimes(this.startTime + ms.longValue(), this.startTime
							+ nextms);
					int i = 0;
					Date date = null;
					for (Iterator it = times2.iterator(); it.hasNext(); i++) {
						date = (Date) it.next();
						if (date.getTime() == this.startTime + milliseconds) {
							break;
						}
						date = null;
					}

//					Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | found date " + date, Log.FINEST);

					if (date != null) {

//						Log.debugMessage(
//							"IntervalsTemporalPattern.removeIntervalItem | i: " + i + ", " + times2.size(), Log.FINEST);

						if (i == 0) {
							this.intervalsAbstractTemporalPatternMap.remove(ms);
							int newRepeate = times2.size() - 1;
							Long newMilliseconds = new Long(milliseconds + period);
							// Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem
							// | newRepeate " + newRepeate, Log.FINEST);
							if (newRepeate > 1 || newRepeate < 0) {
								// Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem
								// | newRepeate > 1 || newRepeate < 0 ",
								// Log.FINEST);
								if (nextMilliseconds != null) {
									this.intervalsDuration.put(newMilliseconds, new Long((newRepeate - 1) * period));
								}
								this.intervalsAbstractTemporalPatternMap.put(newMilliseconds, abstractTemporalid);

							} else {
								this.intervalsAbstractTemporalPatternMap.put(newMilliseconds,
									Identifier.VOID_IDENTIFIER);
							}
						} else if (i == times2.size() - 1) {
							 Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | last item ", Log.FINEST);
							int newRepeate = times2.size() - 1;
							// Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem
							// | newRepeate:" + newRepeate, Log.FINEST);
							if (newRepeate > 1 || newRepeate < 0) {
								Long prevValue = (Long) this.intervalsDuration.get(ms);
								 Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | prevValue " + prevValue, Log.FINEST);
								this.intervalsDuration.put(ms, new Long(prevValue == null ? newRepeate * period
										: (prevValue.longValue() - period)));
								 Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | new Value " +
								 this.intervalsDuration.get(ms), Log.FINEST);
							} else {
								this.intervalsAbstractTemporalPatternMap.put(ms, Identifier.VOID_IDENTIFIER);
							}
						} else {

							long repeate = times2.size() - i - 1;
//							Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | periodicTemporalPattern "
//									+ periodicTemporalPattern.getId() + " repeate: " + repeate, Log.FINEST);

							Long nextTime = null;
							{
								Date nextDate = null;
								SortedSet futureSet = times2.tailSet(date);
								for (Iterator it = futureSet.iterator(); it.hasNext();) {
									nextDate = (Date) it.next();
									if (nextDate.getTime() == date.getTime()) {
										nextDate = null;
									} else {
										break;
									}
								}
								nextTime = new Long(nextDate.getTime() - this.startTime);
							}

							if (repeate > 1 || repeate < 0) {
								// Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem
								// | nextTime: " + new Date(nextTime.longValue()
								// + this.startTime), Log.FINEST);
								this.intervalsAbstractTemporalPatternMap.put(nextTime, abstractTemporalid);
								this.intervalsDuration.put(nextTime, new Long(repeate * period));
							} else {
								this.addIntervalItem0(nextTime, Identifier.VOID_IDENTIFIER);
							}

//							Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | i is " + i, Log.FINEST);

							if (i > 1) {
								this.intervalsDuration.put(ms, new Long((i - 1) * period));
//								Log.debugMessage("IntervalsTemporalPattern.removeIntervalItem | duration: "
//										+ new Date(ms.longValue() + this.startTime) + ", "
//										+ ((Long) this.intervalsDuration.get(ms)).longValue() / (60L * 1000L),
//									Log.FINEST);
							} else {
								this.intervalsAbstractTemporalPatternMap.remove(long1);
								this.intervalsDuration.put(ms, Identifier.VOID_IDENTIFIER);
							}
						}

					}

				}
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
	}	
	
	public final Identifier getTemporalPatternId(final long milliseconds) {
		Long ms = this.getStartedTime(milliseconds);
		if (ms != null) {
			return (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
		}
		return null;
	}
	
	public final Long getStartedTime(final long milliseconds) {		
		Long ms = null;		
		Long long1 = new Long(milliseconds);		
		Set keys = this.intervalsAbstractTemporalPatternMap.keySet();
		if (keys.contains(long1)) {
			ms = long1;
		} else {
			for (Iterator it = keys.iterator(); it.hasNext();) {
				Long time = (Long) it.next();
				if (ms != null && ms.longValue() < milliseconds && milliseconds < time.longValue()) {
					break;
				}
				ms = time;
			}	
		}		
		return ms;
	}
	
	public Long getNextStartedTime(Long milliseconds) {
		
		Long nextMs = null;
		long ms = milliseconds.longValue();
		{
			SortedMap futureMap = this.intervalsAbstractTemporalPatternMap.tailMap(milliseconds);
			for (Iterator it = futureMap.keySet().iterator(); it.hasNext();) {
				nextMs = (Long) it.next();
				if (nextMs.longValue() == ms) {
					nextMs = null;
				} else {
					break;
				}
			}
			
			if (nextMs == null) {
				nextMs = new Long(this.endTime - this.startTime);
			}
		}
		
		return nextMs;
	}
	
	protected void addIntervalItem0(Long milliseconds, Identifier temporalPatternId) {
		if (super.times != null) {
			super.times.clear();
		}
		
		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap();
		}
		
		this.intervalsAbstractTemporalPatternMap.put(milliseconds, temporalPatternId == null ? Identifier.VOID_IDENTIFIER : temporalPatternId);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		java.util.Set dependencies = new HashSet();
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Identifier abstractTemporalPatternId = (Identifier)this.intervalsAbstractTemporalPatternMap.get(it.next());
			dependencies.add(abstractTemporalPatternId);
		}
		return dependencies;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		ITP_Interval_Temporal_Pattern_Id[] intervalTemporalPatternsIdT;
		ITP_Interval_Duration[] durationsT;
		{
			java.util.Set keys = this.intervalsAbstractTemporalPatternMap.keySet();
			intervalTemporalPatternsIdT = new ITP_Interval_Temporal_Pattern_Id[keys
					.size()];
			int i = 0;
			for (Iterator it = keys.iterator(); it.hasNext(); i++) {
				Long ms = (Long) it.next();
				Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
				intervalTemporalPatternsIdT[i] = new ITP_Interval_Temporal_Pattern_Id(
																						ms.longValue(),
																						(Identifier_Transferable) temporalPatternId
																								.getTransferable());
			}
		}
		
		{
			if (this.intervalsDuration == null) {
				durationsT = new ITP_Interval_Duration[0];
			} else {
				java.util.Set keys = this.intervalsDuration.keySet();
				durationsT = new ITP_Interval_Duration[keys.size()];
				int i = 0;
				for (Iterator it = keys.iterator(); it.hasNext(); i++) {
					Long ms = (Long) it.next();
					Long duration = (Long) this.intervalsDuration.get(ms);
					durationsT[i] = new ITP_Interval_Duration(ms.longValue(), duration.longValue());
				}

			}
		}
		
		return new IntervalsTemporalPattern_Transferable(super.getHeaderTransferable(), intervalTemporalPatternsIdT, durationsT);
	}

	
	public SortedMap getIntervalsAbstractTemporalPatternMap() {
		return Collections.unmodifiableSortedMap(this.intervalsAbstractTemporalPatternMap);
	}
	
	public void setIntervalsAbstractTemporalPatternMap(SortedMap intervalsAbstractTemporalPatternMap) {
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.changed = true;
	}
	

}

