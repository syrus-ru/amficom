/*
 * $Id: MeasurementSetup.java,v 1.101 2006/03/13 13:53:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetup;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetupHelper;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.101 $, $Date: 2006/03/13 13:53:58 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MeasurementSetup extends StorableObject {
	private static final long serialVersionUID = 3256442525404443446L;

	private ParameterSet parameterSet;
	private ParameterSet criteriaSet;
	private ParameterSet thresholdSet;
	private ParameterSet etalon;
	private String description;
	/**
	 * measurement duration in milliseconds
	 */
	private long measurementDuration;

	private Set<Identifier> monitoredElementIds;
	private EnumSet<MeasurementType> measurementTypes;

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
			final StorableObjectVersion version,
			final ParameterSet parameterSet,
			final ParameterSet criteriaSet,
			final ParameterSet thresholdSet,
			final ParameterSet etalon,
			final String description,
			final long measurementDuration,
			final Set<Identifier> monitoredElementIds,			
			final EnumSet<MeasurementType> measurementTypes) {
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

		this.measurementTypes = EnumSet.noneOf(MeasurementType.class);
		this.setMeasurementTypes0(measurementTypes);
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param parameterSet
	 * @param criteriaSet
	 * @param thresholdSet
	 * @param etalon
	 * @param description
	 * @param measurementDurationMS in milliseconds
	 * @param monitoredElementIds
	 * @throws CreateObjectException
	 */
	public static MeasurementSetup createInstance(final Identifier creatorId,
			final ParameterSet parameterSet,
			final ParameterSet criteriaSet,
			final ParameterSet thresholdSet,
			final ParameterSet etalon,
			final String description,
			final long measurementDurationMS,
			final Set<Identifier> monitoredElementIds,
			final EnumSet<MeasurementType> measurementTypes) throws CreateObjectException {

		try {
			final MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTSETUP_CODE),
				creatorId,
				StorableObjectVersion.INITIAL_VERSION,
				parameterSet,
				criteriaSet,
				thresholdSet,
				etalon,
				description,
				measurementDurationMS,
				monitoredElementIds,
				measurementTypes);

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
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurementSetup mst = (IdlMeasurementSetup) transferable;
		super.fromTransferable(mst);


		this.parameterSet = null;
		this.criteriaSet = null;
		this.thresholdSet = null;
		this.etalon = null;

		final Set<Identifier> parameterSetIds = new HashSet<Identifier>(4);
		parameterSetIds.add(new Identifier(mst.parameterSetId));
		Identifier psId = new Identifier(mst.criteriaSetId);
		if (!psId.isVoid()) {
			parameterSetIds.add(psId);
		}
		psId = new Identifier(mst.thresholdSetId);
		if (!psId.isVoid()) {
			parameterSetIds.add(psId);
		}
		psId = new Identifier(mst.etalonId);
		if (!psId.isVoid()) {
			parameterSetIds.add(psId);
		}

		final Set<ParameterSet> parameterSets = StorableObjectPool.getStorableObjects(parameterSetIds, true);
		for (final ParameterSet ps : parameterSets) {
			switch (ps.getSort().value()) {
				case ParameterSetSort._SET_SORT_MEASUREMENT_PARAMETERS:
					this.parameterSet = ps;
					break;
				case ParameterSetSort._SET_SORT_ANALYSIS_CRITERIA:
					this.criteriaSet = ps;
					break;
				case ParameterSetSort._SET_SORT_EVALUATION_THRESHOLDS:
					this.thresholdSet = ps;
					break;
				case ParameterSetSort._SET_SORT_ETALON:
					this.etalon = ps;
					break;
				default:
					Log.errorMessage("Unknown sort: " + ps.getSort().value() + " of ParameterSet '" + ps.getId()
							+ "' for MeasurementSetup '" + this.id + "'");
			}
		}


		this.description = mst.description;
		this.measurementDuration = mst.measurementDuration;

		this.monitoredElementIds = Identifier.fromTransferables(mst.monitoredElementIds);
		this.measurementTypes = MeasurementType.fromTransferables(mst.measurementTypes);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurementSetup getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		final IdlMeasurementType[] mts = MeasurementType.createTransferables(this.measurementTypes, orb);

		final IdlIdentifier voidIdlIdentifier = VOID_IDENTIFIER.getIdlTransferable();
		return IdlMeasurementSetupHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.parameterSet.getId().getIdlTransferable(),
				(this.criteriaSet != null) ? this.criteriaSet.getId().getIdlTransferable() : voidIdlIdentifier,
				(this.thresholdSet != null) ? this.thresholdSet.getId().getIdlTransferable() : voidIdlIdentifier,
				(this.etalon != null) ? this.etalon.getId().getIdlTransferable() : voidIdlIdentifier,
				this.description,
				this.measurementDuration,
				meIds,
				mts);
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
				&& this.measurementTypes != null && !this.measurementTypes.isEmpty();
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

	public EnumSet<MeasurementType> getMeasurementTypes() {
		return EnumSet.copyOf(this.measurementTypes);
	}

	public String[] getParameterTypeCodenames() {
		final Parameter[] parameters = this.parameterSet.getParameters();
		final String[] parameterTypeCodenames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterTypeCodenames[i] = parameters[i].getType().getCodename();
		}
		return parameterTypeCodenames;
	}

	public byte[][] getParameterValues() {
		final Parameter[] parameters = this.parameterSet.getParameters();
		final byte[][] parameterValues = new byte[parameters.length][];
		for (int i = 0; i < parameters.length; i++) {
			parameterValues[i] = parameters[i].getValue();
		}
		return parameterValues;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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
		if (monitoredElementIds != null) {
			this.monitoredElementIds.addAll(monitoredElementIds);
		}
	}

	/**
	 * Client setter for measurement type ids
	 * @param measurementTypes
	 */
	public void setMeasurementTypes(final EnumSet<MeasurementType> measurementTypes) {
		this.setMeasurementTypes0(measurementTypes);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMeasurementTypes0(final EnumSet<MeasurementType> measurementTypes) {
		this.measurementTypes.clear();
		if (measurementTypes != null) {
			this.measurementTypes.addAll(measurementTypes);
		}
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
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.parameterSet != null) {
			dependencies.add(this.parameterSet);
		}

		if (this.criteriaSet != null) {
			dependencies.add(this.criteriaSet);
		}

		if (this.thresholdSet != null) {
			dependencies.add(this.thresholdSet);
		}

		if (this.etalon != null) {
			dependencies.add(this.etalon);
		}

		dependencies.addAll(this.monitoredElementIds);

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementSetupWrapper getWrapper() {
		return MeasurementSetupWrapper.getInstance();
	}
}
