/*
 * $Id: AnalysisType.java,v 1.90 2005/08/19 14:19:04 arseniy Exp $
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
import com.syrus.AMFICOM.general.ParameterTypeEnum;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeEnum;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisTypeHelper;

/**
 * @version $Revision: 1.90 $, $Date: 2005/08/19 14:19:04 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class AnalysisType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722866476495413L;

	public static final String CODENAME_DADARA = "dadara";

	private enum ParameterMode {
		MODE_IN("IN"),
		MODE_CRITERION("CRI"),
		MODE_ETALON("ETA"),
		MODE_OUT("OUT");

		private String stringValue;

		private ParameterMode(final String stringValue) {
			this.stringValue = stringValue;
		}

		String stringValue() {
			return this.stringValue;
		}
	}

	private Set<ParameterTypeEnum> inParameterTypes;
	private Set<ParameterTypeEnum> criteriaParameterTypes;
	private Set<ParameterTypeEnum> etalonParameterTypes;
	private Set<ParameterTypeEnum> outParameterTypes;

	private Set<Identifier> measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	AnalysisType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet<ParameterTypeEnum>();
		this.criteriaParameterTypes = new HashSet<ParameterTypeEnum>();
		this.etalonParameterTypes = new HashSet<ParameterTypeEnum>();
		this.outParameterTypes = new HashSet<ParameterTypeEnum>();

		this.measurementTypeIds = new HashSet<Identifier>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_TYPE_CODE).retrieve(this);
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
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final Set<ParameterTypeEnum> inParameterTypes,
			final Set<ParameterTypeEnum> criteriaParameterTypes,
			final Set<ParameterTypeEnum> etalonParameterTypes,
			final Set<ParameterTypeEnum> outParameterTypes,
			final Set<Identifier> measurementTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new HashSet<ParameterTypeEnum>();
		this.setInParameterTypes0(inParameterTypes);

		this.criteriaParameterTypes = new HashSet<ParameterTypeEnum>();
		this.setCriteriaParameterTypes0(criteriaParameterTypes);

		this.etalonParameterTypes = new HashSet<ParameterTypeEnum>();
		this.setEtalonParameterTypes0(etalonParameterTypes);

		this.outParameterTypes = new HashSet<ParameterTypeEnum>();
		this.setOutParameterTypes0(outParameterTypes);


		this.measurementTypeIds = new HashSet<Identifier>();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param criteriaParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static AnalysisType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<ParameterTypeEnum> inParameterTypes,
			final Set<ParameterTypeEnum> criteriaParameterTypes,
			final Set<ParameterTypeEnum> etalonParameterTypes,
			final Set<ParameterTypeEnum> outParameterTypes,
			final Set<Identifier> measurementTypeIds) throws CreateObjectException {		

		try {
			final AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					inParameterTypes,
					criteriaParameterTypes,
					etalonParameterTypes,
					outParameterTypes,
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
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlAnalysisType att = (IdlAnalysisType) transferable;
		super.fromTransferable(att, att.codename, att.description);

		this.inParameterTypes = ParameterTypeEnum.fromTransferables(att.inParameterTypes);
		this.criteriaParameterTypes = ParameterTypeEnum.fromTransferables(att.criteriaParameterTypes);
		this.etalonParameterTypes = ParameterTypeEnum.fromTransferables(att.etalonParameterTypes);
		this.outParameterTypes = ParameterTypeEnum.fromTransferables(att.outParameterTypes);

		this.measurementTypeIds = Identifier.fromTransferables(att.measurementTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlAnalysisType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlParameterTypeEnum[] inParTypes = ParameterTypeEnum.createTransferables(this.inParameterTypes, orb);
		final IdlParameterTypeEnum[] criteriaParTypes = ParameterTypeEnum.createTransferables(this.criteriaParameterTypes, orb);
		final IdlParameterTypeEnum[] etalonParTypes = ParameterTypeEnum.createTransferables(this.etalonParameterTypes, orb);
		final IdlParameterTypeEnum[] outParTypes = ParameterTypeEnum.createTransferables(this.outParameterTypes, orb);

		final IdlIdentifier[] measTypIds = Identifier.createTransferables(this.measurementTypeIds);

		return IdlAnalysisTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				inParTypes,
				criteriaParTypes,
				etalonParTypes,
				outParTypes,
				measTypIds);
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET && !this.inParameterTypes.contains(null)
				&& this.criteriaParameterTypes != null && this.criteriaParameterTypes != Collections.EMPTY_SET && !this.criteriaParameterTypes.contains(null)
				&& this.etalonParameterTypes != null && this.etalonParameterTypes != Collections.EMPTY_SET && !this.etalonParameterTypes.contains(null)
				&& this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET && !this.outParameterTypes.contains(null)
				&& this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET && !this.measurementTypeIds.contains(null);
	}

	public Set<ParameterTypeEnum> getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public Set<ParameterTypeEnum> getCriteriaParameterTypes() {
		return Collections.unmodifiableSet(this.criteriaParameterTypes);
	}

	public Set<ParameterTypeEnum> getEtalonParameterTypes() {
		return Collections.unmodifiableSet(this.etalonParameterTypes);
	}

	public Set<ParameterTypeEnum> getOutParameterTypes() {
		return Collections.unmodifiableSet(this.outParameterTypes);
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
			final StorableObjectVersion version,
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
	protected synchronized void setParameterTypes(final Map<String, Set<ParameterTypeEnum>> parameterTypesModeMap) {
		assert parameterTypesModeMap != null : ErrorMessages.NON_NULL_EXPECTED;
		this.setInParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_IN.stringValue()));
		this.setCriteriaParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_CRITERION.stringValue()));
		this.setEtalonParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_ETALON.stringValue()));
		this.setOutParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_OUT.stringValue()));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<ParameterTypeEnum>> getParameterTypesModeMap() {
		final Map<String, Set<ParameterTypeEnum>> parameterTypesModeMap = new HashMap<String, Set<ParameterTypeEnum>>(4);
		parameterTypesModeMap.put(ParameterMode.MODE_IN.stringValue(), this.inParameterTypes);
		parameterTypesModeMap.put(ParameterMode.MODE_CRITERION.stringValue(), this.criteriaParameterTypes);
		parameterTypesModeMap.put(ParameterMode.MODE_ETALON.stringValue(), this.etalonParameterTypes);
		parameterTypesModeMap.put(ParameterMode.MODE_OUT.stringValue(), this.outParameterTypes);
		return parameterTypesModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypes0(final Set<ParameterTypeEnum> inParameterTypes) {
		this.inParameterTypes.clear();
		if (inParameterTypes != null) {
			this.inParameterTypes.addAll(inParameterTypes);
		}
	}
	
	/**
	 * client setter for inParameterTypes
	 *
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypes(final Set<ParameterTypeEnum> inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setCriteriaParameterTypes0(final Set<ParameterTypeEnum> criteriaParameterTypes) {
		this.criteriaParameterTypes.clear();
		if (criteriaParameterTypes != null) {
			this.criteriaParameterTypes.addAll(criteriaParameterTypes);
		}
	}
	/**
	 * client setter for criteriaParameterTypes
	 *
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setCriteriaParameterTypes(final Set<ParameterTypeEnum> thresholdParameterTypes) {
		this.setCriteriaParameterTypes0(thresholdParameterTypes);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEtalonParameterTypes0(final Set<ParameterTypeEnum> etalonParameterTypes) {
		this.etalonParameterTypes.clear();
		if (etalonParameterTypes != null) {
			this.etalonParameterTypes.addAll(etalonParameterTypes);
		}
	}
	/**
	 * client setter for etalonParameterTypes
	 *
	 * @param etalonParameterTypes
	 *            The etalonParameterTypes to set.
	 */
	public void setEtalonParameterTypes(final Set<ParameterTypeEnum> etalonParameterTypes) {
		this.setEtalonParameterTypes0(etalonParameterTypes);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypes0(final Set<ParameterTypeEnum> outParameterTypes) {
		this.outParameterTypes.clear();
		if (outParameterTypes != null) {
			this.outParameterTypes.addAll(outParameterTypes);
		}
	}

	/**
	 * client setter for outParameterTypes
	 *
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypes(final Set<ParameterTypeEnum> outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.markAsChanged();
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setMeasurementTypeIds0(final Set<Identifier> measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null) {
			this.measurementTypeIds.addAll(measurementTypeIds);
		}
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

		if (this.measurementTypeIds != null) {
			dependencies.addAll(this.measurementTypeIds);
		}

		return dependencies;
	}
}
