/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.35 2006/07/05 02:23:17 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_IN_RANGE;
import static java.util.logging.Level.FINEST;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlChangeLogRecord;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlAffectedPathElementSpacious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StringToValueConverter;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;
import com.syrus.util.Math2;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.35 $, $Date: 2006/07/05 02:23:17 $
 * @module event
 */
public final class DefaultLineMismatchEvent extends AbstractLineMismatchEvent {
	private static final long serialVersionUID = -1651689764279078776L;

	/**
	 * @serial include
	 */
	private Identifier affectedPathElementId;

	/**
	 * @serial include
	 */
	private boolean affectedPathElementSpacious;

	/**
	 * @serial include
	 */
	private double physicalDistanceToStart;

	/**
	 * @serial include
	 */
	private double physicalDistanceToEnd;

	/**
	 * @serial include
	 */
	private double mismatchOpticalDistance;

	/**
	 * @serial include
	 */
	private double mismatchPhysicalDistance;

	/**
	 * @serial include
	 */
	private String plainTextMessage;

	/**
	 * @serial include
	 */
	private String richTextMessage;

	/**
	 * @serial include
	 */
	private Identifier reflectogramMismatchEventId;

	/**
	 * @serial include
	 */
	final AlarmStatus.Proxy alarmStatus;

	/**
	 * @serial include
	 */
	private Identifier parentLineMismatchEventId;

	/**
	 * @serial include
	 */
	private final SortedSet<ChangeLogRecord> changeLog = new TreeSet<ChangeLogRecord>();

	/**
	 * Ctor used solely by database driver.
	 *
	 * @param id
	 */
	DefaultLineMismatchEvent(final Identifier id) {
		super(id, VOID_IDENTIFIER, null, ILLEGAL_VERSION);
		this.alarmStatus = new AlarmStatus.Proxy();
	}

	/**
	 * @param id
	 * @param creatorId
	 * @param affectedPathElementId
	 * @param affectedPathElementSpacious
	 * @param physicalDistanceToStart
	 * @param physicalDistanceToEnd
	 * @param mismatchOpticalDistance
	 * @param mismatchPhysicalDistance
	 * @param plainTextMessage
	 * @param richTextMessage
	 * @param reflectogramMismatchEvent
	 */
	private DefaultLineMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpacious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String plainTextMessage,
			final String richTextMessage,
			final ReflectogramMismatchEvent reflectogramMismatchEvent) {
		super(id, creatorId, new Date(), INITIAL_VERSION);

		this.affectedPathElementId = affectedPathElementId;

		if (!!(this.affectedPathElementSpacious = affectedPathElementSpacious)) {
			this.physicalDistanceToStart = Math2.roundEpsilon(physicalDistanceToStart, reflectogramMismatchEvent.getDeltaX());
			this.physicalDistanceToEnd = Math2.roundEpsilon(physicalDistanceToEnd, reflectogramMismatchEvent.getDeltaX());

			if (this.physicalDistanceToStart < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToStart));
			} else if (this.physicalDistanceToEnd < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToEnd));
			}
		}

		this.mismatchOpticalDistance = Math2.roundEpsilon(mismatchOpticalDistance, reflectogramMismatchEvent.getDeltaX());
		this.mismatchPhysicalDistance = Math2.roundEpsilon(mismatchPhysicalDistance, reflectogramMismatchEvent.getDeltaX());
		this.plainTextMessage = plainTextMessage;
		this.richTextMessage = richTextMessage;
		this.reflectogramMismatchEventId = reflectogramMismatchEvent.getId();
		this.alarmStatus = new AlarmStatus.Proxy(AlarmStatus.PENDING);
		this.parentLineMismatchEventId = VOID_IDENTIFIER;
	}

	/**
	 * This contructor is invoked from both
	 * {@link com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventImpl#getNativeEvent()}
	 * and {@link com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventImpl#getNative()}.
	 *
	 * @param lineMismatchEvent
	 * @throws CreateObjectException
	 */
	public DefaultLineMismatchEvent(final IdlLineMismatchEvent lineMismatchEvent)
	throws CreateObjectException {
		super(lineMismatchEvent);
		this.alarmStatus = new AlarmStatus.Proxy();
		try {
			this.fromIdlTransferable(lineMismatchEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public void fromIdlTransferable(final IdlLineMismatchEvent lineMismatchEvent)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable((IdlStorableObject) lineMismatchEvent);

			this.affectedPathElementId = Identifier.valueOf(lineMismatchEvent.getAffectedPathElementId());

			if (!!(this.affectedPathElementSpacious = lineMismatchEvent.isAffectedPathElementSpacious())) {
				this.physicalDistanceToStart = lineMismatchEvent.getPhysicalDistanceToStart();
				this.physicalDistanceToEnd = lineMismatchEvent.getPhysicalDistanceToEnd();

				if (this.physicalDistanceToStart < 0) {
					throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToStart));
				} else if (this.physicalDistanceToEnd < 0) {
					throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToEnd));
				}
			}

			this.mismatchOpticalDistance = lineMismatchEvent.getMismatchOpticalDistance();
			this.mismatchPhysicalDistance = lineMismatchEvent.getMismatchPhysicalDistance();
			this.plainTextMessage = lineMismatchEvent.getPlainTextMessage();
			this.richTextMessage = lineMismatchEvent.getRichTextMessage();
			this.reflectogramMismatchEventId = Identifier.valueOf(lineMismatchEvent.getReflectogramMismatchEventId());
			this.alarmStatus.fromIdlTransferable(lineMismatchEvent.getAlarmStatus());
			this.parentLineMismatchEventId = Identifier.valueOf(lineMismatchEvent.getParentLineMismatchEventId());

			this.changeLog.clear();
			for (final IdlChangeLogRecord changeLogRecord : lineMismatchEvent.getChangeLog()) {
				new ChangeLogRecordImpl(changeLogRecord);
			}
		}
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpacious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String plainTextMessage,
			final String richTextMessage,
			final Identifier reflectogramMismatchEventId,
			final AlarmStatus alarmStatus,
			final Identifier parentLineMismatchEventId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
		this.affectedPathElementId = affectedPathElementId;

		if (!!(this.affectedPathElementSpacious = affectedPathElementSpacious)) {
			this.physicalDistanceToStart = physicalDistanceToStart;
			this.physicalDistanceToEnd = physicalDistanceToEnd;

			if (this.physicalDistanceToStart < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToStart));
			} else if (this.physicalDistanceToEnd < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToEnd));
			}
		}

		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;
		this.plainTextMessage = plainTextMessage;
		this.richTextMessage = richTextMessage;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;

		if (alarmStatus != null ^ parentLineMismatchEventId.isVoid()) {
			throw new IllegalArgumentException("alarmStatus = " + alarmStatus + "; " +
					"parentLineMismatchEventId = " + parentLineMismatchEventId); 
		}

		this.alarmStatus.setValue(alarmStatus);
		this.parentLineMismatchEventId = parentLineMismatchEventId;
	}

	/**
	 * <p>Creates new event and automatically adds it to the appropriate
	 * group based on path element, optical distance and severity data.</p>
	 *
	 * <p>This method should be invoked on the event server (where line 
	 * mismatch event generation occurs) for the newly created object gets
	 * flushed prior to being returned.</p>
	 *
	 * @param creatorId
	 * @param affectedPathElementId
	 * @param affectedPathElementSpacious
	 * @param physicalDistanceToStart
	 * @param physicalDistanceToEnd
	 * @param mismatchOpticalDistance
	 * @param mismatchPhysicalDistance
	 * @param plainTextMessage
	 * @param richTextMessage
	 * @param reflectogramMismatchEvent
	 * @throws CreateObjectException
	 */
	public static synchronized LineMismatchEvent newInstance(
			final Identifier creatorId,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpacious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String plainTextMessage,
			final String richTextMessage,
			final ReflectogramMismatchEvent reflectogramMismatchEvent)
	throws CreateObjectException {
		if (creatorId == null) {
			throw new NullPointerException("creatorId is null");
		}
		if (creatorId.isVoid()) {
			throw new IllegalArgumentException("creatorId is void");
		}

		try {
			/*-
			 * Events are grouped by optical distance, severity and
			 * path element id.
			 */
			final long t0 = System.nanoTime();


			final TypicalCondition typicalCondition = new TypicalCondition(
					reflectogramMismatchEvent.getSeverity(),
					OPERATION_EQUALS,
					REFLECTOGRAMMISMATCHEVENT_CODE,
					COLUMN_SEVERITY);
			final Set<Identifier> reflectogramMismatcheventIds
					= StorableObjectPool.getIdentifiersByCondition(
							typicalCondition,
							true);
			final double epsilon = reflectogramMismatchEvent.getDeltaX() / 2.0;


			final StorableObjectCondition condition0 = new LinkedIdsCondition(
					reflectogramMismatcheventIds,
					LINEMISMATCHEVENT_CODE);
			final StorableObjectCondition condition1 = new LinkedIdsCondition(
					affectedPathElementId,
					LINEMISMATCHEVENT_CODE);
			final StorableObjectCondition condition2 = new TypicalCondition(
					mismatchOpticalDistance - epsilon,
					mismatchOpticalDistance + epsilon,
					OPERATION_IN_RANGE,
					LINEMISMATCHEVENT_CODE,
					COLUMN_MISMATCH_OPTICAL_DISTANCE);
			final StorableObjectCondition condition3 = new LinkedIdsCondition(
					VOID_IDENTIFIER,
					LINEMISMATCHEVENT_CODE);


			final Set<StorableObjectCondition> conditions
					= new HashSet<StorableObjectCondition>();
			conditions.add(condition0);
			conditions.add(condition1);
			conditions.add(condition2);
			conditions.add(condition3);


			final Set<? extends LineMismatchEvent> lineMismatchEvents
					= StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsByCondition(
							new CompoundCondition(conditions, AND),
							true);


			final long t1 = System.nanoTime();


			if (!lineMismatchEvents.isEmpty()) {
				Log.debugMessage(lineMismatchEvents.size()
						+ " matching event(s) found in "
						+ ((t1 - t0) / 1e9) + " second(s).",
						FINEST);
			}


			final DefaultLineMismatchEvent lineMismatchEvent = new DefaultLineMismatchEvent(
					IdentifierPool.getGeneratedIdentifier(LINEMISMATCHEVENT_CODE),
					creatorId,
					affectedPathElementId,
					affectedPathElementSpacious, physicalDistanceToStart,
					physicalDistanceToEnd, mismatchOpticalDistance,
					mismatchPhysicalDistance, plainTextMessage,
					richTextMessage, reflectogramMismatchEvent);
			lineMismatchEvent.markAsChanged();


			final Comparator<LineMismatchEvent> comparator = new Comparator<LineMismatchEvent>() {
				public int compare(
						final LineMismatchEvent event1,
						final LineMismatchEvent event2) {
					try {
						final ReflectogramMismatchEvent reflectogramMismatchEvent1
								= event1.getReflectogramMismatchEvent();
						final ReflectogramMismatchEvent reflectogramMismatchEvent2
								= event2.getReflectogramMismatchEvent();
						return reflectogramMismatchEvent1.compareTo(reflectogramMismatchEvent2);
					} catch (final ApplicationException ae) {
						throw new Error(ae);
					}
				}
			};
			final SortedSet<LineMismatchEvent> sortedEvents
					= new TreeSet<LineMismatchEvent>(comparator);
			sortedEvents.addAll(lineMismatchEvents);
			sortedEvents.add(lineMismatchEvent);
			final Iterator<LineMismatchEvent> it = sortedEvents.iterator();
			final LineMismatchEvent groupLeader = it.next();
			while (it.hasNext()) {
				it.next().setParentLineMismatchEvent(groupLeader);
			}


			StorableObjectPool.flush(sortedEvents, creatorId, false);
			return lineMismatchEvent;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @see LineMismatchEvent#getAffectedPathElementId()
	 */
	public Identifier getAffectedPathElementId() {
		return this.affectedPathElementId;
	}

	/**
	 * @see LineMismatchEvent#isAffectedPathElementSpacious()
	 */
	public boolean isAffectedPathElementSpacious() {
		return this.affectedPathElementSpacious;
	}

	/**
	 * @see LineMismatchEvent#getPhysicalDistanceToStart()
	 */
	public double getPhysicalDistanceToStart() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToStart;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see LineMismatchEvent#getPhysicalDistanceToEnd()
	 */
	public double getPhysicalDistanceToEnd() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToEnd;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see LineMismatchEvent#getMismatchOpticalDistance()
	 */
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see LineMismatchEvent#getMismatchPhysicalDistance()
	 */
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see LineMismatchEvent#getPlainTextMessage()
	 */
	public String getPlainTextMessage() {
		return this.plainTextMessage;
	}

	/**
	 * @see LineMismatchEvent#getRichTextMessage()
	 */
	public String getRichTextMessage() {
		return this.richTextMessage;
	}

	/**
	 * @see LineMismatchEvent#getReflectogramMismatchEventId()
	 */
	public Identifier getReflectogramMismatchEventId() {
		return this.reflectogramMismatchEventId;
	}

	/**
	 * @see LineMismatchEvent#setAlarmStatus(AlarmStatus)
	 */
	@SuppressWarnings("null")
	public void setAlarmStatus(final AlarmStatus alarmStatus)
	throws ApplicationException {
		final AlarmStatus oldAlarmStatus = this.alarmStatus.getValue();
		final boolean parentLineMismatchEventIdVoid = this.parentLineMismatchEventId.isVoid();
		assert oldAlarmStatus == null ^ parentLineMismatchEventIdVoid :
				"alarmStatus = " + oldAlarmStatus + "; "
				+ "parentLineMismatchEventId = " + this.parentLineMismatchEventId;
		if (!parentLineMismatchEventIdVoid) {
			this.getParentLineMismatchEvent().setAlarmStatus(alarmStatus);
		} else if (oldAlarmStatus.isAllowedPredecessorOf(alarmStatus)) {
			new ChangeLogRecordImpl(COLUMN_ALARM_STATUS,
					oldAlarmStatus,
					alarmStatus);
			this.alarmStatus.setValue(alarmStatus);
		} else {
			throw new IllegalArgumentException(oldAlarmStatus
					+ " is not an allowed predecessor of "
					+ alarmStatus);
		}
	}

	/**
	 * @see LineMismatchEvent#getAlarmStatus()
	 */
	public AlarmStatus getAlarmStatus() throws ApplicationException {
		@SuppressWarnings("hiding")
		final AlarmStatus alarmStatus = this.alarmStatus.getValue();
		final boolean alarmStatusNull = alarmStatus == null;
		assert alarmStatusNull ^ this.parentLineMismatchEventId.isVoid() :
				"alarmStatus = " + alarmStatus + "; "
				+ "parentLineMismatchEventId = " + this.parentLineMismatchEventId;
		return alarmStatusNull
				? this.getParentLineMismatchEvent().getAlarmStatus()
				: alarmStatus;
	}

	/**
	 * @see LineMismatchEvent#setParentLineMismatchEvent(LineMismatchEvent)
	 */
	public void setParentLineMismatchEvent(final LineMismatchEvent parentLineMismatchEvent)
	throws ApplicationException {
		final Identifier newParentLineMismatchEventId = Identifier.possiblyVoid(parentLineMismatchEvent);

		if (this.parentLineMismatchEventId.equals(newParentLineMismatchEventId)) {
			return;
		}

		if (parentLineMismatchEvent == null) {
			/*-
			 * Has been having a parent - refuse to detach.
			 */
			throw new IllegalStateException(
					"Cowardly refusing to detach the event: "
					+ "id = " + this.id + "; "
					+ "parentLineMismatchEventId = "
					+ this.parentLineMismatchEventId + '.');
		} else if (!this.parentLineMismatchEventId.isVoid()) {
			/*-
			 * Has been having a parent - refuse to reattach.
			 */
			throw new IllegalStateException(
					"Cowardly refusing to reattach the event: "
					+ "id = " + this.id + "; "
					+ "oldParentLineMismatchEventId = "
					+ this.parentLineMismatchEventId + "; "
					+ "newParentLineMismatchEventId = "
					+ newParentLineMismatchEventId + '.');
		}

		/*-
		 * Has been having no parent, would have one - let's see.
		 * 
		 * First, check whether parentLineMismatchEvent is a valid group
		 * leader.
		 */
		if (this.equals(newParentLineMismatchEventId)) {
			throw new IllegalArgumentException(
					"Dependency circularity error: can't subject "
					+ this.id + " to itself.");
		}
		final Identifier parentParentId
				= parentLineMismatchEvent.getParentLineMismatchEventId();
		if (this.equals(parentParentId)) {
			throw new IllegalArgumentException(
					"Dependency circularity error: "
					+ newParentLineMismatchEventId
					+ " is already referencing " + this.id
					+ " as a parent.");
		} else if (!parentParentId.isVoid()) {
			throw new IllegalArgumentException("Since "
					+ newParentLineMismatchEventId
					+ " is already referencing "
					+ parentParentId
					+ " as a parent, it's not a valid group leader for "
					+ this.id + '.');
		}

		/*-
		 * Second, check whether "this" is a valid subordinate.
		 */
		final SortedSet<LineMismatchEvent> childLineMismatchEvents
				= this.getChildLineMismatchEvents();
		if (!childLineMismatchEvents.isEmpty()) {
			throw new IllegalStateException("Since " + this.id
					+ " is already a group leader with "
					+ childLineMismatchEvents.size()
					+ " child(ren) "
					+ Identifier.createIdentifiers(childLineMismatchEvents)
					+ ", it's not a valid subordinate for "
					+ newParentLineMismatchEventId
					+ " and can't be \"attached\".");
		}

		/*-
		 * Third, check whether parentLineMismatchEvent < "this"
		 */
		final ReflectogramMismatchEvent parentReflectogramMismatchEvent = parentLineMismatchEvent.getReflectogramMismatchEvent();
		final ReflectogramMismatchEvent thisReflectogramMismatchEvent = this.getReflectogramMismatchEvent();
		if (parentReflectogramMismatchEvent.compareTo(thisReflectogramMismatchEvent) >= 0) {
			throw new IllegalArgumentException(""
					+ parentReflectogramMismatchEvent.getId() + ' '
					+ '(' + DATE_FORMAT.format(parentReflectogramMismatchEvent.getCreated()) + ')'
					+ " >= "
					+ thisReflectogramMismatchEvent.getId() + ' '
					+ '(' + DATE_FORMAT.format(thisReflectogramMismatchEvent.getCreated()) + ')');
		}

		/*-
		 * Fourth, actually perform the action we've been asked for.
		 */
		new ChangeLogRecordImpl(COLUMN_PARENT_LINE_MISMATCH_EVENT_ID,
				this.parentLineMismatchEventId,
				newParentLineMismatchEventId);
		this.parentLineMismatchEventId = newParentLineMismatchEventId;
		this.alarmStatus.setValue(null);

		/*-
		 * If our new parent event is closed, reopen it.
		 */
		if (parentLineMismatchEvent.isClosed()) {
			try {
				parentLineMismatchEvent.reopen();
			} catch (final UnsupportedOperationException uoe) {
				// do nothing
			}
		}
	}

	/**
	 * @see LineMismatchEvent#getParentLineMismatchEventId()
	 */
	public Identifier getParentLineMismatchEventId() {
		@SuppressWarnings("hiding")
		final AlarmStatus alarmStatus = this.alarmStatus.getValue();
		assert alarmStatus == null ^ this.parentLineMismatchEventId.isVoid() :
				"alarmStatus = " + alarmStatus + "; "
				+ "parentLineMismatchEventId = " + this.parentLineMismatchEventId;
		return this.parentLineMismatchEventId;
	}

	void setChangeLog0(final Set<ChangeLogRecord> changeLog) {
		this.changeLog.clear();
		this.changeLog.addAll(changeLog);
	}

	/**
	 * @see LineMismatchEvent#getChangeLog()
	 */
	public SortedSet<ChangeLogRecord> getChangeLog() {
		return Collections.unmodifiableSortedSet(this.changeLog);
	}

	/**
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public IdlLineMismatchEvent getIdlTransferable(final ORB orb) {
		final IdlSpacialData spacialData = new IdlSpacialData();
		if (this.isAffectedPathElementSpacious()) {
			spacialData.physicalDistancePair(
					IdlAffectedPathElementSpacious._TRUE,
					new IdlPhysicalDistancePair(
							this.getPhysicalDistanceToStart(),
							this.getPhysicalDistanceToEnd()));
		} else {
			spacialData._default(IdlAffectedPathElementSpacious._FALSE);
		}

		final IdlChangeLogRecord idlChangeLog[] = new IdlChangeLogRecord[this.changeLog.size()];
		int i = 0;
		for (final ChangeLogRecord changeLogRecord : this.changeLog) {
			idlChangeLog[i++] = changeLogRecord.getIdlTransferable(orb);
		}

		return IdlLineMismatchEventHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.getAffectedPathElementId().getIdlTransferable(orb),
				spacialData,
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.getPlainTextMessage(),
				this.getRichTextMessage(),
				this.getReflectogramMismatchEventId().getIdlTransferable(orb),
				this.alarmStatus.getIdlTransferable(orb),
				this.getParentLineMismatchEventId().getIdlTransferable(orb),
				idlChangeLog);
	}

	@SuppressWarnings("hiding")
	ChangeLogRecord newChangeLogRecord(final Date modified,
				final String key,
				final String oldValue,
				final String newValue) {
		return new ChangeLogRecordImpl(modified, key, oldValue, newValue);
	}

	/**
	 * Immutable.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @module event
	 */
	private class ChangeLogRecordImpl implements ChangeLogRecord, Serializable {
		private static final long serialVersionUID = -772564340276801643L;

		@SuppressWarnings("hiding")
		private final Date modified;

		private final String key;

		private /*final*/ transient Object oldValue;

		private /*final*/ transient Object newValue;

		/**
		 * Ctor to be invoked from modifier methods.
		 *
		 * @param key
		 * @param oldValue
		 * @param newValue
		 */
		@SuppressWarnings("synthetic-access")
		ChangeLogRecordImpl(final String key,
				final Object oldValue,
				final Object newValue) {
			this.modified = new Date();
			this.key = key;
			this.oldValue = oldValue;
			this.newValue = newValue;

			DefaultLineMismatchEvent.this.changeLog.add(ChangeLogRecordImpl.this);
			DefaultLineMismatchEvent.this.markAsChanged();
		}

		/**
		 * Ctor invoked by database driver.
		 *
		 * @param modified
		 * @param key
		 * @param oldValue
		 * @param newValue
		 */
		ChangeLogRecordImpl(final Date modified,
				final String key,
				final String oldValue,
				final String newValue) {
			this.modified = new Date(modified.getTime());
			final StringToValueConverter stringToValueConverter
					= ChangeLogRecordImpl.this.getStringToValueConverter();
			this.oldValue = stringToValueConverter.stringToValue(this.key = key, oldValue);
			this.newValue = stringToValueConverter.stringToValue(ChangeLogRecordImpl.this.key, newValue);
		}

		/**
		 * Ctor to be invoked from {@code fromIdlTransferable(...)}
		 * method of enclosing entity.
		 *
		 * @param changeLogRecord
		 */
		@SuppressWarnings("synthetic-access")
		ChangeLogRecordImpl(final IdlChangeLogRecord changeLogRecord) {
			this(new Date(changeLogRecord.modified),
					changeLogRecord.key,
					changeLogRecord.oldValue,
					changeLogRecord.newValue);

			DefaultLineMismatchEvent.this.changeLog.add(ChangeLogRecordImpl.this);
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getModified()
		 */
		public Date getModified() {
			return (Date) ChangeLogRecordImpl.this.modified.clone();
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getKey()
		 */
		public String getKey() {
			return ChangeLogRecordImpl.this.key;
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getOldValue()
		 */
		public Object getOldValue() {
			return ChangeLogRecordImpl.this.oldValue;
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getNewValue()
		 */
		public Object getNewValue() {
			return ChangeLogRecordImpl.this.newValue;
		}

		/**
		 * Compares {@code modified} properties and, if they&apos;re
		 * equal, compares {@code key} properties.
		 *
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(final ChangeLogRecord that) {
			final int returnValue = ChangeLogRecordImpl.this.modified.compareTo(that.getModified());
			return returnValue == 0
					? ChangeLogRecordImpl.this.key.compareTo(that.getKey())
					: returnValue;
		}

		/**
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int hashCode = 1;
			hashCode = prime * hashCode + ChangeLogRecordImpl.this.key.hashCode();
			hashCode = prime * hashCode + ChangeLogRecordImpl.this.modified.hashCode();
			return hashCode;
		}

		/**
		 * @see Object#equals(Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof ChangeLogRecord) {
				final ChangeLogRecord that = (ChangeLogRecord) obj;
				return ChangeLogRecordImpl.this == that ||
						ChangeLogRecordImpl.this.modified.equals(that.getModified()) &&
						ChangeLogRecordImpl.this.key.equals(that.getKey());
			}
			return false;
		}

		/**
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlChangeLogRecord getIdlTransferable(final ORB orb) {
			final StringToValueConverter stringToValueConverter
					= ChangeLogRecordImpl.this.getStringToValueConverter();
			return new IdlChangeLogRecord(
					ChangeLogRecordImpl.this.modified.getTime(),
					ChangeLogRecordImpl.this.key,
					stringToValueConverter.valueToString(
							ChangeLogRecordImpl.this.key,
							ChangeLogRecordImpl.this.oldValue),
					stringToValueConverter.valueToString(
							ChangeLogRecordImpl.this.key,
							ChangeLogRecordImpl.this.newValue));
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getStringToValueConverter()
		 */
		public StringToValueConverter getStringToValueConverter() {
			return LineMismatchEventWrapper.getInstance();
		}

		private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
			in.defaultReadObject();

			final StringToValueConverter stringToValueConverter
					= ChangeLogRecordImpl.this.getStringToValueConverter();
			if (in.readBoolean()) {
				ChangeLogRecordImpl.this.oldValue = stringToValueConverter.stringToValue(
						ChangeLogRecordImpl.this.key,
						in.readUTF());
			}
			if (in.readBoolean()) {
				ChangeLogRecordImpl.this.newValue = stringToValueConverter.stringToValue(
						ChangeLogRecordImpl.this.key,
						in.readUTF());
			}
		}

		private void writeObject(final ObjectOutputStream out) throws IOException {
			out.defaultWriteObject();

			final StringToValueConverter stringToValueConverter
					= ChangeLogRecordImpl.this.getStringToValueConverter();
			final boolean oldValueNonNull = ChangeLogRecordImpl.this.oldValue != null;
			final boolean newValueNonNull = ChangeLogRecordImpl.this.newValue != null;
			
			out.writeBoolean(oldValueNonNull);
			if (oldValueNonNull) {
				out.writeUTF(stringToValueConverter.valueToString(
						ChangeLogRecordImpl.this.key,
						ChangeLogRecordImpl.this.oldValue));
			}
			out.writeBoolean(newValueNonNull);
			if (newValueNonNull) {
				out.writeUTF(stringToValueConverter.valueToString(
						ChangeLogRecordImpl.this.key,
						ChangeLogRecordImpl.this.newValue));
			}
		}
	}
}
