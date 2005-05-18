/*
 * $Id: VersionCollisionException.java,v 1.6 2005/05/18 11:07:38 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/18 11:07:38 $
 * @author $Author: bass $
 * @module general_v1
 */

public class VersionCollisionException extends ApplicationException {
	static final long serialVersionUID = -3847337178607890353L;

	private long desiredVersion;
	private long actualVersion;

	/**
	 *
	 * @param message
	 * @param desiredVersion
	 * @param actualVersion
	 */
	public VersionCollisionException(String message, long desiredVersion, long actualVersion) {
		super(message);
		this.desiredVersion = desiredVersion;
		this.actualVersion = actualVersion;
	}

	/**
	 *
	 * @param message
	 * @param cause
	 * @param desiredVersion
	 * @param actualVersion
	 */
	public VersionCollisionException(String message, Throwable cause, long desiredVersion, long actualVersion) {
		super(message, cause);
		this.desiredVersion = desiredVersion;
		this.actualVersion = actualVersion;
	}

	/**
	 *
	 * @param cause
	 * @param desiredVersion
	 * @param actualVersion
	 */
	public VersionCollisionException(Throwable cause, long desiredVersion, long actualVersion) {
		super(cause);
		this.desiredVersion = desiredVersion;
		this.actualVersion = actualVersion;
	}

	/**
	 *
	 * @return desired version
	 */
	public long getDesiredVersion() {
		return this.desiredVersion;
	}

	/**
	 *
	 * @return actual version
	 */
	public long getActualVersion() {
		return this.actualVersion;
	}
}
