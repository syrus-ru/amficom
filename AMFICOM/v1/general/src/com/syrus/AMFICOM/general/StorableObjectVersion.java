/*-
 * $Id: StorableObjectVersion.java,v 1.12.2.1 2006/06/07 08:18:10 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

/**
 * @version $Revision: 1.12.2.1 $, $Date: 2006/06/07 08:18:10 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class StorableObjectVersion implements Serializable, Comparable<StorableObjectVersion> {
	private static final long serialVersionUID = 5911000938457805521L;

	/**
	 * Corresponds to {@link #ILLEGAL_VERSION}
	 */
	private static final int MIN_CACHED_VERSION = -1;

	private static final int MAX_CACHED_VERSION = 256;
	
	public static final StorableObjectVersion INITIAL_VERSION = valueOf(0L);
	public static final StorableObjectVersion ILLEGAL_VERSION = valueOf(-1L); 

	private final long version;

	StorableObjectVersion(final long version) {
		this.version = version;
	}

	public boolean isNewer(final StorableObjectVersion that) {
		return (Math.abs(this.version - that.version) < (Long.MAX_VALUE >> 1 - Long.MIN_VALUE >> 1))
			? (this.version > that.version)
			: (this.version < that.version);
	}

	public final boolean isOlder(final StorableObjectVersion that) {
		return that.isNewer(this);
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
			return valueOf(newVersion);
		}
		return valueOf(Long.MIN_VALUE);
	}

	public long longValue() {
		return this.version;
	}

	@Override
	public final boolean equals(final Object object) {
		return object == this || (object instanceof StorableObjectVersion
				&& this.version == ((StorableObjectVersion) object).version);
	}

	@Override
	public final int hashCode() {
		return (int) (this.version ^ (this.version >>> 32));
	}

	@Override
	public String toString() {
		if (this.equals(ILLEGAL_VERSION)) {
			return Long.toString(this.version) + " (ILLEGAL)";
		}
		if (this.equals(INITIAL_VERSION)) {
			return Long.toString(this.version) + " (INITIAL)";
		}
		return Long.toString(this.version);
	}

	public int compareTo(final StorableObjectVersion that) {
		return this.isOlder(that)
				? -1
				: this.equals(that) ? 0 : 1;
	}

	public static StorableObjectVersion valueOf(final long l) {
		return l >= MIN_CACHED_VERSION && l <= MAX_CACHED_VERSION
				? VersionCache.cache[(int) l - MIN_CACHED_VERSION]
				: new StorableObjectVersion(l);
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @спизжено_у Josh Bloch
	 */
	private static class VersionCache {
		private VersionCache() {
			assert false;
		}

		static final StorableObjectVersion cache[] = new StorableObjectVersion[MAX_CACHED_VERSION - MIN_CACHED_VERSION + 1];

		static {
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new StorableObjectVersion(i + MIN_CACHED_VERSION);
			}
		}
	}
}
