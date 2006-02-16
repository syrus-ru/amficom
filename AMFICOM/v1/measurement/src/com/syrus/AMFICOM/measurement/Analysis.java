/*
 * $Id: Analysis.java,v 1.91 2006/02/16 13:42:34 bass Exp $
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
 * @version $Revision: 1.91 $, $Date: 2006/02/16 13:42:34 $
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
			final Identifier measurementId,
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
					measurementId);

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
		super.parentActionId = new Identifier(at.measurementId);

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
				super.parentActionId.getIdlTransferable(),
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

	public Identifier getMeasurementId() {
		return super.getParentActionId();
	}
	
	public Measurement getMeasurement() throws ApplicationException {
		return (Measurement) super.getParentAction();
	}

	public void setMeasurementId(final Identifier measurementId) {
		assert measurementId.getMajor() == ObjectEntities.MEASUREMENT_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		super.parentActionId = measurementId;
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
			final Identifier measurementId,
			final String name,
			final ParameterSet criteriaSet) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				type,
				monitoredElementId,
				measurementId);
		this.name = name;
		this.criteriaSet = criteriaSet;
	}

	/**
	 * Create a new instance for client
	 *
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param measurementId
	 * @param criteriaSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Analysis createInstance(final Identifier creatorId,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Identifier measurementId,
			final String name,
			final ParameterSet criteriaSet) throws CreateObjectException {
		assert measurementId.getMajor() == ObjectEntities.MEASUREMENT_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		try {
			final Analysis analysis = new Analysis(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_CODE),
				creatorId,
				StorableObjectVersion.INITIAL_VERSION,
				type,
				monitoredElementId,
				measurementId,
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
		return Result.createInstance(resultCreatorId, this.id, ResultSort.RESULT_SORT_ANALYSIS, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		dependencies.add(super.parentActionId);	//VOID_IDENTIFIER replaced in StorableObject#getDependencies()
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
