/*
 * $Id: Analysis.java,v 1.32 2004/12/06 10:59:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.32 $, $Date: 2004/12/06 10:59:15 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Analysis extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3979266967062721849L;

	private Set criteriaSet;

	private StorableObjectDatabase analysisDatabase;

	public Analysis(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide){
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public short getEntityCode() {
        return ObjectEntities.ANALYSIS_ENTITY_CODE;
    }
    
    public Analysis(Analysis_Transferable at) throws CreateObjectException {
		super(at.header,
			  null,
			  new Identifier(at.monitored_element_id));
		try {
			super.type = (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(at.type_id), true);

			this.criteriaSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(at.criteria_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Analysis(Identifier id,
					   Identifier creatorId,
					   AnalysisType type,
					   Identifier monitoredElementId,
					   Set criteriaSet) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.type = type;
		super.monitoredElementId = monitoredElementId;
	
		this.criteriaSet = criteriaSet;

		super.currentVersion = super.getNextVersion();
		
		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;

	}

	public Object getTransferable() {
		return new Analysis_Transferable(super.getHeaderTransferable(),
			(Identifier_Transferable)super.type.getId().getTransferable(),
			(Identifier_Transferable)super.monitoredElementId.getTransferable(),
			new String(super.type.getCodename()),
			(Identifier_Transferable)this.criteriaSet.getId().getTransferable());
	}

	public Set getCriteriaSet() {
		return this.criteriaSet;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  AnalysisType type,
											  Identifier monitoredElementId,
											  Set criteriaSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												type,
												monitoredElementId);
		this.criteriaSet = criteriaSet;
	}

	public Result createResult(Identifier creatorId,
							   Measurement measurement,
							   AlarmLevel alarmLevel,
							   SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(creatorId,
			measurement,
			this,
			ResultSort.RESULT_SORT_ANALYSIS,
			alarmLevel,
			parameters);
	}
	
	/** 
	 * @deprecated as unsupport method
	 */
	public Result createResult(Identifier creatorId, SetParameter[] parameters)
			throws CreateObjectException {
		throw new UnsupportedOperationException("method isn't support");
	}

	public static Analysis createInstance(Identifier creatorId,
										  AnalysisType type,
										  Identifier monitoredElementId,
										  Set criteriaSet) throws CreateObjectException {
		return new Analysis(IdentifierPool.generateId(ObjectEntities.ANALYSIS_ENTITY_CODE),
			creatorId,
			type,
			monitoredElementId,
			criteriaSet);
	}
	
	public static Analysis getInstance(Analysis_Transferable at) throws CreateObjectException {
		Analysis analysis = new Analysis(at);
		
		analysis.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			if (analysis.analysisDatabase != null)
				analysis.analysisDatabase.insert(analysis);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		return analysis;
	}	
	
	public List getDependencies() {		
		return Collections.singletonList(this.criteriaSet);
	}
}
