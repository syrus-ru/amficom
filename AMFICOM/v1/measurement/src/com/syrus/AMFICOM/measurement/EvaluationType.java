/*
 * $Id: EvaluationType.java,v 1.60 2005/04/13 15:26:00 arseniy Exp $
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
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.60 $, $Date: 2005/04/13 15:26:00 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3978420317305583161L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypes;
	private java.util.Set thresholdParameterTypes;
	private java.util.Set etalonParameterTypes;
	private java.util.Set outParameterTypes;

	private java.util.Set measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public EvaluationType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet();
		this.thresholdParameterTypes = new HashSet();
		this.etalonParameterTypes = new HashSet();
		this.outParameterTypes = new HashSet();

		this.measurementTypeIds = new HashSet();

		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.thresholdParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.etalonParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public EvaluationType(EvaluationType_Transferable ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}	
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected EvaluationType(Identifier id,
							 Identifier creatorId,
							 long version,
							 String codename,
							 String description,
							 java.util.Set inParameterTypes,
							 java.util.Set thresholdParameterTypes,
							 java.util.Set etalonParameterTypes,
							 java.util.Set outParameterTypes,
						   java.util.Set measurementTypeIds) {
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

		this.thresholdParameterTypes = new HashSet();
		this.setThresholdParameterTypes0(thresholdParameterTypes);

		this.etalonParameterTypes = new HashSet();
		this.setEtalonParameterTypes0(etalonParameterTypes);

		this.outParameterTypes = new HashSet();
		this.setOutParameterTypes0(outParameterTypes);


		this.measurementTypeIds = new HashSet();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param thresholdParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static EvaluationType createInstance(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypes,
			java.util.Set thresholdParameterTypes,
			java.util.Set etalonParameterTypes,
			java.util.Set outParameterTypes,
			java.util.Set measurementTypeIds) throws CreateObjectException {
		try {
			EvaluationType evaluationType = new EvaluationType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypes,
					thresholdParameterTypes,
					etalonParameterTypes,
					outParameterTypes,
					measurementTypeIds);
			assert evaluationType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			evaluationType.changed = true;
			return evaluationType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("EvaluationType.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		EvaluationType_Transferable ett = (EvaluationType_Transferable)transferable;
		super.fromTransferable(ett.header, ett.codename, ett.description);

		java.util.Set parTypIds;

		parTypIds = Identifier.fromTransferables(ett.in_parameter_type_ids);
		this.inParameterTypes = new HashSet(ett.in_parameter_type_ids.length);
		this.setInParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypIds, true));

		parTypIds = Identifier.fromTransferables(ett.threshold_parameter_type_ids);
		this.thresholdParameterTypes = new HashSet(ett.threshold_parameter_type_ids.length);
		this.setThresholdParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypIds, true));

		parTypIds = Identifier.fromTransferables(ett.etalon_parameter_type_ids);
		this.etalonParameterTypes = new HashSet(ett.etalon_parameter_type_ids.length);
		this.setEtalonParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypIds, true));

		parTypIds = Identifier.fromTransferables(ett.out_parameter_type_ids);
		this.outParameterTypes = new HashSet(ett.out_parameter_type_ids.length);
		this.setOutParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypIds, true));		

		this.measurementTypeIds = Identifier.fromTransferables(ett.measurement_type_ids);
		
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

		Identifier_Transferable[] thresholdParTypeIds = new Identifier_Transferable[this.thresholdParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.thresholdParameterTypes.iterator(); iterator.hasNext();)
			thresholdParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] etalonParTypeIds = new Identifier_Transferable[this.etalonParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypes.iterator(); iterator.hasNext();)
			etalonParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();


		Identifier_Transferable[] measTypIds = new Identifier_Transferable[this.measurementTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.measurementTypeIds.iterator(); iterator.hasNext();)
			measTypIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new EvaluationType_Transferable(super.getHeaderTransferable(),
											   super.codename,
											   super.description != null ? super.description : "",
											   inParTypeIds,
											   thresholdParTypeIds,
											   etalonParTypeIds,
											   outParTypeIds,
											   measTypIds);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET
				&& this.thresholdParameterTypes != null && this.thresholdParameterTypes != Collections.EMPTY_SET
				&& this.etalonParameterTypes != null && this.etalonParameterTypes != Collections.EMPTY_SET
				&& this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET
				&& this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET;
	}

	public java.util.Set getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public java.util.Set getThresholdParameterTypes() {
		return Collections.unmodifiableSet(this.thresholdParameterTypes);
	}

	public java.util.Set getEtalonParameterTypes() {
		return Collections.unmodifiableSet(this.etalonParameterTypes);
	}

	public java.util.Set getOutParameterTypes() {
		return Collections.unmodifiableSet(this.outParameterTypes);
	}

	public java.util.Set getMeasurementTypeIds() {
		return Collections.unmodifiableSet(this.measurementTypeIds);
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
	protected synchronized void setParameterTypes(java.util.Set inParameterTypes,
			java.util.Set thresholdParameterTypes,
			java.util.Set etalonParameterTypes,
			java.util.Set outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setThresholdParameterTypes0(thresholdParameterTypes);
		this.setEtalonParameterTypes0(etalonParameterTypes);
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
	protected void setThresholdParameterTypes0(java.util.Set thresholdParameterTypes) {
		this.thresholdParameterTypes.clear();
		if (thresholdParameterTypes != null)
			this.thresholdParameterTypes.addAll(thresholdParameterTypes);
	}

	/**
	 * client setter for thresholdParameterTypes
	 * 
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setThresholdParameterTypes(java.util.Set thresholdParameterTypes) {
		this.setThresholdParameterTypes0(thresholdParameterTypes);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEtalonParameterTypes0(java.util.Set etalonParameterTypes) {
		this.etalonParameterTypes.clear();
		if (etalonParameterTypes != null)
			this.etalonParameterTypes.addAll(etalonParameterTypes);
	}

	/**
	 * client setter for etalonParameterTypes
	 * 
	 * @param etalonParameterTypes
	 *            The etalonParameterTypes to set.
	 */
	public void setEtalonParameterTypes(java.util.Set etalonParameterTypes) {
		this.setEtalonParameterTypes0(etalonParameterTypes);
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
	protected void setMeasurementTypeIds0(java.util.Set measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null)
			this.measurementTypeIds.addAll(measurementTypeIds);
	}

	/**
	 * client setter for outParameterTypes
	 * @param measurementTypeIds
	 */
	public void setMeasurementTypeIds(java.util.Set measurementTypeIds) {
		this.setMeasurementTypeIds0(measurementTypeIds);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);

		if (this.thresholdParameterTypes != null)
			dependencies.addAll(this.thresholdParameterTypes);

		if (this.etalonParameterTypes != null)
			dependencies.addAll(this.etalonParameterTypes);

		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
