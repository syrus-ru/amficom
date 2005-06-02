/*
 * $Id: EvaluationType.java,v 1.68 2005/06/02 14:27:15 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.68 $, $Date: 2005/06/02 14:27:15 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3978420317305583161L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypeIds;
	private java.util.Set thresholdParameterTypeIds;
	private java.util.Set etalonParameterTypeIds;
	private java.util.Set outParameterTypeIds;

	private java.util.Set measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	EvaluationType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet();
		this.thresholdParameterTypeIds = new HashSet();
		this.etalonParameterTypeIds = new HashSet();
		this.outParameterTypeIds = new HashSet();

		this.measurementTypeIds = new HashSet();

		EvaluationTypeDatabase database = (EvaluationTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
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
	EvaluationType(Identifier id,
							 Identifier creatorId,
							 long version,
							 String codename,
							 String description,
							 java.util.Set inParameterTypeIds,
							 java.util.Set thresholdParameterTypeIds,
							 java.util.Set etalonParameterTypeIds,
							 java.util.Set outParameterTypeIds,
						   java.util.Set measurementTypeIds) {
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

		this.thresholdParameterTypeIds = new HashSet();
		this.setThresholdParameterTypeIds0(thresholdParameterTypeIds);

		this.etalonParameterTypeIds = new HashSet();
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);

		this.outParameterTypeIds = new HashSet();
		this.setOutParameterTypeIds0(outParameterTypeIds);


		this.measurementTypeIds = new HashSet();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param thresholdParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static EvaluationType createInstance(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypeIds,
			java.util.Set thresholdParameterTypeIds,
			java.util.Set etalonParameterTypeIds,
			java.util.Set outParameterTypeIds,
			java.util.Set measurementTypeIds) throws CreateObjectException {
		try {
			EvaluationType evaluationType = new EvaluationType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					thresholdParameterTypeIds,
					etalonParameterTypeIds,
					outParameterTypeIds,
					measurementTypeIds);

			assert evaluationType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			evaluationType.changed = true;
			try {
				StorableObjectPool.putStorableObject(evaluationType);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return evaluationType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		EvaluationType_Transferable ett = (EvaluationType_Transferable)transferable;
		super.fromTransferable(ett.header, ett.codename, ett.description);

		this.inParameterTypeIds = Identifier.fromTransferables(ett.in_parameter_type_ids);
		this.thresholdParameterTypeIds = Identifier.fromTransferables(ett.threshold_parameter_type_ids);
		this.etalonParameterTypeIds = Identifier.fromTransferables(ett.etalon_parameter_type_ids);
		this.outParameterTypeIds = Identifier.fromTransferables(ett.out_parameter_type_ids);		

		this.measurementTypeIds = Identifier.fromTransferables(ett.measurement_type_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		Identifier_Transferable[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		Identifier_Transferable[] thresholdParTypeIds = Identifier.createTransferables(this.thresholdParameterTypeIds);
		Identifier_Transferable[] etalonParTypeIds = Identifier.createTransferables(this.etalonParameterTypeIds);
		Identifier_Transferable[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		Identifier_Transferable[] measTypIds = Identifier.createTransferables(this.measurementTypeIds);

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
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET
				&& this.thresholdParameterTypeIds != null && this.thresholdParameterTypeIds != Collections.EMPTY_SET
				&& this.etalonParameterTypeIds != null && this.etalonParameterTypeIds != Collections.EMPTY_SET
				&& this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET
				&& this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET;
	}

	public java.util.Set getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public java.util.Set getThresholdParameterTypeIds() {
		return Collections.unmodifiableSet(this.thresholdParameterTypeIds);
	}

	public java.util.Set getEtalonParameterTypeIds() {
		return Collections.unmodifiableSet(this.etalonParameterTypeIds);
	}

	public java.util.Set getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
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
	protected synchronized void setParameterTypeIds(Map parameterTypeIdsModeMap) {
		this.setInParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_IN));
		this.setThresholdParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_THRESHOLD));
		this.setEtalonParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_ETALON));
		this.setOutParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(EvaluationTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Map getParameterTypeIdsModeMap() {
		Map parameterTypeIdsModeMap = new HashMap(4);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_THRESHOLD, this.thresholdParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_ETALON, this.etalonParameterTypeIds);
		parameterTypeIdsModeMap.put(EvaluationTypeWrapper.MODE_OUT, this.outParameterTypeIds);
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
	protected void setThresholdParameterTypeIds0(java.util.Set thresholdParameterTypeIds) {
		this.thresholdParameterTypeIds.clear();
		if (thresholdParameterTypeIds != null)
			this.thresholdParameterTypeIds.addAll(thresholdParameterTypeIds);
	}

	/**
	 * client setter for thresholdParameterTypeIds
	 *
	 * @param thresholdParameterTypeIds
	 *            The thresholdParameterTypeIds to set.
	 */
	public void setThresholdParameterTypeIds(java.util.Set thresholdParameterTypeIds) {
		this.setThresholdParameterTypeIds0(thresholdParameterTypeIds);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEtalonParameterTypeIds0(java.util.Set etalonParameterTypeIds) {
		this.etalonParameterTypeIds.clear();
		if (etalonParameterTypeIds != null)
			this.etalonParameterTypeIds.addAll(etalonParameterTypeIds);
	}

	/**
	 * client setter for etalonParameterTypeIds
	 *
	 * @param etalonParameterTypeIds
	 *            The etalonParameterTypeIds to set.
	 */
	public void setEtalonParameterTypeIds(java.util.Set etalonParameterTypeIds) {
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);
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
	protected void setMeasurementTypeIds0(java.util.Set measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null)
			this.measurementTypeIds.addAll(measurementTypeIds);
	}

	/**
	 * client setter for measurementTypeIds
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
		if (this.inParameterTypeIds != null)
			dependencies.addAll(this.inParameterTypeIds);

		if (this.thresholdParameterTypeIds != null)
			dependencies.addAll(this.thresholdParameterTypeIds);

		if (this.etalonParameterTypeIds != null)
			dependencies.addAll(this.etalonParameterTypeIds);

		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
