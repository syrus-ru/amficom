/*
 * $Id: MeasurementType.java,v 1.83 2005/06/25 17:07:41 bass Exp $
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;

/**
 * @version $Revision: 1.83 $, $Date: 2005/06/25 17:07:41 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class MeasurementType extends ActionType implements Namable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257570589907562804L;

	public static final String CODENAME_REFLECTOMETRY = "reflectometry";

	private Set<Identifier> inParameterTypeIds;
	private Set<Identifier> outParameterTypeIds;
	private Set<Identifier> measurementPortTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MeasurementType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.outParameterTypeIds = new HashSet<Identifier>();
		this.measurementPortTypeIds = new HashSet<Identifier>();

		MeasurementTypeDatabase database = (MeasurementTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_TYPE_CODE);
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
			final long version,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> outParameterTypeIds,
			final Set<Identifier> measurementPortTypeIds) {
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

		this.outParameterTypeIds = new HashSet<Identifier>();
		this.setOutParameterTypeIds0(outParameterTypeIds);

		this.measurementPortTypeIds = new HashSet<Identifier>();
		this.setMeasurementPortTypeIds0(measurementPortTypeIds);
	}

	/**
	 * Create new instance
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param outParameterTypeIds
	 * @param measurementPortTypeIds
	 * @throws CreateObjectException
	 */
	public static MeasurementType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> outParameterTypeIds,
			final Set<Identifier> measurementPortTypeIds) throws CreateObjectException {
		try {
			final MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					outParameterTypeIds,
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
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlMeasurementType mtt = (IdlMeasurementType) transferable;
		super.fromTransferable(mtt.header, mtt.codename, mtt.description);

		this.inParameterTypeIds = Identifier.fromTransferables(mtt.inParameterTypeIds);
		this.outParameterTypeIds = Identifier.fromTransferables(mtt.outParameterTypeIds);
		this.measurementPortTypeIds = Identifier.fromTransferables(mtt.measurementPortTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurementType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		final IdlIdentifier[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		final IdlIdentifier[] measPortTypeIds = Identifier.createTransferables(this.measurementPortTypeIds);

		return new IdlMeasurementType(super.getHeaderTransferable(orb),
				super.codename,
				super.description != null ? super.description : "",
				inParTypeIds,
				outParTypeIds,
				measPortTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET
				&& this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET
				&& this.measurementPortTypeIds != null && this.measurementPortTypeIds != Collections.EMPTY_SET;
	}
	
	public Set<Identifier> getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public Set<Identifier> getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
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
		this.setInParameterTypeIds0(parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_IN));
		this.setOutParameterTypeIds0(parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<Identifier>> getParameterTypeIdsModeMap() {
		final Map<String, Set<Identifier>> parameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>(2);
		parameterTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_OUT, this.outParameterTypeIds);
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
	protected void setMeasurementPortTypeIds0(final Set<Identifier> measurementPortTypeIds) {
		this.measurementPortTypeIds.clear();
		if (measurementPortTypeIds != null)
	     	this.measurementPortTypeIds.addAll(measurementPortTypeIds);
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

		if (this.inParameterTypeIds != null)
			dependencies.addAll(this.inParameterTypeIds);

		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);

		if (this.measurementPortTypeIds != null)
			dependencies.addAll(this.measurementPortTypeIds);

		return dependencies;
	}

	public String getName() {
		return getDescription();
	}

	public void setName(final String name) {
		this.setDescription(name);
	}
}
