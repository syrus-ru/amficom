/*-
* $Id: IntervalsTemporalPattern.java,v 1.17 2005/05/25 13:01:05 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.Undoable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ITP_Interval_Duration;
import com.syrus.AMFICOM.measurement.corba.ITP_Interval_Temporal_Pattern_Id;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.17 $, $Date: 2005/05/25 13:01:05 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class IntervalsTemporalPattern extends AbstractTemporalPattern implements Undoable {

	private static final long serialVersionUID = 3257567312898175032L;

	/** SortedMap <Long milliseconds, Identifier <AbstractTemporalPattern>> */
	private SortedMap intervalsAbstractTemporalPatternMap;

	/** SortedMap <Long milliseconds, Long duration> */
	private SortedMap intervalsDuration;

	private String name;

	private SortedMap undoIntervalsAbstractTemporalPatternMap;
	private SortedMap undoIntervalsDuration;

	IntervalsTemporalPattern(final Identifier id,
			final Identifier creatorId,
			final long version,
			final SortedMap intervalsAbstractTemporalPatternMap,
			final SortedMap intervalsDuration) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.setIntervalsDuration0(intervalsDuration);
		this.changed = false;
	}	

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 *          creator id
	 * @param intervalsAbstractTemporalPatternMap
	 * @param intervalsDuration
	 */
	public static IntervalsTemporalPattern createInstance(final Identifier creatorId,
			final SortedMap intervalsAbstractTemporalPatternMap,
			final SortedMap intervalsDuration) throws CreateObjectException {

		try {
			IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE),
					creatorId,
					0L,
					intervalsAbstractTemporalPatternMap,
					intervalsDuration);
			intervalsTemporalPattern.changed = true;

			assert intervalsTemporalPattern.isValid() : ErrorMessages.OBJECT_NOT_INITIALIZED;

			return intervalsTemporalPattern;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final SortedMap intervalsAbstractTemporalPatternMap,
			final SortedMap intervalsDuration) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.setName(name);
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.setIntervalsDuration0(intervalsDuration);
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
		IntervalsTemporalPattern_Transferable itpt = (IntervalsTemporalPattern_Transferable) transferable;
		super.fromTransferable(itpt.header);

		{
			SortedMap map = new TreeMap();
			for (int i = 0; i < itpt.intervals_temporal_pattern_id.length; i++) {
				map.put(new Long(itpt.intervals_temporal_pattern_id[i].ms),
						new Identifier(itpt.intervals_temporal_pattern_id[i].temporal_pattern_id));
			}
			this.setIntervalsAbstractTemporalPatternMap0(map);
		}

		{
			SortedMap map = new TreeMap();
			for (int i = 0; i < itpt.intervals_duration.length; i++) {
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
					temporalPattern = (AbstractTemporalPattern) StorableObjectPool.getStorableObject(abstractTemporalPatternId, true);
				}
				catch (ApplicationException e) {
					// nothing do, just ignore temporal pattern
					Log.errorException(e);
				}
			}

			// Log.debugMessage("IntervalsTemporalPattern.fillTimes | milliseconds " +
			// milliseconds, Log.FINEST);
			long localStartTime = startDate.getTime() + milliseconds.longValue();

			Date localStartDate = new Date(localStartTime);

			// Log.debugMessage("IntervalsTemporalPattern.fillTimes | previousDate:" +
			// previousDate + " ,\n\t localStartDate:" + localStartDate + ",
			// \n\tpreviousTemporalPattern:"
			// + (previousTemporalPattern == null ? "'null'" :
			// previousTemporalPattern.getId().toString()), Log.FINEST);
			/* add not added last (previous item) */
			this.addTimeItem(previousDate, localStartDate, previousDuration, previousTemporalPattern);

			previousDate = localStartDate;
			previousTemporalPattern = temporalPattern;
			previousDuration = this.intervalsDuration != null ? (Long) this.intervalsDuration.get(milliseconds) : null;
		}

		// Log.debugMessage("IntervalsTemporalPattern.fillTimes | previousDate:" +
		// previousDate + " ,\n\t localStartDate:" + new Date(this.endTime) + ",
		// \n\tpreviousTemporalPattern:"
		// + (previousTemporalPattern == null ? "'null'" :
		// previousTemporalPattern.getId().toString()), Log.FINEST);
		/* add not added last (previous item) */
		this.addTimeItem(previousDate, new Date(this.endTime), previousDuration, previousTemporalPattern);
	}

	private void addTimeItem(final Date startDate,
			final Date endDate,
			final Long duration,
			final AbstractTemporalPattern temporalPattern) {
		if (startDate != null) {
			if (temporalPattern == null) {
				super.times.add(startDate);
			}
			else {
				Date endDate2 = null;
				if (duration == null) {
					endDate2 = endDate;
				}
				else {
					long time = startDate.getTime() + duration.longValue();
					// Log.debugMessage("IntervalsTemporalPattern.addTimeItem | duration
					// occur " + duration +" , "+ new Date(time) +" \n\ttime <
					// endDate.getTime():" + (time < endDate.getTime()), Log.FINEST);
					endDate2 = time < endDate.getTime() ? new Date(time) : endDate;
					// Log.debugMessage("IntervalsTemporalPattern.addTimeItem | duration
					// occur, start date is " + startDate +", end date is " + endDate2 ,
					// Log.FINEST);
				}
				super.times.addAll(temporalPattern.getTimes(startDate, endDate2));
			}
		}
	}

	/**
	 * @param intervalsAbstractTemporalPatternMap
	 */
	protected void setIntervalsAbstractTemporalPatternMap0(SortedMap intervalsAbstractTemporalPatternMap) {
		/* save state before */
		this.saveState();

		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap();
		}
		else {
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
		/* save state before */
		this.saveState();

		if (this.intervalsDuration == null) {
			this.intervalsDuration = new TreeMap();
		}
		else {
			this.intervalsDuration.clear();
		}

		if (intervalsDuration != null) {
			this.intervalsDuration.putAll(intervalsDuration);
		}
	}

	public void addIntervalItems(Map offsetId, Map durations) throws IllegalDataException {
		/* save state before */
		this.saveState();

		if (super.times != null) {
			super.times.clear();
		}

		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap();
		}

		long minMs = 0;

		{
			Set idSet = this.intervalsAbstractTemporalPatternMap.keySet();
			for (Iterator it = offsetId.keySet().iterator(); it.hasNext();) {
				Long ms = (Long) it.next();
				long l = ms.longValue();
				if (l < minMs) {
					minMs = l;
				}
				Long duration = (Long) durations.get(ms);
				long dur = duration != null ? duration.longValue() : 0;

				for (Iterator iterator = idSet.iterator(); iterator.hasNext();) {
					Long offset = (Long) iterator.next();
					Long duration2 = (Long) this.intervalsDuration.get(ms);
					long l3 = offset.longValue();
					long duration3 = duration2 != null ? duration2.longValue() : 0;
					// Log.debugMessage("IntervalsTemporalPattern.addIntervalItems | 1: "
					// +(l3 >= l ), Log.FINEST);
					// Log.debugMessage("IntervalsTemporalPattern.addIntervalItems | 1.5:
					// " +(l <= l3 + duration3), Log.FINEST);
					// Log.debugMessage("IntervalsTemporalPattern.addIntervalItems |
					// 2:"+(l < l3 && (l + dur > l3)), Log.FINEST);
					if ((duration3 == 0 && l == l3) || (duration3 != 0 && l3 >= l && l <= l3 + duration3) || (l < l3 && (l + dur > l3))) {
						throw new IllegalDataException(LangModelMeasurement.getString("Cannot put over other items"));
					}

				}
			}
		}

		for (Iterator it = offsetId.keySet().iterator(); it.hasNext();) {
			Long ms = (Long) it.next();
			Identifier temporalPatternId = (Identifier) offsetId.get(ms);
			this.intervalsAbstractTemporalPatternMap.put(ms, temporalPatternId == null ? Identifier.VOID_IDENTIFIER : temporalPatternId);
			this.intervalsDuration.put(ms, durations.get(ms));
		}

		if (minMs < 0) {
			this.moveIntervalItems0(new TreeSet(this.intervalsAbstractTemporalPatternMap.keySet()), -minMs);
		}

		super.changed = true;
	}

	public final void printStructure() {
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Long ms = (Long) it.next();
			Identifier abstractTemporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
			Log.debugMessage("IntervalsTemporalPattern.printStructure | ms:"
					+ ms.longValue()
					+ (abstractTemporalPatternId.isVoid() ? "" : (", id:" + abstractTemporalPatternId.toString())), Log.FINEST);
		}

		if (this.undoIntervalsAbstractTemporalPatternMap != null) {
			for (Iterator it = this.undoIntervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long ms = (Long) it.next();
				Identifier abstractTemporalPatternId = (Identifier) this.undoIntervalsAbstractTemporalPatternMap.get(ms);
				Log.debugMessage("IntervalsTemporalPattern.printStructure | UNDO ms:"
						+ ms.longValue()
						+ (abstractTemporalPatternId.isVoid() ? "" : (", id:" + abstractTemporalPatternId.toString())), Log.FINEST);
			}
		}
	}

	public void setIntervalDuration(long millesonds, long duration) {
		/* save state before */
		this.saveState();

		Long startedTime = this.getStartedTime(millesonds);
		if (startedTime != null) {
			this.intervalsDuration.put(startedTime, new Long(duration));
		}
	}

	public void removeIntervalItems(java.util.Set offsets) {
		/* save state before */
		this.saveState();

		for (Iterator it = offsets.iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			// this.removeIntervalItem0(offset);
			this.intervalsAbstractTemporalPatternMap.remove(offset);
			this.intervalsDuration.remove(offset);
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
		return this.getStartedTime(new Long(milliseconds));
	}

	public final Long getStartedTime(final Long milliseconds) {
		Long ms = null;
		long long2 = milliseconds.longValue();
		Set keys = this.intervalsAbstractTemporalPatternMap.keySet();
		if (keys.contains(milliseconds)) {
			ms = milliseconds;
		}
		else {
			for (Iterator it = keys.iterator(); it.hasNext();) {
				Long time = (Long) it.next();
				if (ms != null && ms.longValue() < long2 && long2 < time.longValue()) {
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
				}
				else {
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

		this.intervalsAbstractTemporalPatternMap.put(milliseconds, temporalPatternId == null ? Identifier.VOID_IDENTIFIER
				: temporalPatternId);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	public Set getDependencies() {
		java.util.Set dependencies = new HashSet();
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Identifier abstractTemporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(it.next());
			dependencies.add(abstractTemporalPatternId);
		}
		return dependencies;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		ITP_Interval_Temporal_Pattern_Id[] intervalTemporalPatternsIdT;
		ITP_Interval_Duration[] durationsT;
		{
			java.util.Set keys = this.intervalsAbstractTemporalPatternMap.keySet();
			intervalTemporalPatternsIdT = new ITP_Interval_Temporal_Pattern_Id[keys.size()];
			int i = 0;
			for (Iterator it = keys.iterator(); it.hasNext(); i++) {
				Long ms = (Long) it.next();
				Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
				intervalTemporalPatternsIdT[i] = new ITP_Interval_Temporal_Pattern_Id(ms.longValue(),
						(Identifier_Transferable) temporalPatternId.getTransferable());
			}
		}

		{
			if (this.intervalsDuration == null) {
				durationsT = new ITP_Interval_Duration[0];
			}
			else {
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
		/* save state before */
		this.saveState();
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.changed = true;
	}

	public SortedMap getIntervalsDuration() {
		return Collections.unmodifiableSortedMap(this.intervalsDuration);
	}

	public void moveAllItems(long offset) throws IllegalDataException {
		this.moveIntervalItems(new TreeSet(this.intervalsAbstractTemporalPatternMap.keySet()), offset);
	}

	/**
	 * move offsets forward for offset ms
	 * 
	 * @param offsets
	 * @param offset
	 *          to future if <code> offset &gt; 0 </code>, to past otherwise
	 * @throws IllegalDataException
	 */
	public void moveIntervalItems(java.util.Set offsets, long offset) throws IllegalDataException {
		/* save state before */
		this.saveState();
		moveIntervalItems0(offsets, offset);
	}

	protected void moveIntervalItems0(java.util.Set offsets, long offset) throws IllegalDataException {
		if (super.times != null) {
			super.times.clear();
		}

		long minOffset = 0;

		for (Iterator it = offsets.iterator(); it.hasNext();) {
			Long ms = (Long) it.next();
			long ms1 = ms.longValue();
			Long newMs = new Long(ms1 + offset);

			Long duration = (Long) this.intervalsDuration.get(ms);
			Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(ms);
			this.intervalsAbstractTemporalPatternMap.remove(ms);
			this.intervalsAbstractTemporalPatternMap.put(newMs, temporalPatternId);
			if (duration != null) {
				this.intervalsDuration.remove(ms);
				this.intervalsDuration.put(newMs, duration);
			}

			if (ms1 + offset < minOffset) {
				minOffset = ms1 + offset;
			}
		}

		if (minOffset < 0) {
			Map map = new HashMap();
			Map durationMap = new HashMap();
			for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long ms = (Long) it.next();
				Long newMs = new Long(ms.longValue() - minOffset);
				map.put(newMs, this.intervalsAbstractTemporalPatternMap.get(ms));
				durationMap.put(newMs, this.intervalsDuration.get(ms));
			}
			this.intervalsAbstractTemporalPatternMap.clear();
			this.intervalsAbstractTemporalPatternMap.putAll(map);

			this.intervalsDuration.clear();
			this.intervalsDuration.putAll(durationMap);
		}

		this.chechPositions();
	}

	private void chechPositions() throws IllegalDataException {
		Long prev = null;
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			if (prev == null) {
				prev = offset;
				continue;
			}
			Long durationL = (Long) this.intervalsDuration.get(prev);
			long duration = durationL != null ? durationL.longValue() : 0;

			if (offset.longValue() < prev.longValue() + duration) {
				this.undo();
				throw new IllegalDataException(LangModelMeasurement.getString("Illegal position"));
			}
			prev = offset;
		}
	}

	private void saveState() {
		// Log.debugMessage("IntervalsTemporalPattern.saveState | 1", Log.FINEST);
		if (this.intervalsAbstractTemporalPatternMap == null
				&& this.intervalsDuration == null
				&& this.undoIntervalsAbstractTemporalPatternMap == null
				&& this.undoIntervalsDuration == null)
			return;

		if (this.intervalsAbstractTemporalPatternMap != null) {
			if (this.undoIntervalsAbstractTemporalPatternMap == null) {
				this.undoIntervalsAbstractTemporalPatternMap = new TreeMap();
			}
			else {
				this.undoIntervalsAbstractTemporalPatternMap.clear();
			}
			// Log.debugMessage("IntervalsTemporalPattern.saveState | 2", Log.FINEST);
			this.undoIntervalsAbstractTemporalPatternMap.putAll(this.intervalsAbstractTemporalPatternMap);
		}

		if (this.intervalsDuration != null) {
			if (this.undoIntervalsDuration == null) {
				this.undoIntervalsDuration = new TreeMap();
			}
			else {
				this.undoIntervalsDuration.clear();
			}
			// Log.debugMessage("IntervalsTemporalPattern.saveState | 3", Log.FINEST);
			this.undoIntervalsDuration.putAll(this.intervalsDuration);
		}

		this.printStructure();
	}

	public void undo() {
		if (super.times != null) {
			super.times.clear();
		}

		if (this.undoIntervalsAbstractTemporalPatternMap != null || this.undoIntervalsDuration != null) {
			// Log.debugMessage("IntervalsTemporalPattern.undo | ", Log.FINEST);
			this.printStructure();

			SortedMap map = new TreeMap(this.intervalsAbstractTemporalPatternMap);

			this.intervalsAbstractTemporalPatternMap.clear();

			if (this.undoIntervalsAbstractTemporalPatternMap != null) {
				this.intervalsAbstractTemporalPatternMap.putAll(this.undoIntervalsAbstractTemporalPatternMap);
			}

			SortedMap map2 = new TreeMap(this.intervalsDuration);

			this.intervalsDuration.clear();
			if (this.undoIntervalsDuration != null) {
				this.intervalsDuration.putAll(this.undoIntervalsDuration);
			}

			if (this.undoIntervalsAbstractTemporalPatternMap == null) {
				this.undoIntervalsAbstractTemporalPatternMap = map;
			}
			else {
				this.undoIntervalsAbstractTemporalPatternMap.clear();
				this.undoIntervalsAbstractTemporalPatternMap.putAll(map);
			}

			if (this.undoIntervalsDuration == null) {
				this.undoIntervalsDuration = map2;
			}
			else {
				this.undoIntervalsDuration.clear();
				this.undoIntervalsDuration.putAll(map2);
			}

		}
		else {
			assert false : "Cannot undo due to haven't any actions yet";
		}
	}

	/**
	 * joint from fist offset till last offset
	 * 
	 * @param offsets
	 * @throws CreateObjectException
	 */
	public void joinIntervalItems(SortedSet offsets) throws CreateObjectException {
		if (offsets == null || offsets.size() <= 1)
			return;

		/* save state */
		this.saveState();

		if (super.times != null) {
			super.times.clear();
		}

		Long firstOffset = null;
		long firstTime = 0;
		boolean initedFirst = false;

		Long lastOffset = null;

		for (Iterator it = offsets.iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			if (!initedFirst) {
				initedFirst = true;
				firstOffset = offset;
				firstTime = offset.longValue();
			}
			lastOffset = offset;
		}

		long lastTime = lastOffset.longValue();

		SortedMap offsetIds = new TreeMap();
		SortedMap offsetDuration = new TreeMap();

		long duration = 0;

		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			long offsetMs = offset.longValue();

			if (offsetMs < firstTime)
				continue;

			if (offsetMs > lastTime)
				break;

			long newOffset = offset.longValue() - firstTime;
			Long newMs = new Long(newOffset);
			Long durationLong = (Long) this.intervalsDuration.get(offset);
			offsetIds.put(newMs, this.intervalsAbstractTemporalPatternMap.get(offset));

			/* remove it */
			it.remove();
			this.intervalsDuration.remove(offset);

			offsetDuration.put(newMs, durationLong);
			duration = newOffset + (durationLong != null ? durationLong.longValue() : 0);
		}

		IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern.createInstance(this.modifierId,
				offsetIds,
				offsetDuration);
		try {
			StorableObjectPool.putStorableObject(intervalsTemporalPattern);
		}
		catch (IllegalObjectEntityException e) {
			// newer occur
			assert false;
		}

		this.intervalsAbstractTemporalPatternMap.put(firstOffset, intervalsTemporalPattern.getId());
		this.intervalsDuration.put(firstOffset, new Long(duration));
	}

	public void setIntervalsDuration(SortedMap intervalsDuration) {
		this.setIntervalsDuration0(intervalsDuration);
		this.changed = true;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void disjoinIntervalItems(SortedSet offsets) throws ApplicationException {
		if (offsets == null || offsets.isEmpty())
			return;

		/* save state */
		this.saveState();

		if (super.times != null) {
			super.times.clear();
		}

		for (Iterator it = offsets.iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			long ms = offset.longValue();
			// Log.debugMessage("IntervalsTemporalPattern.disjoinIntervalItems | ms "
			// + ms, Log.FINEST);
			Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(offset);
			if (temporalPatternId.isVoid())
				continue;
			this.intervalsAbstractTemporalPatternMap.remove(offset);
			Long duration = (Long) this.intervalsDuration.get(offset);
			this.intervalsDuration.remove(offset);
			short major = temporalPatternId.getMajor();
			switch (major) {
				case ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE:
					IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) StorableObjectPool.getStorableObject(temporalPatternId,
							true);
					SortedMap intervalsAbstractTemporalPatternMap2 = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
					SortedMap intervalsDuration2 = intervalsTemporalPattern.getIntervalsDuration();

					for (Iterator iterator = intervalsAbstractTemporalPatternMap2.keySet().iterator(); iterator.hasNext();) {
						Long offset2 = (Long) iterator.next();
						Long newOffset = new Long(offset2.longValue() + ms);
						// Log.debugMessage("IntervalsTemporalPattern.disjoin | INTERVALS "
						// + offset2, Log.FINEST);

						this.intervalsAbstractTemporalPatternMap.put(newOffset, intervalsAbstractTemporalPatternMap2.get(offset2));
						this.intervalsDuration.put(newOffset, intervalsDuration2.get(offset2));
					}
					break;
				case ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE:
					PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) StorableObjectPool.getStorableObject(temporalPatternId,
							true);
					// Log.debugMessage("IntervalsTemporalPattern.disjoinIntervalItems |
					// PERIODICAL duration: " + duration, Log.FINEST);
					SortedSet times2 = periodicalTemporalPattern.getTimes(0, duration != null ? duration.longValue() : 0);
					long firstTime = 0;
					boolean initedFirstTime = false;
					for (Iterator iterator = times2.iterator(); iterator.hasNext();) {
						Date date = (Date) iterator.next();
						// Log.debugMessage("IntervalsTemporalPattern.disjoinIntervalItems |
						// date " + date, Log.FINEST);
						if (!initedFirstTime) {
							firstTime = date.getTime();
							initedFirstTime = true;
						}
						// Log.debugMessage("IntervalsTemporalPattern.disjoinIntervalItems |
						// add " + new Long(ms + date.getTime() - firstTime), Log.FINEST);
						this.intervalsAbstractTemporalPatternMap.put(new Long(ms + date.getTime() - firstTime), Identifier.VOID_IDENTIFIER);
					}
					break;
				default:
					Log.debugMessage("IntervalsTemporalPattern.disjoin | temporalPatternId isn't support as temporal pattern", Log.FINEST);
					break;
			}

		}
	}
}
