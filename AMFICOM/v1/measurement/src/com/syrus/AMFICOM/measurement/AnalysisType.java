/*
 * $Id: AnalysisType.java,v 1.60 2005/04/08 12:33:24 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.60 $, $Date: 2005/04/08 12:33:24 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722866476495413L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypes;
	private java.util.Set criteriaParameterTypes;
	private java.util.Set etalonParameterTypes;
	private java.util.Set outParameterTypes;

	private java.util.Set measurementTypeIds;

	public AnalysisType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet();
		this.criteriaParameterTypes = new HashSet();
		this.etalonParameterTypes = new HashSet();
		this.outParameterTypes = new HashSet();

		this.measurementTypeIds = new HashSet();

		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.criteriaParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.etalonParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	public AnalysisType(AnalysisType_Transferable att) throws CreateObjectException {
		try {
			this.fromTransferable(att);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected AnalysisType(Identifier id,
						   Identifier creatorId,
						   long version,
						   String codename,
						   String description,
						   java.util.Set inParameterTypes,
						   java.util.Set criteriaParameterTypes,
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

		this.criteriaParameterTypes = new HashSet();
		this.setCriteriaParameterTypes0(criteriaParameterTypes);

		this.etalonParameterTypes = new HashSet();
		this.setEtalonParameterTypes0(etalonParameterTypes);

		this.outParameterTypes = new HashSet();
		this.setOutParameterTypes0(outParameterTypes);


		this.measurementTypeIds = new HashSet();
		this.setMeasurementTypeIds0(measurementTypeIds);
	}
	
	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param criteriaParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 * @param measurementTypeIds
	 * @throws CreateObjectException
	 */
	public static AnalysisType createInstance(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypes,
			java.util.Set criteriaParameterTypes,
			java.util.Set etalonParameterTypes,
			java.util.Set outParameterTypes,
			java.util.Set measurementTypeIds) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypes,
					criteriaParameterTypes,
					etalonParameterTypes,
					outParameterTypes,
					measurementTypeIds);
			analysisType.changed = true;
			return analysisType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("AnalysisType.createInstance | cannot generate identifier ", e);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		AnalysisType_Transferable att = (AnalysisType_Transferable) transferable;
		super.fromTransferable(att.header, att.codename, att.description);

		java.util.Set parTypIds;

		parTypIds = Identifier.fromTransferables(att.in_parameter_type_ids);
		this.inParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

		parTypIds = Identifier.fromTransferables(att.criteria_parameter_type_ids);
		this.criteriaParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

		parTypIds = Identifier.fromTransferables(att.etalon_parameter_type_ids);
		this.etalonParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

		parTypIds = Identifier.fromTransferables(att.out_parameter_type_ids);
		this.outParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

		this.measurementTypeIds = new HashSet(att.measurement_type_ids.length);
		for (int i = 0; i < att.measurement_type_ids.length; i++)
			this.measurementTypeIds.add(new Identifier(att.measurement_type_ids[i]));
	}

	public IDLEntity getTransferable() {
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] criteriaParTypeIds = new Identifier_Transferable[this.criteriaParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.criteriaParameterTypes.iterator(); iterator.hasNext();)
			criteriaParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

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

		return new AnalysisType_Transferable(super.getHeaderTransferable(),
											 super.codename,
											 super.description != null ? super.description : "",
											 inParTypeIds,
											 criteriaParTypeIds,
											 etalonParTypeIds,
											 outParTypeIds,
											 measTypIds);
	}

	public java.util.Set getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public java.util.Set getCriteriaParameterTypes() {
		return Collections.unmodifiableSet(this.criteriaParameterTypes);
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

	protected synchronized void setParameterTypes(java.util.Set inParameterTypes,
			java.util.Set criteriaParameterTypes,
			java.util.Set etalonParameterTypes,
			java.util.Set outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setCriteriaParameterTypes0(criteriaParameterTypes);
		this.setEtalonParameterTypes0(etalonParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

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

	protected void setCriteriaParameterTypes0(java.util.Set criteriaParameterTypes) {
		this.criteriaParameterTypes.clear();
		if (criteriaParameterTypes != null)
			this.criteriaParameterTypes.addAll(criteriaParameterTypes);
	}
	/**
	 * client setter for criteriaParameterTypes
	 * 
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setCriteriaParameterTypes(java.util.Set thresholdParameterTypes) {
		this.setCriteriaParameterTypes0(thresholdParameterTypes);
		super.changed = true;
	}

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

	public java.util.Set getDependencies() {
		java.util.Set dependencies = new HashSet();
		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);

		if (this.criteriaParameterTypes != null)
			dependencies.addAll(this.criteriaParameterTypes);

		if (this.etalonParameterTypes != null)
			dependencies.addAll(this.etalonParameterTypes);

		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);


		if (this.measurementTypeIds != null)
			dependencies.addAll(this.measurementTypeIds);

		return dependencies;
	}
}
