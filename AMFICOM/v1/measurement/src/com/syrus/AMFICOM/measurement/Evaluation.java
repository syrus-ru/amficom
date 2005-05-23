/*
 * $Id: Evaluation.java,v 1.55 2005/05/23 18:45:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.55 $, $Date: 2005/05/23 18:45:15 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class Evaluation extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3617570505297703480L;

	private Set thresholdSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Evaluation(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Evaluation(Evaluation_Transferable et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
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
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Evaluation_Transferable et = (Evaluation_Transferable) transferable;
		super.fromTransferable(et.header, null, new Identifier(et.monitored_element_id), null);

		super.type = (EvaluationType) StorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		super.parentAction = (et.measurement_id.identifier_string.length() != 0)
				? (Measurement) StorableObjectPool.getStorableObject(new Identifier(et.measurement_id), true) : null;

		this.thresholdSet = (Set) StorableObjectPool.getStorableObject(new Identifier(et.threshold_set_id), true);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Evaluation_Transferable(super.getHeaderTransferable(),
										   (Identifier_Transferable)super.type.getId().getTransferable(),
										   (Identifier_Transferable)super.monitoredElementId.getTransferable(),
										   (super.parentAction != null) ? (Identifier_Transferable) super.parentAction.getId().getTransferable() : new Identifier_Transferable(""),
										   (Identifier_Transferable)this.thresholdSet.getId().getTransferable());
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.measurement.Action#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.thresholdSet != null;
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
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
		try {
			Evaluation evaluation = new Evaluation(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_ENTITY_CODE),
				creatorId,
				0L,
				type,
				monitoredElementId,
				measurement,
				thresholdSet);
			assert evaluation.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			evaluation.changed = true;
			return evaluation;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_EVALUATION,
				resultParameters);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		//	Measurement if exists
		if (super.parentAction != null)
			dependencies.add(super.parentAction);
		dependencies.add(this.thresholdSet);
		return dependencies;
	}
}
