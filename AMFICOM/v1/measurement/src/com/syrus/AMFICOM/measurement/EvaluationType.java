/*
 * $Id: EvaluationType.java,v 1.75 2005/06/24 13:54:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluationType;

/**
 * @version $Revision: 1.75 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class EvaluationType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3978420317305583161L;

	public static final String CODENAME_DADARA = "dadara";

	private Set<Identifier> inParameterTypeIds;
	private Set<Identifier> thresholdParameterTypeIds;
	private Set<Identifier> etalonParameterTypeIds;
	private Set<Identifier> outParameterTypeIds;

	private Set<Identifier> measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	EvaluationType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.thresholdParameterTypeIds = new HashSet<Identifier>();
		this.etalonParameterTypeIds = new HashSet<Identifier>();
		this.outParameterTypeIds = new HashSet<Identifier>();

		this.measurementTypeIds = new HashSet<Identifier>();

		final EvaluationTypeDatabase database = (EvaluationTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATION_TYPE_CODE);
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
	public EvaluationType(final IdlEvaluationType ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	EvaluationType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> thresholdParameterTypeIds,
			final Set<Identifier> etalonParameterTypeIds,
			final Set<Identifier> outParameterTypeIds,
			final Set<Identifier> measurementTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.setInParameterTypeIds0(inParameterTypeIds);

		this.thresholdParameterTypeIds = new HashSet<Identifier>();
		this.setThresholdParameterTypeIds0(thresholdParameterTypeIds);

		this.etalonParameterTypeIds = new HashSet<Identifier>();
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);

		this.outParameterTypeIds = new HashSet<Identifier>();
		this.setOutParameterTypeIds0(outParameterTypeIds);


		this.measurementTypeIds = new HashSet<Identifier>();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param thresholdParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static EvaluationType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> thresholdParameterTypeIds,
			final Set<Identifier> etalonParameterTypeIds,
			final Set<Identifier> outParameterTypeIds,
			final Set<Identifier> measurementTypeIds) throws CreateObjectException {
		try {
			final EvaluationType evaluationType = new EvaluationType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					thresholdParameterTypeIds,
					etalonParameterTypeIds,
					outParameterTypeIds,
					measurementTypeIds);

			assert evaluationType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			evaluationType.markAsChanged();

			return evaluationType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlEvaluationType ett = (IdlEvaluationType) transferable;
		super.fromTransferable(ett.header, ett.codename, ett.description);

		this.inParameterTypeIds = Identifier.fromTransferables(ett.inParameterTypeIds);
		this.thresholdParameterTypeIds = Identifier.fromTransferables(ett.thresholdParameterTypeIds);
		this.etalonParameterTypeIds = Identifier.fromTransferables(ett.etalonParameterTypeIds);
		this.outParameterTypeIds = Identifier.fromTransferables(ett.outParameterTypeIds);		

		this.measurementTypeIds = Identifier.fromTransferables(ett.measurementTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlEvaluationType getTransferable() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		final IdlIdentifier[] thresholdParTypeIds = Identifier.createTransferables(this.thresholdParameterTypeIds);
		final IdlIdentifier[] etalonParTypeIds = Identifier.createTransferables(this.etalonParameterTypeIds);
		final IdlIdentifier[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		final IdlIdentifier[] measTypIds = Identifier.createTransferables(this.measurementTypeIds);

		return new IdlEvaluationType(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				inParTypeIds,
				thresholdParTypeIds,
				etalonParTypeIds,
				outParTypeIds,
				measTypIds);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET
				&& this.thresholdParameterTypeIds != null && this.thresholdParameterTypeIds != Collections.EMPTY_SET
				&& this.etalonParameterTypeIds != null && this.etalonParameterTypeIds != Collections.EMPTY_SET
				&& this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET
				&& this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET;
	}

	public Set<Identifier> getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public Set<Identifier> getThresholdParameterTypeIds() {
		return Collections.unmodifiableSet(this.thresholdParameterTypeIds);
	}

	public Set<Identifier> getEtalonParameterTypeIds() {
		return Collections.unmodifiableSet(this.etalonParameterTypeIds);
	}

	public Set<Identifier> getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
	}

	public Set<Identifier> getMeasurementTypeIds() {
		return Collections.unmodifiableSet(this.measurementTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setParameterTypeIds(final Map<String, Set<Identifier>> parameterTypeIdsModeMap) {
		this.setInParameterTypeIds0(parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_IN));
		this.setThresholdParameterTypeIds0(parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_THRESHOLD));
		this.setEtalonParameterTypeIds0(parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_ETALON));
		this.setOutParameterTypeIds0(parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<Identifier>> getParameterTypeIdsModeMap() {
		final Map<String, Set<Identifier>> parameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>(4);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_THRESHOLD, this.thresholdParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_ETALON, this.etalonParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_OUT, this.outParameterTypeIds);
		return parameterTypeIdsModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypeIds0(final Set<Identifier> inParameterTypeIds) {
		this.inParameterTypeIds.clear();
		if (inParameterTypeIds != null)
			this.inParameterTypeIds.addAll(inParameterTypeIds);
	}
	
	/**
	 * client setter for inParameterTypeIds
	 *
	 * @param inParameterTypeIds
	 *            The inParameterTypeIds to set.
	 */
	public void setInParameterTypeIds(final Set<Identifier> inParameterTypeIds) {
		this.setInParameterTypeIds0(inParameterTypeIds);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setThresholdParameterTypeIds0(final Set<Identifier> thresholdParameterTypeIds) {
		this.thresholdParameterTypeIds.clear();
		if (thresholdParameterTypeIds != null)
			this.thresholdParameterTypeIds.addAll(thresholdParameterTypeIds);
	}

	/**
	 * client setter for thresholdParameterTypeIds
	 *
	 * @param thresholdParameterTypeIds
	 *            The thresholdParameterTypeIds to set.
	 */
	public void setThresholdParameterTypeIds(final Set<Identifier> thresholdParameterTypeIds) {
		this.setThresholdParameterTypeIds0(thresholdParameterTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEtalonParameterTypeIds0(final Set<Identifier> etalonParameterTypeIds) {
		this.etalonParameterTypeIds.clear();
		if (etalonParameterTypeIds != null)
			this.etalonParameterTypeIds.addAll(etalonParameterTypeIds);
	}

	/**
	 * client setter for etalonParameterTypeIds
	 *
	 * @param etalonParameterTypeIds
	 *            The etalonParameterTypeIds to set.
	 */
	public void setEtalonParameterTypeIds(final Set<Identifier> etalonParameterTypeIds) {
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypeIds0(final Set<Identifier> outParameterTypeIds) {
		this.outParameterTypeIds.clear();
		if (outParameterTypeIds != null)
			this.outParameterTypeIds.addAll(outParameterTypeIds);
	}

	/**
	 * client setter for outParameterTypeIds
	 *
	 * @param outParameterTypeIds
	 *            The outParameterTypeIds to set.
	 */
	public void setOutParameterTypeIds(final Set<Identifier> outParameterTypeIds) {
		this.setOutParameterTypeIds0(outParameterTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setMeasurementTypeIds0(final Set<Identifier> measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null)
			this.measurementTypeIds.addAll(measurementTypeIds);
	}

	/**
	 * client setter for measurementTypeIds
	 * @param measurementTypeIds
	 */
	public void setMeasurementTypeIds(final Set<Identifier> measurementTypeIds) {
		this.setMeasurementTypeIds0(measurementTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.inParameterTypeIds != null)
			dependencies.addAll(this.inParameterTypeIds);

		if (this.thresholdParameterTypeIds != null)
			dependencies.addAll(this.thresholdParameterTypeIds);

		if (this.etalonParameterTypeIds != null)
			dependencies.addAll(this.etalonParameterTypeIds);

		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
