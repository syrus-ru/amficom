/*
 * $Id: MeasurementType.java,v 1.71 2005/05/18 11:34:41 bass Exp $
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

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;

/**
 * @version $Revision: 1.71 $, $Date: 2005/05/18 11:34:41 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class MeasurementType extends ActionType implements Namable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257570589907562804L;

	public static final String CODENAME_REFLECTOMETRY = "reflectometry";

	private java.util.Set inParameterTypeIds;
	private java.util.Set outParameterTypeIds;
	private java.util.Set measurementPortTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet();
		this.outParameterTypeIds = new HashSet();
		this.measurementPortTypeIds = new HashSet();

		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
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
	public MeasurementType(MeasurementType_Transferable mtt) throws CreateObjectException {
		try {
			this.fromTransferable(mtt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected MeasurementType(Identifier id,
							  Identifier creatorId,
							  long version,
							  String codename,
							  String description,
							  java.util.Set inParameterTypeIds,
							  java.util.Set outParameterTypeIds,
							  java.util.Set measurementPortTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypeIds = new HashSet();
		this.setInParameterTypeIds0(inParameterTypeIds);

		this.outParameterTypeIds = new HashSet();
		this.setOutParameterTypeIds0(outParameterTypeIds);

		this.measurementPortTypeIds = new HashSet();
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
	public static MeasurementType createInstance(Identifier creatorId,
												 String codename,
												 String description,
												 java.util.Set inParameterTypeIds,
												 java.util.Set outParameterTypeIds,
												 java.util.Set measurementPortTypeIds) throws CreateObjectException {
		try {
			MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					outParameterTypeIds,
					measurementPortTypeIds);
			
			assert measurementType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			measurementType.changed = true;
			return measurementType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MeasurementType_Transferable mtt = (MeasurementType_Transferable) transferable;
		super.fromTransferable(mtt.header, mtt.codename, mtt.description);

		this.inParameterTypeIds = Identifier.fromTransferables(mtt.in_parameter_type_ids);
		this.outParameterTypeIds = Identifier.fromTransferables(mtt.out_parameter_type_ids);
		this.measurementPortTypeIds = Identifier.fromTransferables(mtt.measurement_port_type_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		Identifier_Transferable[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		Identifier_Transferable[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		Identifier_Transferable[] measPortTypeIds = Identifier.createTransferables(this.measurementPortTypeIds);

		return new MeasurementType_Transferable(super.getHeaderTransferable(),
												super.codename,
												super.description != null ? super.description : "",
												inParTypeIds,	
												outParTypeIds,
												measPortTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET &&
			this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET &&
			this.measurementPortTypeIds != null && !this.measurementPortTypeIds.isEmpty();
	}
	
	public java.util.Set getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public java.util.Set getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  String codename,
											  String description) {
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
	protected synchronized void setParameterTypeIds(Map parameterTypeIdsModeMap) {
		this.setInParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_IN));
		this.setOutParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Map getParameterTypeIdsModeMap() {
		Map parameterTypeIdsModeMap = new HashMap(2);
		parameterTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_OUT, this.outParameterTypeIds);
		return parameterTypeIdsModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypeIds0(java.util.Set inParameterTypeIds) {
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
	public void setInParameterTypeIds(java.util.Set inParameterTypeIds) {
		this.setInParameterTypeIds0(inParameterTypeIds);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypeIds0(java.util.Set outParameterTypeIds) {
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
	public void setOutParameterTypeIds(java.util.Set outParameterTypeIds) {
		this.setOutParameterTypeIds0(outParameterTypeIds);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setMeasurementPortTypeIds0(java.util.Set measurementPortTypeIds) {
		this.measurementPortTypeIds.clear();
		if (measurementPortTypeIds != null)
	     	this.measurementPortTypeIds.addAll(measurementPortTypeIds);
	}

	/**
	 * client setter for measurementPortTypeIds
	 * @param measurementPortTypeIds
	 * 		The measurementPortTypeIds to set
	 */
	public void setMeasurementPortTypeIds(java.util.Set measurementPortTypeIds) {
		this.setMeasurementPortTypeIds0(measurementPortTypeIds);
		super.changed = true;		
	}

	public java.util.Set getMeasurementPortTypeIds() {
		return Collections.unmodifiableSet(this.measurementPortTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();

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
		setDescription(name);
	}
}
