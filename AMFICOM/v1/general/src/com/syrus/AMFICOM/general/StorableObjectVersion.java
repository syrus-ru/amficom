/*-
 * $Id: StorableObjectVersion.java,v 1.5 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.io.Serializable;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class StorableObjectVersion implements Cloneable, Serializable {
	private static final long serialVersionUID = -5642797178846561548L;

	public static final StorableObjectVersion ILLEGAL_VERSION = new StorableObjectVersion(-1L);

	private long version;

	public StorableObjectVersion(final long version) {
		this.version = version;
	}

	public boolean isNewer(final StorableObjectVersion storableObjectVersion) {
		if (Math.abs(this.version - storableObjectVersion.version) < (Long.MAX_VALUE >> 1 - Long.MIN_VALUE >> 1))
			return (this.version > storableObjectVersion.version);
		return (this.version < storableObjectVersion.version);
	}

	public final boolean isOlder(final StorableObjectVersion storableObjectVersion) {
		if (Math.abs(this.version - storableObjectVersion.version) < (Long.MAX_VALUE >> 1 - Long.MIN_VALUE >> 1))
			return (this.version < storableObjectVersion.version);
		return (this.version > storableObjectVersion.version);
	}

	public void increment() {
		if (this.version < Long.MAX_VALUE) {
			this.version++;
			if (this.equals(ILLEGAL_VERSION))
				this.version++;
		}
		else {
			this.version = Long.MIN_VALUE;
		}
	}

	public long longValue() {
		return this.version;
	}

	@Override
	public boolean equals(final Object object) {
		if (super.equals(object)) {
			return true;
		}

		if (object instanceof StorableObjectVersion) {
			final StorableObjectVersion storableObjectVersion = (StorableObjectVersion) object;
			return this.version == storableObjectVersion.version;
		}
		return false;
	}

	@Override
	public StorableObjectVersion clone() {
		return new StorableObjectVersion(this.version);
	}

	@Override
	public String toString() {
		return Long.toString(this.version);
	}

	public static StorableObjectVersion createInitial() {
		return new StorableObjectVersion(0L);
	}

}
