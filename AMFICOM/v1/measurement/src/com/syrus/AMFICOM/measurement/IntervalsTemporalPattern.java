/*-
* $Id: IntervalsTemporalPattern.java,v 1.33 2005/08/08 11:31:45 arseniy Exp $
*
* Copyright � 2005 Syrus Systems.
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
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.Undoable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPatternHelper;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPatternPackage.IntervalDuration;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPatternPackage.IntervalTemporalPatternId;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.33 $, $Date: 2005/08/08 11:31:45 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public final class IntervalsTemporalPattern extends AbstractTemporalPattern implements Undoable {

	private static final long serialVersionUID = 3257567312898175032L;

	/** SortedMap <Long milliseconds, Identifier <AbstractTemporalPattern>> */
	private SortedMap<Long, Identifier> intervalsAbstractTemporalPatternMap;

	/** SortedMap <Long milliseconds, Long duration> */
	private SortedMap<Long, Long> intervalsDuration;

	private String name;

	private SortedMap<Long, Identifier> undoIntervalsAbstractTemporalPatternMap;
	private SortedMap<Long, Long> undoIntervalsDuration;

	IntervalsTemporalPattern(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final SortedMap<Long, Identifier> intervalsAbstractTemporalPatternMap,
			final SortedMap<Long, Long> intervalsDuration) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.setIntervalsDuration0(intervalsDuration);
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
			final SortedMap<Long, Identifier> intervalsAbstractTemporalPatternMap,
			final SortedMap<Long, Long> intervalsDuration) throws CreateObjectException {

		try {
			final IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					intervalsAbstractTemporalPatternMap,
					intervalsDuration);

			assert intervalsTemporalPattern.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			intervalsTemporalPattern.markAsChanged();

			return intervalsTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final SortedMap<Long, Identifier> intervalsAbstractTemporalPatternMap,
			final SortedMap<Long, Long> intervalsDuration) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.setName(name);
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.setIntervalsDuration0(intervalsDuration);
	}

	public IntervalsTemporalPattern(final IdlIntervalsTemporalPattern itpt) throws CreateObjectException {

		try {
			this.fromTransferable(itpt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlIntervalsTemporalPattern itpt = (IdlIntervalsTemporalPattern) transferable;
		super.fromTransferable(itpt);

		{
			final SortedMap<Long, Identifier> map = new TreeMap<Long, Identifier>();
			for (int i = 0; i < itpt.intervalsTemporalPatternId.length; i++) {
				map.put(new Long(itpt.intervalsTemporalPatternId[i].ms),
						new Identifier(itpt.intervalsTemporalPatternId[i].temporalPatternId));
			}
			this.setIntervalsAbstractTemporalPatternMap0(map);
		}

		{
			final SortedMap<Long, Long> map = new TreeMap<Long, Long>();
			for (int i = 0; i < itpt.intervalsDuration.length; i++) {
				map.put(new Long(itpt.intervalsDuration[i].ms), new Long(itpt.intervalsDuration[i].duration));
			}
			this.setIntervalsDuration0(map);
		}

	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.intervalsAbstractTemporalPatternMap != null;
	}

	@Override
	protected void fillTimes() {
		AbstractTemporalPattern previousTemporalPattern = null;

		Long previousDuration = null;
		Date previousDate = null;
		final Date startDate = new Date(this.startTime);

		final Set<Long> keys = this.intervalsAbstractTemporalPatternMap.keySet();
		for (final Iterator<Long> it = keys.iterator(); it.hasNext();) {
			final Long milliseconds = it.next();
			final Identifier abstractTemporalPatternId = this.intervalsAbstractTemporalPatternMap.get(milliseconds);

			AbstractTemporalPattern temporalPattern = null;
			if (abstractTemporalPatternId != null && !abstractTemporalPatternId.isVoid()) {
				try {
					temporalPattern = (AbstractTemporalPattern) StorableObjectPool.getStorableObject(abstractTemporalPatternId, true);
				} catch (ApplicationException e) {
					// nothing do, just ignore temporal pattern
					Log.errorException(e);
				}
			}

			// Log.debugMessage("IntervalsTemporalPattern.fillTimes | milliseconds " +
			// milliseconds, Log.FINEST);
			long localStartTime = startDate.getTime() + milliseconds.longValue();

			final Date localStartDate = new Date(localStartTime);

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
			} else {
				Date endDate2 = null;
				if (duration == null) {
					endDate2 = endDate;
				} else {
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
	protected void setIntervalsAbstractTemporalPatternMap0(final SortedMap intervalsAbstractTemporalPatternMap) {
		/* save state before */
		this.saveState();

		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap<Long, Identifier>();
		} else {
			this.intervalsAbstractTemporalPatternMap.clear();
		}

		if (intervalsAbstractTemporalPatternMap != null) {
			for (final Iterator it = intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long ms = (Long) it.next();
				Identifier abstractTemporalPatternId = (Identifier) intervalsAbstractTemporalPatternMap.get(ms);
				this.addIntervalItem0(ms, abstractTemporalPatternId);

			}
		}

	}

	protected void setIntervalsDuration0(final SortedMap<Long, Long> intervalsDuration) {
		/* save state before */
		this.saveState();

		if (this.intervalsDuration == null) {
			this.intervalsDuration = new TreeMap<Long, Long>();
		} else {
			this.intervalsDuration.clear();
		}

		if (intervalsDuration != null) {
			this.intervalsDuration.putAll(intervalsDuration);
		}
	}

	public void addIntervalItems(final Map<Long, Identifier> offsetId, final Map<?, Long> durations) throws IllegalDataException {
		/* save state before */
		this.saveState();

		if (super.times != null) {
			super.times.clear();
		}

		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap<Long, Identifier>();
		}

		long minMs = 0;

		{
			final Set<Long> idSet = this.intervalsAbstractTemporalPatternMap.keySet();
			for (final Iterator<Long> it = offsetId.keySet().iterator(); it.hasNext();) {
				Long ms = it.next();
				long l = ms.longValue();
				if (l < minMs) {
					minMs = l;
				}
				Long duration = durations.get(ms);
				long dur = duration != null ? duration.longValue() : 0;

				for (final Iterator<Long> iterator = idSet.iterator(); iterator.hasNext();) {
					Long offset = iterator.next();
					Long duration2 = this.intervalsDuration.get(ms);
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

		for (final Iterator<Long> it = offsetId.keySet().iterator(); it.hasNext();) {
			final Long ms = it.next();
			final Identifier temporalPatternId = offsetId.get(ms);
			this.intervalsAbstractTemporalPatternMap.put(ms, temporalPatternId == null ? Identifier.VOID_IDENTIFIER : temporalPatternId);
			this.intervalsDuration.put(ms, durations.get(ms));
		}

		if (minMs < 0) {
			this.moveIntervalItems0(new TreeSet<Long>(this.intervalsAbstractTemporalPatternMap.keySet()), -minMs);
		}

		super.markAsChanged();
	}

	public final void printStructure() {
		for (final Iterator<Long> it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			final Long ms = it.next();
			final Identifier abstractTemporalPatternId = this.intervalsAbstractTemporalPatternMap.get(ms);
			Log.debugMessage("IntervalsTemporalPattern.printStructure | ms:"
					+ ms.longValue()
					+ (abstractTemporalPatternId.isVoid() ? "" : (", id:" + abstractTemporalPatternId.toString())), Level.FINEST);
		}

		if (this.undoIntervalsAbstractTemporalPatternMap != null) {
			for (final Iterator<Long> it = this.undoIntervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				final Long ms = it.next();
				final Identifier abstractTemporalPatternId = this.undoIntervalsAbstractTemporalPatternMap.get(ms);
				Log.debugMessage("IntervalsTemporalPattern.printStructure | UNDO ms:"
						+ ms.longValue()
						+ (abstractTemporalPatternId.isVoid() ? "" : (", id:" + abstractTemporalPatternId.toString())), Level.FINEST);
			}
		}
	}

	public void setIntervalDuration(final long millesonds, final long duration) {
		/* save state before */
		this.saveState();

		Long startedTime = this.getStartedTime(millesonds);
		if (startedTime != null) {
			this.intervalsDuration.put(startedTime, new Long(duration));
		}
	}

	public void removeIntervalItems(final java.util.Set offsets) {
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
		final Long ms = this.getStartedTime(milliseconds);
		if (ms != null) {
			return this.intervalsAbstractTemporalPatternMap.get(ms);
		}
		return null;
	}

	public final Long getStartedTime(final long milliseconds) {
		return this.getStartedTime(new Long(milliseconds));
	}

	public final Long getStartedTime(final Long milliseconds) {
		Long ms = null;
		long long2 = milliseconds.longValue();
		final Set<Long> keys = this.intervalsAbstractTemporalPatternMap.keySet();
		if (keys.contains(milliseconds)) {
			ms = milliseconds;
		} else {
			for (final Iterator<Long> it = keys.iterator(); it.hasNext();) {
				final Long time = it.next();
				if (ms != null && ms.longValue() < long2 && long2 < time.longValue()) {
					break;
				}
				ms = time;
			}
		}
		return ms;
	}

	public Long getNextStartedTime(final Long milliseconds) {

		Long nextMs = null;
		long ms = milliseconds.longValue();
		{
			final SortedMap<Long, Identifier> futureMap = this.intervalsAbstractTemporalPatternMap.tailMap(milliseconds);
			for (final Iterator<Long> it = futureMap.keySet().iterator(); it.hasNext();) {
				nextMs = it.next();
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

	protected void addIntervalItem0(final Long milliseconds, final Identifier temporalPatternId) {

		if (super.times != null) {
			super.times.clear();
		}

		if (this.intervalsAbstractTemporalPatternMap == null) {
			this.intervalsAbstractTemporalPatternMap = new TreeMap<Long, Identifier>();
		}

		this.intervalsAbstractTemporalPatternMap.put(milliseconds, temporalPatternId == null ? Identifier.VOID_IDENTIFIER
				: temporalPatternId);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		for (final Long milliseconds : this.intervalsAbstractTemporalPatternMap.keySet()) {
			final Identifier abstractTemporalPatternId = this.intervalsAbstractTemporalPatternMap.get(milliseconds);
			dependencies.add(abstractTemporalPatternId);
		}
		return dependencies;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	public IdlIntervalsTemporalPattern getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		IntervalTemporalPatternId[] intervalTemporalPatternsIdT;
		IntervalDuration[] durationsT;
		{
			final Set<Long> keys = this.intervalsAbstractTemporalPatternMap.keySet();
			intervalTemporalPatternsIdT = new IntervalTemporalPatternId[keys.size()];
			int i = 0;
			for (final Iterator<Long> it = keys.iterator(); it.hasNext(); i++) {
				final Long ms = it.next();
				final Identifier temporalPatternId = this.intervalsAbstractTemporalPatternMap.get(ms);
				intervalTemporalPatternsIdT[i] = new IntervalTemporalPatternId(ms.longValue(),
						temporalPatternId.getTransferable());
			}
		}

		{
			if (this.intervalsDuration == null) {
				durationsT = new IntervalDuration[0];
			} else {
				final Set<Long> keys = this.intervalsDuration.keySet();
				durationsT = new IntervalDuration[keys.size()];
				int i = 0;
				for (final Iterator<Long> it = keys.iterator(); it.hasNext(); i++) {
					Long ms = it.next();
					Long duration = this.intervalsDuration.get(ms);
					durationsT[i] = new IntervalDuration(ms.longValue(), duration.longValue());
				}

			}
		}

		return IdlIntervalsTemporalPatternHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				intervalTemporalPatternsIdT,
				durationsT);
	}

	public SortedMap<Long, Identifier> getIntervalsAbstractTemporalPatternMap() {
		return Collections.unmodifiableSortedMap(this.intervalsAbstractTemporalPatternMap);
	}

	public void setIntervalsAbstractTemporalPatternMap(final SortedMap<Long, Identifier> intervalsAbstractTemporalPatternMap) {
		/* save state before */
		this.saveState();
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);
		this.markAsChanged();
	}

	public SortedMap<Long, Long> getIntervalsDuration() {
		return Collections.unmodifiableSortedMap(this.intervalsDuration);
	}

	public void moveAllItems(long offset) throws IllegalDataException {
		this.moveIntervalItems(new TreeSet<Long>(this.intervalsAbstractTemporalPatternMap.keySet()), offset);
	}

	/**
	 * move offsets forward for offset ms
	 * 
	 * @param offsets
	 * @param offset
	 *          to future if <code> offset &gt; 0 </code>, to past otherwise
	 * @throws IllegalDataException
	 */
	public void moveIntervalItems(final Set<Long> offsets, final long offset) throws IllegalDataException {
		/* save state before */
		this.saveState();
		moveIntervalItems0(offsets, offset);
	}

	protected void moveIntervalItems0(final Set<Long> offsets, final long offset) throws IllegalDataException {
		if (super.times != null) {
			super.times.clear();
		}

		long minOffset = 0;

		for (final Iterator<Long> it = offsets.iterator(); it.hasNext();) {
			final Long ms = it.next();
			final long ms1 = ms.longValue();
			Long newMs = new Long(ms1 + offset);

			Long duration = this.intervalsDuration.get(ms);
			Identifier temporalPatternId = this.intervalsAbstractTemporalPatternMap.get(ms);
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
			final Map<Long, Identifier> map = new HashMap<Long, Identifier>();
			final Map<Long, Long> durationMap = new HashMap<Long, Long>();
			for (final Iterator<Long> it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				final Long ms = it.next();
				final Long newMs = new Long(ms.longValue() - minOffset);
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
		for (final Iterator<Long> it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			final Long offset = it.next();
			if (prev == null) {
				prev = offset;
				continue;
			}
			final Long durationL = this.intervalsDuration.get(prev);
			final long duration = durationL != null ? durationL.longValue() : 0;

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
				this.undoIntervalsAbstractTemporalPatternMap = new TreeMap<Long, Identifier>();
			} else {
				this.undoIntervalsAbstractTemporalPatternMap.clear();
			}
			// Log.debugMessage("IntervalsTemporalPattern.saveState | 2", Log.FINEST);
			this.undoIntervalsAbstractTemporalPatternMap.putAll(this.intervalsAbstractTemporalPatternMap);
		}

		if (this.intervalsDuration != null) {
			if (this.undoIntervalsDuration == null) {
				this.undoIntervalsDuration = new TreeMap<Long, Long>();
			} else {
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

			SortedMap<Long, Identifier> map = new TreeMap<Long, Identifier>(this.intervalsAbstractTemporalPatternMap);

			this.intervalsAbstractTemporalPatternMap.clear();

			if (this.undoIntervalsAbstractTemporalPatternMap != null) {
				this.intervalsAbstractTemporalPatternMap.putAll(this.undoIntervalsAbstractTemporalPatternMap);
			}

			SortedMap<Long, Long> map2 = new TreeMap<Long, Long>(this.intervalsDuration);

			this.intervalsDuration.clear();
			if (this.undoIntervalsDuration != null) {
				this.intervalsDuration.putAll(this.undoIntervalsDuration);
			}

			if (this.undoIntervalsAbstractTemporalPatternMap == null) {
				this.undoIntervalsAbstractTemporalPatternMap = map;
			} else {
				this.undoIntervalsAbstractTemporalPatternMap.clear();
				this.undoIntervalsAbstractTemporalPatternMap.putAll(map);
			}

			if (this.undoIntervalsDuration == null) {
				this.undoIntervalsDuration = map2;
			} else {
				this.undoIntervalsDuration.clear();
				this.undoIntervalsDuration.putAll(map2);
			}

		} else {
			assert false : "Cannot undo due to haven't any actions yet";
		}
	}

	/**
	 * joint from fist offset till last offset
	 * 
	 * @param offsets
	 * @throws CreateObjectException
	 */
	public void joinIntervalItems(final SortedSet<Long> offsets) throws CreateObjectException {
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

		for (final Iterator<Long> it = offsets.iterator(); it.hasNext();) {
			final Long offset = it.next();
			if (!initedFirst) {
				initedFirst = true;
				firstOffset = offset;
				firstTime = offset.longValue();
			}
			lastOffset = offset;
		}

		long lastTime = lastOffset.longValue();

		final SortedMap<Long, Identifier> offsetIds = new TreeMap<Long, Identifier>();
		final SortedMap<Long, Long> offsetDuration = new TreeMap<Long, Long>();

		long duration = 0;

		for (final Iterator<Long> it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			final Long offset = it.next();
			final long offsetMs = offset.longValue();

			if (offsetMs < firstTime)
				continue;

			if (offsetMs > lastTime)
				break;

			final long newOffset = offset.longValue() - firstTime;
			final Long newMs = new Long(newOffset);
			final Long durationLong = this.intervalsDuration.get(offset);
			offsetIds.put(newMs, this.intervalsAbstractTemporalPatternMap.get(offset));

			/* remove it */
			it.remove();
			this.intervalsDuration.remove(offset);

			offsetDuration.put(newMs, durationLong);
			duration = newOffset + (durationLong != null ? durationLong.longValue() : 0);
		}

		final IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern.createInstance(this.modifierId,
				offsetIds,
				offsetDuration);
		this.intervalsAbstractTemporalPatternMap.put(firstOffset, intervalsTemporalPattern.getId());
		this.intervalsDuration.put(firstOffset, new Long(duration));
	}

	public void setIntervalsDuration(final SortedMap<Long, Long> intervalsDuration) {
		this.setIntervalsDuration0(intervalsDuration);
		this.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void disjoinIntervalItems(SortedSet<Long> offsets) throws ApplicationException {
		if (offsets == null || offsets.isEmpty())
			return;

		/* save state */
		this.saveState();

		if (super.times != null) {
			super.times.clear();
		}

		for (Iterator<Long> it = offsets.iterator(); it.hasNext();) {
			final Long offset = it.next();
			final long ms = offset.longValue();
			// Log.debugMessage("IntervalsTemporalPattern.disjoinIntervalItems | ms "
			// + ms, Log.FINEST);
			final Identifier temporalPatternId = this.intervalsAbstractTemporalPatternMap.get(offset);
			if (temporalPatternId.isVoid())
				continue;
			this.intervalsAbstractTemporalPatternMap.remove(offset);
			final Long duration = this.intervalsDuration.get(offset);
			this.intervalsDuration.remove(offset);
			final short major = temporalPatternId.getMajor();
			switch (major) {
				case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
					final IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) StorableObjectPool.getStorableObject(temporalPatternId,
							true);
					SortedMap<?, Identifier> intervalsAbstractTemporalPatternMap2 = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
					SortedMap<?, Long> intervalsDuration2 = intervalsTemporalPattern.getIntervalsDuration();

					for (Iterator iterator = intervalsAbstractTemporalPatternMap2.keySet().iterator(); iterator.hasNext();) {
						Long offset2 = (Long) iterator.next();
						Long newOffset = new Long(offset2.longValue() + ms);
						// Log.debugMessage("IntervalsTemporalPattern.disjoin | INTERVALS "
						// + offset2, Log.FINEST);

						this.intervalsAbstractTemporalPatternMap.put(newOffset, intervalsAbstractTemporalPatternMap2.get(offset2));
						this.intervalsDuration.put(newOffset, intervalsDuration2.get(offset2));
					}
					break;
				case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
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
					Log.debugMessage("IntervalsTemporalPattern.disjoin | temporalPatternId isn't support as temporal pattern", Level.FINEST);
					break;
			}

		}
	}
}
