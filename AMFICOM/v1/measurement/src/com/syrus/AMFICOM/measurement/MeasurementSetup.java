/*
 * $Id: MeasurementSetup.java,v 1.52 2005/04/01 15:40:18 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

/**
 * @version $Revision: 1.52 $, $Date: 2005/04/01 15:40:18 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementSetup extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256442525404443446L;
	/**
	 * @deprecated
	 */
	protected static final int		UPDATE_ATTACH_ME	= 1;
	/**
	 * @deprecated
	 */
	protected static final int		UPDATE_DETACH_ME	= 2;

	private Set parameterSet;
	private Set criteriaSet;
	private Set thresholdSet;
	private Set etalon;
	private String description;
	private long measurementDuration;
	private java.util.Set monitoredElementIds;

	private StorableObjectDatabase measurementSetupDatabase;

	public MeasurementSetup(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.monitoredElementIds = new HashSet();

		this.measurementSetupDatabase = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		try {
			this.measurementSetupDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MeasurementSetup(MeasurementSetup_Transferable mst) throws CreateObjectException {
		this.measurementSetupDatabase = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		this.fromTransferable(mst);
	}

	protected MeasurementSetup(Identifier id,
							   Identifier creatorId,
							   long version,
							   Set parameterSet,
							   Set criteriaSet,
							   Set thresholdSet,
							   Set etalon,
							   String description,
							   long measurementDuration,
							   java.util.Set monitoredElementIds) {
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
		
		this.measurementSetupDatabase = MeasurementDatabaseContext.getMeasurementSetupDatabase();
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
												  java.util.Set monitoredElementIds) throws CreateObjectException {
		
		if (creatorId == null || description == null || parameterSet == null || monitoredElementIds == null)
			throw new IllegalArgumentException("Argument is 'null'");
	
		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MS_ENTITY_CODE),
				creatorId,
				0L,
				parameterSet,
				criteriaSet,
				thresholdSet,
				etalon,
				description,
				measurementDuration,
				monitoredElementIds);
			measurementSetup.changed = true;
			return measurementSetup;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementSetup.createInstance | cannot generate identifier ", e);
		}
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId, Identifier modifierId1) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitoredElementId))
			return;
		super.modifierId = modifierId1;
		this.monitoredElementIds.add(monitoredElementId);
		try {
			this.measurementSetupDatabase.update(this, modifierId1, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (VersionCollisionException vce){
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
		this.monitoredElementIds.add(monitoredElementId);
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId, Identifier modifierId1) throws UpdateObjectException {
		if (!this.isAttachedToMonitoredElement(monitoredElementId))
			return;
		super.modifierId = modifierId1;
		this.monitoredElementIds.remove(monitoredElementId);
		try {
			this.measurementSetupDatabase.update(this, modifierId1, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (VersionCollisionException vce) {
			throw new UpdateObjectException(
											"MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '"
													+ this.id + "' from monitored element '" + monitoredElementId
													+ "' -- " + vce.getMessage(), vce);
		}
		this.monitoredElementIds.remove(monitoredElementId);
		//this.monitoredElementIds.trimToSize();
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		MeasurementSetup_Transferable mst = (MeasurementSetup_Transferable)transferable;
		super.fromTransferable(mst.header);

		try {
			this.parameterSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mst.parameter_set_id), true);
			/**
			 * @todo when change DB Identifier model ,change identifier_string
			 *       to identifier_code
			 */
			this.criteriaSet = (mst.criteria_set_id.identifier_string.length() != 0) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mst.criteria_set_id), true) : null;
			/**
			 * @todo when change DB Identifier model ,change identifier_string
			 *       to identifier_code
			 */
			this.thresholdSet = (mst.threshold_set_id.identifier_string.length() != 0) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mst.threshold_set_id), true) : null;
			/**
			 * @todo when change DB Identifier model ,change identifier_string
			 *       to identifier_code
			 */
			this.etalon = (mst.etalon_id.identifier_string.length() != 0) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mst.etalon_id), true) : null;
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.description = new String(mst.description);
		this.measurementDuration = mst.measurement_duration;
		this.monitoredElementIds = new HashSet(mst.monitored_element_ids.length);
		for (int i = 0; i < mst.monitored_element_ids.length; i++)
			this.monitoredElementIds.add(new Identifier(mst.monitored_element_ids[i]));
	}

	public Object getTransferable() {
		Identifier_Transferable[] meIds = new Identifier_Transferable[this.monitoredElementIds.size()];
		int i = 0;
		for (Iterator it = this.monitoredElementIds.iterator(); it.hasNext(); i++)
			meIds[i] = (Identifier_Transferable) ((Identifier) it.next()).getTransferable();

		return new MeasurementSetup_Transferable(super.getHeaderTransferable(),
												 (Identifier_Transferable) this.parameterSet.getId().getTransferable(),
												 (this.criteriaSet != null) ? (Identifier_Transferable) this.criteriaSet.getId().getTransferable() : (new Identifier_Transferable("")),
												 (this.thresholdSet != null) ? (Identifier_Transferable) this.thresholdSet.getId().getTransferable() : (new Identifier_Transferable("")),
												 (this.etalon != null) ? (Identifier_Transferable) this.etalon.getId().getTransferable() : (new Identifier_Transferable("")),
												 this.description,
												 this.measurementDuration,
												 meIds);
	}

    public short getEntityCode() {
        return ObjectEntities.MS_ENTITY_CODE;
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

	protected synchronized void setMonitoredElementIds0(java.util.Set monitoredElementIds) {
		this.monitoredElementIds.clear();
		if (monitoredElementIds != null)
			this.monitoredElementIds.addAll(monitoredElementIds);
	}

	public void setMonitoredElementIds(java.util.Set monitoredElementIds) {
		this.setMonitoredElementIds0(monitoredElementIds);
		super.changed = true;
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
	
	public java.util.Set getDependencies() {
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
