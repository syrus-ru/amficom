/*-
 * $Id: StorableObjectVersion.java,v 1.10 2005/12/02 09:44:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.io.Serializable;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/02 09:44:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class StorableObjectVersion implements Serializable {
	private static final long serialVersionUID = 4373418889318876641L;

	public transient static final StorableObjectVersion INITIAL_VERSION = new StorableObjectVersion(0L);
	public transient static final StorableObjectVersion ILLEGAL_VERSION = new StorableObjectVersion(-1L);

	private final long version;

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

	public StorableObjectVersion increment() {
		if (this.version < Long.MAX_VALUE) {
			long newVersion = this.version + 1;
			if (newVersion == ILLEGAL_VERSION.longValue()) {
				newVersion++;
			}
			if (newVersion == INITIAL_VERSION.longValue()) {
				newVersion++;
			}
			return new StorableObjectVersion(newVersion);
		}
		return new StorableObjectVersion(Long.MIN_VALUE);
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

	/**
	 * @deprecated as unnecessary
	 */
	@Override
	@Deprecated
	public StorableObjectVersion clone() {
		return new StorableObjectVersion(this.version);
	}

	
	@Override
	public String toString() {
		return Long.toString(this.version);
	}

	/**
	 * @deprecated use direct {@link #INITIAL_VERSION} 
	 */
	@Deprecated
	public static StorableObjectVersion createInitial() {
		return INITIAL_VERSION;
	}

}
