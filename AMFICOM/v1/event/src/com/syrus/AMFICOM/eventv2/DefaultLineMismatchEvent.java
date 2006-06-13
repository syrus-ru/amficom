/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.15 2006/06/13 15:12:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
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
 * @version $Revision: 1.15 $, $Date: 2006/06/13 15:12:12 $
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
	private final AlarmStatus.Proxy alarmStatus;

	/**
	 * @serial include
	 */
	private Identifier parentLineMismatchEventId;

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
		try {
			this.alarmStatus = new AlarmStatus.Proxy();
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
			/**
			 * @todo Write something meaningful here.
			 */
//			this.alarmStatus.fromIdlTransferable(...);
//			this.parentLineMismatchEventId = ...;
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
			this.alarmStatus.setValue(alarmStatus);
			this.markAsChanged();
		} else {
			throw new IllegalArgumentException(oldAlarmStatus
					+ " is not an allowed predecessor of "
					+ alarmStatus);
		}
	}

	/**
	 * @see LineMismatchEvent#getAlarmStatus()
	 */
	public AlarmStatus getAlarmStatus() {
		@SuppressWarnings("hiding")
		final AlarmStatus alarmStatus = this.alarmStatus.getValue();
		assert alarmStatus == null ^ this.parentLineMismatchEventId.isVoid() :
				"alarmStatus = " + alarmStatus + "; "
				+ "parentLineMismatchEventId = " + this.parentLineMismatchEventId;
		return alarmStatus;
	}

	/**
	 * @see LineMismatchEvent#setParentLineMismatchEventId(Identifier)
	 */
	public void setParentLineMismatchEventId(final Identifier parentLineMismatchEventId) {
		if (parentLineMismatchEventId.isVoid()) {
			if (this.parentLineMismatchEventId.isVoid()) {
				/*-
				 * Has been having no parent, would have no
				 * parent - no harm.
				 */
				return;
			}
			/*-
			 * Has been having a parent - refuse to detach.
			 */
			throw new IllegalStateException("Cowardly refusing to detach the event: "
					+ "id = " + this.id + "; "
					+ "oldParentLineMismatchEventId = " + this.parentLineMismatchEventId + "; "
					+ "newParentLineMismatchEventId = " + parentLineMismatchEventId); 
		} else if (this.parentLineMismatchEventId.isVoid()) {
			/*-
			 * Has been having no parent, would have one - let's see.
			 */
			throw new UnsupportedOperationException();
		} else {
			/*-
			 * Has been having a parent - refuse to reattach.
			 */
			throw new IllegalStateException("Cowardly refusing to reattach the event: "
					+ "id = " + this.id + "; "
					+ "oldParentLineMismatchEventId = " + this.parentLineMismatchEventId + "; "
					+ "newParentLineMismatchEventId = " + parentLineMismatchEventId); 
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
}
