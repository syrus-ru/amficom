/*-
* $Id: PeriodicalTemporalPattern.java,v 1.29 2006/01/30 11:20:49 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPatternHelper;


/**
 * @version $Revision: 1.29 $, $Date: 2006/01/30 11:20:49 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public final class PeriodicalTemporalPattern
		extends AbstractTemporalPattern<PeriodicalTemporalPattern> {

	private static final long serialVersionUID = 3257567312898175032L;

	private long period;

	private static final long SECOND_LONG = 1000;
	private static final long MINUTE_LONG = 60 * SECOND_LONG;
	private static final long HOUR_LONG = 60 * MINUTE_LONG;
	private static final long DAY_LONG = 24 * HOUR_LONG;

	private static final String I18N_KEY_MIN = "min";
	private static final String I18N_KEY_HOUR = "hour";
	private static final String I18N_KEY_DAYS = "days";

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	PeriodicalTemporalPattern(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final long period) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.period = period;
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public PeriodicalTemporalPattern(final IdlPeriodicalTemporalPattern itpt) throws CreateObjectException {
		try {
			this.fromTransferable(itpt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPeriodicalTemporalPattern ptpt = (IdlPeriodicalTemporalPattern)transferable;
		super.fromTransferable(ptpt);
		this.period = ptpt.period;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}	
	
	/**
	 * create new instance for client
	 * @param creatorId creator id
	 * @param period period in milliseconds
	 */
	public static PeriodicalTemporalPattern createInstance(final Identifier creatorId, final long period)
			throws CreateObjectException {

		try {
			final PeriodicalTemporalPattern periodicalTemporalPattern = new PeriodicalTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					period);

			assert periodicalTemporalPattern.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			periodicalTemporalPattern.markAsChanged();

			return periodicalTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.period > 0;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fillTimes() {
		try {
			throw new Exception("PeriodicalTemporalPattern.fillTimes");
		} catch (final Exception e) {
			e.printStackTrace();
		}
		

		if (this.period > this.endTime - this.startTime) {
			return;
		}
		for (long time = this.startTime; time <= this.endTime; time += this.period) {
			this.times.add(new Date(time));
		}
	}
	
	@Override
	public final SortedSet<Date> getTimes(final Date start,
		final Date end,
		final Date startInterval,
		final Date endInterval) {
		
		if (start.compareTo(end) >= 0) {
			throw new IllegalArgumentException("Start date later than end date");
		}
		
		if (startInterval.compareTo(endInterval) >= 0) {
			throw new IllegalArgumentException("Start interval date later than end interval date");
		}
		
		final long start0 = start.getTime();
		
		final long end0 = endInterval.compareTo(end) < 0 ? 
				endInterval.getTime() : 
				end.getTime();
		
		final long startTime0 = start.compareTo(startInterval) < 0 ? 
				start0 + this.period * (1 + (int)(startInterval.getTime() - start0) / this.period) :
				start0;
				
		final SortedSet<Date> times1 = new TreeSet<Date>();
		for(long time = startTime0; time <= end0; time += this.period) {
			times1.add(new Date(time));
		}
		
		return times1;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlPeriodicalTemporalPattern getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return IdlPeriodicalTemporalPatternHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.period);
	}

	
	public long getPeriod() {
		return this.period;
	}
	
	public String getPeriodDescription() {
		StringBuffer buffer = new StringBuffer();
		long period1 = this.period;
		
		int days = (int) (period1 / DAY_LONG);
		period1 -= days * DAY_LONG;
		int hours = (int) (period1 / HOUR_LONG);
		period1 -= hours * HOUR_LONG;
		int mins = (int) (period1 / MINUTE_LONG);
		
		if (days > 0) {
			buffer.append(days);
			buffer.append(" ");
			buffer.append(LangModelMeasurement.getString(I18N_KEY_DAYS));
		}
		
		if (hours > 0) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(hours);
			buffer.append(" ");
			buffer.append(LangModelMeasurement.getString(I18N_KEY_HOUR));
		}
		
		if (mins > 0) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(mins);
			buffer.append(" ");
			buffer.append(LangModelMeasurement.getString(I18N_KEY_MIN));
		}
		return buffer.toString();
	}
	
	public void setPeriod(final long period) {
		if (this.period != period) {
			super.times = null;
			this.period = period;
			super.markAsChanged();
		}
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final long period) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.period = period;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PeriodicalTemporalPatternWrapper getWrapper() {
		return PeriodicalTemporalPatternWrapper.getInstance();
	}
}
