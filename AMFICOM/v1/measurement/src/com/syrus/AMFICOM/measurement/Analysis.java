/*
 * $Id: Analysis.java,v 1.40 2005/01/26 15:38:40 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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

/**
 * @version $Revision: 1.40 $, $Date: 2005/01/26 15:38:40 $
 * @author $Author: arseniy $
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

  public Analysis(Analysis_Transferable at) throws CreateObjectException {
		super(at.header,
			  null,
			  new Identifier(at.monitored_element_id),
			  null);

		try {
			super.type = (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(at.type_id), true);
			super.parentAction = (at.measurement_id.identifier_string.length() != 0) ? (Measurement) MeasurementStorableObjectPool.getStorableObject(new Identifier(at.measurement_id), true) : null;

			this.criteriaSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(at.criteria_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
	}

	protected Analysis(Identifier id,
					   Identifier creatorId,
					   AnalysisType type,
					   Identifier monitoredElementId,
					   Measurement measurement,
					   Set criteriaSet) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					type,
					monitoredElementId,
					measurement);

		this.criteriaSet = criteriaSet;

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;

	}

	public void insert() throws CreateObjectException {
		try {
			if (this.analysisDatabase != null)
				this.analysisDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		return new Analysis_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.type.getId().getTransferable(),
									(Identifier_Transferable)super.monitoredElementId.getTransferable(),
									(super.parentAction != null) ? (Identifier_Transferable) super.parentAction.getId().getTransferable() : new Identifier_Transferable(""),
									(Identifier_Transferable)this.criteriaSet.getId().getTransferable());
	}

	public short getEntityCode() {
		return ObjectEntities.ANALYSIS_ENTITY_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement)super.parentAction;
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
																	Measurement measurement,
																	Set criteriaSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												type,
												monitoredElementId,
												measurement);
		this.criteriaSet = criteriaSet;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param measurement
	 * @param criteriaSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Analysis createInstance(Identifier creatorId,
										  AnalysisType type,
										  Identifier monitoredElementId,
										  Measurement measurement,
										  Set criteriaSet) throws CreateObjectException {
		if (creatorId == null || type == null || monitoredElementId == null || criteriaSet == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			return new Analysis(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_ENTITY_CODE),
				creatorId,
				type,
				monitoredElementId,
				measurement,
				criteriaSet);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Analysis.createInstance | cannot generate identifier ", e);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_ANALYSIS,
				resultParameters);
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		//	Measurement, if exists
		if (super.parentAction != null)
			dependencies.add(super.parentAction);
		dependencies.add(this.criteriaSet);
		return dependencies;
	}
}
