/*-
 * $Id: StorableObjectVersion.java,v 1.8 2005/09/20 06:50:57 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.io.Serializable;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/20 06:50:57 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class StorableObjectVersion implements Cloneable, Serializable {
	private static final long serialVersionUID = -5642797178846561548L;

	public transient static final StorableObjectVersion INITIAL_VERSION = new StorableObjectVersion(0L);
	public transient static final StorableObjectVersion ILLEGAL_VERSION = new StorableObjectVersion(-1L);

	private long version;

	public StorableObjectVersion(final long version) {
		this.version = version;
	}

	public boolean isNewer(final StorableObjectVersion storableObjectVersion) {
		if (Math.abs(this.version - storableObjectVersion.version) < (Long.MAX_VALUE >> 1 - Long.MIN_VALUE >> 1)) {
			return (this.version > storableObjectVersion.version);
		}
		return (this.version < storableObjectVersion.version);
	}

	public final boolean isOlder(final StorableObjectVersion storableObjectVersion) {
		if (Math.abs(this.version - storableObjectVersion.version) < (Long.MAX_VALUE >> 1 - Long.MIN_VALUE >> 1)) {
			return (this.version < storableObjectVersion.version);
		}
		return (this.version > storableObjectVersion.version);
	}

	public void increment() {
		if (this.version < Long.MAX_VALUE) {
			this.version++;
			if (this.equals(ILLEGAL_VERSION)) {
				this.version++;
			}
			if (this.equals((INITIAL_VERSION))) {
				this.version++;
			}
		}
		else {
			this.version = Long.MIN_VALUE;
		}
	}

	public long longValue() {
		return this.version;
	}

	@Override
	public final boolean equals(final Object object) {
		if (this == object) {
			return true;
		}

		if (object instanceof StorableObjectVersion) {
			final StorableObjectVersion storableObjectVersion = (StorableObjectVersion) object;
			return this.version == storableObjectVersion.version;
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return (int) (this.version ^ (this.version >>> 32));
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
		return INITIAL_VERSION.clone();
	}

}
