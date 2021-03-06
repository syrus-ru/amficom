/*-
 * $Id: GeneralTimeLabel.java,v 1.2 2006/03/07 09:41:48 saa Exp $
 * 
 * Copyright ? 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.validator;

import java.util.Set;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/03/07 09:41:48 $
 * @module
 */
public class GeneralTimeLabel extends TimeLabel {
	private final Set<Range> ranges;

	protected GeneralTimeLabel(final Set<Range> ranges) {
		this.ranges = ranges;
	}

	@Override
	public boolean intersects(final long beginInc, final long endInc) {
		for (final Range r: this.ranges) {
			if (r.intersects(beginInc, endInc)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean intersects(final TimeLabel that) {
		for (final Range r: this.ranges) {
			if (that.intersects(r)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected long rangeIntersectDifficulty() {
		return this.ranges.size();
	}

	@Override
	protected long patternIntersectDifficulty(final TimeLabel that) {
		return that.rangeIntersectDifficulty() * this.ranges.size();
	}
}
