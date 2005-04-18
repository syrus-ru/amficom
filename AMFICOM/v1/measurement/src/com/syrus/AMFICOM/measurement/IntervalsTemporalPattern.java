/*-
* $Id: IntervalsTemporalPattern.java,v 1.1 2005/04/18 15:14:27 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/04/18 15:14:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class IntervalsTemporalPattern extends AbstractTemporalPattern {

	private static final long	serialVersionUID	= 3257567312898175032L;
	
	/** SortedMap<Long seconds, Identifier<AbstractTemporalPattern>> */
	private SortedMap intervalsAbstractTemporalPatternMap;	

	
	protected IntervalsTemporalPattern(Identifier id, Identifier creatorId, long version, SortedMap intervalsAbstractTemporalPatternMap) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);
		this.intervalsAbstractTemporalPatternMap = new TreeMap();
		this.setIntervalsAbstractTemporalPatternMap0(intervalsAbstractTemporalPatternMap);		
		this.changed = false;
	}	
	
	public IntervalsTemporalPattern(IntervalsTemporalPattern_Transferable itpt) throws CreateObjectException {
		this.intervalsAbstractTemporalPatternMap = new TreeMap();
		
		try {
			this.fromTransferable(itpt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		IntervalsTemporalPattern_Transferable itpt = (IntervalsTemporalPattern_Transferable)transferable;
		super.fromTransferable(itpt.header);

		SortedMap map = new TreeMap();
		
		assert itpt.seconds.length != itpt.temporal_patterns_ids.length : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		for (int i = 0; i < itpt.seconds.length; i++) {
			Identifier identifier = new Identifier(itpt.temporal_patterns_ids[i]);
			map.put(new Long(itpt.seconds[i]), identifier);
		}
		
		this.setIntervalsAbstractTemporalPatternMap0(map);

		this.changed = false;	
	}


	protected boolean isValid() {
		return super.isValid() && this.intervalsAbstractTemporalPatternMap != null;
	}
	
	protected void fillTimes() {
		if (this.intervalsAbstractTemporalPatternMap != null) {
			Date previousDate = new Date(this.startTime);
			Date startDate = previousDate;
			for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long seconds = (Long) it.next();
				Identifier abstractTemporalPatternId = (Identifier)this.intervalsAbstractTemporalPatternMap.get(seconds);
				AbstractTemporalPattern temporalPattern = null;
				if (abstractTemporalPatternId != null && !abstractTemporalPatternId.isVoid()) {
					try {
						temporalPattern = (AbstractTemporalPattern) MeasurementStorableObjectPool.getStorableObject(
							abstractTemporalPatternId, true);
					} catch (ApplicationException e) {
						// nothing do, just ignore temporal pattern
						Log.errorException(e);
					}
				}
				
				Date localEndTime = new Date(startDate.getTime() + seconds.longValue() * 1000L);
				
				if (temporalPattern == null) {
					super.times.add(previousDate);
				} else {
					super.times.add(temporalPattern.getTimes(previousDate, localEndTime));
				}
				
				previousDate = localEndTime;

			}
		}
		
	}

	/**
	 * @param intervalsAbstractTemporalPatternMap2
	 */
	protected void setIntervalsAbstractTemporalPatternMap0(SortedMap intervalsAbstractTemporalPatternMap2) {
		this.intervalsAbstractTemporalPatternMap.clear();
		if (intervalsAbstractTemporalPatternMap2 != null)
			this.intervalsAbstractTemporalPatternMap.putAll(intervalsAbstractTemporalPatternMap2);
		
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		java.util.Set dependencies = new HashSet();
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
			Identifier abstractTemporalPatternId = (Identifier)this.intervalsAbstractTemporalPatternMap.get(it.next());
			dependencies.add(abstractTemporalPatternId);
		}
		return dependencies;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		long[] seconds = new long[this.intervalsAbstractTemporalPatternMap.size()];
		Identifier_Transferable[] temporalPatternIds = new Identifier_Transferable[seconds.length];
		
		int i = 0;
		
		for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();i++) {
			Long second = (Long) it.next();
			Identifier abstractTemporalPatternId = (Identifier)this.intervalsAbstractTemporalPatternMap.get(seconds);
			seconds[i] = second.longValue();
			temporalPatternIds[i] = (Identifier_Transferable) abstractTemporalPatternId.getTransferable();
		}
		
		return new IntervalsTemporalPattern_Transferable(super.getHeaderTransferable(), seconds, temporalPatternIds);
	}

}

