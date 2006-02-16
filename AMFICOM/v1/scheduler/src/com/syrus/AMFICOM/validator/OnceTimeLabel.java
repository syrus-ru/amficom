/*-
 * $Id: OnceTimeLabel.java,v 1.1 2006/02/16 12:22:45 bob Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.validator;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2006/02/16 12:22:45 $
 * @module
 */
public class OnceTimeLabel extends TimeLabel {
	private Range range;

	public OnceTimeLabel(Range range) {
		this.range = range; // Range is immutable
	}

	@Override
	public boolean intersects(long beginInc, long endInc) {
		return this.range.intersects(beginInc, endInc);
	}

	@Override
	public boolean intersects(TimeLabel that) {
		return that.intersects(this.range);
	}
	@Override
	public long rangeIntersectDifficulty() {
		return 1; // one check required
	}

	@Override
	protected long patternIntersectDifficulty(TimeLabel that) {
		return that.rangeIntersectDifficulty();
	}

}
