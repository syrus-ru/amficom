/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.23 2006/06/27 19:08:14 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlAffectedPathElementSpacious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.23 $, $Date: 2006/06/27 19:08:14 $
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
	 * @param reflectogramMismatchEventId
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
			final Identifier reflectogramMismatchEventId) {
		super(id, creatorId, new Date(), INITIAL_VERSION);

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
	 * This method should be invoked on the event server (where line 
	 * mismatch event generation occurs) for the newly created object gets
	 * flushed prior to being returned.
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
	 * @param reflectogramMismatchEventId
	 * @throws CreateObjectException
	 */
	public static LineMismatchEvent newInstance(
			final Identifier creatorId,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpacious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String plainTextMessage,
			final String richTextMessage,
			final Identifier reflectogramMismatchEventId)
	throws CreateObjectException {
		if (creatorId == null) {
			throw new NullPointerException("creatorId is null");
		}
		if (creatorId.isVoid()) {
			throw new IllegalArgumentException("creatorId is void");
		}

		try {
			final DefaultLineMismatchEvent lineMismatchEvent = new DefaultLineMismatchEvent(
					IdentifierPool.getGeneratedIdentifier(LINEMISMATCHEVENT_CODE),
					creatorId,
					affectedPathElementId,
					affectedPathElementSpacious, physicalDistanceToStart,
					physicalDistanceToEnd, mismatchOpticalDistance,
					mismatchPhysicalDistance, plainTextMessage,
					richTextMessage, reflectogramMismatchEventId);
			lineMismatchEvent.markAsChanged();
			StorableObjectPool.flush(lineMismatchEvent, creatorId, false);
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
			this.changeLog.add(new ChangeLogRecordImpl(
					COLUMN_ALARM_STATUS,
					oldAlarmStatus,
					alarmStatus));
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
		this.changeLog.add(new ChangeLogRecordImpl(
				COLUMN_PARENT_LINE_MISMATCH_EVENT_ID,
				this.parentLineMismatchEventId,
				newParentLineMismatchEventId));
		this.parentLineMismatchEventId = newParentLineMismatchEventId;
		this.alarmStatus.setValue(null);
		/**
		 * @todo If our new parent event is closed, reopen it.
		 */
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
				this.getParentLineMismatchEventId().getIdlTransferable(orb));
	}

	/**
	 * Immutable.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.23 $, $Date: 2006/06/27 19:08:14 $
	 * @module event
	 */
	private class ChangeLogRecordImpl implements ChangeLogRecord, Serializable {
		private static final long serialVersionUID = -772564340276801643L;

		@SuppressWarnings("hiding")
		private final Date modified;

		private final String key;

		private final Object oldValue;

		private final Object newValue;

		@SuppressWarnings("synthetic-access")
		ChangeLogRecordImpl(final String key,
				final Object oldValue,
				final Object newValue) {
			this.modified = new Date();
			this.key = key;
			this.oldValue = oldValue;
			this.newValue = newValue;

			DefaultLineMismatchEvent.this.markAsChanged();
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getModified()
		 */
		public Date getModified() {
			return (Date) this.modified.clone();
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getKey()
		 */
		public String getKey() {
			return this.key;
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getOldValue()
		 */
		public Object getOldValue() {
			return this.oldValue;
		}

		/**
		 * @see LineMismatchEvent.ChangeLogRecord#getNewValue()
		 */
		public Object getNewValue() {
			return this.newValue;
		}

		/**
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(final ChangeLogRecord that) {
			return this.getModified().compareTo(that.getModified());
		}
	}
}
