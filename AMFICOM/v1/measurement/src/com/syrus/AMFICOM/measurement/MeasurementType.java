/*
 * $Id: MeasurementType.java,v 1.66 2005/04/14 16:05:37 bass Exp $
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.66 $, $Date: 2005/04/14 16:05:37 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class MeasurementType extends ActionType implements Namable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257570589907562804L;

	public static final String CODENAME_REFLECTOMETRY = "reflectometry";

	private java.util.Set inParameterTypes;
	private java.util.Set outParameterTypes;
	private java.util.Set measurementPortTypes;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet();
		this.outParameterTypes = new HashSet();
		this.measurementPortTypes = new HashSet();

		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.measurementPortTypes.iterator(); it.hasNext();)
				ConfigurationStorableObjectPool.putStorableObject((MeasurementPortType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
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
							  java.util.Set inParameterTypes,
							  java.util.Set outParameterTypes,
							  java.util.Set measurementPortTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new HashSet(); 
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = new HashSet();
		this.setOutParameterTypes0(outParameterTypes);

		this.measurementPortTypes = new HashSet();
		this.setMeasurementPortTypes0(measurementPortTypes);
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @throws CreateObjectException
	 */
	public static MeasurementType createInstance(Identifier creatorId,
												 String codename,
												 String description,
												 java.util.Set inParameterTypes,
												 java.util.Set outParameterTypes,
												 java.util.Set measurementPortTypes) throws CreateObjectException {
		try {
			MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										inParameterTypes,
										outParameterTypes,
										measurementPortTypes);
			
			assert measurementType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			measurementType.changed = true;
			return measurementType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementType.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MeasurementType_Transferable mtt = (MeasurementType_Transferable) transferable;
		super.fromTransferable(mtt.header, mtt.codename, mtt.description);

		java.util.Set typeIds;

		typeIds = Identifier.fromTransferables(mtt.in_parameter_type_ids);
		this.inParameterTypes = new HashSet(mtt.in_parameter_type_ids.length);
		this.setInParameterTypes0(GeneralStorableObjectPool.getStorableObjects(typeIds, true));

		typeIds = Identifier.fromTransferables(mtt.out_parameter_type_ids);
		this.outParameterTypes = new HashSet(mtt.out_parameter_type_ids.length);
		this.setOutParameterTypes0(GeneralStorableObjectPool.getStorableObjects(typeIds, true));

		typeIds = Identifier.fromTransferables(mtt.measurement_port_type_ids);
		this.measurementPortTypes = new HashSet(mtt.measurement_port_type_ids.length);
		this.setMeasurementPortTypes0(ConfigurationStorableObjectPool.getStorableObjects(typeIds, true));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();
		
		Identifier_Transferable[] measurementPortTypeIds = new Identifier_Transferable[this.measurementPortTypes.size()];
		i = 0;
		for (Iterator iterator = this.measurementPortTypes.iterator(); iterator.hasNext();){
			MeasurementPortType measurementPortType = (MeasurementPortType) iterator.next();
			if (measurementPortType != null)
				measurementPortTypeIds[i++] = (Identifier_Transferable) measurementPortType.getId().getTransferable();
		}

		return new MeasurementType_Transferable(super.getHeaderTransferable(),
												super.codename,
												super.description != null ? super.description : "",
												inParTypeIds,	
												outParTypeIds,
												measurementPortTypeIds);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET &&
			this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET &&
			this.measurementPortTypes != null && !this.measurementPortTypes.isEmpty();
	}
	
	public java.util.Set getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public java.util.Set getOutParameterTypes() {
		return Collections.unmodifiableSet(this.outParameterTypes);
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
	protected synchronized void setParameterTypes(java.util.Set inParameterTypes, java.util.Set outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypes0(java.util.Set inParameterTypes) {
		this.inParameterTypes.clear();
		if (inParameterTypes != null)
			this.inParameterTypes.addAll(inParameterTypes);
	}

	/**
	 * client setter for inParameterTypes
	 * 
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypes(java.util.Set inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypes0(java.util.Set outParameterTypes) {
		this.outParameterTypes.clear();
		if (outParameterTypes != null)
			this.outParameterTypes.addAll(outParameterTypes);
	}

	/**
	 * client setter for outParameterTypes
	 * 
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypes(java.util.Set outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setMeasurementPortTypes0(java.util.Set measurementPortTypes) {
		this.measurementPortTypes.clear();
		if (measurementPortTypes != null)
	     	this.measurementPortTypes.addAll(measurementPortTypes);
	}

	/**
	 * client setter for measurementPortTypes
	 * @param measurementPortTypes
	 * 		The measurementPortTypes to set
	 */
	public void setMeasurementPortTypes(java.util.Set measurementPortTypes) {
		this.setMeasurementPortTypes0(measurementPortTypes);
		super.changed = true;		
	}

	public java.util.Set getMeasurementPortTypes() {
		return Collections.unmodifiableSet(this.measurementPortTypes);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();

		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);

		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);

		if (this.measurementPortTypes != null)
			dependencies.addAll(this.measurementPortTypes);

		return dependencies;
	}

	public String getName() {
		return getDescription();
	}

	public void setName(final String name) {
		setDescription(name);
	}
}
