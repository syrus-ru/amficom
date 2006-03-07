/*-
 * $Id: Range.java,v 1.2 2006/03/07 09:41:48 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.validator;


/**
 * Represents []-like range. Non-empty.
 * Immutable.
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/03/07 09:41:48 $
 * @module
 */
public final class Range {
	private final long begin; 
	private final long end;
	
	/**
	 * @param begin inclusive begin
	 * @param end inclusive end
	 */
	public Range(final long begin, final long end) {
		assert begin <= end;
		this.begin = begin;
		this.end = end;
	}
	
	public boolean intersects(final Range that) {
		return this.begin <= that.end
			&& that.begin <= this.end;
	}
	
	/**
	 * @param beginInc inclusive begin
	 * @param endInc inclusive end
	 * @return true if this intersects [beginInc, endInc]
	 */
	public boolean intersects(final long beginInc, final long endInc) {
		return this.begin <= endInc
			&& beginInc <= this.end;
	}
	
	public long getBegin() {
		return this.begin;
	}
	
	public long getEnd() {
		return this.end;
	}
}
