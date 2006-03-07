/*-
 * $Id: TimeLabel.java,v 1.2 2006/03/07 09:41:48 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.validator;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/03/07 09:41:48 $
 * @module
 */
public abstract class TimeLabel {
	public abstract boolean intersects(long beginInc, long endInc);

	public abstract boolean intersects(TimeLabel that);

	protected abstract long rangeIntersectDifficulty();

	protected abstract long patternIntersectDifficulty(TimeLabel that);

	protected final boolean intersects(Range range) {
		return intersects(range.getBegin(), range.getEnd());
	}

	public static boolean patternsIntersect(TimeLabel pattern1,
			TimeLabel pattern2) {
		if (pattern1.patternIntersectDifficulty(pattern2) <
				pattern2.patternIntersectDifficulty(pattern1)) {
			return pattern1.intersects(pattern2);
		}
		return pattern2.intersects(pattern1);
	}
}
