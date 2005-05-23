/*
 * $Id: MeasurementSetup.java,v 1.68 2005/05/23 18:45:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

/**
 * @version $Revision: 1.68 $, $Date: 2005/05/23 18:45:15 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class MeasurementSetup extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256442525404443446L;

	private Set parameterSet;
	private Set criteriaSet;
	private Set thresholdSet;
	private Set etalon;
	private String description;
	private long measurementDuration;

	private java.util.Set monitoredElementIds;
	private java.util.Set measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementSetup(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet();
		this.measurementTypeIds = new HashSet();

		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementSetup(MeasurementSetup_Transferable mst) throws CreateObjectException {
		try {
			this.fromTransferable(mst);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected MeasurementSetup(Identifier id,
							   Identifier creatorId,
							   long version,
							   Set parameterSet,
							   Set criteriaSet,
							   Set thresholdSet,
							   Set etalon,
							   String description,
							   long measurementDuration,
							   java.util.Set monitoredElementIds,
							   java.util.Set measurementTypeIds) {
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

		this.monitoredElementIds = new HashSet();
		this.setMonitoredElementIds0(monitoredElementIds);

		this.measurementTypeIds = new HashSet();
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
	public static MeasurementSetup createInstance(Identifier creatorId,
												  Set parameterSet,
												  Set criteriaSet,
												  Set thresholdSet,
												  Set etalon,
												  String description,
												  long measurementDuration,
												  java.util.Set monitoredElementIds,
												  java.util.Set measurementTypeIds) throws CreateObjectException {

		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE),
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
			
			measurementSetup.changed = true;
			return measurementSetup;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MeasurementSetup_Transferable mst = (MeasurementSetup_Transferable)transferable;
		super.fromTransferable(mst.header);

		this.parameterSet = (Set) StorableObjectPool.getStorableObject(new Identifier(mst.parameter_set_id), true);
		/**
		 * @todo when change DB Identifier model ,change identifier_string
		 *       to identifier_code
		 */
		this.criteriaSet = (mst.criteria_set_id.identifier_string.length() != 0)
				? (Set) StorableObjectPool.getStorableObject(new Identifier(mst.criteria_set_id), true) : null;
		/**
		 * @todo when change DB Identifier model ,change identifier_string
		 *       to identifier_code
		 */
		this.thresholdSet = (mst.threshold_set_id.identifier_string.length() != 0)
				? (Set) StorableObjectPool.getStorableObject(new Identifier(mst.threshold_set_id), true) : null;
		/**
		 * @todo when change DB Identifier model ,change identifier_string
		 *       to identifier_code
		 */
		this.etalon = (mst.etalon_id.identifier_string.length() != 0)
				? (Set) StorableObjectPool.getStorableObject(new Identifier(mst.etalon_id), true) : null;

		this.description = mst.description;
		this.measurementDuration = mst.measurement_duration;

		this.monitoredElementIds = Identifier.fromTransferables(mst.monitored_element_ids);
		this.measurementTypeIds = Identifier.fromTransferables(mst.measurement_type_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		Identifier_Transferable[] meIds = Identifier.createTransferables(this.monitoredElementIds);
		Identifier_Transferable[] mtIds = Identifier.createTransferables(this.measurementTypeIds);
		return new MeasurementSetup_Transferable(super.getHeaderTransferable(),
												 (Identifier_Transferable) this.parameterSet.getId().getTransferable(),
												 (this.criteriaSet != null) ? (Identifier_Transferable) this.criteriaSet.getId().getTransferable() : (new Identifier_Transferable("")),
												 (this.thresholdSet != null) ? (Identifier_Transferable) this.thresholdSet.getId().getTransferable() : (new Identifier_Transferable("")),
												 (this.etalon != null) ? (Identifier_Transferable) this.etalon.getId().getTransferable() : (new Identifier_Transferable("")),
												 this.description,
												 this.measurementDuration,
												 meIds,
												 mtIds);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.parameterSet != null && this.description != null && this.monitoredElementIds != null && !this.monitoredElementIds.isEmpty() &&
			this.measurementTypeIds != null && !this.measurementTypeIds.isEmpty();
	}

    public short getEntityCode() {
        return ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE;
    }

    public Set getParameterSet() {
		return this.parameterSet;
	}

	public Set getCriteriaSet() {
		return this.criteriaSet;
	}

	public Set getThresholdSet() {
		return this.thresholdSet;
	}

	public Set getEtalon() {
		return this.etalon;
	}

	public String getDescription() {
		return this.description;
	}

	public long getMeasurementDuration() {
		return this.measurementDuration;
	}

	public java.util.Set getMonitoredElementIds() {
		return Collections.unmodifiableSet(this.monitoredElementIds);
	}

	public java.util.Set getMeasurementTypeIds() {
		return Collections.unmodifiableSet(this.measurementTypeIds);
	}

	public String[] getParameterTypeCodenames() {
		SetParameter[] parameters = this.parameterSet.getParameters();
		String[] parameterTypeCodenames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++)
			parameterTypeCodenames[i] = parameters[i].getType().getCodename();
		return parameterTypeCodenames;
	}

	public byte[][] getParameterValues() {
		SetParameter[] parameters = this.parameterSet.getParameters();
		byte[][] parameterValues = new byte[parameters.length][];
		for (int i = 0; i < parameters.length; i++)
			parameterValues[i] = parameters[i].getValue();
		return parameterValues;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  Set parameterSet,
											  Set criteriaSet,
											  Set thresholdSet,
											  Set etalon,
											  String description,
											  long measurementDuration) {
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

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId) {
		if (monitoredElementId != null && !this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.add(monitoredElementId);
			super.changed = true;
		}
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId) {
		if (monitoredElementId != null && this.isAttachedToMonitoredElement(monitoredElementId)) {
			this.monitoredElementIds.remove(monitoredElementId);
			super.changed = true;
		}
	}

	/**
	 * Clent setter for monitored element ids
	 * @param monitoredElementIds
	 */
	public void setMonitoredElementIds(java.util.Set monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMonitoredElementIds0(java.util.Set monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
			this.monitoredElementIds.addAll(monitoredElementIds);
	}

	/**
	 * Client setter for measurement type ids
	 * @param measurementTypeIds
	 */
	public void setMeasurementTypeIds(java.util.Set measurementTypeIds) {
		this.setMeasurementTypeIds0(measurementTypeIds);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMeasurementTypeIds0(java.util.Set measurementTypeIds) {
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
	public void setCriteriaSet(Set criteriaSet) {
		super.changed = true;
		this.criteriaSet = criteriaSet;
	}

	/**
	 * client setter for description
	 *
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		super.changed = true;
		this.description = description;
	}

	/**
	 * client setter for etalon
	 *
	 * @param etalon
	 *            The etalon to set.
	 */
	public void setEtalon(Set etalon) {
		super.changed = true;
		this.etalon = etalon;
	}

	/**
	 * client setter for measurementDuration
	 *
	 * @param measurementDuration
	 *            The measurementDuration to set.
	 */
	public void setMeasurementDuration(long measurementDuration) {
		super.changed = true;
		this.measurementDuration = measurementDuration;
	}

	/**
	 * client setter for
	 *
	 * @param parameterSet
	 *            The parameterSet to set.
	 */
	public void setParameterSet(Set parameterSet) {
		super.changed = true;
		this.parameterSet = parameterSet;
	}

	/**
	 * client setter for thresholdSet
	 *
	 * @param thresholdSet
	 *            The thresholdSet to set.
	 */
	public void setThresholdSet(Set thresholdSet) {
		super.changed = true;
		this.thresholdSet = thresholdSet;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
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
