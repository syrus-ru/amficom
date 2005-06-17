/*
 * $Id: Evaluation.java,v 1.63 2005/06/17 13:06:57 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.63 $, $Date: 2005/06/17 13:06:57 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class Evaluation extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3617570505297703480L;

	private ParameterSet thresholdSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Evaluation(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		EvaluationDatabase database = (EvaluationDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATION_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Evaluation(final Evaluation_Transferable et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Evaluation(final Identifier id,
			final Identifier creatorId,
			final long version,
			final EvaluationType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final ParameterSet thresholdSet) {
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
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Evaluation_Transferable et = (Evaluation_Transferable) transferable;
		super.fromTransferable(et.header, null, new Identifier(et.monitored_element_id), null);

		super.type = (EvaluationType) StorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		super.parentAction = (Measurement) StorableObjectPool.getStorableObject(new Identifier(et.measurement_id), true);

		this.thresholdSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(et.threshold_set_id), true);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Evaluation_Transferable(super.getHeaderTransferable(),
										   (IdlIdentifier)super.type.getId().getTransferable(),
										   (IdlIdentifier)super.monitoredElementId.getTransferable(),
										   (super.parentAction != null) ? (IdlIdentifier) super.parentAction.getId().getTransferable() : new IdlIdentifier(""),
										   (IdlIdentifier)this.thresholdSet.getId().getTransferable());
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
		return ObjectEntities.EVALUATION_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement) super.parentAction;
	}
	
	public void setMeasurement(final Measurement measurement) {
		super.parentAction = measurement;
		super.markAsChanged();
	}

	public ParameterSet getThresholdSet() {
		return this.thresholdSet;
	}
	
	public void setThresholdSet(final ParameterSet thresholdSet) {
		this.thresholdSet = thresholdSet;
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
			final EvaluationType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final ParameterSet thresholdSet) {
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
	public static Evaluation createInstance(final Identifier creatorId,
			final EvaluationType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final ParameterSet thresholdSet) throws CreateObjectException {
		try {
			Evaluation evaluation = new Evaluation(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_CODE),
				creatorId,
				0L,
				type,
				monitoredElementId,
				measurement,
				thresholdSet);

			assert evaluation.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			evaluation.markAsChanged();

			return evaluation;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_EVALUATION, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
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
