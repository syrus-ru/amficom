/*-
 * $Id: DeliveryMethod.java,v 1.2.4.2 2006/06/07 09:32:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2.4.2 $, $Date: 2006/06/07 09:32:09 $
 * @module event
 */
public enum DeliveryMethod {
	EMAIL("email"),
	SMS("sms"),
	POPUP("popup");

	private static final DeliveryMethod VALUES[] = values();

	private String codename;

	private DeliveryMethod(final String codename) {
		this.codename = codename;
	}

	public String getCodename() {
		return this.codename;
	}

	/**
	 * @param ordinal
	 * @throws IllegalArgumentException
	 */
	public static DeliveryMethod valueOf(final int ordinal) {
		try {
			return VALUES[ordinal];
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			throw new IllegalArgumentException(String.valueOf(ordinal));
		}
	}

	/**
	 * @param deliveryMethod
	 * @throws IllegalArgumentException
	 */
	public static DeliveryMethod valueOf(final IdlDeliveryMethod deliveryMethod) {
		return valueOf(deliveryMethod.value());
	}

	/**
	 * A mutable holder for immutable enum instances.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 */
	public static final class Proxy
			implements IdlTransferableObjectExt<IdlDeliveryMethod> {
		private DeliveryMethod value;

		/**
		 * Creates a new uninitialized instance.
		 */
		public Proxy() {
			// empty
		}

		public Proxy(final DeliveryMethod value) {
			this.value = value;
		}

		public DeliveryMethod getValue() {
			return this.value;
		}

		public void setValue(final DeliveryMethod value) {
			this.value = value;
		}

		/**
		 * @param orb
		 * @throws IllegalArgumentException
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlDeliveryMethod getIdlTransferable(final ORB orb) {
			try {
				return IdlDeliveryMethod.from_int(this.value.ordinal());
			} catch (final BAD_PARAM bp) {
				throw new IllegalArgumentException(String.valueOf(this.value.ordinal()), bp);
			}
		}

		/**
		 * @param deliveryMethod
		 * @throws IllegalArgumentException
		 * @see IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
		 */
		public void fromIdlTransferable(final IdlDeliveryMethod deliveryMethod) {
			this.value = valueOf(deliveryMethod);
		}
	}
}
