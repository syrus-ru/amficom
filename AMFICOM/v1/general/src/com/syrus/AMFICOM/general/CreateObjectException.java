/*
 * $Id: CreateObjectException.java,v 1.6.2.1 2006/03/15 13:28:07 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.6.2.1 $, $Date: 2006/03/15 13:28:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class CreateObjectException extends DatabaseException
		implements IdlTransferableObjectExt<IdlCreateObjectException> {
	private static final long serialVersionUID = -1884812292291066856L;

	public CreateObjectException(String message) {
		super(message);
	}

	public CreateObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreateObjectException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param transferable
	 * @throws IdlConversionException
	 * @see IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
	 */
	public void fromIdlTransferable(final IdlCreateObjectException transferable)
	throws IdlConversionException {
		final Throwable cause = this.getCause();
		if (cause == null) {
			this.initCause(transferable);
		} else {
			this.setStackTrace(transferable.getStackTrace());
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlCreateObjectException getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlCreateObjectException getIdlTransferable() {
		final IdlCreateObjectException coe = new IdlCreateObjectException(this.getLocalizedMessage());
		coe.setStackTrace(this.getStackTrace());
		return coe;
	}
}
