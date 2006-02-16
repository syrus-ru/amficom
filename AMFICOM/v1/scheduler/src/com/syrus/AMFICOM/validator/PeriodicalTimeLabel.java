/*-
 * $Id: PeriodicalTimeLabel.java,v 1.1 2006/02/16 12:22:45 bob Exp $
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
public class PeriodicalTimeLabel extends TimeLabel {
	private long firstTimeBegin;
	private long eachTimeDuration;
	private long period;
	private int numberOfTimes;

	public PeriodicalTimeLabel(long firstTimeBegin,
			long eachTimeDuration,
			long period,
			int numberOfTimes) {
		assert (eachTimeDuration < period);
		assert (eachTimeDuration >= 0);
		assert (numberOfTimes >= 0);
		this.firstTimeBegin = firstTimeBegin;
		this.eachTimeDuration = eachTimeDuration;
		this.period = period;
		this.numberOfTimes = numberOfTimes;
	}	

	@Override
	public boolean intersects(long beginInc, long endInc) {
		assert(endInc >= beginInc);

		if (beginInc < this.firstTimeBegin) {
			return endInc >= this.firstTimeBegin;
		}
		long beginOffset = beginInc - this.firstTimeBegin; // >= 0
		long endOffset = endInc - this.firstTimeBegin; // >= 0
		long beginId = beginOffset / this.period;
		long beginShift = beginOffset % this.period;
		if(beginId >= this.numberOfTimes) {
			return false; // starts surely after the end
		}
		if (beginShift <= this.eachTimeDuration) {
			return true; // begin is inside some range
		}
		long endId = endOffset / this.period;
		if (endId > beginId) {
			return true;
		}
		return false;
	}

	@Override
	public boolean intersects(TimeLabel that) {
		if (that instanceof PeriodicalTimeLabel && false) {
			return specialIntersect((PeriodicalTimeLabel)that);
		}
		long begin = this.firstTimeBegin;
		for (int i = 0; i < this.numberOfTimes; i++) {
			if (that.intersects(begin, begin + this.eachTimeDuration)) {
				return true;
			}
			begin += this.period;
		}
		return false;
	}

	@Override
	protected long rangeIntersectDifficulty() {
		return 3; // estimation of difficulty
	}

	@Override
	protected long patternIntersectDifficulty(TimeLabel that) {
		if (that instanceof PeriodicalTimeLabel && false) {
			return specialIntersectDifficulty((PeriodicalTimeLabel)that);
		}
		return this.numberOfTimes * that.rangeIntersectDifficulty();
	}
	
	@SuppressWarnings("unused")
	private boolean specialIntersect(PeriodicalTimeLabel pattern) {
		// todo: implement smart math :)
		throw new InternalError();
	}
	
	@SuppressWarnings("unused")
	private long specialIntersectDifficulty(PeriodicalTimeLabel pattern) {
		// todo: implement smart math :)
		throw new InternalError();
	}
}
