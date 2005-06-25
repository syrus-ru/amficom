/*
 * $Id: AnalysisType.java,v 1.80 2005/06/25 17:07:41 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;

/**
 * @version $Revision: 1.80 $, $Date: 2005/06/25 17:07:41 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class AnalysisType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722866476495413L;

	public static final String CODENAME_DADARA = "dadara";

	private Set<Identifier> inParameterTypeIds;
	private Set<Identifier> criteriaParameterTypeIds;
	private Set<Identifier> etalonParameterTypeIds;
	private Set<Identifier> outParameterTypeIds;

	private Set<Identifier> measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	AnalysisType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.criteriaParameterTypeIds = new HashSet<Identifier>();
		this.etalonParameterTypeIds = new HashSet<Identifier>();
		this.outParameterTypeIds = new HashSet<Identifier>();

		this.measurementTypeIds = new HashSet<Identifier>();

		final AnalysisTypeDatabase database = (AnalysisTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public AnalysisType(final IdlAnalysisType att) throws CreateObjectException {
		try {
			this.fromTransferable(att);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	AnalysisType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> criteriaParameterTypeIds,
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

		this.criteriaParameterTypeIds = new HashSet<Identifier>();
		this.setCriteriaParameterTypeIds0(criteriaParameterTypeIds);

		this.etalonParameterTypeIds = new HashSet<Identifier>();
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);

		this.outParameterTypeIds = new HashSet<Identifier>();
		this.setOutParameterTypeIds0(outParameterTypeIds);


		this.measurementTypeIds = new HashSet<Identifier>();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param criteriaParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static AnalysisType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> criteriaParameterTypeIds,
			final Set<Identifier> etalonParameterTypeIds,
			final Set<Identifier> outParameterTypeIds,
			final Set<Identifier> measurementTypeIds) throws CreateObjectException {		

		try {
			final AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					criteriaParameterTypeIds,
					etalonParameterTypeIds,
					outParameterTypeIds,
					measurementTypeIds);

			assert analysisType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			analysisType.markAsChanged();

			return analysisType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlAnalysisType att = (IdlAnalysisType) transferable;
		super.fromTransferable(att.header, att.codename, att.description);

		this.inParameterTypeIds = Identifier.fromTransferables(att.inParameterTypeIds);
		this.criteriaParameterTypeIds = Identifier.fromTransferables(att.criteriaParameterTypeIds);
		this.etalonParameterTypeIds = Identifier.fromTransferables(att.etalonParameterTypeIds);
		this.outParameterTypeIds = Identifier.fromTransferables(att.outParameterTypeIds);

		this.measurementTypeIds = Identifier.fromTransferables(att.measurementTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlAnalysisType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		final IdlIdentifier[] criteriaParTypeIds = Identifier.createTransferables(this.criteriaParameterTypeIds);
		final IdlIdentifier[] etalonParTypeIds = Identifier.createTransferables(this.etalonParameterTypeIds);
		final IdlIdentifier[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		final IdlIdentifier[] measTypIds = Identifier.createTransferables(this.measurementTypeIds);

		return new IdlAnalysisType(super.getHeaderTransferable(orb),
				super.codename,
				super.description != null ? super.description : "",
				inParTypeIds,
				criteriaParTypeIds,
				etalonParTypeIds,
				outParTypeIds,
				measTypIds);
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET &&
			this.criteriaParameterTypeIds != null && this.criteriaParameterTypeIds != Collections.EMPTY_SET &&
			this.etalonParameterTypeIds != null && this.etalonParameterTypeIds != Collections.EMPTY_SET &&
			this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET &&
			this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET;
	}
	
	public Set<Identifier> getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public Set<Identifier> getCriteriaParameterTypeIds() {
		return Collections.unmodifiableSet(this.criteriaParameterTypeIds);
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
		this.setInParameterTypeIds0(parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_IN));
		this.setCriteriaParameterTypeIds0(parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_CRITERION));
		this.setEtalonParameterTypeIds0(parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_ETALON));
		this.setOutParameterTypeIds0(parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<Identifier>> getParameterTypeIdsModeMap() {
		final Map<String, Set<Identifier>> parameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>(4);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_CRITERION, this.criteriaParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_ETALON, this.etalonParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_OUT, this.outParameterTypeIds);
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
	protected void setCriteriaParameterTypeIds0(final Set<Identifier> criteriaParameterTypeIds) {
		this.criteriaParameterTypeIds.clear();
		if (criteriaParameterTypeIds != null)
			this.criteriaParameterTypeIds.addAll(criteriaParameterTypeIds);
	}
	/**
	 * client setter for criteriaParameterTypeIds
	 *
	 * @param thresholdParameterTypeIds
	 *            The thresholdParameterTypeIds to set.
	 */
	public void setCriteriaParameterTypeIds(final Set<Identifier> thresholdParameterTypeIds) {
		this.setCriteriaParameterTypeIds0(thresholdParameterTypeIds);
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
	 * client setter for outParameterTypeIds
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

		if (this.criteriaParameterTypeIds != null)
			dependencies.addAll(this.criteriaParameterTypeIds);

		if (this.etalonParameterTypeIds != null)
			dependencies.addAll(this.etalonParameterTypeIds);

		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
