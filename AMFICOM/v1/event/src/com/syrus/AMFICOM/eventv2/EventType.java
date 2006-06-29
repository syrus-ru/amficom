/*-
 * $Id: EventType.java,v 1.12 2006/06/29 10:20:37 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2006/06/29 10:20:37 $
 * @module event
 */
public enum EventType {
	REFLECTORGAM_MISMATCH("reflectogramMismatch"),
	LINE_MISMATCH("lineMismatch"),
	NOTIFICATION("notification"),
	MEASUREMENT_STATUS_CHANGED("measurementStatusChanged");

	private static final EventType VALUES[] = values();

	private String codename;

	private EventType(final String codename) {
		this.codename = codename;
	}

	public String getCodename() {
		return this.codename;
	}

	/**
	 * @param ordinal
	 * @throws IllegalArgumentException
	 */
	public static EventType valueOf(final int ordinal) {
		try {
			return VALUES[ordinal];
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(String.valueOf(ordinal));
		}
	}

	/**
	 * @param eventType
	 * @throws IllegalArgumentException
	 */
	public static EventType valueOf(final IdlEventType eventType) {
		return valueOf(eventType.value());
	}

	/**
	 * A mutable holder for immutable enum instances.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @module event
	 */
	public static final class Proxy
			implements IdlTransferableObjectExt<IdlEventType> {
		private EventType value;

		/**
		 * Creates a new uninitialized instance.
		 */
		public Proxy() {
			// empty
		}

		public Proxy(final EventType value) {
			this.value = value;
		}

		public EventType getValue() {
			return this.value;
		}

		public void setValue(final EventType value) {
			this.value = value;
		}

		/**
		 * @param orb
		 * @throws IllegalArgumentException
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlEventType getIdlTransferable(final ORB orb) {
			try {
				return IdlEventType.from_int(this.value.ordinal());
			} catch (final BAD_PARAM bp) {
				throw new IllegalArgumentException(String.valueOf(this.value.ordinal()), bp);
			}
		}

		/**
		 * @param eventType
		 * @throws IllegalArgumentException
		 * @see IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
		 */
		public void fromIdlTransferable(final IdlEventType eventType) {
			this.value = valueOf(eventType);
		}
	}
}
