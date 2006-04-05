/*
 * $Id: Measurement.java,v 1.101.2.9 2006/04/05 07:41:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
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
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementHelper;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.101.2.9 $, $Date: 2006/04/05 07:41:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Measurement extends Action<MeasurementResultParameter> implements IdlTransferableObjectExt<IdlMeasurement> {
	private static final long serialVersionUID = -1217428566443489958L;

	private Identifier testId;

	private transient Identifier analysisId = VOID_IDENTIFIER;

	Measurement(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status,
			final Identifier testId) {
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
		this.testId = testId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement(final IdlMeasurement idlMeasurement) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlMeasurement);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	static Measurement createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final Identifier testId) throws CreateObjectException {

		try {
			final Measurement measurement = new Measurement(IdentifierPool.getGeneratedIdentifier(MEASUREMENT_CODE),
					creatorId,
					INITIAL_VERSION,
					typeId,
					monitoredElementId,
					actionTemplateId,
					name,
					startTime,
					duration,
					ActionStatus.ACTION_STATUS_NEW,
					testId);

			assert measurement.isValid() : OBJECT_STATE_ILLEGAL;

			measurement.markAsChanged();

			return measurement;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurement getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlMeasurementHelper.init(orb,
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
				this.testId.getIdlTransferable(orb));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlMeasurement idlMeasurement) throws IdlConversionException {
		super.fromIdlTransferable(idlMeasurement);
		this.testId = Identifier.valueOf(idlMeasurement.testId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	public Identifier getTestId() {
		return this.testId;
	}

	public Test getTest() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.testId, true);
	}

	/**
	 * Этот метод написан в предположении, что у измерения не может быть больше
	 * одного анализа. Если нет ни одного анализа - возвращает <code>null</code>.
	 * Если есть один - возвращает его идентификатор. Если есть более одного -
	 * возвращает идентификатор первого попавшегося и выдаёт сообщение.
	 * 
	 * @return Идентификатор анализа для данного измерения.
	 * @throws ApplicationException
	 */
	public Identifier getAnalysisId() throws ApplicationException {
		if (this.analysisId.isVoid()) {
			final StorableObjectCondition condition = new LinkedIdsCondition(this.id, ANALYSIS_CODE);
			final Set<Identifier> analysisIds = StorableObjectPool.getIdentifiersByCondition(condition, true);
			if (analysisIds.isEmpty()) {
				this.analysisId = null;
			} else {
				if (analysisIds.size() > 1) {
					Log.errorMessage("Measurement '" + this.id + "' has more, than 1 analysis: " + analysisIds);
				}
				this.analysisId = analysisIds.iterator().next();
			}
		}
		return this.analysisId;
	}

	/**
	 * Этот метод написан в предположении, что у измерения не может быть больше
	 * одного анализа. Если нет ни одного анализа - возвращает <code>null</code>.
	 * Если есть один - возвращает его. Если есть более одного - возвращает
	 * первий попавшийся и выдаёт сообщение.
	 * 
	 * @return Анализ для данного измерения.
	 * @throws ApplicationException
	 */
	public Analysis getAnalysis() throws ApplicationException {
		if (this.analysisId.isVoid()) {
			final StorableObjectCondition condition = new LinkedIdsCondition(this.id, ANALYSIS_CODE);
			final Set<Analysis> analyses = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (analyses.isEmpty()) {
				this.analysisId = null;
				return null;
			}
			if (analyses.size() > 1) {
				Log.errorMessage("Measurement '" + this.id + "' has more, than 1 analysis: " + analyses);
			}
			final Analysis analysis = analyses.iterator().next();
			this.analysisId = analysis.getId();
			return analysis;
		}

		if (this.analysisId == null) {
			return null;
		}
		return StorableObjectPool.getStorableObject(this.analysisId, true);
	}

	@Override
	short getResultParameterEntityCode() {
		return MEASUREMENTRESULTPARAMETER_CODE;
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
			final Identifier testId) {
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
		this.testId = testId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.getTypeId().getMajor() == MEASUREMENT_TYPE_CODE
				&& this.testId != null && this.testId.getMajor() == TEST_CODE;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = super.getDependenciesTmpl();
		dependencies.add(this.testId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementWrapper getWrapper() {
		return MeasurementWrapper.getInstance();
	}

	@Override
	public MeasurementResultParameter createActionResultParameter(final Identifier resultParameterCreatorId,
			final byte[] resultParameterValue,
			final Identifier resultParameterTypeId) throws CreateObjectException {
		return MeasurementResultParameter.createInstance(resultParameterCreatorId,
				resultParameterValue,
				resultParameterTypeId,
				this.id);
	}
}
