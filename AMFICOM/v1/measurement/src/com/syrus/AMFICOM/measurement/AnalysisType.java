/*
 * $Id: AnalysisType.java,v 1.71 2005/05/25 13:01:05 bass Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.71 $, $Date: 2005/05/25 13:01:05 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722866476495413L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypeIds;
	private java.util.Set criteriaParameterTypeIds;
	private java.util.Set etalonParameterTypeIds;
	private java.util.Set outParameterTypeIds;

	private java.util.Set measurementTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	AnalysisType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet();
		this.criteriaParameterTypeIds = new HashSet();
		this.etalonParameterTypeIds = new HashSet();
		this.outParameterTypeIds = new HashSet();

		this.measurementTypeIds = new HashSet();

		AnalysisTypeDatabase database = (AnalysisTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public AnalysisType(AnalysisType_Transferable att) throws CreateObjectException {
		try {
			this.fromTransferable(att);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	AnalysisType(Identifier id,
						   Identifier creatorId,
						   long version,
						   String codename,
						   String description,
						   java.util.Set inParameterTypeIds,
						   java.util.Set criteriaParameterTypeIds,
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

		this.criteriaParameterTypeIds = new HashSet();
		this.setCriteriaParameterTypeIds0(criteriaParameterTypeIds);

		this.etalonParameterTypeIds = new HashSet();
		this.setEtalonParameterTypeIds0(etalonParameterTypeIds);

		this.outParameterTypeIds = new HashSet();
		this.setOutParameterTypeIds0(outParameterTypeIds);


		this.measurementTypeIds = new HashSet();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param criteriaParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static AnalysisType createInstance(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypeIds,
			java.util.Set criteriaParameterTypeIds,
			java.util.Set etalonParameterTypeIds,
			java.util.Set outParameterTypeIds,
			java.util.Set measurementTypeIds) throws CreateObjectException {		

		try {
			AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					criteriaParameterTypeIds,
					etalonParameterTypeIds,
					outParameterTypeIds,
					measurementTypeIds);
			analysisType.changed = true;
			assert analysisType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			return analysisType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		AnalysisType_Transferable att = (AnalysisType_Transferable) transferable;
		super.fromTransferable(att.header, att.codename, att.description);

		this.inParameterTypeIds = Identifier.fromTransferables(att.in_parameter_type_ids);
		this.criteriaParameterTypeIds = Identifier.fromTransferables(att.criteria_parameter_type_ids);
		this.etalonParameterTypeIds = Identifier.fromTransferables(att.etalon_parameter_type_ids);
		this.outParameterTypeIds = Identifier.fromTransferables(att.out_parameter_type_ids);

		this.measurementTypeIds = Identifier.fromTransferables(att.measurement_type_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		Identifier_Transferable[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		Identifier_Transferable[] criteriaParTypeIds = Identifier.createTransferables(this.criteriaParameterTypeIds);
		Identifier_Transferable[] etalonParTypeIds = Identifier.createTransferables(this.etalonParameterTypeIds);
		Identifier_Transferable[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);
		Identifier_Transferable[] measTypIds = Identifier.createTransferables(this.measurementTypeIds);

		return new AnalysisType_Transferable(super.getHeaderTransferable(),
											 super.codename,
											 super.description != null ? super.description : "",
											 inParTypeIds,
											 criteriaParTypeIds,
											 etalonParTypeIds,
											 outParTypeIds,
											 measTypIds);
	}

	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET &&
			this.criteriaParameterTypeIds != null && this.criteriaParameterTypeIds != Collections.EMPTY_SET &&
			this.etalonParameterTypeIds != null && this.etalonParameterTypeIds != Collections.EMPTY_SET &&
			this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET &&
			this.measurementTypeIds != null && this.measurementTypeIds != Collections.EMPTY_SET;
	}
	
	public java.util.Set getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public java.util.Set getCriteriaParameterTypeIds() {
		return Collections.unmodifiableSet(this.criteriaParameterTypeIds);
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
		this.setInParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_IN));
		this.setCriteriaParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_CRITERION));
		this.setEtalonParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_ETALON));
		this.setOutParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(AnalysisTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Map getParameterTypeIdsModeMap() {
		Map parameterTypeIdsModeMap = new HashMap(4);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_CRITERION, this.criteriaParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_ETALON, this.etalonParameterTypeIds);
		parameterTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_OUT, this.outParameterTypeIds);
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
	protected void setCriteriaParameterTypeIds0(java.util.Set criteriaParameterTypeIds) {
		this.criteriaParameterTypeIds.clear();
		if (criteriaParameterTypeIds != null)
			this.criteriaParameterTypeIds.addAll(criteriaParameterTypeIds);
	}
	/**
	 * client setter for criteriaParameterTypeIds
	 *
	 * @param thresholdParameterTypeIds
	 *            The thresholdParameterTypeIds to set.
	 */
	public void setCriteriaParameterTypeIds(java.util.Set thresholdParameterTypeIds) {
		this.setCriteriaParameterTypeIds0(thresholdParameterTypeIds);
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
	 * client setter for outParameterTypeIds
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

		if (this.criteriaParameterTypeIds != null)
			dependencies.addAll(this.criteriaParameterTypeIds);

		if (this.etalonParameterTypeIds != null)
			dependencies.addAll(this.etalonParameterTypeIds);

		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
