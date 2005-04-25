/*-
* $Id: PeriodicalTemporalPattern.java,v 1.2 2005/04/25 08:20:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;


/**
 * @version $Revision: 1.2 $, $Date: 2005/04/25 08:20:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class PeriodicalTemporalPattern extends AbstractTemporalPattern {

	private static final long	serialVersionUID	= 3257567312898175032L;
	
	private long period;	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected PeriodicalTemporalPattern(Identifier id, Identifier creatorId, long version, long period) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.period = period;
		this.changed = false;
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public PeriodicalTemporalPattern(PeriodicalTemporalPattern_Transferable itpt) throws CreateObjectException {
		try {
			this.fromTransferable(itpt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		PeriodicalTemporalPattern_Transferable ptpt = (PeriodicalTemporalPattern_Transferable)transferable;
		super.fromTransferable(ptpt.header);
		this.period = ptpt.period;		
		this.changed = false;	
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}	
	
	/**
	 * create new instance for client
	 * @param creatorId creator id 
	 * @param period period in milliseconds
	 */
	public static PeriodicalTemporalPattern createInstance(	Identifier creatorId,
															long period) throws CreateObjectException {

		try {
			PeriodicalTemporalPattern periodicalTemporalPattern = 
				new PeriodicalTemporalPattern(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE), 
					creatorId, 
					0L, 
					period);
			periodicalTemporalPattern.changed = true;
			
			assert periodicalTemporalPattern.isValid() : ErrorMessages.OBJECT_NOT_INITIALIZED;			
			
			return periodicalTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.period > 0;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fillTimes() {
		long time = this.startTime;
		for(long i=0; time <= this.endTime; i++) {
			Date date = new Date(time);
			this.times.add(date);
			time += this.period;
		}
	}		
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new PeriodicalTemporalPattern_Transferable(super.getHeaderTransferable(), this.period);
	}

	
	public long getPeriod() {
		return this.period;
	}
	
	public void setPeriod(long period) {
		if (super.times != null) {
			super.times.clear();
		}
		this.period = period;
		super.changed = true;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			long period) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.period = period;
	}

}

