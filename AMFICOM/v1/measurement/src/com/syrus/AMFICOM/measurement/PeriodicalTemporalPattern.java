/*-
* $Id: PeriodicalTemporalPattern.java,v 1.13 2005/06/25 17:07:41 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPattern;
import com.syrus.AMFICOM.resource.LangModelMeasurement;


/**
 * @version $Revision: 1.13 $, $Date: 2005/06/25 17:07:41 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public final class PeriodicalTemporalPattern extends AbstractTemporalPattern {

	private static final long	serialVersionUID	= 3257567312898175032L;
	
	private long period;	
	
	private static final long	SECOND_LONG	= 1000;
	private static final long	MINUTE_LONG	= 60 * SECOND_LONG;
	private static final long	HOUR_LONG	= 60 * MINUTE_LONG;
	private static final long	DAY_LONG	= 24 * HOUR_LONG;

	private static final String	I18N_KEY_MIN		= "min";
	private static final String	I18N_KEY_HOUR		= "hour";
	private static final String	I18N_KEY_DAYS		= "days";

	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	PeriodicalTemporalPattern(final Identifier id, final Identifier creatorId, final long version, final long period) {
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
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlPeriodicalTemporalPattern ptpt = (IdlPeriodicalTemporalPattern)transferable;
		super.fromTransferable(ptpt.header);
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
			PeriodicalTemporalPattern periodicalTemporalPattern = new PeriodicalTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE),
					creatorId,
					0L,
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
	protected void fillTimes() {
		long time = this.startTime;
		for(long i=0; time <= this.endTime; i++) {
			final Date date = new Date(time);
			this.times.add(date);
			time += this.period;
		}
	}		
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlPeriodicalTemporalPattern getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new IdlPeriodicalTemporalPattern(super.getHeaderTransferable(orb), this.period);
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
			buffer.append(LangModelMeasurement.getString(I18N_KEY_DAYS));
		}
		
		if (hours > 0) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(hours);
			buffer.append(LangModelMeasurement.getString(I18N_KEY_HOUR));
		}
		
		if (mins > 0) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(mins);
			buffer.append(LangModelMeasurement.getString(I18N_KEY_MIN));
		}
		return buffer.toString();
	}
	
	public void setPeriod(final long period) {
		if (super.times != null) {
			super.times.clear();
		}
		this.period = period;
		super.markAsChanged();
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final long period) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.period = period;
	}

}

