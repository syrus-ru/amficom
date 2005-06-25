/*
 * $Id: Evaluation.java,v 1.68 2005/06/25 17:07:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluation;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.68 $, $Date: 2005/06/25 17:07:41 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class Evaluation extends Action {
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

		final EvaluationDatabase database = (EvaluationDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATION_CODE);
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
	public Evaluation(final IdlEvaluation et) throws CreateObjectException {
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
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlEvaluation et = (IdlEvaluation) transferable;
		super.fromTransferable(et.header, null, new Identifier(et.monitoredElementId), null);

		super.type = (EvaluationType) StorableObjectPool.getStorableObject(new Identifier(et._typeId), true);
		final Identifier parentActionId = new Identifier(et.measurementId);
		super.parentAction = (!parentActionId.equals(Identifier.VOID_IDENTIFIER))
				? (Action) StorableObjectPool.getStorableObject(parentActionId, true) : null;

		this.thresholdSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(et.thresholdSetId), true);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlEvaluation getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return new IdlEvaluation(super.getHeaderTransferable(orb),
				super.type.getId().getTransferable(),
				super.monitoredElementId.getTransferable(),
				(super.parentAction != null) ? super.parentAction.getId().getTransferable()
						: Identifier.VOID_IDENTIFIER.getTransferable(),
				this.thresholdSet.getId().getTransferable());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.measurement.Action#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
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
			final Evaluation evaluation = new Evaluation(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_CODE),
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
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		//	Measurement if exists
		if (super.parentAction != null)
			dependencies.add(super.parentAction);
		dependencies.add(this.thresholdSet);
		return dependencies;
	}
}
