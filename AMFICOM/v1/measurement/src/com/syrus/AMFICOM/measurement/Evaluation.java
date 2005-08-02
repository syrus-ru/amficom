/*
 * $Id: Evaluation.java,v 1.75 2005/08/02 18:08:52 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluation;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluationHelper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.75 $, $Date: 2005/08/02 18:08:52 $
 * @author $Author: arseniy $
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

		try {
			DatabaseContext.getDatabase(ObjectEntities.EVALUATION_CODE).retrieve(this);
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
			final StorableObjectVersion version,
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
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEvaluation et = (IdlEvaluation) transferable;
		super.fromTransferable(et, null, new Identifier(et.monitoredElementId), null);

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

		return IdlEvaluationHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
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
			final StorableObjectVersion version,
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
				StorableObjectVersion.createInitial(),
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

	@Override
	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_EVALUATION, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		//	Measurement if exists
		if (super.parentAction != null) {
			dependencies.add(super.parentAction);
		}
		dependencies.add(this.thresholdSet);
		return dependencies;
	}
}
