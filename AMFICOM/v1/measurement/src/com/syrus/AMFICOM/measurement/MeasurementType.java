/*
 * $Id: MeasurementType.java,v 1.96 2005/08/22 15:06:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementTypeHelper;

/**
 * @version $Revision: 1.96 $, $Date: 2005/08/22 15:06:21 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class MeasurementType extends ActionType implements Namable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257570589907562804L;

	public static final String CODENAME_REFLECTOMETRY = "reflectometry";

	private enum ParameterMode {
		MODE_IN("IN"),
		MODE_OUT("OUT");

		private String stringValue;

		private ParameterMode(final String stringValue) {
			this.stringValue = stringValue;
		}

		String stringValue() {
			return this.stringValue;
		}
	}

	private EnumSet<ParameterType> inParameterTypes;
	private EnumSet<ParameterType> outParameterTypes;
	private Set<Identifier> measurementPortTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MeasurementType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = EnumSet.noneOf(ParameterType.class);
		this.outParameterTypes = EnumSet.noneOf(ParameterType.class);
		this.measurementPortTypeIds = new HashSet<Identifier>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementType(final IdlMeasurementType mtt) throws CreateObjectException {
		try {
			this.fromTransferable(mtt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MeasurementType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> outParameterTypes,
			final Set<Identifier> measurementPortTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = EnumSet.noneOf(ParameterType.class);
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = EnumSet.noneOf(ParameterType.class);
		this.setOutParameterTypes0(outParameterTypes);

		this.measurementPortTypeIds = new HashSet<Identifier>();
		this.setMeasurementPortTypeIds0(measurementPortTypeIds);
	}

	/**
	 * Create new instance
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @param measurementPortTypeIds
	 * @throws CreateObjectException
	 */
	public static MeasurementType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> outParameterTypes,
			final Set<Identifier> measurementPortTypeIds) throws CreateObjectException {
		try {
			final MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					inParameterTypes,
					outParameterTypes,
					measurementPortTypeIds);

			assert measurementType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementType.markAsChanged();

			return measurementType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurementType mtt = (IdlMeasurementType) transferable;
		super.fromTransferable(mtt, mtt.codename, mtt.description);

		this.inParameterTypes = ParameterType.fromTransferables(mtt.inParameterTypes);
		this.outParameterTypes = ParameterType.fromTransferables(mtt.outParameterTypes);

		this.measurementPortTypeIds = Identifier.fromTransferables(mtt.measurementPortTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurementType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlParameterType[] inParTypes = ParameterType.createTransferables(this.inParameterTypes, orb);
		final IdlParameterType[] outParTypes = ParameterType.createTransferables(this.outParameterTypes, orb);

		final IdlIdentifier[] measPortTypeIds = Identifier.createTransferables(this.measurementPortTypeIds);

		return IdlMeasurementTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				inParTypes,
				outParTypes,
				measPortTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET && !this.inParameterTypes.contains(null)
				&& this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET && !this.outParameterTypes.contains(null)
				&& this.measurementPortTypeIds != null && this.measurementPortTypeIds != Collections.EMPTY_SET && !this.measurementPortTypeIds.contains(null);
	}

	public EnumSet<ParameterType> getInParameterTypes() {
		return this.inParameterTypes.clone();
	}

	public EnumSet<ParameterType> getOutParameterTypes() {
		return this.outParameterTypes.clone();
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
	protected synchronized void setParameterTypes(final Map<String, EnumSet<ParameterType>> parameterTypesModeMap) {
		assert parameterTypesModeMap != null : ErrorMessages.NON_NULL_EXPECTED;
		this.setInParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_IN.stringValue()));
		this.setOutParameterTypes0(parameterTypesModeMap.get(ParameterMode.MODE_OUT.stringValue()));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, EnumSet<ParameterType>> getParameterTypesModeMap() {
		final Map<String, EnumSet<ParameterType>> parameterTypesModeMap = new HashMap<String, EnumSet<ParameterType>>(2);
		parameterTypesModeMap.put(ParameterMode.MODE_IN.stringValue(), this.inParameterTypes);
		parameterTypesModeMap.put(ParameterMode.MODE_OUT.stringValue(), this.outParameterTypes);
		return parameterTypesModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypes0(final EnumSet<ParameterType> inParameterTypes) {
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
	public void setInParameterTypes(final EnumSet<ParameterType> inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypes0(final EnumSet<ParameterType> outParameterTypes) {
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
	public void setOutParameterTypes(final EnumSet<ParameterType> outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setMeasurementPortTypeIds0(final Set<Identifier> measurementPortTypeIds) {
		this.measurementPortTypeIds.clear();
		if (measurementPortTypeIds != null) {
	     	this.measurementPortTypeIds.addAll(measurementPortTypeIds);
		}
	}

	/**
	 * client setter for measurementPortTypeIds
	 * @param measurementPortTypeIds
	 * 		The measurementPortTypeIds to set
	 */
	public void setMeasurementPortTypeIds(final Set<Identifier> measurementPortTypeIds) {
		this.setMeasurementPortTypeIds0(measurementPortTypeIds);
		super.markAsChanged();		
	}

	public Set<Identifier> getMeasurementPortTypeIds() {
		return Collections.unmodifiableSet(this.measurementPortTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		if (this.measurementPortTypeIds != null) {
			dependencies.addAll(this.measurementPortTypeIds);
		}

		return dependencies;
	}

	public String getName() {
		return getDescription();
	}

	public void setName(final String name) {
		this.setDescription(name);
	}
}
