/*-
 * $Id: IdlLineMismatchEventImpl.java,v 1.7.4.1 2006/06/07 09:07:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlAffectedPathElementSpacious;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.4.1 $, $Date: 2006/06/07 09:07:10 $
 * @module event
 */
final class IdlLineMismatchEventImpl extends IdlLineMismatchEvent {
	private static final long serialVersionUID = -3747979777168377362L;

	IdlLineMismatchEventImpl() {
		// empty
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param affectedPathElementId
	 * @param spacialData
	 * @param mismatchOpticalDistance
	 * @param mismatchPhysicalDistance
	 * @param plainTextMessage
	 * @param richTextMessage
	 * @param reflectogramMismatchEventId
	 */
	IdlLineMismatchEventImpl(final IdlIdentifier id,
			final long created, final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId, final long version,
			final IdlIdentifier affectedPathElementId,
			final IdlSpacialData spacialData,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String plainTextMessage,
			final String richTextMessage,
			final IdlIdentifier reflectogramMismatchEventId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.affectedPathElementId = affectedPathElementId;
		this.spacialData = spacialData;

		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;

		this.plainTextMessage = plainTextMessage;
		this.richTextMessage = richTextMessage;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;
	}

	/**
	 * @see IdlLineMismatchEvent#getAffectedPathElementId()
	 */
	@Override
	public IdlIdentifier getAffectedPathElementId() {
		return this.affectedPathElementId;
	}

	/**
	 * @see IdlLineMismatchEvent#isAffectedPathElementSpacious()
	 */
	@Override
	public boolean isAffectedPathElementSpacious() {
		return this.spacialData.discriminator() == IdlAffectedPathElementSpacious._TRUE;
	}

	/**
	 * @see IdlLineMismatchEvent#getPhysicalDistanceToStart()
	 */
	@Override
	public double getPhysicalDistanceToStart() {
		if (this.isAffectedPathElementSpacious()) {
			return this.spacialData.physicalDistancePair().physicalDistanceToStart;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlLineMismatchEvent#getPhysicalDistanceToEnd()
	 */
	@Override
	public double getPhysicalDistanceToEnd() {
		if (this.isAffectedPathElementSpacious()) {
			return this.spacialData.physicalDistancePair().physicalDistanceToEnd;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlEvent#getType()
	 */
	public IdlEventType getType() {
		return IdlEventType.LINE_MISMATCH;
	}

	/**
	 * @see IdlLineMismatchEvent#getMismatchOpticalDistance()
	 */
	@Override
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see IdlLineMismatchEvent#getMismatchPhysicalDistance()
	 */
	@Override
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see IdlLineMismatchEvent#getPlainTextMessage()
	 */
	@Override
	public String getPlainTextMessage() {
		return this.plainTextMessage;
	}

	/**
	 * @see IdlLineMismatchEvent#getRichTextMessage()
	 */
	@Override
	public String getRichTextMessage() {
		return this.richTextMessage;
	}

	/**
	 * @see IdlLineMismatchEvent#getReflectogramMismatchEventId()
	 */
	@Override
	public IdlIdentifier getReflectogramMismatchEventId() {
		return this.reflectogramMismatchEventId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public DefaultLineMismatchEvent getNative() throws IdlCreateObjectException {
		try {
			return new DefaultLineMismatchEvent(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see IdlEvent#getNativeEvent()
	 */
	public LineMismatchEvent getNativeEvent() throws IdlCreateObjectException {
		return this.getNative();
	}
}
