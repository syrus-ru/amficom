/*
 * $Id: Analysis.java,v 1.90.2.5 2006/03/01 15:41:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysis;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisHelper;

/**
 * @version $Revision: 1.90.2.5 $, $Date: 2006/03/01 15:41:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Analysis extends Action<AnalysisResultParameter, Analysis> {
	private static final long serialVersionUID = 2935808157242604848L;

	private Identifier measurementId;

	Analysis(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status,
			final Identifier measurementId) {
		super(id,
				creatorId,
				version,
				typeId,
				monitoredElementId,
				actionTemplateId,
				name,
				startTime,
				duration,
				status);
		this.measurementId = measurementId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(final IdlAnalysis idlAnalysis) throws CreateObjectException {
		super(idlAnalysis);
	}

	public static Analysis createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final Identifier measurementId) throws CreateObjectException {

		try {
			final Analysis analysis = new Analysis(IdentifierPool.getGeneratedIdentifier(ANALYSIS_CODE),
					creatorId,
					INITIAL_VERSION,
					typeId,
					monitoredElementId,
					actionTemplateId,
					name,
					startTime,
					duration,
					ActionStatus.ACTION_STATUS_NEW,
					measurementId);

			assert analysis.isValid() : OBJECT_STATE_ILLEGAL;

			analysis.markAsChanged();

			return analysis;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlAnalysis getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlAnalysisHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getTypeId().getIdlTransferable(orb),
				super.getMonitoredElementId().getIdlTransferable(orb),
				super.getActionTemplateId().getIdlTransferable(orb),
				super.getName(),
				super.getStartTime().getTime(),
				super.getDuration(),
				super.getStatus().getIdlTransferable(),
				this.measurementId.getIdlTransferable(orb));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlAnalysis idlAnalysis = (IdlAnalysis) transferable;
		super.fromTransferable(idlAnalysis);
		this.measurementId = Identifier.valueOf(idlAnalysis.measurementId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}

	public Measurement getMeasurement() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.measurementId, true);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status,
			final Identifier measurementId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				typeId,
				monitoredElementId,
				actionTemplateId,
				name,
				startTime,
				duration,
				status);
		this.measurementId = measurementId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.getTypeId().getMajor() == ANALYSIS_TYPE_CODE
				&& (this.measurementId == null || this.measurementId.getMajor() == MEASUREMENT_CODE);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = super.getDependenciesTmpl();
		dependencies.add(this.measurementId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected AnalysisWrapper getWrapper() {
		return AnalysisWrapper.getInstance();
	}

	@Override
	public AnalysisResultParameter createActionResultParameter(final Identifier resultParameterCreatorId,
			final byte[] resultParameterValue,
			final Identifier resultParameterTypeId) throws CreateObjectException {
		return AnalysisResultParameter.createInstance(resultParameterCreatorId,
				resultParameterValue,
				resultParameterTypeId,
				this.id);
	}
}
