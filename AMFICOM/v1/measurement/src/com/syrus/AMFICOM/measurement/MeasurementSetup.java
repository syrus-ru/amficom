/*
 * $Id: MeasurementSetup.java,v 1.27 2004/09/01 15:08:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;

/**
 * @version $Revision: 1.27 $, $Date: 2004/09/01 15:08:11 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementSetup extends StorableObject {

	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;

	private Set parameterSet;
	private Set criteriaSet;
	private Set thresholdSet;
	private Set etalon;
	private String description;
	private long measurementDuration;
	private List monitoredElementIds;

	private StorableObjectDatabase measurementSetupDatabase;

	public MeasurementSetup(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.measurementSetupDatabase = MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			this.measurementSetupDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MeasurementSetup(MeasurementSetup_Transferable mst) throws CreateObjectException {
		super(new Identifier(mst.id),
					new Date(mst.created),
					new Date(mst.modified),
					new Identifier(mst.creator_id),
					new Identifier(mst.modifier_id));

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
		this.monitoredElementIds = new ArrayList(mst.monitored_element_ids.length);
		for (int i = 0; i < mst.monitored_element_ids.length; i++)
			this.monitoredElementIds.add(new Identifier(mst.monitored_element_ids[i]));
		
	}

	protected MeasurementSetup(Identifier id,
													 Identifier creatorId,
													 Set parameterSet,
													 Set criteriaSet,
													 Set thresholdSet,
													 Set etalon,
													 String description,
													 long measurementDuration,
													 List monitoredElementIds) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.parameterSet = parameterSet;
		this.criteriaSet = criteriaSet;
		this.thresholdSet = thresholdSet;
		this.etalon = etalon;
		this.description = description;
		this.measurementDuration = measurementDuration;
		this.monitoredElementIds = monitoredElementIds;
		
		this.measurementSetupDatabase = MeasurementDatabaseContext.measurementSetupDatabase;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param parameterSet
	 * @param criteriaSet
	 * @param thresholdSet
	 * @param etalon
	 * @param description
	 * @param measurementDuration
	 * @param monitoredElementIds
	 * @return
	 */
	public static MeasurementSetup createInstance(Identifier id,
																								Identifier creatorId,
																								Set parameterSet,
																								Set criteriaSet,
																								Set thresholdSet,
																								Set etalon,
																								String description,
																								long measurementDuration,
																								List monitoredElementIds) {
		return new MeasurementSetup(id,
																creatorId,
																parameterSet,
																criteriaSet,
																thresholdSet,
																etalon,
																description,
																measurementDuration,
																monitoredElementIds);
	}
	
	public static MeasurementSetup getInstance(MeasurementSetup_Transferable mst) throws CreateObjectException {
		MeasurementSetup measurementSetup = new MeasurementSetup(mst);
		
		measurementSetup.measurementSetupDatabase = MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			if (measurementSetup.measurementSetupDatabase != null)
				measurementSetup.measurementSetupDatabase.insert(measurementSetup);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return measurementSetup;
	}

	public boolean isAttachedToMonitoredElement(Identifier monitoredElementId) {
		return this.monitoredElementIds.contains(monitoredElementId);
	}

	public void attachToMonitoredElement(Identifier monitoredElementId, Identifier modifierId) throws UpdateObjectException {
		if (this.isAttachedToMonitoredElement(monitoredElementId))
			return;
		super.modifierId = (Identifier) modifierId.clone();
		try {
			this.measurementSetupDatabase.update(this, UPDATE_ATTACH_ME, monitoredElementId);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(
											"MeasurementSetup.attachToMonitoredElement | Cannot attach measurement setup '"
													+ this.id + "' to monitored element '" + monitoredElementId
													+ "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.add(monitoredElementId);
		//this.monitoredElementIds.trimToSize();
	}

	public void detachFromMonitoredElement(Identifier monitoredElementId, Identifier modifierId) throws UpdateObjectException {
		if (!this.isAttachedToMonitoredElement(monitoredElementId))
			return;
		super.modifierId = (Identifier) modifierId.clone();
		try {
			this.measurementSetupDatabase.update(this, UPDATE_DETACH_ME, monitoredElementId);
		}
		catch (Exception e) {
			throw new UpdateObjectException(
											"MeasurementSetup.detachFromMonitoredElement | Cannot dettach measurement setup '"
													+ this.id + "' from monitored element '" + monitoredElementId
													+ "' -- " + e.getMessage(), e);
		}
		this.monitoredElementIds.remove(monitoredElementId);
		//this.monitoredElementIds.trimToSize();
	}

	public Object getTransferable() {
		Identifier_Transferable[] meIds = new Identifier_Transferable[this.monitoredElementIds.size()];
		for (int i = 0; i < meIds.length; i++)
			meIds[i] = (Identifier_Transferable) ((Identifier) this.monitoredElementIds.get(i)).getTransferable();

		return new MeasurementSetup_Transferable((Identifier_Transferable) super.id.getTransferable(),
																						 super.created.getTime(), super.modified.getTime(),
																						 (Identifier_Transferable) super.creatorId.getTransferable(),
																						 (Identifier_Transferable) super.modifierId.getTransferable(),
																						 (Identifier_Transferable) this.parameterSet.getId().getTransferable(),
																						 (this.criteriaSet != null) ? (Identifier_Transferable) this.criteriaSet.getId().getTransferable() : (new Identifier_Transferable("")),
																						 (this.thresholdSet != null) ? (Identifier_Transferable) this.thresholdSet.getId().getTransferable() : (new Identifier_Transferable("")),
																						 (this.etalon != null) ? (Identifier_Transferable) this.etalon.getId().getTransferable() : (new Identifier_Transferable("")),
																						 this.description,
																						 this.measurementDuration,
																						 meIds);
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

	public List getMonitoredElementIds() {
		return this.monitoredElementIds;
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
																						Set parameterSet,
																						Set criteriaSet,
																						Set thresholdSet,
																						Set etalon,
																						String description,
																						long measurementDuration) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.parameterSet = parameterSet;
		this.criteriaSet = criteriaSet;
		this.thresholdSet = thresholdSet;
		this.etalon = etalon;
		this.description = description;
		this.measurementDuration = measurementDuration;
	}

	protected synchronized void setMonitoredElementIds(List monitoredElementIds) {
		this.monitoredElementIds = monitoredElementIds;
	}

	/**
	 * client setter for criteriaSet
	 * 
	 * @param criteriaSet
	 *            The criteriaSet to set.
	 */
	public void setCriteriaSet(Set criteriaSet) {
		this.currentVersion = super.getNextVersion();
		this.criteriaSet = criteriaSet;
	}

	/**
	 * client setter for description
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.currentVersion = super.getNextVersion();
		this.description = description;
	}

	/**
	 * client setter for etalon
	 * 
	 * @param etalon
	 *            The etalon to set.
	 */
	public void setEtalon(Set etalon) {
		this.currentVersion = super.getNextVersion();
		this.etalon = etalon;
	}

	/**
	 * client setter for measurementDuration
	 * 
	 * @param measurementDuration
	 *            The measurementDuration to set.
	 */
	public void setMeasurementDuration(long measurementDuration) {
		this.currentVersion = super.getNextVersion();
		this.measurementDuration = measurementDuration;
	}

	/**
	 * client setter for
	 * 
	 * @param parameterSet
	 *            The parameterSet to set.
	 */
	public void setParameterSet(Set parameterSet) {
		this.currentVersion = super.getNextVersion();
		this.parameterSet = parameterSet;
	}

	/**
	 * client setter for thresholdSet
	 * 
	 * @param thresholdSet
	 *            The thresholdSet to set.
	 */
	public void setThresholdSet(Set thresholdSet) {
		this.currentVersion = super.getNextVersion();
		this.thresholdSet = thresholdSet;
	}
}
