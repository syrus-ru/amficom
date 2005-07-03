/*
 * $Id: MeasurementSetup.java,v 1.82 2005/06/25 17:07:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetup;

/**
 * @version $Revision: 1.82 $, $Date: 2005/06/25 17:07:41 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class MeasurementSetup extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256442525404443446L;

	private ParameterSet parameterSet;
	private ParameterSet criteriaSet;
	private ParameterSet thresholdSet;
	private ParameterSet etalon;
	private String description;
	private long measurementDuration;

	private Set<Identifier> monitoredElementIds;
	private Set<Identifier> measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MeasurementSetup(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet<Identifier>();
		this.measurementTypeIds = new HashSet<Identifier>();

		final MeasurementSetupDatabase database = (MeasurementSetupDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTSETUP_CODE);
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
	public MeasurementSetup(final IdlMeasurementSetup mst) throws CreateObjectException {
		try {
			this.fromTransferable(mst);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MeasurementSetup(final Identifier id,
			final Identifier creatorId,
			final long version,
			final ParameterSet parameterSet,
			final ParameterSet criteriaSet,
			final ParameterSet thresholdSet,
			final ParameterSet etalon,
			final String description,
			final long measurementDuration,
			final Set<Identifier> monitoredElementIds,
			final Set<Identifier> measurementTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.parameterSet = parameterSet;
		this.criteriaSet = criteriaSet;
		this.thresholdSet = thresholdSet;
		this.etalon = etalon;
		this.description = description;
		this.measurementDuration = measurementDuration;

		this.monitoredElementIds = new HashSet<Identifier>();
		this.setMonitoredElementIds0(monitoredElementIds);

		this.measurementTypeIds = new HashSet<Identifier>();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param parameterSet
	 * @param criteriaSet
	 * @param thresholdSet
	 * @param etalon
	 * @param description
	 * @param measurementDuration
	 * @param monitoredElementIds
	 * @throws CreateObjectException
	 */
	public static MeasurementSetup createInstance(final Identifier creatorId,
			final ParameterSet parameterSet,
			final ParameterSet criteriaSet,
			final ParameterSet thresholdSet,
			final ParameterSet etalon,
			final String description,
			final long measurementDuration,
			final Set<Identifier> monitoredElementIds,
			final Set<Identifier> measurementTypeIds) throws CreateObjectException {

		try {
			final MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTSETUP_CODE),
				creatorId,
				0L,
				parameterSet,
				criteriaSet,
				thresholdSet,
				etalon,
				description,
				measurementDuration,
				monitoredElementIds,
				measurementTypeIds);

			assert measurementSetup.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementSetup.markAsChanged();

			return measurementSetup;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlMeasurementSetup mst = (IdlMeasurementSetup) transferable;
		super.fromTransferable(mst.header);

		this.parameterSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(mst.parameterSetId), true);

		Identifier setId = new Identifier(mst.criteriaSetId);
		this.criteriaSet = (!setId.equals(Identifier.VOID_IDENTIFIER)) ? (ParameterSet) StorableObjectPool.getStorableObject(setId,
				true) : null;

		setId = new Identifier(mst.thresholdSetId);
		this.thresholdSet = (!setId.equals(Identifier.VOID_IDENTIFIER)) ? (ParameterSet) StorableObjectPool.getStorableObject(setId,
				true) : null;

		setId = new Identifier(mst.etalonId);
		this.etalon = (!setId.equals(Identifier.VOID_IDENTIFIER)) ? (ParameterSet) StorableObjectPool.getStorableObject(setId,
				true) : null;

		this.description = mst.description;
		this.measurementDuration = mst.measurementDuration;

		this.monitoredElementIds = Identifier.fromTransferables(mst.monitoredElementIds);
		this.measurementTypeIds = Identifier.fromTransferables(mst.measurementTypeIds);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurementSetup getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		final IdlIdentifier[] mtIds = Identifier.createTransferables(this.measurementTypeIds);

		final IdlIdentifier voidIdlIdentifier = Identifier.VOID_IDENTIFIER.getTransferable();
		return new IdlMeasurementSetup(super.getHeaderTransferable(orb),
				this.parameterSet.getId().getTransferable(),
				(this.criteriaSet != null) ? this.criteriaSet.getId().getTransferable() : voidIdlIdentifier,
				(this.thresholdSet != null) ? this.thresholdSet.getId().getTransferable() : voidIdlIdentifier,
				(this.etalon != null) ? this.etalon.getId().getTransferable() : voidIdlIdentifier,
				this.description,
				this.measurementDuration,
				meIds,
				mtIds);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.parameterSet != null
				&& this.description != null
				&& this.monitoredElementIds != null && !this.monitoredElementIds.isEmpty()
				&& this.measurementTypeIds != null && !this.measurementTypeIds.isEmpty();
	}

	public short getEntityCode() {
		return ObjectEntities.MEASUREMENTSETUP_CODE;
	}

	public ParameterSet getParameterSet() {
		return this.parameterSet;
	}

	public ParameterSet getCriteriaSet() {
		return this.criteriaSet;
	}

	public ParameterSet getThresholdSet() {
		return this.thresholdSet;
	}

	public ParameterSet getEtalon() {
		return this.etalon;
	}

	public String getDescription() {
		return this.description;
	}

	public long getMeasurementDuration() {
		return this.measurementDuration;
	}

	public Set<Identifier> getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	public Set<Identifier> getMeasurementTypeIds() {
		return Collections.unmodifiableSet(this.measurementTypeIds);
	}

	public String[] getParameterTypeCodenames() {
		Parameter[] parameters = this.parameterSet.getParameters();
		String[] parameterTypeCodenames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++)
			parameterTypeCodenames[i] = parameters[i].getType().getCodename();
		return parameterTypeCodenames;
	}

	public byte[][] getParameterValues() {
		Parameter[] parameters = this.parameterSet.getParameters();
		byte[][] parameterValues = new byte[parameters.length][];
		for (int i = 0; i < parameters.length; i++)
			parameterValues[i] = parameters[i].getValue();
		return parameterValues;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final ParameterSet parameterSet,
			final ParameterSet criteriaSet,
			final ParameterSet thresholdSet,
			final ParameterSet etalon,
			final String description,
			final long measurementDuration) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.parameterSet = parameterSet;
		this.criteriaSet = criteriaSet;
		this.thresholdSet = thresholdSet;
		this.etalon = etalon;
		this.description = description;
		this.measurementDuration = measurementDuration;
	}

	public boolean isAttachedToMonitoredElement(final Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(final Identifier monitoredElementId) {
		if (monitoredElementId != null && !this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.add(monitoredElementId);
			super.markAsChanged();
		}
	}

	public void detachFromMonitoredElement(final Identifier monitoredElementId) {
		if (monitoredElementId != null && this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.remove(monitoredElementId);
			super.markAsChanged();
		}
	}

	/**
	 * Clent setter for monitored element ids
	 * @param monitoredElementIds
	 */
	public void setMonitoredElementIds(final Set<Identifier> monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMonitoredElementIds0(final Set<Identifier> monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
			this.monitoredElementIds.addAll(monitoredElementIds);
	}

	/**
	 * Client setter for measurement type ids
	 * @param measurementTypeIds
	 */
	public void setMeasurementTypeIds(final Set<Identifier> measurementTypeIds) {
		this.setMeasurementTypeIds0(measurementTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMeasurementTypeIds0(final Set<Identifier> measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null)
			this.measurementTypeIds.addAll(measurementTypeIds);
	}

	/**
	 * client setter for criteriaSet
	 *
	 * @param criteriaSet
	 *          The criteriaSet to set.
	 */
	public void setCriteriaSet(final ParameterSet criteriaSet) {
		this.criteriaSet = criteriaSet;
		super.markAsChanged();
	}

	/**
	 * client setter for description
	 *
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * client setter for etalon
	 *
	 * @param etalon
	 *            The etalon to set.
	 */
	public void setEtalon(final ParameterSet etalon) {
		this.etalon = etalon;
		super.markAsChanged();
	}

	/**
	 * client setter for measurementDuration
	 *
	 * @param measurementDuration
	 *            The measurementDuration to set.
	 */
	public void setMeasurementDuration(final long measurementDuration) {
		this.measurementDuration = measurementDuration;
		super.markAsChanged();
	}

	/**
	 * client setter for
	 *
	 * @param parameterSet
	 *            The parameterSet to set.
	 */
	public void setParameterSet(final ParameterSet parameterSet) {
		this.parameterSet = parameterSet;
		super.markAsChanged();
	}

	/**
	 * client setter for thresholdSet
	 *
	 * @param thresholdSet
	 *            The thresholdSet to set.
	 */
	public void setThresholdSet(final ParameterSet thresholdSet) {
		this.thresholdSet = thresholdSet;
		super.markAsChanged();
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.parameterSet != null)
			dependencies.add(this.parameterSet);

		if (this.criteriaSet != null)
			dependencies.add(this.criteriaSet);

		if (this.thresholdSet != null)
			dependencies.add(this.thresholdSet);

		if (this.etalon != null)
			dependencies.add(this.etalon);

		dependencies.addAll(this.monitoredElementIds);
		return dependencies;
	}
}
