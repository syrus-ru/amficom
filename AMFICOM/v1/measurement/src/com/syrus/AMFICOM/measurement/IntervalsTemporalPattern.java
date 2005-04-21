/*-
* $Id: IntervalsTemporalPattern.java,v 1.5 2005/04/21 16:17:28 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.InnerDisplacement;
import com.syrus.AMFICOM.measurement.corba.InnerHole;
import com.syrus.AMFICOM.measurement.corba.InnerTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.5 $, $Date: 2005/04/21 16:17:28 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class IntervalsTemporalPattern extends AbstractTemporalPattern {

	private static final long	serialVersionUID	= 3257567312898175032L;
	
	private long period;
	
	/* yeah, Set hole positions in period units , hole is not cunt - it is like empty place */
	private java.util.Set holePositions;
	
	/* displacement from periodical positions 
	 * displacement > 0 - displacement to future position, 
	 * displacement < 0 - displacement to past position
	 * 
	 * Map <Integer number of period, Long displacement>
	 */
	private Map displacements;
	
	/*
	 * Temporal Patterns mapping
	 * 
	 * Map <Integer number of period, Identifier abstractTemporalPatternId>
	 */
	private Map temporalPatterns;
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected IntervalsTemporalPattern(Identifier id, 
	                                   Identifier creatorId, 
	                                   long version, 
	                                   long period, 
	                                   java.util.Set holePositions,
	                                   Map displacements,
	                                   Map temporalPatterns) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);

		this.period = period;
		
		this.setHolePositions0(holePositions);
		this.setDisplacements0(displacements);
		this.setTemporalPatterns0(temporalPatterns);		
		
		this.changed = false;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IntervalsTemporalPattern(IntervalsTemporalPattern_Transferable transferable) {
		try {
			this.fromTransferable(transferable);
		} catch (ApplicationException e) {
			// never occur
			assert false;
		}
	}	
	
	public static IntervalsTemporalPattern createInstance(Identifier creatorId, 
	                                                      long period, 
	                                                      java.util.Set holePositions,
	                                                      Map displacements,
	                                                      Map temporalPatterns) throws CreateObjectException {
		try {
			IntervalsTemporalPattern intervalsTemporalPattern = new IntervalsTemporalPattern(IdentifierPool
					.getGeneratedIdentifier(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE), creatorId, 0L,
																								period, holePositions,
																								displacements,
																								temporalPatterns);
			assert intervalsTemporalPattern.isValid() : ErrorMessages.OBJECT_BADLY_INITIALIZED;
			intervalsTemporalPattern.changed = true;
			return intervalsTemporalPattern;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}

	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		IntervalsTemporalPattern_Transferable itpt = (IntervalsTemporalPattern_Transferable) transferable;

		super.fromTransferable(itpt);

		this.period = itpt.period;

		InnerHole[] ihs = itpt.inner_holes;
		if (this.holePositions == null)
			this.holePositions = new HashSet(ihs.length);
		else
			this.holePositions.clear();
		for (int i = 0; i < ihs.length; i++)
			this.holePositions.add(new Integer(ihs[i].position));

		InnerDisplacement[] ids = itpt.inner_displacements;
		if (this.displacements == null)
			this.displacements = new HashMap(ids.length);
		else
			this.displacements.clear();
		for (int i = 0; i < ids.length; i++)
			this.displacements.put(new Integer(ids[i].position), new Long(ids[i].value));

		InnerTemporalPattern[] itps = itpt.inner_temporal_patterns;
		if (this.temporalPatterns == null)
			this.temporalPatterns = new HashMap(itps.length);
		else
			this.temporalPatterns.clear();
		for (int i = 0; i < itps.length; i++)
			this.temporalPatterns.put(new Integer(itps[i].position), new Identifier(itps[i].temporal_pattern_id));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		super.changed = false;		
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		// TODO Auto-generated method stub
		return super.isValid();
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fillTimes() {
		long time = this.startTime - this.period;
		int periodNumber = 0;
		
		while(time < this.endTime) {
			Integer periodNumberLong = new Integer(periodNumber);
			periodNumber++;
			time += this.period;
			
			if (this.holePositions != null && this.holePositions.contains(periodNumberLong)) {
				continue;
			}
			
			long displacement = 0;
			
			if (this.displacements != null) {
				Long displacementLong = (Long)this.displacements.get(periodNumberLong);
				displacement = displacementLong == null ? 0L : displacementLong.longValue();
			}
			
			AbstractTemporalPattern abstractTemporalPattern = null;
			if (this.temporalPatterns != null) {
				Identifier abstractTemporalPatternId = (Identifier) this.temporalPatterns.get(periodNumberLong);
				if (abstractTemporalPatternId != null && !abstractTemporalPatternId.isVoid()) {
					try {
						abstractTemporalPattern = (AbstractTemporalPattern) MeasurementStorableObjectPool
								.getStorableObject(abstractTemporalPatternId, true);
					} catch (ApplicationException e) {
						// nothing do, just ignore
						Log.errorException(e);
					}
				}
			}
			
			if (abstractTemporalPattern != null) {		
				int nextNotHolePosition = 0;
				while(time + nextNotHolePosition * this.period < this.endTime) {
					Integer nextNotHolePositionInteger = new Integer(periodNumber + nextNotHolePosition);
					if (this.holePositions == null || !this.holePositions.contains(nextNotHolePositionInteger)) {
						break;
					}
					nextNotHolePosition++;
				}
				
				this.times.addAll(abstractTemporalPattern.getTimes(time + displacement , time + displacement + (nextNotHolePosition + 1) * this.period));
			} else {
				this.times.add(new Date(time + displacement));
			}
		}
		
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		InnerHole[] ihs;
		if (this.holePositions != null && !this.holePositions.isEmpty()) {
			ihs = new InnerHole[this.holePositions.size()];
			int i = 0;
			for (Iterator it = this.holePositions.iterator(); it.hasNext(); i++)
				ihs[i] = new InnerHole(((Integer) it.next()).intValue());
		}
		else
			ihs = new InnerHole[0];

		InnerDisplacement[] ids;
		if (this.displacements != null && !this.displacements.isEmpty()) {
			ids = new InnerDisplacement[this.displacements.size()];
			int i = 0;
			for (Iterator it = this.displacements.keySet().iterator(); it.hasNext(); i++) {
				final Integer position = (Integer) it.next();
				final Long value = (Long) this.displacements.get(position);
				ids[i] = new InnerDisplacement(position.intValue(), value.longValue());
			}
		}
		else
			ids = new InnerDisplacement[0];

		InnerTemporalPattern[] itps;
		if (this.temporalPatterns != null && !this.temporalPatterns.isEmpty()) {
			itps = new InnerTemporalPattern[this.temporalPatterns.size()];
			int i = 0;
			for (Iterator it = this.temporalPatterns.keySet().iterator(); it.hasNext(); i++) {
				final Integer position = (Integer) it.next();
				final Identifier temporalPatternId = (Identifier) this.temporalPatterns.get(position);
				itps[i] = new InnerTemporalPattern(position.intValue(), (Identifier_Transferable) temporalPatternId.getTransferable());
			}
		}
		else
			itps = new InnerTemporalPattern[0];

		return new IntervalsTemporalPattern_Transferable(super.getHeaderTransferable(), this.period, ihs, ids, itps);
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		if (this.temporalPatterns == null || this.temporalPatterns.isEmpty())
			return Collections.EMPTY_SET;
		
		java.util.Set dependencies = new HashSet();
		for (Iterator it = this.temporalPatterns.keySet().iterator(); it.hasNext();) {
			Identifier identifier = (Identifier) this.temporalPatterns.get(it.next());
			if (!identifier.isVoid()) {
				dependencies.add(identifier);
			}
		}
		return dependencies;
	}
	
	public Map getDisplacements() {
		return Collections.unmodifiableMap(this.displacements);
	}

	public void setDisplacement(int periodNumber, long displacement) {
		this.setDisplacement0(new Integer(periodNumber), new Long(displacement));
		super.changed = true;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDisplacement0(Integer periodNumber, Long displacement) {
		if (this.holePositions != null && this.holePositions.contains(periodNumber)) {
			assert false : "Masha, what the hell you need black velvet shoes for ?"; //$NON-NLS-1$
		} else {
			if (this.displacements == null) {
				this.displacements = new HashMap();
			}
			
			this.displacements.put(periodNumber, displacement);
		}
	}
	
	public void setDisplacements(Map displacements) {
		this.setDisplacements0(displacements);
		this.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDisplacements0(Map displacements) {
		if (this.displacements == null) {
			this.displacements = new HashMap();
		} else {
			this.displacements.clear();
		}
		if (displacements != null) {
			for (Iterator it = displacements.keySet().iterator(); it.hasNext();) {
				Integer periodNumber = (Integer) it.next();
				Long displacement = (Long) displacements.get(periodNumber);
				
				this.setDisplacement0(periodNumber, displacement);
			}
			this.displacements.putAll(displacements);
		}
	}

	public java.util.Set getHolePositions() {
		return Collections.unmodifiableSet(this.holePositions);
	}
	
	public void setHolePosition(int holePosition) {
		this.setHolePosition0(new Integer(holePosition));
		super.changed = true;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setHolePosition0(Integer holePosition) {
		if (this.displacements != null && this.displacements.keySet().contains(holePosition)) {
			this.displacements.remove(holePosition);
		}
		
		if (this.temporalPatterns != null && this.temporalPatterns.keySet().contains(holePosition)) {
			this.temporalPatterns.remove(holePosition);
		}
		
		if (this.holePositions == null) {
			this.holePositions = new HashSet();
		}
		
		this.holePositions.add(holePosition);
	}

	public void setHolePositions(java.util.Set holePositions) {
		this.setHolePositions0(holePositions);
		this.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setHolePositions0(java.util.Set holePositions) {
		if (this.holePositions == null) {
			this.holePositions = new HashSet();
		} else {
			this.holePositions.clear();
		}
		if (holePositions != null) {
			for (Iterator it = holePositions.iterator(); it.hasNext();) {
				Integer holePosition = (Integer) it.next();
				this.setHolePosition0(holePosition);
			}
			
		}
	}

	public long getPeriod() {
		return this.period;
	}

	public void setPeriod(long period) {
		this.setPeriod0(period);
		this.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setPeriod0(long period) {
		this.period = period;
	}

	public Map getTemporalPatterns() {
		return Collections.unmodifiableMap(this.temporalPatterns);
	}
	
	public void addTemporalPattern(int periodNumber, Identifier temporalPatternId) {
		this.addTemporalPattern0(new Integer(periodNumber), temporalPatternId);
		super.changed = true;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void addTemporalPattern0(Integer periodNumber, Identifier temporalPatternId) {
		if (this.holePositions != null && this.holePositions.contains(periodNumber)) {
			this.holePositions.remove(periodNumber);
		}
		
		if (this.temporalPatterns == null) {
			this.temporalPatterns = new HashMap();
		}
		
		if (!this.temporalPatterns.keySet().contains(periodNumber)) {
			this.temporalPatterns.put(periodNumber, temporalPatternId);
		} else {
			assert false : "Cannot put temporal pattern to exists temporal pattern";
		}
	}

	public void setTemporalPatterns(Map temporalPatterns) {
		this.setTemporalPatterns(temporalPatterns);
		this.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setTemporalPatterns0(Map temporalPatterns) {
		if (this.temporalPatterns == null) {
			this.temporalPatterns = new HashMap();
		} else {
			this.temporalPatterns.clear();
		}
		if (temporalPatterns != null) {
			for (Iterator iter = temporalPatterns.keySet().iterator(); iter.hasNext();) {
				Integer periodNumber = (Integer) iter.next();
				Identifier temporalPatternId = (Identifier)temporalPatterns.get(periodNumber);  
				this.addTemporalPattern0(periodNumber, temporalPatternId);
			}
		}
	}
	
	
}

