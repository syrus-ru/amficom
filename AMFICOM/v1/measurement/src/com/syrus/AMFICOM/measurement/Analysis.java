/*
 * $Id: Analysis.java,v 1.87 2005/12/06 09:45:11 bass Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysis;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisHelper;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.87 $, $Date: 2005/12/06 09:45:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Analysis extends Action<Analysis> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3979266967062721849L;

	private String name;
	private ParameterSet criteriaSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(final IdlAnalysis at) throws CreateObjectException {
		try {
			this.fromTransferable(at);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Analysis(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					version,
					type,
					monitoredElementId,
					measurement);

		this.name = name;
		this.criteriaSet = criteriaSet;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlAnalysis at = (IdlAnalysis) transferable;
		super.fromTransferable(at, AnalysisType.fromTransferable(at.type), new Identifier(at.monitoredElementId), null);

		this.name = at.name;
		final Identifier parentActionId = new Identifier(at.measurementId);
		super.parentAction = (!parentActionId.equals(Identifier.VOID_IDENTIFIER))
				? (Action) StorableObjectPool.getStorableObject(parentActionId, true)
					: null;

		this.criteriaSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(at.criteriaSetId), true);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlAnalysis getIdlTransferable(final ORB orb) {

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlAnalysisHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				(IdlAnalysisType) super.type.getIdlTransferable(orb),
				super.monitoredElementId.getIdlTransferable(),
				((super.parentAction != null) ? super.parentAction.getId() : Identifier.VOID_IDENTIFIER).getIdlTransferable(),
				this.name != null ? this.name : "",
				this.criteriaSet.getId().getIdlTransferable());
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.criteriaSet != null;
	}

	public short getEntityCode() {
		return ObjectEntities.ANALYSIS_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement) super.parentAction;
	}

	public void setMeasurement(final Measurement measurement) {
		super.parentAction = measurement;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public void setCriteriaSet(final ParameterSet criteriaSet) {
		this.criteriaSet = criteriaSet;
		super.markAsChanged();
	}

	public ParameterSet getCriteriaSet() {
		return this.criteriaSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				type,
				monitoredElementId,
				measurement);
		this.name = name;
		this.criteriaSet = criteriaSet;
	}

	/**
	 * Create a new instance for client
	 *
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param measurement
	 * @param criteriaSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Analysis createInstance(final Identifier creatorId,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) throws CreateObjectException {
		try {
			final Analysis analysis = new Analysis(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_CODE),
				creatorId,
				StorableObjectVersion.INITIAL_VERSION,
				type,
				monitoredElementId,
				measurement,
				name,
				criteriaSet);

			assert analysis.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			analysis.markAsChanged();

			return analysis;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_ANALYSIS, resultParameters);
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

		//	Measurement, if exists
		if (super.parentAction != null) {
			dependencies.add(super.parentAction);
		}

		dependencies.add(this.criteriaSet);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected AnalysisWrapper getWrapper() {
		return AnalysisWrapper.getInstance();
	}
}
