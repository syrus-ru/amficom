/*
 * $Id: Analysis.java,v 1.24 2004/09/01 15:08:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
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
 * @version $Revision: 1.24 $, $Date: 2004/09/01 15:08:10 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Analysis extends Action {
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

	public Analysis(Analysis_Transferable at) throws CreateObjectException {
		super(new Identifier(at.id),
					new Date(at.created),
					new Date(at.modified),
					new Identifier(at.creator_id),
					new Identifier(at.modifier_id),
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
		return new Analysis_Transferable((Identifier_Transferable)super.id.getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
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

	public Result createResult(Identifier id,
														 Identifier creatorId,
														 Measurement measurement,
														 AlarmLevel alarmLevel,
														 SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(id,
																 creatorId,
																 measurement,
																 this,
																 ResultSort.RESULT_SORT_ANALYSIS,
																 alarmLevel,
																 parameters);
	}

	public static Analysis createInstance(Identifier id,
																				Identifier creatorId,
																				AnalysisType type,
																				Identifier monitoredElementId,
																				Set criteriaSet) throws CreateObjectException {
		return new Analysis(id,
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
}
