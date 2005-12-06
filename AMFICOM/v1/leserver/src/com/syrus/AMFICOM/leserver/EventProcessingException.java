/*-
 * $Id: EventProcessingException.java,v 1.3 2005/12/06 09:43:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.IdlTransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/12/06 09:43:07 $
 * @module leserver
 */
final class EventProcessingException extends Exception
		implements IdlTransferableObject<IdlEventProcessingException> {
	private static final long serialVersionUID = 5663391392400492553L;

	EventProcessingException() {
		// empty
	}

	/**
	 * @param message
	 * @param cause
	 */
	EventProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	EventProcessingException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	EventProcessingException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param orb
	 * @see IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlEventProcessingException getIdlTransferable(final ORB orb) {
		return new IdlEventProcessingException(this.getMessage());
	}
}
