/*
 * $Id: VersionCollisionException.java,v 1.9 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
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
	public VersionCollisionException(final String message, final long desiredVersion, final long actualVersion) {
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
	public VersionCollisionException(final String message,
			final Throwable cause,
			final long desiredVersion,
			final long actualVersion) {
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
	public VersionCollisionException(final Throwable cause, final long desiredVersion, final long actualVersion) {
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

	@Override
	public String getMessage() {
		return super.getMessage() + "; desired version: " + this.desiredVersion + ", actual version: " + this.actualVersion;
	}
}
