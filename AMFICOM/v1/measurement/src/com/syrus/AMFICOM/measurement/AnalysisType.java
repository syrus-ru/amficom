/*
 * $Id: AnalysisType.java,v 1.53 2005/03/24 15:42:36 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.53 $, $Date: 2005/03/24 15:42:36 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256722866476495413L;

	public static final String CODENAME_DADARA = "dadara";

	private Collection inParameterTypes;
	private Collection criteriaParameterTypes;
	private Collection etalonParameterTypes;
	private Collection outParameterTypes;

	private Collection measurementTypeIds;

	private StorableObjectDatabase	analysisTypeDatabase;	

	public AnalysisType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new ArrayList();
		this.criteriaParameterTypes = new ArrayList();
		this.etalonParameterTypes = new ArrayList();
		this.outParameterTypes = new ArrayList();

		this.measurementTypeIds = new ArrayList();

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.retrieve(this);
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
		super(att.header, new String(att.codename), new String(att.description));

		try {
			List parTypIds;

			parTypIds = new ArrayList(att.in_parameter_type_ids.length);
			for (int i = 0; i < att.in_parameter_type_ids.length; i++)
				parTypIds.add(new Identifier(att.in_parameter_type_ids[i]));
			this.inParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

			parTypIds.clear();
			for (int i = 0; i < att.criteria_parameter_type_ids.length; i++)
				parTypIds.add(new Identifier(att.criteria_parameter_type_ids[i]));
			this.criteriaParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

			parTypIds.clear();
			for (int i = 0; i < att.etalon_parameter_type_ids.length; i++)
				parTypIds.add(new Identifier(att.etalon_parameter_type_ids[i]));
			this.etalonParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);

			parTypIds.clear();
			for (int i = 0; i < att.out_parameter_type_ids.length; i++)
				parTypIds.add(new Identifier(att.out_parameter_type_ids[i]));
			this.outParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypIds, true);


			this.measurementTypeIds = new ArrayList(att.measurement_type_ids.length);
			for (int i = 0; i < att.measurement_type_ids.length; i++)
				this.measurementTypeIds.add(new Identifier(att.measurement_type_ids[i]));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
	}

	protected AnalysisType(Identifier id,
						   Identifier creatorId,
						   long version,
						   String codename,
						   String description,
						   Collection inParameterTypes,
						   Collection criteriaParameterTypes,
						   Collection etalonParameterTypes,
						   Collection outParameterTypes,
						   Collection measurementTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new ArrayList();
		this.setInParameterTypes0(inParameterTypes);

		this.criteriaParameterTypes = new ArrayList();
		this.setCriteriaParameterTypes0(criteriaParameterTypes);

		this.etalonParameterTypes = new ArrayList();
		this.setEtalonParameterTypes0(etalonParameterTypes);

		this.outParameterTypes = new ArrayList();
		this.setOutParameterTypes0(outParameterTypes);


		this.measurementTypeIds = new ArrayList();
		this.setMeasurementTypeIds0(measurementTypeIds);

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
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
			Collection inParameterTypes,
			Collection criteriaParameterTypes,
			Collection etalonParameterTypes,
			Collection outParameterTypes,
			Collection measurementTypeIds) throws CreateObjectException {
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

	public Object getTransferable() {
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
											 new String(super.codename),
											 (super.description != null) ? (new String(super.description)) : "",
											 inParTypeIds,
											 criteriaParTypeIds,
											 etalonParTypeIds,
											 outParTypeIds,
											 measTypIds);
	}

	public Collection getInParameterTypes() {
		return Collections.unmodifiableCollection(this.inParameterTypes);
	}

	public Collection getCriteriaParameterTypes() {
		return Collections.unmodifiableCollection(this.criteriaParameterTypes);
	}

	public Collection getEtalonParameterTypes() {
		return Collections.unmodifiableCollection(this.etalonParameterTypes);
	}

	public Collection getOutParameterTypes() {
		return Collections.unmodifiableCollection(this.outParameterTypes);
	}

	public Collection getMeasurementTypeIds() {
		return Collections.unmodifiableCollection(this.measurementTypeIds);
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

	protected synchronized void setParameterTypes(Collection inParameterTypes,
			Collection criteriaParameterTypes,
			Collection etalonParameterTypes,
			Collection outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setCriteriaParameterTypes0(criteriaParameterTypes);
		this.setEtalonParameterTypes0(etalonParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	protected void setInParameterTypes0(Collection inParameterTypes) {
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
	public void setInParameterTypes(Collection inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.changed = true;		
	}

	protected void setCriteriaParameterTypes0(Collection criteriaParameterTypes) {
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
	public void setCriteriaParameterTypes(Collection thresholdParameterTypes) {
		this.setCriteriaParameterTypes0(thresholdParameterTypes);
		super.changed = true;
	}

	protected void setEtalonParameterTypes0(Collection etalonParameterTypes) {
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
	public void setEtalonParameterTypes(Collection etalonParameterTypes) {
		this.setEtalonParameterTypes0(etalonParameterTypes);
		super.changed = true;
	}

	protected void setOutParameterTypes0(Collection outParameterTypes) {
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
	public void setOutParameterTypes(Collection outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.changed = true;
	}	

	protected void setMeasurementTypeIds0(Collection measurementTypeIds) {
		this.measurementTypeIds.clear();
		if (measurementTypeIds != null)
			this.measurementTypeIds.addAll(measurementTypeIds);
	}

	/**
	 * client setter for outParameterTypes
	 * @param measurementTypeIds
	 */
	public void setMeasurementTypeIds(Collection measurementTypeIds) {
		this.setMeasurementTypeIds0(measurementTypeIds);
		super.changed = true;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
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
