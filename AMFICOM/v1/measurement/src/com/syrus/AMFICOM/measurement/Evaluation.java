/*
 * $Id: Evaluation.java,v 1.45 2005/04/01 08:43:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.45 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Evaluation extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3617570505297703480L;

	private Set thresholdSet;

	private StorableObjectDatabase evaluationDatabase;

	public Evaluation(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Evaluation(Evaluation_Transferable et) throws CreateObjectException {
		super(et.header,
			  null,
			  new Identifier(et.monitored_element_id),
			  null);

		try {
			super.type = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
			super.parentAction = (et.measurement_id.identifier_string.length() != 0) ? (Measurement) MeasurementStorableObjectPool.getStorableObject(new Identifier(et.measurement_id), true) : null;

			this.thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(et.threshold_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
	}

	protected Evaluation(Identifier id,
						 Identifier creatorId,
						 long version,
						 EvaluationType type,
						 Identifier monitoredElementId,
						 Measurement measurement,
						 Set thresholdSet) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				type,
				monitoredElementId,
				measurement);

		this.thresholdSet = thresholdSet;

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
	}

	public Object getTransferable() {
		return new Evaluation_Transferable(super.getHeaderTransferable(),
										   (Identifier_Transferable)super.type.getId().getTransferable(),
										   (Identifier_Transferable)super.monitoredElementId.getTransferable(),
										   (super.parentAction != null) ? (Identifier_Transferable) super.parentAction.getId().getTransferable() : new Identifier_Transferable(""),
										   (Identifier_Transferable)this.thresholdSet.getId().getTransferable());
	}

	public short getEntityCode() {
		return ObjectEntities.EVALUATION_ENTITY_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement) super.parentAction;
	}
	
	public void setMeasurement(Measurement measurement) {
		super.parentAction = measurement;
		super.changed = true;
	}

	public Set getThresholdSet() {
		return this.thresholdSet;
	}
	
	public void setThresholdSet(Set thresholdSet) {
		this.thresholdSet = thresholdSet;
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  EvaluationType type,
											  Identifier monitoredElementId,
											  Measurement measurement,
											  Set thresholdSet) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			type,
			monitoredElementId,
			measurement);
		this.thresholdSet = thresholdSet;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param measurement
	 * @param thresholdSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Evaluation createInstance(Identifier creatorId,
											EvaluationType type,
											Identifier monitoredElementId,
											Measurement measurement,
											Set thresholdSet) throws CreateObjectException {
		if (creatorId == null || type == null || monitoredElementId == null || thresholdSet == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			Evaluation evaluation = new Evaluation(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_ENTITY_CODE),
				creatorId,
				0L,
				type,
				monitoredElementId,
				measurement,
				thresholdSet);
			evaluation.changed = true;
			return evaluation;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Evaluation.createInstance | cannot generate identifier ", e);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_EVALUATION,
				resultParameters);
	}

	public java.util.Set getDependencies() {
		java.util.Set dependencies = new HashSet();
		//	Measurement if exists
		if (super.parentAction != null)
			dependencies.add(super.parentAction);
		dependencies.add(this.thresholdSet);
		return dependencies;
	}
}
